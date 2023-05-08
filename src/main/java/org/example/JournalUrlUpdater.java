package org.example;

import java.io.IOException;
import java.util.Map;

public class JournalUrlUpdater {

    public static void main(String[] args) {
        String myLocation = "C:\\Users\\mmorgan\\OneDrive - EBSCO Industries\\Documents\\java\\";
        String excelFilePath = myLocation + "afh-journals.xlsx";
        String txtFilePath = myLocation + "a9h-all (1).txt";

        var journalProcessor = new JournalProcessor();

        try {
            Map<String, String> urlMap = journalProcessor.readUrlsFromTxtFile(txtFilePath);
            journalProcessor.updateExcelFileWithUrls(excelFilePath, urlMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
