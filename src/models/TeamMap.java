package models;

import java.util.HashMap;
import java.util.Map;

public class TeamMap {
    private Map<Integer, String> map = new HashMap<Integer, String>();
    private int score = 0;
    private boolean isActive = true;

    public void addAnswer(int index, String answer, int score) {
        if (isActive) {
            map.put(index, answer);
            this.score += score;
        } else {
            map.put(index, "");
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void deactivate() {
        isActive = false;
    }

    public void activate() {
        isActive = true;
    }
}
