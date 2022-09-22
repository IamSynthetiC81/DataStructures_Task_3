package util;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * A progress bar for displaying the progress of a task.
 * <p>
 *     This runs in a separate thread and can be interrupted.
 * </p>
 */
public class ProgressBar extends Thread implements HijackConsole{

    /** The length of the progress bar. */
    static private int BarLength = 35;
    /** The progress of the task. */
    static private float Progress;
    /** Start time of the progress bar. */
    static private Instant start;
    /** Delay between updates of the progress bar. */
    static private int Delay = 500;
    /** Total number of operations to be performed.
     * <p>
     *     To be used in Incremental mode.
     * </p>
     */
    static private int OperationsCount;
    /** The estimated time remaining. */
    static private double EstimatedTime;
    /** The user can choose whether to show the Estimated time of the process.
     *  <p>
     *      Stupid idea, Do No Use, Does Not Work, Waste Of Time and Waste Of Space
     *  </p>
     */
    static private boolean ShowEstimatedTime = false;
    /** Enables the incremental mode. */
    static private boolean Incremental = false;

    static private LinkedList<Instant> est = new LinkedList<>();

    public ProgressBar(int BarLength, int Delay, boolean Incremental) {
        this.setName("ProgressBar");
        this.setPriority(Thread.MAX_PRIORITY);

        ProgressBar.Progress = 0;
        ProgressBar.BarLength = BarLength;
        ProgressBar.Delay = Delay;
        ProgressBar.Incremental = Incremental;

    }

    /**
     *  Starts the progress bar
     */
    @Override
    public void run(){
        start = Instant.now();

        int End = (ProgressBar.Incremental) ? (int) OperationsCount : 100;

        while(Progress < End){
            PrintProgress();

            try {

                /* Maybe use wait(millis) instead of sleep(millis) */

                Thread.sleep(Delay);
            } catch (InterruptedException WakeEvent) {
                if (Progress > End){

                    System.out.println("Incorrect OperationCount for Incremental mode\n" +
                            "The current progress is " + Progress +
                            " and the total number of operations is " + OperationsCount);

                    break;
                } else if (Progress == End) {
                    break;
                } else
                    HijackConsole.RunOnDefaultConsole(WakeEvent::printStackTrace);
            }
        }

        Progress = 0;

        Duration TimeElapsed = Duration.between(start, Instant.now());
        int hours   = (int) (TimeElapsed.toMinutes()/60);
        int minutes = TimeElapsed.toMinutesPart();
        int seconds = TimeElapsed.toSecondsPart();

        System.out.flush();

        System.out.printf("\rTime elapsed: %02d:%02d:%02d\n",hours,minutes,seconds);

        Thread.currentThread().interrupt();
    }

    /**
     * Updates the progress parameter of the progress bar
     * @param progress the progress.
     */
    public void UpdateProgress(int progress){
        Progress = progress;
    }

    /**
     * Prints the progress bar to the console
     */
    public void PrintProgress() {
        HijackConsole.RunOnDefaultConsole(() -> {
            if (Incremental)
                assert Progress >= 0 && Progress <= OperationsCount : "Progress must be between 0 and OperationsCount";
            else
                assert Progress >= 0 && Progress <= 100 : "Progress must be between 0 and 100";
        });

        float progress;
        if (Incremental)
            progress = ( Progress * 100L / OperationsCount);
        else
            progress = Progress;

        int progressBarLength = (int) ( progress * BarLength / 100);

        HijackConsole.RunOnDefaultConsole( ()->{
            System.out.print("[");
        });
        for (int i = 0; i < progressBarLength; i++)
            HijackConsole.RunOnDefaultConsole( ()-> {
                System.out.print("=");});
        for (int i = progressBarLength; i < BarLength; i++)
            HijackConsole.RunOnDefaultConsole( ()-> {
                System.out.print(" ");});

        Duration timeElapsed = Duration.between(start, Instant.now());
        int hours   = (int) (timeElapsed.toMinutes()/60);
        int min     = timeElapsed.toMinutesPart();
        int sec     = timeElapsed.toSecondsPart();

        HijackConsole.RunOnDefaultConsole( ()-> {
            System.out.printf("] %4.2f%% : Time elapsed %02d:%02d:%02d", (double) progress,hours,min,sec);});
        if (!ShowEstimatedTime || !TimeEstimation.EstimationReady)
            HijackConsole.RunOnDefaultConsole( ()-> {
                        System.out.print("\r");});
        else
            PrintEstimatedTime();

        System.out.flush();
    }

    /**
     * Prints the estimated time remaining to the console
     */
    private void PrintEstimatedTime() {
        double s  = (EstimatedTime*(OperationsCount-Progress)*1000000000);
        double ms = (s/60);
        double hs = (ms/60);

        ms = ms%60;
        hs = hs%60;

        int finalMs = (int) ms;
        int finalHs = (int) hs;
        HijackConsole.RunOnDefaultConsole(() -> {
            System.out.printf(" | estimated time : %02d:%02d\r",
                    finalHs,
                    finalMs
            );
        });
    }

    /**
     * Sets the length of the progress bar in characters
     * @param length the length of the progress bar
     */
    public void setLength(int length) {
        BarLength = length;
    }

    /**
     * Sets the delay between updates of the progress bar
     * @param delay the delay in milliseconds
     */
    public void setDelay(int delay) {
        Delay = delay;
    }

    /**
     * Sets the total number of operations to be performed
     * @param FinalOperationsCount the total number of operations to be performed
     */
    public void setFinalOperationsCount(int FinalOperationsCount) {
        OperationsCount = FinalOperationsCount;
    }

    /**
     * Increments the progress of the progress bar.
     */
    public void IncrementProgress(){
        Progress++;

        if(Progress >= OperationsCount)
            this.interrupt();

        EstimatedTime = TimeEstimation.EstimateTime();
    }

    /**
     * Toggles the Incremental mode.
     * @param incrementalState the state of the incremental mode
     */
    public void setIncremental(boolean incrementalState) {
        Incremental = incrementalState;
    }

    /**
     * Sets the number of increments required to be
     * performed before the task is considered complete.
     * @param operationsCount the number of increments
     *                       required for the task.
     */
    public void setOperationsCount(int operationsCount) {
        OperationsCount = operationsCount;
    }



}
