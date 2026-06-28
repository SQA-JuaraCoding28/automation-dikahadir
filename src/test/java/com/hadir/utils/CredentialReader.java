package com.hadir.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CredentialReader {
    public static Map<String, String> getCredentials() {
        Map<String, String> creds = new HashMap<>();
        // Try reading from Docs/Credential.md
        try (BufferedReader br = new BufferedReader(new FileReader("Docs/Credential.md"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("User Admin :")) {
                    creds.put("adminEmail", line.split("User Admin :")[1].trim());
                } else if (line.contains("Pass Admin :")) {
                    creds.put("adminPassword", line.split("Pass Admin :")[1].trim());
                }
            }
        } catch (IOException e) {
            // Log warning or fallback
            System.out.println("Could not read Docs/Credential.md. Using fallback credentials.");
        }
        
        // Add fallback if not read successfully
        if (!creds.containsKey("adminEmail")) {
            creds.put("adminEmail", "admin@hadir.com");
        }
        if (!creds.containsKey("adminPassword")) {
            creds.put("adminPassword", "MagangSQA_JC@123");
        }
        return creds;
    }
}
