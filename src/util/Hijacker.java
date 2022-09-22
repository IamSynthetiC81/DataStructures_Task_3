package util;

import java.io.*;

public class Hijacker {

    /** FileName of the file to hijack the console to */
    private static String hijackFileName;
    /** The default output stream */
    private static final PrintStream DefaultPrintStream = System.out;
    /** The previous output stream hijacked */
    private static PrintStream prevStream;
    /** Print stream to hijack anything written to the  console, to also be written to a file */
    private static final PrintStream StreamToConsoleAndFile = new PrintStream(new OutputStream() {

        @Override
        public void write(int b) {
            DefaultPrintStream.write(b);

            File file = new File(hijackFileName);
            RandomAccessFile raf;
            try {
                raf = new RandomAccessFile(file, "rw");

                raf.seek(file.length());

                raf.write(b);

                raf.close();

            } catch ( Exception e ) {
                System.err.println("Error writing to file");
                e.printStackTrace();
            }
        }
    });

    private static final PrintStream NullStream = new PrintStream(new OutputStream() {

        @Override
        public void write(int b) {
            // do nothing
        }
    });

    /**
     * Sets the console output to both, the console and the file
     * @param fileName the name of the file to hijack to
     */
    public static void hijackConsoleToFile(String fileName) {
        prevStream = System.out;

        hijackFileName = fileName;

        /* The file to hijack the console to */
        File hijackFile = new File(hijackFileName);

        System.setOut(StreamToConsoleAndFile);
    }

    /**
     * Sets the console output to the default output stream
     */
    public static void hijackConsoleToDefault() {
        prevStream = System.out;

        System.setOut(DefaultPrintStream);
    }

    /**
     * Sets the console output to the previously used Stream.
     */
    public static void hijackConsoleToPreviousConsole(){
        PrintStream currentStream = System.out;
        System.setOut(prevStream);
        if(prevStream != currentStream)
            prevStream = currentStream;
    }

    /**
     * Sets the console output to ignore any input.
     */
    public static void hijackConsoleToNull() {
        prevStream = System.out;

        System.setOut(NullStream);
    }

    private static void setPrevStream(PrintStream CurrentStream){
        if(CurrentStream != prevStream)
            prevStream = CurrentStream;
    }

    public static void RunWithoutConsole(Runnable r) {
        PrintStream currentStream = System.out;
        hijackConsoleToNull();
        r.run();
        System.setOut(currentStream);
    }

    public static void RunOnDefaultConsole(Runnable r){
        PrintStream currentStream = System.out;
        hijackConsoleToDefault();
        r.run();
        System.setOut(currentStream);
    }


    public static void RunOnHijackedConsole(Runnable r) {
        PrintStream currentStream = System.out;
        hijackConsoleToFile(hijackFileName);
        r.run();
        System.setOut(currentStream);
    }
}
