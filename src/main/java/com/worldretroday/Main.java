package com.worldretroday;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

public class Main {

    private static final String OUTPUT_NAME_PREFIX = "wrd2019-";

    public static void main(String[] args) {
        try {
            readJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<WRDEntry> entries = null;
        try {
            entries = readJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeJSON(entries);
    }

    private static ArrayList<WRDEntry> readJson() throws IOException {

        ArrayList<WRDEntry> entries = new ArrayList<WRDEntry>();
        URL url = new URL("https://us19.api.mailchimp.com/3.0/lists/3b39ee2d1a/members?count=100");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);

        String authStringEnc = Base64.getEncoder().encodeToString(getAuthentication().getBytes());
        connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {

            byte[] jsonData = IOUtils.toByteArray(connection.getInputStream());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode membersNode = rootNode.path("members");
            Iterator<JsonNode> elements = membersNode.elements();
            while(elements.hasNext()){
                WRDEntry entry = new WRDEntry();
                JsonNode member = elements.next();
                JsonNode mergeFields = member.path("merge_fields");
                entry.setId(cleanUpString(member.path("unique_email_id").textValue()));
                entry.setModerators(cleanUpString(mergeFields.path("FNAME").textValue() + " " + cleanUpString(mergeFields.path("LNAME").textValue())));
                entry.setTitle(cleanUpString(mergeFields.path("MMERGE6").textValue()));
                JsonNode addressNode = mergeFields.path("ADDRESS");
                entry.setCity(cleanUpString(addressNode.path("city").textValue()));
                entry.setUrl(cleanUpString(mergeFields.path("MMERGE4").textValue()));
                entry.setLatitude(cleanUpString(mergeFields.path("MMERGE7").textValue()));
                entry.setLongitude(cleanUpString(mergeFields.path("MMERGE8").textValue()));
                entry.setUtcOffset(cleanUpString(""));
                entry.setTimezone(cleanUpString(member.path("location").path("timezone").textValue()));
                entry.setCountry(cleanUpString(addressNode.path("country").textValue()));
                entries.add(entry);
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
        int newFileCounter = 0;
        for (WRDEntry entry : entries) {
            File file = new File(OUTPUT_NAME_PREFIX + entry.getId() + ".json");
            try {
                if (file.createNewFile()) {
                    newFileCounter++;
                }
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
                fileWriter.write("  \"timezone\": \"" + entry.getTimezone() + "\"}}\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(counter + " files generated, " + newFileCounter + " of them new.");
    }

    private static String getAuthentication() {
        String everything = "";
        try (BufferedReader br = new BufferedReader(new FileReader("authentication.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return everything;
    }

}
