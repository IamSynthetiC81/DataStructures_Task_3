import Bplus.bptree.BPlusConfiguration;
import Bplus.bptree.BPlusTree;
import Bplus.bptree.BPlusTreePerformanceCounter;
import Bplus.util.InvalidBTreeStateException;
import Experimental.WriteDataToExcel;
import Files.FileData;
import Interfaces.Statistics.StatisticsInterface;
import Interfaces.TucTree;
import util.HijackConsole;
import util.ProgressBarInterface;

import java.io.*;

public class Task_A extends FileData implements ProgressBarInterface, HijackConsole {

    /** Used to Write data to excel  */
    private static WriteDataToExcel ExcelOutput;
    /** The user can select whether to show the progress bar or not. */
    private static final boolean ShowProgressBar = true;
    /** The user can select whether to output the results to an Excel file or not.
     * <p>
     *     Currently not working
     * </p>
     */
    private static final boolean OutputToExcel = false;
    /** The user can select whether to validate the tree after each process or not. */
    private static final boolean TestTreeValidity = true;
    /**
     * <h2>Important</h2>
     * The user can select whether to perform the {@link #QuickTestRoutine(int, boolean)}, or the {@link #TestRoutine(int, boolean)}.
     * @implNote see {@link #QuickTestRoutine(int, boolean)} and {@link #TestRoutine(int, boolean)}
     * */
    private static final boolean QuickTest = true;


    private static BPlusConfiguration BPlusTreeConfigurator;
    private static BPlusTreePerformanceCounter BPlusTreePerformanceCounter;
    private static BPlusTree BPlusTree;

    public static void main(String[] args) throws InvalidBTreeStateException, IOException {
        int[][] ResultsFor128;
        int[][] ResultsFor256;

        ReadDataFromFiles();                                    // Read the data from the files

        if(QuickTest) {

            HijackConsole.RunOnDefaultConsole(() -> {
                System.out.println("Quick Test is Enabled \n" +
                        "This will perform a quick test of the tree. See QuickTestRoutine method for " +
                        "more details\n"
                        + "This will take a about 60-80 minutes to complete.\n");
            });

            /*
             * This process, for each take, will take 10^5 + 500 increments of the progress bar.
             *
             * 10^5 for adding the data to the tree
             * 300 for the quick test routine
             *
             * therefore the total increments will be :
             * 10*(10^5 + 500) = 10^6 + 5000 = 1005000
             *
             * Because we run the test twice we double that.
             */
            ProgressBarInterface.IncrementalMode(true, 1005000*2);
        } else {

            HijackConsole.RunOnDefaultConsole(() -> {
                System.out.println("Quick Test is Disabled \n" +
                        "This will perform a thorough test of the tree. See TestRoutine method for" +
                        "more details\n"
                        + "This will take a about 5 hours to complete.\n");
            });

            /*
             * This process, for each take, will take i*10^5 + 300 increments of the progress bar.
             *
             * i*10^5 is for building the tree
             * 300 is for the test routine
             *
             * therefore the total increments, for i = 1 to 10, is:
             *  (1+2+3+...+10)*10^5 + 10*300
             *  55 * 10^5 + 3000 = 5503000
             */
            ProgressBarInterface.IncrementalMode(true, 5503000*2);
        }

        /*  if enabled, show the progress bar */
        if(ShowProgressBar)
            HijackConsole.RunOnDefaultConsole(ProgressBarInterface::StartProgressBar);

        HijackConsole.RunOnDefaultConsole( ()-> {
                System.out.println("\n============================-128-=============================\n");});

        if(QuickTest)                                 			// If the user wants to perform the quick test routine
            ResultsFor128 = QuickTestRoutine(128, true);        // Perform the quick test routine
        else
            ResultsFor128 = TestRoutine(128, true);             // Perform the normal test routine
        HijackConsole.RunOnDefaultConsole( ()-> {
            System.out.println("\n============================-256-=============================\n");});
        if(QuickTest) {                                         // If the user wants to perform the quick test routine
            ResultsFor256 = QuickTestRoutine(256, true);        // Perform the quick test routine
        } else
            ResultsFor256 = TestRoutine(256, true);             // Perform the normal test routine

        if(OutputToExcel) {
            ExcelOutput = new WriteDataToExcel();
            ExcelOutput.Write("Page Size = 128b", ResultsFor128);
            ExcelOutput.Write("Page Size = 256b", ResultsFor256);
        }
    }

