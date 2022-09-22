import AVL.AVLTree;
import Bminus.BTree;
import Experimental.WriteDataToExcel;
import Files.FileData;
import Interfaces.Statistics.StatisticsInterface;
import Interfaces.TucTree;
import util.HijackConsole;

import java.io.IOException;

import static java.lang.Math.pow;

public class Task_B extends FileData {
    /** Used to enable the use of {@link TucTree#isValidTree()} for each tree
     *
     * <p>
     *     The user can select whether to validate the tree or not.
     * </p>
     * <p>
     *     Used for debugging purposes.
     * </p>
     * <p>
     *     Not every tree has this function implemented, yet.
     *     @TODO : implement Validation Routines
     * </p>
     */
    private static boolean TestTreeValidity = false;
    /** Used to enable outputting the results into an Excel file
     *      <ul>
     *          Do not enable. Will crush
     */
    private static boolean OutputToExcel = false;
    /** Holds the results of the AVL tree */
    static int[][] Results_AVL      = new int[10][3];
    /** Holds the results of the B-Tree, 3d Degree */
    static int[][] Results_BTREE_3  = new int[10][3];
    /** Holds the results of the B-Tree, 64th Degree */
    static int[][] Results_BTREE_64 = new int[10][3];


    public static void main(String[] args) throws IOException {
        ReadDataFromFiles();                                                    // Read the data from the files

        BTree Btree = new BTree(3);                                             // Create a B-Tree 
        AVLTree AVLTree = new AVLTree();                                        // Create an AVL Tree

        System.out.println("\n========================- 3  -========================");

        HijackConsole.hijackConsoleToFile("./Results/TaskB-3-Results.txt");     // Hijack the console output to a file

        for(int i = 0 ; i < 10 ; i++) {
            ResetTree(Btree, i*100000);                                        	// Reset the B-Tree

            System.out.println("\t~===================-10^"+ (i+1)+"-===================~");
            Results_BTREE_3[i] = Test(Btree, i*100000, true);               	// QuickTestRoutine the tree
        }

        HijackConsole.hijackConsoleToDefault();                                 // Hijack the console output to the default output stream

        System.out.println("\n========================- 64 -========================");

        HijackConsole.hijackConsoleToFile("./Results/TaskB-64-Results.txt");	// Hijack the console output to a file

        for(int i = 0 ; i < 10 ; i++) {
            ResetTree(Btree, i*100000);                                         // Reset the B-Tree

            System.out.println("\t~===================-10^"+ (i+1)+"-===================~");
            Results_BTREE_64[i] = Test(Btree, i*100000, true);              	// QuickTestRoutine the tree
        }

        HijackConsole.hijackConsoleToDefault();                                 // Hijack the console output to the default output stream

        System.out.println("\n========================-AVL -========================");

        HijackConsole.hijackConsoleToFile("./Results/TaskB-AVL-Results.txt");	// Hijack the console output to a file

        for(int i = 0 ; i < 10 ; i++) {
            ResetTree(AVLTree, i*100000);                                       // Reset the AVL Tree

            System.out.println("\t~===================-10^"+ (i+1)+"-===================~");
            Results_AVL[i] = Test(AVLTree,i*100000, true);                  	// QuickTestRoutine the tree
        }

        HijackConsole.hijackConsoleToDefault();                                 // Hijack the console output to the default output streamS

        if(OutputToExcel)
            WriteToExcel();

    }

