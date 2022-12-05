package cpp.concentrationgameapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// Singleton class
public class HighScoreTable {

    private static HighScoreTable instance;
    private static final String FILENAME = "high_scores.json";

    private HighScore[][] highScores;
    private File highScoresFile;

    private HighScoreTable() {
        // 3 high scores for each tile count (4, 6, 8, 10, ... 20)
        highScores = new HighScore[9][3];
    }

    /**
     * @return A singleton instance of HighScoreTable
     */
    public static HighScoreTable getInstance() {
        if (instance == null)
            instance = new HighScoreTable();
        return instance;
    }

    /**
     * Initializes the high score table. Reads JSON file if it exists.
     * @param context The context to get the file directory from
     */
    public void init(Context context) {
        highScoresFile = new File(context.getFilesDir(), FILENAME);
        try {
            // Read JSON file
            Scanner scanner = new Scanner(highScoresFile);
            StringBuilder jsonStringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonStringBuilder.append(scanner.nextLine());
            }
            scanner.close();

            JSONArray json = new JSONArray(jsonStringBuilder.toString());
            int[] counters = new int[9];
            for (int i = 0; i < json.length(); i++) {
                HighScore highScore = HighScore.fromJSON(json.getJSONObject(i));
                int tileIndex = tileCountToIndex(highScore.getTileCount());
                highScores[tileIndex][counters[tileIndex]++] = highScore;
            }

        } catch (FileNotFoundException e) {
            Log.i("cpp_concentration", "High scores file not found (will be created)");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the given score is a high score.
     * @param tileCount The number of tiles to check for the score
     * @param score The score to check
     * @return Whether or not the given score is a high score
     */
    public boolean isHighScore(int tileCount, int score) {
        if (score == 0)
            return false;

        int index = getFirstNullIndex(tileCount);
        if (index != -1) {
            return true;
        } else {
            return score > highScores[tileCountToIndex(tileCount)][2].getScore();
        }
    }

    /**
     * Adds a high score to the high score table and updates the high score JSON file.
     * @param name The name of the high score
     * @param tileCount The number of tiles for the high score
     * @param score The score to add
     * @return True if the score was a high score and adding the score was successful
     */
    public boolean addHighScore(String name, int tileCount, int score) {
        if (!isHighScore(tileCount, score))
            return false;

        HighScore highScore = new HighScore(name, score, tileCount);

        // Find index to insert score into
        int tileIndex = tileCountToIndex(tileCount);
        for (int i = 0; i < 3; i++) {
            if (highScores[tileIndex][i] == null) {
                highScores[tileIndex][i] = highScore;
                break;
            }
            if (score > highScores[tileIndex][i].getScore()) {
                for (int x = 2; x > i; x--) {
                    highScores[tileIndex][x] = highScores[tileIndex][x - 1];
                    highScores[tileIndex][x - 1] = null;
                }
                highScores[tileIndex][i] = highScore;
                break;
            }
        }

        save();
        return true;
    }


    /**
     * Gets the high scores for the given tile count.
     * @param index Index to return from the high score table
     * @param isTileCount Set to true if index is a tile count (4, 6, 8, 10, ... 20)
     * @return An array of high scores
     */
    public HighScore[] getHighScores(int index, boolean isTileCount) {
        return highScores[isTileCount ? tileCountToIndex(index) : index];
    }

    /**
     * Resets the high score table and updates the JSON file.
     */
    public void clear() {
        highScores = new HighScore[9][3];
        save();
    }

    private void save() {
        JSONArray json = new JSONArray();
        for (HighScore[] tileScore : highScores) {
            for (HighScore score : tileScore) {
                if (score != null)
                    json.put(score.toJSON());
            }
        }

        try {
            FileWriter writer = new FileWriter(highScoresFile);
            writer.write(json.toString());
            writer.close();
        } catch (IOException e) {
            Log.e("cpp_concentration", "High scores file could not be written");
            e.printStackTrace();
        }
    }

    private int getFirstNullIndex(int tileCount) {
        int index = tileCountToIndex(tileCount);
        for (int i = 0; i < 3; i++) {
            if (highScores[index][i] == null)
                return i;
        }
        return -1;
    }

    /**
     * Converts a tile count (4, 6, 8, 10, ... 20) to an index (0, 1, 2, 3, ... 8) to access the
     * high score table.
     * @param tileCount The tile count to convert
     * @return An index
     */
    public int tileCountToIndex(int tileCount) {
        return (tileCount - 4) / 2;
    }
}