    static int[][] QuickTestRoutine(int PageSize, boolean Verbose) throws InvalidBTreeStateException, IOException {
        int[][] Results = new int[3][10];

        HijackConsole.hijackConsoleToNull();
            /* Create a new B+ tree */
            BPlusTreeConfigurator = new BPlusConfiguration(PageSize, 8, 8);
            BPlusTreePerformanceCounter = new BPlusTreePerformanceCounter(true);
            BPlusTree = new BPlusTree(BPlusTreeConfigurator, "rw+", BPlusTreePerformanceCounter);

        HijackConsole.hijackConsoleToFile("./Results/TaskA-"+PageSize+"b-Results-QuickTestRoutine.txt");

        /* Testing */
        for(int i = 0 ; i < 10 ; i++){                          // 10 times
            for(int j = 0 ; j < 100000 ; j++){                  // - Insert 10^5 keys
                int key = Data[i*100000 + j];

                HijackConsole.RunWithoutConsole(() -> {
                    try {
                        BPlusTree.insertKey(key, String.valueOf(key), true);
                    } catch (IOException | InvalidBTreeStateException e) {
                        throw new RuntimeException(e);
                    }
                });

                /*
                *   Ideally the progress bar should be able to access
                *   this variable on its own, to avoid unnecessary writes.
                *   TODO: Make this happen.
                */
                ProgressBarInterface.IncrementProgress();
            }
            int finalI = i;
            HijackConsole.RunOnHijackedConsole( ()-> {
                System.out.println("\rFor 10^" + (finalI +1) + " keys, the degree is : " + BPlusTreeConfigurator.getTreeDegree() + "," + BPlusTreeConfigurator.getLeafNodeDegree());

                Results[0][finalI] = Insert(BPlusTree, InsertData,  Verbose);    // - Insert the 100 keys into the tree
                Delete(BPlusTree, InsertData, false);                       // - Delete the keys previously inserted

                Results[1][finalI] = Delete(BPlusTree, DeleteData, Verbose);     // - Delete the 100 keys from the tree
                Insert(BPlusTree, DeleteData,  false);                      // - Insert the keys previously deleted

                Results[2][finalI] = Search(BPlusTree, SearchData, Verbose);     // - Search for the 100 keys in the tree
            });


            HijackConsole.hijackConsoleToDefault();
        }
        return Results;
    }

    static int[][] TestRoutine(int PageSize, boolean Verbose) throws InvalidBTreeStateException, IOException {
        int[][] Results = new int[3][10];

        HijackConsole.hijackConsoleToNull();

        /* Create a new B+ tree */
        BPlusTreeConfigurator = new BPlusConfiguration(PageSize, 8, 8);
        BPlusTreePerformanceCounter = new BPlusTreePerformanceCounter(true);
        BPlusTree = new BPlusTree(BPlusTreeConfigurator, "rw+", BPlusTreePerformanceCounter);

        HijackConsole.hijackConsoleToFile("./Results/TaskA-"+PageSize+"b-Results-TestRoutine.txt");

        /* Testing */
        for(int i = 0 ; i < 10 ; i++){                                  // 10 times
            Reset((i+1)*100000);                                        // - Reset the data array to the next 100000 keys

            int finalI = i;
            HijackConsole.RunOnHijackedConsole( ()-> {
                System.out.println("\rFor 10^" + (finalI +1) + " keys");

                Results[0][finalI] = Insert(BPlusTree, InsertData, Verbose);     // - Insert the 100 keys into the tree
                Results[1][finalI] = Delete(BPlusTree, DeleteData, Verbose);     // - Delete the 100 keys from the tree
                Results[2][finalI] = Search(BPlusTree, SearchData, Verbose);     // - Search for the 100 keys in the tree
            });
        }

        HijackConsole.hijackConsoleToDefault();

        return Results;
    }

