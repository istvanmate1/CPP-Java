
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class HighScoreTableModel extends AbstractTableModel {

    private final ArrayList<HighScore> highScores;
    private final String[] colName = new String[]{"Player name", "Victories"};

    public HighScoreTableModel(ArrayList<HighScore> highScores) {
        this.highScores = highScores;
    }

    @Override
    public int getRowCount() {
        return highScores.size();
    }

    @Override
    public int getColumnCount() {
        return colName.length;
    }

    @Override
    public Object getValueAt(int r, int c) {
        HighScore h = highScores.get(r);
        if (c == 0) {
            return h.getPlayerName();
        } else {//c == 1
            return h.getScore();
        }
    }

    @Override
    public String getColumnName(int i) {
        return colName[i];
    }

}
