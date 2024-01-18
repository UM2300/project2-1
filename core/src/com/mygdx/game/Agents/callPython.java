package com.mygdx.game.Agents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class callPython {
    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Press 1 to play Baseline vs MCTS");
            System.out.println("Press 2 to call main.py");
            System.out.println("Press 3 to exit");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    callBaselineVS_MCTS();
                    break;
                case "2":
                    callPythonML();
                    break;
                case "3":
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void callBaselineVS_MCTS() {
        Baseline_VS_MCTS gameController = new Baseline_VS_MCTS();
        gameController.playGame();
        //gameController.experiment();
    }

    private static void callPythonML() {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "core\\python\\main.py");
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            try (Scanner processScanner = new Scanner(process.getInputStream())) {
                StringBuilder outputBuilder = new StringBuilder();
                boolean captureOutput = false;

                while (processScanner.hasNextLine()) {
                    String line = processScanner.nextLine();
                    outputBuilder.append(line).append("\n");
                    System.out.println(line);

                    // Check if the line contains the start of the relevant information
                    if (line.contains("Game Board State:")) {
                        captureOutput = true;
                    }

                    // Stop capturing output after the end of the relevant information
                    if (line.contains("Episode") && line.contains("completed in")) {
                        captureOutput = false;
                        break;  // Exit the loop after capturing the relevant part
                    }
                }

                // Save the captured output to a file (you can modify the filename as needed)
                saveOutputToFile(outputBuilder.toString(), "output.txt");
            }

            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void saveOutputToFile(String output, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(output);
            System.out.println("Output saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

