package com.example.minesweeper;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Scanner;

public class FileController {

    public String getProjectPath() {
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
                //System.out.println(line);
                // read next line
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

    public void createMedialab() {
        String projectPath = getProjectPath();
        File medialab = new File(projectPath + "medialab/");
        if (!medialab.exists()) {
            medialab.mkdirs();
        }
    }

    public void createScenarioID(String id, String level, String mines, String hypermine, String time) {
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
