/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.demo;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.tracing.Tracer;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class MyServiceImpl implements MyService {
    @Override
    public OffsetDateTime incrementTime(OffsetDateTime base, int numHours) {
        if (numHours < 0) {
            throw new ServiceException(ErrorType.INVALID_ARGUMENT);
        } else if (numHours == 0) {
            throw new RuntimeException("Must not add 0 hours");
        }

        return base.plus(Duration.ofHours(numHours));
    }

    @Override
    public Map<OffsetDateTime, Boolean> isSunday(List<OffsetDateTime> dates) {
        return dates
                .stream()
                .collect(Collectors.toMap(d -> d, d -> d.getDayOfWeek().equals(DayOfWeek.SUNDAY)));
    }

    @Override
    public String returnTrace() {
        return Tracer.getTraceId();
    }

    @Override
    public Optional<String> maybeString(boolean shouldReturnString) {
        return shouldReturnString ? Optional.of("foo") : Optional.empty();
    }
}
