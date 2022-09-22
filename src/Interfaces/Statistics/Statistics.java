package Interfaces.Statistics;

import java.time.Duration;
import java.time.Instant;

public class Statistics extends SecurityManager{
    Instant StartTime;
    Instant EndTime;
    /** Flag to indicate whether the timer is running. */
    private Boolean IsRunning;
    /** Counter for the number of times the function has been called */
    Integer Counter;

    public Statistics() {
        StartTime = null;
        EndTime = null;
        IsRunning = null;
        Counter = 0;
    }

    /**
     * Starts the timer
     */
    public void Start() {
        StartTime = Instant.now();                      // Set the start time
        IsRunning = true;                               // Set the timer to running
        EndTime = null;                                 // Reset the end time
    }

    /**
     * Stops the timer
     */
    public void End() {
        EndTime = Instant.now();                        // Set the end time
        IsRunning = false;                              // Set the timer to not running
    }

    /**
     * Returns the time elapsed since the timer was started
     * @return Duration between the Run and End Instances.
     */
    public Duration getDuration() {
        assert !IsRunning;                              // If the timer is running, the duration is not correct
        return Duration.between(StartTime, EndTime);    // Returns the duration between the start and end time
    }
    
    /**
     * Increments the counter by a specified amount
     * @param increment amount to increment the counter by
     */
    public void Increment(int increment){
        Counter += increment;
    }
    
    /**
     * Increments the counter
     */
    public void Increment(){
        Counter++;
    }

    /**
     * Returns the current value of the counter
     * @return int value of the counter
     */
    public int getCounter(){
        return Counter;
    }

    /**
     * Resets the counter
     */
    public void Reset(){
        Counter = 0;
    }
}
