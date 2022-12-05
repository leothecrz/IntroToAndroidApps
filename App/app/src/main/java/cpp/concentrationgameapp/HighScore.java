package cpp.concentrationgameapp;

import org.json.JSONException;
import org.json.JSONObject;

public class HighScore {

    private final String name;
    private final int score;
    private final int tileCount;

    public HighScore(String name, int score, int tileCount) {
        this.name = name;
        this.score = score;
        this.tileCount = tileCount;
    }

    public static HighScore fromJSON(JSONObject json) {
        try {
            return new HighScore(
                    json.getString("name"),
                    json.getInt("score"),
                    json.getInt("tileCount")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create high score from JSON object.");
        }
    }
    
    public JSONObject toJSON() {
        try {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("score", score);
            json.put("tileCount", tileCount);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create JSON object from high score.");
        }
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getTileCount() {
        return tileCount;
    }
}
