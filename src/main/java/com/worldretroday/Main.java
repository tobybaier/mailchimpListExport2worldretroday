package com.worldretroday;

import java.io.*;
import java.util.ArrayList;

public class Main {

    private static final String OUTPUT_NAME_PREFIX = "wrd2019-";
    private static String INPUT_FILE = "subscribed_members_export.csv";

    public static void main(String[] args) {
        if (args.length == 1) {
            INPUT_FILE = args[0];
        }
        ArrayList<WRDEntry> entries = readCSV();
        writeJSON(entries);
    }

    private static ArrayList<WRDEntry> readCSV() {

        BufferedReader reader = null;
        String line;
        String cvsSplitBy = ",";

        ArrayList<WRDEntry> entries = new ArrayList<WRDEntry>();

        try {

            reader = new BufferedReader(new FileReader(INPUT_FILE));
            boolean skippedHeadings = false; // don't need those
            while ((line = reader.readLine()) != null) {
                if (skippedHeadings) {
                    // use comma as separator
                    String[] lineEntries = line.split(cvsSplitBy);

                    WRDEntry entry = new WRDEntry();
                    entry.setId(cleanUpString(lineEntries[24]));
                    entry.setModerators(cleanUpString(lineEntries[1] + " " + cleanUpString(lineEntries[2])));
                    entry.setTitle(cleanUpString(lineEntries[3]));
                    entry.setCity(cleanUpString(lineEntries[4]));
                    entry.setUrl(cleanUpString(lineEntries[5]));
                    entry.setLatitude(cleanUpString(lineEntries[7]));
                    entry.setLongitude(cleanUpString(lineEntries[8]));
                    entry.setUtcOffset(cleanUpString(lineEntries[17]));
                    entry.setTimezone(cleanUpString(lineEntries[18]));
                    entry.setCountry(cleanUpString(lineEntries[20]));

                    entries.add(entry);
                } else {
                    skippedHeadings = true;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return entries;
    }

    private static String cleanUpString(String string) {
        string =  string.replaceAll("\"","");
        string =  string.replaceAll("'","");
        return string;
    }


    private static void writeJSON(ArrayList<WRDEntry> entries) {
        int counter = 0;
        for (WRDEntry entry : entries
        ) {
            File file = new File(OUTPUT_NAME_PREFIX + entry.getId() + ".json");
            try {
                if (file.createNewFile()) {
                    counter++;
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write("{\n");
                    fileWriter.write("  \"title\": \"" + entry.getTitle() + "\",\n");
                    fileWriter.write("  \"url\": \"" + entry.getUrl() + "\",\n");
                    fileWriter.write("  \"moderators\": [\"" + entry.getModerators() + "\"],\n");
                    fileWriter.write("  \"location\": { \n");
                    fileWriter.write("    \"city\": \"" + entry.getCity() + "\" ,\n");
                    fileWriter.write("    \"country\": \"" + entry.getCountry() + "\",\n");
                    fileWriter.write("    \"coordinates\": { \n");
                    fileWriter.write("      \"latitude\": " + entry.getLatitude() + ",\n");
                    fileWriter.write("      \"longitude\": " + entry.getLongitude() + "},\n");
                    fileWriter.write("  \"utcOffset\": " + entry.getUtcOffset() + " ,\n");
                    fileWriter.write("  \"timezone\": \"" + entry.getTimezone() + " \"}}\n");
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(counter + " new files generated.");
    }

}
