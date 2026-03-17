package com.palantir.conjure.java.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "repro")
public final class PicocliExceptionRepro implements Runnable {

    @Override
    public void run() {
        throw new IllegalStateException("boom");
    }

    @SuppressWarnings({"BanSystemOut", "deprecation"})
    public static void main(String[] _args) {
        System.out.println("=== execute() without System.exit ===");
        int exitCode = new CommandLine(new PicocliExceptionRepro()).execute();
        System.out.println("Returned exit code: " + exitCode);
        System.out.println("Execution continued past execute() — exception was swallowed!\n");

        System.out.println("=== CommandLine.run() (deprecated) ===");
        try {
            CommandLine.run(new PicocliExceptionRepro());
            System.out.println("This should not print");
        } catch (Exception e) {
            System.out.println("Exception propagated: " + e.getCause().getMessage());
        }
    }
}
