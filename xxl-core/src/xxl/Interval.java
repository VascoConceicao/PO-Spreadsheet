package xxl;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Interval extends Range {

    private int _lowerLine;
    private int _lowerColumn;
    private int _upperLine;
    private int _upperColumn;

    public Interval(int lowerLine, int lowerColumn, int upperLine, int upperColumn) {
        _lowerLine = lowerLine;
        _lowerColumn = lowerColumn;
        _upperLine = upperLine;
        _upperColumn = upperColumn;
    }

    @Override
    public List<Position> getPositions() {
        List<Position> positions = new ArrayList<>();
        for (int i = _lowerLine; i <= _upperLine; i++)
            for (int j = _lowerColumn; j <= _upperColumn; j++)
                positions.add(new Position(i, j));
        return positions;
    }
    
}