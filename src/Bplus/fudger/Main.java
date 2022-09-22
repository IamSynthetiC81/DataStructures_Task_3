package Bplus.fudger;

import Bplus.bptree.BPlusConfiguration;
import Bplus.bptree.BPlusTree;
import Bplus.bptree.BPlusTreePerformanceCounter;
import Bplus.util.InvalidBTreeStateException;
import Bplus.util.TestRunner;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args)
            throws IOException, InvalidBTreeStateException {
        boolean fastTrials = true;
        boolean recreateTree = true;
        BPlusConfiguration btconf = new BPlusConfiguration();
        BPlusTreePerformanceCounter bPerf = new BPlusTreePerformanceCounter(true);
        BPlusTree bt = new BPlusTree(btconf, recreateTree ? "rw+" : "rw", bPerf);

        bt.printCurrentConfiguration();

        Instant start = Instant.now();
        int[] arr = ThreadLocalRandom.current().ints(0, Integer.MAX_VALUE).distinct().limit(100000).toArray();
        for (int i = 0 ; i < arr.length ; i++ ) {
            String val = String.valueOf(i);
            bt.insertKey(arr[i], val, true);
            System.out.printf("\r%02d%%", (i/100000)*100);
        }

//        if(fastTrials)
//            {TestRunner.runDefaultTrialsFast(bPerf);}
//        else
//            {TestRunner.runBench(bPerf);}

        System.out.println("Time taken: " + (Instant.now().toEpochMilli() - start.toEpochMilli()));
        System.out.println("\n -- Total pages in the end: " + bt.getTotalTreePages());
        // finally close it.
        bt.commitTree();

    }

}
