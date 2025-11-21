package com.devstack.pos.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BackupUtil {
    public static String createBackup(String host, String port, String database,
                                      String user, String password) throws IOException {

        // Full path to mysqldump
        String dumpPath = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe";

        // Command as array (best for Windows)
        String[] command = {
                dumpPath,
                "-h", host,
                "-P", port,
                "-u", user,
                "-p" + password,
                "--databases", database
        };

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true); // merge stdout + stderr

        Process process = pb.start();

        // Read output
        StringBuilder dumpData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dumpData.append(line).append(System.lineSeparator());
            }
        }

        return dumpData.toString();
    }

}
