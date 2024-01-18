package com.mygdx.game.Agents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class callPython {
    public static void main(String[] args) throws Exception {
        //StringBuilder result = new StringBuilder("0");
        ProcessBuilder processBuilder = new ProcessBuilder("python", "core\\python\\main.py");
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
                // Read the output of the Python script
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Wait for the process to finish
        int exitCode = process.waitFor();
        System.out.println("Python script exited with code: " + exitCode);
    }
}
