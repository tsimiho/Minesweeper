package com.example.minesweeper;

import eu.hansolo.toolbox.tuples.Triplet;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.io.RandomAccessFile;

public class FileController {

    /**
     * Class constructor.
     */
    public FileController() {
    }

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

    private void createMedialab() {
        String projectPath = getProjectPath();
        File medialab = new File(projectPath + "medialab/");
        if (!medialab.exists()) {
            medialab.mkdirs();
        }
    }


    /**
     * Given an id, checks if the file with name "id.txt" exists within the medialab directory.
     *
     * @param id the name of the requested file
     * @return true if the file exists, false otherwise
     */
    public Boolean ScenarioExists(String id) {
        String projectPath = getProjectPath();
        File file = new File(projectPath + "medialab/" + id + ".txt");
        return file.exists();
    }


    /**
     * Given an id, reads the file "id.txt", if it exists, within the medialab directory, else throws IOException.
     * If the file does not contain 4 lines with one Integer each, it throws InvalidDescriptionException and if
     * the Integers do not satisfy certain constraints based on the level, the time, the number of mines and
     * whether there is a hypermine or not, it throws InvalidValueException. Else, it constructs an array of integers
     * with the contents of the file and returns it.
     *
     * @param id the name of the requested file
     * @return an array of integers which contains the contents of the requested file
     * @throws InvalidDescriptionException if the file does not contain 4 lines with one Integer each
     * @throws InvalidValueException       if the Integers do not satisfy certain constraints based on the level, the time,
     *                                     the number of mines and whether there is a hypermine or not
     */
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


    /**
     * Given an array of triplets that contain the coordinates of the mines in the grid and whether they are a hypermine,
     * writes the triplets, each in a new line, in the file "mines.txt" in the medialab directory, after clearing its
     * previous contents, if it already exists.
     *
     * @param mines the ArrayList of triplets that describe the coordinates of the mines in the grid and whether they
     *              are a hypermine. The first element of the triplet is the x value, the second is the y value and the
     *              third element is 1 if it is a hypermine, else 0.
     */
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

    /**
     * Writes in the "results.txt" file inside the project direcotry the information about a completed game:
     * the number of mines in the game, the number of tries (left clicks), the game duration and the winner (1 for user
     * and 0 for computer).
     *
     * @param mines  the number of mines in the game
     * @param tries  the number of tries (left clicks) by the user
     * @param time   the game duration
     * @param winner the winner (1 for user and 0 for computer)
     */
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


    /**
     * Gets the results (number of mines, number of tries, game duration and winner) of the last 5 completed games
     * from the results.txt file within the project directory.
     *
     * @return a two-dimensional array with information (number of mines, number of tries, game duration and winner)
     * of the last 5 completed games
     */
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


    /**
     * Given an id and the information about new game (level, number of mines, time and whether it contains a hypermine)
     * creates a file with the name "id.txt" inside the medialab directory.
     *
     * @param id        the id of the created game (SECTION-ID)
     * @param level     the level of the game (1 or 2)
     * @param mines     the number of mines
     * @param hypermine whether it contains a hypermine or not (1 if it does, else 0)
     * @param time      the available time in seconds
     */
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