    static int Insert(BPlusTree tree, int[] data, boolean Verbose) {
        int Writes = 0;                                             // Number of writes
        int Reads  = 0;                                             // Number of reads

        HijackConsole.hijackConsoleToNull();

        for (int datum : data) {
            try {
                int[] result = tree.getPerformanceClass().insertIO(datum, String.valueOf(datum), true, false);
                Reads += result[0];                                     // - Add the number of reads
                Writes += result[1];                                    // - Add the number of writes
            } catch (InvalidBTreeStateException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
            ProgressBarInterface.IncrementProgress();
        }

        if(Verbose)                                                 /* If Verbose is true
																		Print the statistics of the testing
																	*/ {
            int finalReads = Reads;
            int finalWrites = Writes;
            HijackConsole.RunOnHijackedConsole( ()-> {
                System.out.printf("\tInsert Accesses : %4.2f\n",((float)(finalReads + finalWrites))/InsertData.length);
            });
        }


        /* this return the TOTAL number of search operations performed, not the mean */
        return Reads + Writes;
    }

    static int Search(BPlusTree tree, int[] data, boolean Verbose) {
        int Writes = 0;                                             // The number of Writes
        int Reads  = 0;                                             // The number of Reads

        HijackConsole.hijackConsoleToNull();

        for (int datum : data) {
            try {
                int[] result = tree.getPerformanceClass().searchIO(datum, true, false);
                Reads += result[0];                                     // - Add the number of reads
                Writes += result[1];                                    // - Add the number of writes
            } catch (InvalidBTreeStateException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
	
            ProgressBarInterface.IncrementProgress();
        }

        if(Verbose)                                                 /* If Verbose is true
																		Print the statistics of the testing
																	*/ {
            int finalReads = Reads;
            int finalWrites = Writes;
            HijackConsole.RunOnHijackedConsole(() -> {
                System.out.printf("\tSearch Accesses : %4.2f\n",((float)(finalReads + finalWrites))/SearchData.length);
            });
        }

        /* this return the TOTAL number of search operations performed, not the mean */
        return Reads + Writes;
    }

    static int Delete(BPlusTree tree, int[] data, boolean Verbose) {
        int Reads  = 0;                                             // The number of Reads
        int Writes = 0;                                             // The number of Writes

        HijackConsole.hijackConsoleToNull();

        for (int datum : data) {
            try {
                int[] result = tree.getPerformanceClass().deleteIO(datum, true, false);
                Reads += result[0];                                     // - Add the number of reads
                Writes += result[1];                                    // - Add the number of writes
            } catch (InvalidBTreeStateException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }

            ProgressBarInterface.IncrementProgress();
        }

        if(Verbose)                                                 /* If Verbose is true
																		Print the statistics of the testing
																	*/ {
            int finalReads = Reads;
            int finalWrites = Writes;
            HijackConsole.RunOnHijackedConsole( ()-> {
                System.out.printf("\tDelete Accesses : %4.2f\n",((float)(finalReads + finalWrites))/DeleteData.length);
            });
        }

        /* this return the TOTAL number of search operations performed, not the mean */
        return Reads + Writes;
    }

    /**
     * Fills the tree with numberOfData number of elements from the {@link #Data} array
     * @param tree The tree to fill
     * @param numberOfData The number of elements to fill the tree with
     * @param <T> The type of the tree
     *           <p>
     *               (Must implement {@link StatisticsInterface} and {@link TucTree})
     *           </p>
     */
    private static <T extends TucTree> void FillTree(T tree, int numberOfData) {
        /* Housekeeping */
        assert numberOfData >= 0 : "numberOfData can not be a negative number";
        assert numberOfData <= Data.length: "numberOfData exceeds the size of the data array";

        HijackConsole.hijackConsoleToNull();

        for (int i = 0; i < numberOfData; i++) {                    // For each element in the array
            tree.insert(Data[i]);                                   // Insert the element in the tree

            ProgressBarInterface.IncrementProgress();
        }

        HijackConsole.hijackConsoleToPreviousConsole();

        /* this can be used to check if the tree is valid */
        if(TestTreeValidity)
            assert tree.isValidTree() : "Tree is not valid";        // Check if the tree is valid
    }

    /**
     * Resets and fills the tree with the {@link #Data} array
     * <ul>
     *     <li>Resets the tree by calling {@link TucTree#ResetTree()}</li>
     *     <li>Fills the tree by calling {@link #FillTree(TucTree, int)}</li>
     *
     * @param numberOfData The number of elements to fill the tree with
     */
    private static void Reset(int numberOfData){
        HijackConsole.RunWithoutConsole(()-> {
            BPlusTree = (BPlusTree) BPlusTree.ResetTree();          // - Reset the tree
        });

        FillTree(BPlusTree, numberOfData);                          // - Fill the tree with 10^i keys
    }
}


