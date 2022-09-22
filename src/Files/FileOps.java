package Files;

import java.io.*;

public class FileOps {
    File file;
    RandomAccessFile raf;

    public FileOps(File file) {
        this.file = file;
        try {
            this.raf = new RandomAccessFile(file.getPath(), "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileOps(String path) {
        this.file = new File(path);
        assert file.exists()  : "File was not found";
        try {
            this.raf = new RandomAccessFile(file.getPath(), "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Close() throws IOException {
        raf.close();
    }


    public int[] Read() throws IOException {

        byte[] bytes = new byte[(int) raf.length()];

        raf.seek(0);
        raf.read(bytes);

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        int[] data = new int[bytes.length / 4];
        for (int i = 0; i < data.length; i++) {
            data[i] = dis.readInt();
        }
        return data;
    }

    public void write(int b) {


    }
}
