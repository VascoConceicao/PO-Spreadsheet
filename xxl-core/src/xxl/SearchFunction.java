package xxl;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;

public class SearchFunction extends SearchStrategy {

    private String _segment;

    public SearchFunction(String segment) {
        _segment = segment;
    }

    public boolean isSegment(String functionName) {

        for (int i = 0; i < functionName.length() && i + _segment.length() < functionName.length() + 1; i++)
            for (int j = 0; j < _segment.length(); j++) {
                if (functionName.charAt(i+j) != _segment.charAt(j))
                    break;
                if (j == _segment.length() - 1)
                    return true;
            }
        return false;
    }

    public Map<Position, Cell> sortByFunctionName(Map<Position, Cell> map) {
        List<Map.Entry<Position, Cell>> entryList = new ArrayList<>(map.entrySet());

        entryList.sort((entry1, entry2) -> ((Function) entry1.getValue().getContent()).getName().compareTo(((Function) entry2.getValue().getContent()).getName()));

        Map<Position, Cell> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Position, Cell> entry : entryList)
            sortedMap.put(entry.getKey(), entry.getValue());
        return sortedMap;
    }

    @Override
    public Map<Position, Cell> search() {
        Map map = new TreeMap<>();
        for (Position position : getStorageUnit().getAllPositions())
            if (!getStorageUnit().isEmpty(position))
                try {
                    if (isSegment(((Function) getStorageUnit().get(position).getContent()).getName()))
                        map.put(position, getStorageUnit().get(position));
                } catch (ClassCastException e) {
                    // continue
                }
        return sortByFunctionName(map);
    }
    
}