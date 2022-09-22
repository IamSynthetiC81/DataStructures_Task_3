package Interfaces.Statistics;

import java.time.Duration;

public interface StatisticsInterface {
    
    Statistics statistics = new Statistics();

    /**
     * To use, call {@link Statistics#Start()} before the operation and
     * {@link Statistics#End()} after.
     *
     * @return the duration of the last operation
     */
    default Duration getDuration() {
        return statistics.getDuration();
    }

    /**
     * Returns the number of time the {@link Statistics#Counter} was incremented.
     *
     * @return the number of accesses to the array.
     * @implSpec The counter must be reseted after each operating with the {@link #Reset()} function to
     * avoid overlapping of the counter.
     */
    default int getCounter() {
        return statistics.getCounter();
    }

    /**
     * Resets the {@link Statistics counter} to zero.
     * @implNote Must be called after each operation to avoid overlapping of the counter.
     */
    default void Reset() {
        statistics.Reset();
    }
    
    /**
     * Increments the {@link Statistics counter} by the specified amount.
     * @param increment the number of times to increment the counter.
     * @implNote It is best to <i>hide</i> the data we are counting
     *           behind some getter.
     */
    default void Increment(int increment) {
        statistics.Increment(increment);
    }
    
    /**
     * Increments the {@link Statistics counter} by one.
     * @implNote It is best to <i>hide</i> the data we are counting
     *          behind some getter.
     */
    default void Increment() {
        statistics.Increment();
    }

}
