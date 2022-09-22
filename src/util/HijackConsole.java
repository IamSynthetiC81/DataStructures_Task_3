package util;

import java.io.FileNotFoundException;

public interface HijackConsole {

    Hijacker Hijacker = new Hijacker();

    /**
     * Hijacks the console output to a file
     * @param fileName the name of the file to hijack to
     * @throws FileNotFoundException if the file cannot be found
     */
    static void hijackConsoleToFile(String fileName) throws FileNotFoundException {
        util.Hijacker.hijackConsoleToFile(fileName);
    }

    /**
     * Hijacks the console output to the default output stream
     */
    static void hijackConsoleToDefault() {
        util.Hijacker.hijackConsoleToDefault();
    }

    /**
     * Hijacks the console output to null
     */
    static void hijackConsoleToNull() {
        util.Hijacker.hijackConsoleToNull();
    }

    /**
     * Hijacks the console output to the previous output stream
     */
    static void hijackConsoleToPreviousConsole() {
        util.Hijacker.hijackConsoleToPreviousConsole();
    }

    /**
     * Runs the given code while limiting console output.
     * @param r the code to run
     *
     * @TODO : add some "lock" to the stream
     *          so it can't be re-hijacked from within the code
     */
    static void RunWithoutConsole(Runnable r) {
        util.Hijacker.RunWithoutConsole(r);
    }

    /**
     * Runs the given code on the default console.
     * @param r the code to run
     *
     * @TODO : add some "lock" to the stream
     *          so it can't be re-hijacked from within the code
     */
    static void RunOnDefaultConsole(Runnable r) {
        util.Hijacker.RunOnDefaultConsole(r);
    }

    static void RunOnHijackedConsole(Runnable r) {
        util.Hijacker.RunOnHijackedConsole(r);
    }


}
