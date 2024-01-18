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
        // Call main.py
        ProcessBuilder mainProcessBuilder = new ProcessBuilder("python", "core\\python\\main.py");
        mainProcessBuilder.redirectErrorStream(true);

        try {
            Process process = mainProcessBuilder.start();

            try (BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = processReader.readLine()) != null) {
                    System.out.println(line);
                }
            // Call board_matrix method from custEnv.py
            ProcessBuilder boardMatrixProcessBuilder = new ProcessBuilder("python", "core\\python\\custEnv.py", "print_game_state");
            boardMatrixProcessBuilder.redirectErrorStream(true);
    
            try {
                Process boardMatrixProcess = boardMatrixProcessBuilder.start();
    
                try (BufferedReader processReader2 = new BufferedReader(new InputStreamReader(boardMatrixProcess.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
    
                    String line2;
    
                    while ((line2 = processReader2.readLine()) != null) {
                        System.out.println(line2);
                        writer.write(line2);
                        writer.newLine();
                    }
    
                    System.out.println("Output saved to output.txt");
                }
    
                int boardMatrixExitCode = boardMatrixProcess.waitFor();
                System.out.println("Otput save exited with code: " + boardMatrixExitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            }

            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}

