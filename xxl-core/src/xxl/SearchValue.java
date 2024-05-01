package xxl;

import java.util.Map;
import java.util.TreeMap;

public class SearchValue extends SearchStrategy {

    private Literal toSearch;

    public SearchValue(String valueSpecification) {
        if (valueSpecification.charAt(0) == '\'')
            toSearch = new StringLiteral(valueSpecification.substring(1));
        else
            toSearch = new IntegerLiteral(Integer.parseInt(valueSpecification));
    }

    @Override
    public Map<Position, Cell> search() {
        Map<Position, Cell> map = new TreeMap<>();
        for (Position position : getStorageUnit().getAllPositions())
            if (getStorageUnit().get(position) != null && getStorageUnit().get(position).getContent().value().equals(toSearch))
                map.put(position, getStorageUnit().get(position));
        return map;
    }
    
}