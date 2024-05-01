package xxl;

import java.util.List;
import java.util.ArrayList;

import java.io.Serial;
import java.io.Serializable;

public abstract class Range implements Serializable {

    public int size() {
        return getPositions().size();
    }

    public abstract List<Position> getPositions();

}