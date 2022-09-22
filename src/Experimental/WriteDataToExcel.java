package Experimental;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WriteDataToExcel {

    private final static String fileLocation = "Output.xlsx";
    FileInputStream file = new FileInputStream(new File(fileLocation));
    Workbook workbook = new XSSFWorkbook(file);


    private static final String Row1 = "Insert";
    private static final String Row2 = "Delete";
    private static final String Row3 = "Search";

    public WriteDataToExcel() throws IOException {}

    public void Write(String SheetName, int[][] Data) throws IOException {
        XSSFSheet sheet = (XSSFSheet) workbook.getSheet(SheetName);
        if(sheet == null)
            sheet = (XSSFSheet) workbook.createSheet(SheetName);


        for (int i = 0; i < Data.length; i++) {
            XSSFRow row = sheet.createRow(i);
            for (int j = 0; j < Data[i].length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(Data[i][j]);
            }
        }

        // This data needs to be written (Object[])
        Map<String, Object[]> Header = new TreeMap<String, Object[]>();
        Header.put("1", new Object[] { "Operation", "1*10^5", "2*10^5", "3*10^5", "4*10^5", "5*10^5", "6*10^5", "7*10^5", "8*10^5", "9*10^5", "10*10^5" });

        Set<String> HeaderString = Header.keySet();

        // creating a row object
        XSSFRow row;
        int rowIndex = 0;

        for (String key : HeaderString) {

            row = sheet.createRow(rowIndex++);
            Object[] objectArr = Header.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }

        for(int i = 0 ; i < Data.length ; i++ ){
            // This data needs to be written (Object[])
            Map<String, Object[]> Row = new TreeMap<String, Object[]>();
            Row.put("i + 1", new Object[]{"Insert", Data[i][1], Data[i][2], Data[i][3], Data[i][4], Data[i][5], Data[i][6], Data[i][7], Data[i][8], Data[i][9], Data[i][9]});

            Set<String> keyid = Row.keySet();

            for (String key : keyid) {
                row = sheet.createRow(rowIndex++);
                Object[] objectArr = Row.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue((String)obj);
                }
            }


        }

        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(
                new File("Output.xlsx"));

        workbook.write(out);
        out.close();
    }

}