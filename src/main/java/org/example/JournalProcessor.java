package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JournalProcessor {

    public Map<String, String> readUrlsFromTxtFile(String txtFilePath) throws IOException {
        Map<String, String> urlMap = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(txtFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");

                if (parts.length >= 4) {
                    String url = parts[3].trim();
                    String mid = parts[parts.length - 1].trim();

                    // Remove double quotes from the MID value
                    mid = mid.replace("\"", "");

                    if (url.startsWith("http") && !mid.isEmpty()) {
                        urlMap.put(mid, url);
                    }
                }
            }
        }

        return urlMap;
    }




    public void updateExcelFileWithUrls(String excelFilePath, Map<String, String> urlMap) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File(excelFilePath))) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            int startRowIndex = 6;

            for (int rowIndex = startRowIndex; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell midCell = row.getCell(13); // MID is in column N (index 13)

                    if (midCell != null) {
                        String mid = midCell.getStringCellValue();

                        if (urlMap.containsKey(mid)) {
                            Cell urlCell = row.createCell(16, CellType.STRING); // Column Q is the 17th column (index 16)
                            urlCell.setCellValue(urlMap.get(mid));
                        }
                    }
                }
            }

            fis.close();

            // Write the updated workbook to the file
            try (FileOutputStream fos = new FileOutputStream(new File(excelFilePath))) {
                workbook.write(fos);
            }
        }
    }


}