    /**
     * Resets the tree, and fills it with data from the {@link #Data} array
     * @param Tree The tree to reset
     * @param numberOfData The number of data from the {@link #Data} array to add to the tree
     * @param <T> The type of the tree
     */
    @SuppressWarnings("unchecked")
    static <T extends StatisticsInterface & TucTree> void ResetTree(T Tree, int numberOfData) {
        Tree = (T) Tree.ResetTree();                                            // Reset the tree
        FillTree(Tree, numberOfData);                                           // Fill the tree with the data
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
    static <T extends StatisticsInterface & TucTree> void FillTree(T tree, int numberOfData) {
        /* Housekeeping */
        assert numberOfData >= 0 : "numberOfData can not be a negative number";
        assert numberOfData <= Data.length: "numberOfData exceeds the size of the data array";


        for (int i = 0; i < numberOfData; i++)                                  // For each element in the array
            tree.insert(Data[i]);                                               // Insert the element in the tree

        /* this can be used to check if the tree is valid */
        if(TestTreeValidity)
            assert tree.isValidTree() : "Tree is not valid";                    // Check if the tree is valid
    }

    /**
     * Performs the testing of the tree
     * @param tree The tree to test
     * @param DataLength The number of elements to test the tree with
     * @param Verbose If true, the testing will be verbose
     * @param <T> The type of the tree
     *           <p>
     *               (Must implement {@link StatisticsInterface} and {@link TucTree})
     *           </p>
     * @return The results of the testing
     */
    static <T extends StatisticsInterface & TucTree> int[] Test(T tree, int DataLength, boolean Verbose) {
        int[] Results = new int[3];

        Results[0] = Insert(tree, Verbose);                                     // Insertion Testing
        ResetTree(tree , DataLength);                                           // Reset the tree

        Results[1] = Delete(tree, Verbose);                                     // Deletion Testing
        ResetTree(tree , DataLength);                                           // Reset the tree

        Results[2] = Search(tree, Verbose);                                     // Search Testing

        Results[0] = (int) (Results[0]/pow(10,DataLength));                     // Average Insertion results
        Results[1] = (int) (Results[1]/pow(10,DataLength));                     // Average Deletion results
        Results[2] = (int) (Results[2]/pow(10,DataLength));                     // Average Search results

        return Results;
    }

    /**
     * Performs the insertion testing of the tree
     * @param tree The tree to test
     * @param Verbose If true, the testing will be verbose
     * @return The statistics of the testing
     * @param <T> The type of the tree
     *           <p>
     *               (Must implement {@link StatisticsInterface} and {@link TucTree})
     *           </p>
     */
    static <T extends StatisticsInterface & TucTree> int Insert(T tree, boolean Verbose) {
        tree.Reset();                                                       	// Reset the tree

        for (int insertDatum : InsertData)                                  	// For each element in the InsertData array
            tree.insert(insertDatum);                                       	// Insert the element in the tree

        if(TestTreeValidity)
            assert tree.isValidTree() : "Tree is not valid";                    // Check if the tree is valid
        if (Verbose)                                                        	/* If Verbose is true
																					Print the statistics of the testing
																				*/
            System.out.printf("\t Insert operations : %4.2f\n", ((float)tree.getCounter())/InsertData.length);


        /* this return the TOTAL number of insert operations performed, not the mean */
        return tree.getCounter();
    }

    /**
     * Performs the search testing of the tree
     * @param tree The tree to test
     * @param Verbose If true, the testing will be verbose
     * @return The statistics of the testing
     * @param <T> The type of the tree
     *           <p>
     *               (Must implement {@link StatisticsInterface} and {@link TucTree})
     *           </p>
     */
    static <T extends StatisticsInterface & TucTree> int Search( T tree, boolean Verbose) {
        tree.Reset();                                                       	// Reset the tree
		
        for (int insertDatum : SearchData)                                  	// For each element in the SearchData array
            tree.search(insertDatum);                                       	// Search the element in the tree
		
        if (Verbose)                                                        	/* If Verbose is true
																					Print the statistics of the testing
																				*/
            System.out.printf("\t Search operations : %4.2f\n",((float)tree.getCounter())/SearchData.length);

        /* this return the TOTAL number of search operations performed, not the mean */
        return tree.getCounter();
    }

    /**
     * Performs the deletion testing of the tree
     * @param tree The tree to test
     * @param Verbose If true, the testing will be verbose
     * @return The statistics of the testing
     * @param <T> The type of the tree
     *           <p>
     *               (Must implement {@link StatisticsInterface} and {@link TucTree})
     *           </p>
     */
    static <T extends StatisticsInterface & TucTree> int Delete( T tree, boolean Verbose) {
        tree.Reset();                                                       	// Reset the tree
		
        for (int insertDatum : DeleteData)                                  	// For each element in the DeleteData array
            tree.delete(insertDatum);                                       	// Delete the element in the tree
		if(TestTreeValidity)
            assert tree.isValidTree() : "Tree is not valid";                    // Check if the tree is valid
        if (Verbose)                                                        	/* If Verbose is true
																					Print the statistics of the testing
																				*/
            System.out.printf("\t Delete operations : %4.2f\n", ((float)tree.getCounter())/DeleteData.length);

        /* this return the TOTAL number of delete operations performed, not the mean */
        return tree.getCounter();
    }

    /*  ==========================================================  */

    /**
     * Choose whether to test the tree validity when inserting or deleting
     * <p>
     *     <b>Can be time consuming</b>
     * </p>
     * @param testTreeValidity If true, the tree will be tested
     */
    public static void setTestTreeValidity(boolean testTreeValidity) {
        TestTreeValidity = testTreeValidity;
    }

    /**
     * Choose whether to print out data into an Excel spreadsheet
     * @param outputToExcel If true, the data will be printed out.
     * @deprecated  NOT IMPLEMENTED
     */
    @Deprecated
    public static void setOutputToExcel(boolean outputToExcel) {
        OutputToExcel = outputToExcel;
    }

    /**
     * Writes the data to an Excel spreadsheet
     * <p>
     *     The data that are written :
     *     <ul>
     *         <li>{@link #Results_AVL} - Results for AVL tree</li>
     *         <li>{@link #Results_BTREE_3} - Results for BTree, Degree 3</li>
     *         <li>{@link #Results_BTREE_64} - Results for BTree, Degree 64</li>
     * </p>
     * @throws IOException If the file cannot be written
     * @deprecated NOT IMPLEMENTED
     * <p>
     *      <b>Will crash the program</b>
     * </p>
     */
    @Deprecated
    public static void WriteToExcel() throws IOException {

        System.out.println("Calling method WriteToExcel()\nThis method is " +
                "not implemented and will crush the program");

        WriteDataToExcel ExcelOutput = new WriteDataToExcel();
        ExcelOutput.Write("Page Size = 128b", Results_AVL);
    }

}
