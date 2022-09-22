package util;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class TimeEstimation {

    static int ArraySize = 150000;
    static private final long[] est = new long[ArraySize];
    static private int estIndex = 0;
    static private Instant Prev;
    static public boolean EstimationReady = false;


    public static double EstimateTime() {
        if(Prev == null){
            EstimationReady = false;
            Prev = Instant.now();
            return 0;
        }

        Instant Curr = Instant.now();
        Duration d = Duration.between(Prev, Curr);
        Prev = Curr;

        est[estIndex++] = d.toNanos();

        if (estIndex == ArraySize)
            estIndex = 0;

        EstimationReady = true;

        return Arrays.stream(est).filter(x -> x > 0).summaryStatistics().getAverage();
    }
}
