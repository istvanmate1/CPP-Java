
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Database {

    private final String tableName = "highscore";
    private final Connection conn;
    private final HashMap<String, Integer> highScores;

    /**
     * Tries to create a connection with the MySQL database in NetBeans.
     */
    public Database() {
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost/tron?"
                    + "serverTimezone=UTC&user=root&password=root");
        } catch (Exception ex) {
            System.out.println("No connection");
        }
        this.conn = c;
        highScores = new HashMap<>();
        loadHighScores();
    }

    /**
     * Loads the players' data from the data base. (player names, and the number
     * of their victories)
     */
    private void loadHighScores() {
        try ( Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            while (rs.next()) {
                String playerName = rs.getString("PlayerName");
                int score = rs.getInt("VictoryCount");

                highScores.put(playerName, score);
            }
        } catch (Exception e) {
            System.out.println("loadHighScores error: " + e.getMessage());
        }
    }

    /**
     * Creates a new record in the data base for a new player. Can handle if the
     * player already exist in the data base.
     *
     * @param playerName is the new player's name. Their score will be 0.
     */
    public void storeNewPlayer(String playerName) {
        if (!highScores.containsKey(playerName)) {
            highScores.put(playerName, 0);
            storeToDatabase(playerName, 0);
        }
    }

    /**
     * Increase the player's score if this player won.
     *
     * @param playerName the name of the winner player.
     */
    public void increasePlayerScore(String playerName) {
        int newScore = highScores.get(playerName);
        ++newScore;
        highScores.put(playerName, newScore);
        storeToDatabase(playerName, newScore);
    }

    /**
     * Updates the data base. Either create a new record or updates an already
     * existing one.
     *
     * @param playerName identifies the record int the data base
     * @param score will be the new score of the player.
     */
    private int storeToDatabase(String playerName, int score) {
        try ( Statement stmt = conn.createStatement()) {
            String s = "INSERT INTO " + tableName
                    + " (PlayerName, VictoryCount) "
                    + "VALUES('" + playerName + "'," + score
                    + ") ON DUPLICATE KEY UPDATE VictoryCount=" + score;
            return stmt.executeUpdate(s);
        } catch (Exception e) {
            System.out.println("storeToDatabase error");
        }
        return 0;
    }

    /**
     * Gives all the players' name and score in a sorted ArrayList<HighScore>.
     *
     * @return Read the line above this!!!
     */
    private ArrayList<HighScore> getHighScores() {
        ArrayList<HighScore> scores = new ArrayList<>();
        for (String playerName : highScores.keySet()) {
            HighScore h = new HighScore(playerName, highScores.get(playerName));
            scores.add(h);
        }
        Collections.sort(scores);
        Collections.reverse(scores);
        return scores;
    }

    /**
     *
     * @return the top ten player according their victories.
     */
    public ArrayList<HighScore> getTopTenScores() {
        ArrayList<HighScore> scores = getHighScores();
        while (true) {
            if (scores.size() <= 10) {
                break;
            }
            scores.remove(scores.size() - 1);
        }
        return scores;
    }

}
