# Exception-handler benchmarks

These JMH benchmarks measure the additional cost of retry-exhaustion detection on **handled failures**.
Successful requests do not call this exception handler.

* `ConjureExceptionsBenchmark`: compares the production handler with a generated baseline that omits only
  the `isRetriesExhausted` check and header write. Both variants include a fresh in-memory Undertow exchange,
  failure attachment, dispatch, and JSON error serialization where applicable.
* `RetriesExhaustedScanBenchmark`: isolates the cause/suppressed-exception scan, using a generated copy of
  the production method. It does not include header insertion, exchange creation, or serialization.

The production source is unchanged. `generateBenchmarkBaseline` copies `ConjureExceptions` at build time,
removes the entry check, and exposes its otherwise identical scanner to the scan benchmark. It fails if the
expected source pattern changes. This avoids reflection in the measured path and a manually maintained
copy of the old handler.

Exceptions and their diagnostics are constructed during trial setup. Each trial also checks the scanner's
expected result, both handlers' status/body/failure attachment, and presence or absence of the exhaustion
header. The benchmark's SLF4J configuration disables all log levels, and an unsampled request trace is
established outside measurement so JSON serialization does not create a new root trace on every call.
There are no sockets or mocks in the measured paths.

## Run

From the repository root, compare common cases and record allocations:

```sh
./gradlew :conjure-java-undertow-runtime-jmh:jmh \
  -PjmhOptions='ConjureExceptionsBenchmark -p scenario=SERVICE_LOCAL,REMOTE_UNMARKED,REMOTE_MARKED,QOS_UNMARKED,QOS_MARKED -prof gc' \
  -PjmhResultFile=build/reports/jmh/handler.json
```

Measure just the scan:

```sh
./gradlew :conjure-java-undertow-runtime-jmh:jmh \
  -PjmhOptions='RetriesExhaustedScanBenchmark -prof gc' \
  -PjmhResultFile=build/reports/jmh/scan.json
```

Defaults are two fresh JVM forks, three one-second warmup iterations and five one-second measurement
iterations, with a 512 MiB heap and nanoseconds per operation. Use `-f`, `-wi`, `-i`, `-w`, and `-r` in
`jmhOptions` to change these. Running without a scenario filter measures all 18 scenarios. A quick smoke
run validates the fixtures but is not a useful performance result:

```sh
./gradlew :conjure-java-undertow-runtime-jmh:jmh \
  -PjmhOptions='.*Benchmark -f 1 -wi 0 -i 1 -r 100ms' \
  -PjmhResultFile=build/reports/jmh/smoke.json
```

Result paths are relative to this module. `distTar` builds a standalone JMH distribution for running on
an otherwise idle benchmark machine.

## Scenarios

| Scenarios | What they exercise |
| --- | --- |
| `SERVICE_LOCAL` | Ordinary local error with no downstream cause |
| `REMOTE_NO_DIAGNOSTICS`, `REMOTE_UNMARKED`, `REMOTE_MARKED` | Remote errors with no diagnostics, ordinary diagnostics, or the marker |
| `QOS_UNMARKED`, `QOS_MARKED` | QoS unavailable errors |
| `UNKNOWN_UNMARKED`, `UNKNOWN_MARKED` | Unrecognized remote error bodies |
| `IO_UNMARKED`, `IO_MARKED` | Transport exceptions |
| `REMOTE_WRAPPED_MARKED` | Marker under three application wrappers |
| `REMOTE_DEEP_UNMARKED`, `REMOTE_DEEP_MARKED` | 32 wrappers above a remote exception |
| `MANY_DIAGNOSTICS_UNMARKED`, `MANY_DIAGNOSTICS_MARKED` | 32 suppressed diagnostics; any marker is last |
| `MARKER_AT_DEPTH_LIMIT`, `MARKER_BEYOND_DEPTH_LIMIT` | Marker at positions 99 and 100; the latter is not scanned |
| `CAUSE_CYCLE` | A cause cycle stopped by the depth limit |

## Interpret results

For each scenario, compare `withRetryExhaustionCheck` against `withoutRetryExhaustionCheck`:

* Added time: `with - without` in ns per handled failure.
* Relative cost: `100 * (with / without - 1)`.
* `gc.alloc.rate.norm`: allocated bytes per operation, including any copied suppressed-exception arrays
  and additional response-header storage.

Inspect JMH's error bounds and individual forks before treating small differences as a regression.
The full-handler measurement includes the same exchange/body-buffer allocations in both variants;
it is not a measurement of network latency or total application request latency. Disabled log output
also makes the added CPU cost easier to see than it may be when a service writes error logs.
