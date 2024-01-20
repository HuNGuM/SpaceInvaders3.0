package ScoreHistory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ScoreHistoryManager {

    final static String HISTORYPATH = "scoreHistory.ser";

    public static ArrayList<Score> readTopScoreHistory() {
        ArrayList<Score> result = new ArrayList<>();

        try (ObjectInputStream arrayStream = new ObjectInputStream(new FileInputStream(HISTORYPATH))) {
            result = (ArrayList<Score>) arrayStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void writeTopScoreHistory(ArrayList<Score> scoreHistory) {
        try (ObjectOutputStream arrayStream = new ObjectOutputStream(new FileOutputStream(HISTORYPATH))) {
            arrayStream.writeObject(scoreHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRecord(Score score) {
        ArrayList<Score> scoreHistory = readTopScoreHistory();
        scoreHistory.add(score);

        writeTopScoreHistory(scoreHistory);
    }

}
