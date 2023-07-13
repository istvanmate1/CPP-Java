
import java.util.Objects;

public class HighScore implements Comparable<HighScore> {

    private String playerName;
    private int score;

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public HighScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.playerName);
        hash = 89 * hash + this.score;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HighScore other = (HighScore) obj;
        if (this.score != other.score) {
            return false;
        }
        return Objects.equals(this.playerName, other.playerName);
    }

    @Override
    public String toString() {
        return "HighScore{" + "playerName=" + playerName + ", score=" + score + '}';
    }

    @Override
    public int compareTo(HighScore o) {
        return this.score - o.getScore();
    }
}
