package xxl;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Position extends Range implements Comparable<Position> {

    private int _line;
    private int _column;

    public Position(int line, int column) {
        _line = line;
        _column = column;
    }

    public int getLine() {
        return _line;
    }

    public int getColumn() {
        return _column;
    }

    @Override
    public List<Position> getPositions() {
        List<Position> positions = new ArrayList<>();
        positions.add(this);
        return positions;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position position = (Position) o;
            return _line == position.getLine() && _column == position.getColumn();
        }
        return false;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int compareTo(Position position) {
        if (_line == position.getLine())
            return _column - position.getColumn();
        return _line - position.getLine();
    }

}