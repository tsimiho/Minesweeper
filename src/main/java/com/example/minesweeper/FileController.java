package com.example.minesweeper;

import java.io.*;
import java.net.URL;
import java.nio.file.*;

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
            System.err.println("Error: " + e.getMessage());
            return e.getMessage();
        }
    }

    public void getGameDescription() {

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
                myWriter.write(level + '\n' + mines + '\n' + hypermine + "\n" + time);
                myWriter.close();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

        }
    }

}
