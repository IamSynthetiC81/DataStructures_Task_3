package util;

public interface ProgressBarInterface{
    ProgressBar ProgressBar = new ProgressBar(30, 500, true);

    /**
     * Updates the progress parameter of the progress bar
     * @param Progress the progress to be updated
     *  <p>
     *      Must be within 0 and 100
     *  </p>
     */
    static void UpdateProgress(int Progress){
        ProgressBar.UpdateProgress(Progress);
    }

    /**
     * Updates the progress parameter of the progress bar
     * by incrementing it by 1.
     */
    static void IncrementProgress(){
        ProgressBar.IncrementProgress();
    }

    /**
     * Starts the progress bar thread.
     */
    static void StartProgressBar() {
        ProgressBar.start();
    }

    /**
     * Sets the progress bar length.
     * @param BarLength the length of the progress bar in characters
     */
    static void setProgressBarLength(int BarLength){
        ProgressBar.setLength(BarLength);
    }

    /**
     * Sets the delay between the waking of the thread.
     * @param Delay the delay in milliseconds.
     */
    static void setProgressBarDelay(int Delay){
        ProgressBar.setDelay(Delay);
    }

    /**
     * Toggles the progress bar into the IncrementalState.
     * <p>
     *     If the progress bar is in the IncrementalState, the
     *     Total count of operations is specified, and then the progress bar
     *     is  incremented whenever there is progress.
     *
     *     When the progress is incremented to the total count, then the
     *     process is marked as complete.
     * </p>
     *
     * @param IncrementalState true if the progress bar is to be in the IncrementalState
     * @param OperationsCount the total count of operations, if the progress bar is in the IncrementalState
     */
    static void IncrementalMode(boolean IncrementalState, int OperationsCount){

        if(IncrementalState) {
            assert OperationsCount > 0 : "OperationsCount must be greater than 0";

            ProgressBar.setIncremental(true);
            ProgressBar.setOperationsCount(OperationsCount);
        } else {
            ProgressBar.setIncremental(false);
            ProgressBar.setOperationsCount(-1);
        }
    }

    static void InterruptProgressBar() {
        ProgressBar.interrupt();
    }
}
