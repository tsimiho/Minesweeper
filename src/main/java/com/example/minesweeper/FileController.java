package com.example.minesweeper;

import eu.hansolo.toolbox.tuples.Triplet;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.io.RandomAccessFile;

public class FileController {

    private String getProjectPath() {
        try {
            URL res = getClass().getResource("BoardFXML.fxml");
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath();
            int index1 = absolutePath.lastIndexOf("minesweeper");
            String p = absolutePath.substring(0, index1);
            int index2 = p.lastIndexOf("minesweeper");
            String projectPath = p.substring(0, index2);
            return projectPath;
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error");
            return e.getMessage();
        }
    }

    public Boolean ScenarioExists(String id) {
        String projectPath = getProjectPath();
        File file = new File(projectPath + "medialab/" + id + ".txt");
        return file.exists();

    }

    public int[] getGameDescription(String id) throws InvalidDescriptionException, InvalidValueException {
        String projectPath = getProjectPath();
        BufferedReader reader;
        int[] gameData = new int[4];
        try {
            reader = new BufferedReader(new FileReader(projectPath + "medialab/" + id + ".txt"));
            String line = reader.readLine();

            int i = 0;
            while (line != null) {
                try {
                    gameData[i] = Integer.parseInt(line);
                } catch (Exception e) {
                    throw new InvalidDescriptionException("InvalidDescriptionException");
                }
                i++;
                line = reader.readLine();
            }
            reader.close();

            if (i != 4) {
                throw new InvalidDescriptionException("InvalidDescriptionException");
            }

            Boolean check1 = gameData[0] != 1 && gameData[0] != 2;
            Boolean check2 = gameData[0] == 1 && ((gameData[1] < 9 || gameData[1] > 11) || (gameData[2] < 120 || gameData[2] > 180) || gameData[3] != 0);
            Boolean check3 = gameData[0] == 2 && ((gameData[1] < 35 || gameData[1] > 45) || (gameData[2] < 240 || gameData[2] > 360) || gameData[3] != 1);

            if (check1 || check2 || check3) {
                throw new InvalidValueException("InvalidValueException");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameData;
    }

    private void createMedialab() {
        String projectPath = getProjectPath();
        File medialab = new File(projectPath + "medialab/");
        if (!medialab.exists()) {
            medialab.mkdirs();
        }
    }

    public void WriteMines(ArrayList<Triplet> mines) {
        createMedialab();
        String projectPath = getProjectPath();
        Triplet t;
        String s;
        File mines_txt = new File(projectPath + "/medialab/mines.txt");
        try {
            Files.deleteIfExists(Paths.get(projectPath + "/medialab/mines.txt"));
            mines_txt.createNewFile();
            FileWriter myWriter = new FileWriter(mines_txt);
            for (int i = 0; i < mines.size(); i++) {
                t = mines.get(i);
                myWriter.write(t.getA().toString() + ',' + t.getB().toString() + ',' + t.getC().toString() + "\n");
            }
            myWriter.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void WriteResult(int mines, int tries, int time, int winner) { // winner: 1 for user, 0 for computer
        try {
            URL res = getClass().getResource("");
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath() + "/results.txt";
            File results = new File(absolutePath);
            if (!results.exists()) {
                results.createNewFile();
            }
            String content = String.valueOf(mines) + "," + String.valueOf(tries) + "," + String.valueOf(time) + "," + String.valueOf(winner) + "\n";

            RandomAccessFile raf = new RandomAccessFile(results, "rw");
            byte[] oldContent = new byte[(int) results.length()]; // Read the existing content of the file
            raf.readFully(oldContent);
            raf.seek(0); // Set the file pointer to the beginning
            raf.write(content.getBytes()); // Write the new content to the beginning
            raf.write(oldContent);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public int[][] GetResults() {
        try {
            URL res = getClass().getResource("");
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath() + "/results.txt";
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(absolutePath));
            String line = reader.readLine();

            int[][] result = new int[5][4];
            int i = 0;
            while (i < 5) {
                if (line != null) {
                    result[i] = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
                } else {
                    result[i] = new int[]{0, 0, 0, 0};
                }
                i++;
                line = reader.readLine();
            }
            reader.close();
            return result;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return new int[0][0];
    }

    void createScenarioID(String id, String level, String mines, String hypermine, String time) {
        String projectPath = getProjectPath();
        File scenario_id = new File(projectPath + "/medialab/" + id + ".txt");
        if (!scenario_id.exists()) {
            try {
                scenario_id.createNewFile();
                FileWriter myWriter = new FileWriter(scenario_id);
                myWriter.write(level + '\n' + mines + '\n' + hypermine + "\n" + time + "\n");
                myWriter.close();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

        }
    }
}
