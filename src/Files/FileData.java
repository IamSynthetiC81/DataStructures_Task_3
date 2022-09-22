package Files;

import java.io.IOException;

public class FileData {
    /** Array where the data from the <a href="/Files/keys_1000000_BE.bin">keys_1000000_BE.bin</a> file are stored */
    protected static int[] Data       = new int[1000000];
    /** Array where the data from the <a href="/Files/keys_1000000_BE.bin">keys_insert_100_BE.bin</a> file are stored */
    protected static int[] InsertData = new int[100];
    /** Array where the data from the <a href="/Files/keys_1000000_BE.bin">keys_delete_100_BE.bin</a> file are stored */
    protected static int[] DeleteData = new int[100];
    /** Array where the data from the <a href="/Files/keys_1000000_BE.bin">keys_search_100_BE.bin</a> file are stored */
    protected static int[] SearchData = new int[100];
    /**
     * Reads the data from the <a href="/Files"> Files</a> and stores them to their respected arrays.
     * <p>
     *     Data Read :
     *     <ul>
     *         <li>{@link #Data}        <- <a href="/Files/keys_1000000_BE.bin">keys_1000000_BE.bin</a>     </li>
     *         <li>{@link #InsertData}  <- <a href="/Files/keys_1000000_BE.bin">keys_insert_100_BE.bin</a>  </li>
     *         <li>{@link #DeleteData}  <- <a href="/Files/keys_1000000_BE.bin">keys_delete_100_BE.bin</a>  </li>
     *         <li>{@link #SearchData}  <- <a href="/Files/keys_1000000_BE.bin">keys_search_100_BE.bin</a>  </li>
     * </p>
     * @throws IOException If the file is not found or if the file is not readable.
     */
    public static void ReadDataFromFiles() throws IOException {
        FileOps DataFile = new FileOps("src/Files/keys_1000000_BE.bin");
        Data = DataFile.Read();
        assert Data[Data.length - 1] == 1 : "Data is not correct";

        FileOps InsertFile = new FileOps("src/Files/keys_insert_100_BE.bin");
        InsertData = InsertFile.Read();

        FileOps DeleteFile = new FileOps("src/Files/keys_delete_100_BE.bin");
        DeleteData = DeleteFile.Read();

        FileOps SearchFile = new FileOps("src/Files/keys_search_100_BE.bin");
        SearchData = SearchFile.Read();
    }
}
