package xxl;

import java.io.Serial;
import java.io.Serializable;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

import xxl.exceptions.UnrecognizedEntryException;

public class StorageUnit implements Serializable {

    private int _lines = 0;
    private int _columns = 0;
    private Map<Position, Cell> _map = new TreeMap<>();

    public StorageUnit(int lines, int columns) {
        _lines = lines;
        _columns = columns;
    }

    public void setLines(int lines) {
        _lines = lines;
    }

    public void setColumns(int columns) {
        _columns = columns;
    }

    public void setDimensions(int lines, int columns) {
        setLines(lines);
        setColumns(columns);
    }

    public Map<Position, Cell> getMap() {
        return _map;
    }

    public int getLines() {
        return _lines;
    }

    public int getColumns() {
        return _columns;
    }

    public Cell get(Position position) {
        return _map.get(position);
    }

    public void insert(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException {
        Content content = createContent(contentSpecification);
        for (Position position : createRange(rangeSpecification).getPositions())
            put(position, new Cell(content));
    }

    public void put(Position position, Cell cell) {
        if (cell != null)
            _map.put(position, cell);
    }

    public void delete(String rangeSpecification) throws UnrecognizedEntryException {
        Range range = createRange(rangeSpecification);
        for (Position position : range.getPositions()) {
            _map.remove(position);
        }
    }

    public void remove(Position position) throws UnrecognizedEntryException {
        _map.remove(position);
    }

    public boolean isEmpty(Position position) {
        return !_map.containsKey(position);
    }

    public List<Position> getAllPositions() {
        List<Position> positions = new ArrayList<>();
        for (int i = 1; i <= _lines; i++)
            for (int j = 1; j <= _columns; j++)
                positions.add(new Position(i, j));
        return positions;
    }

    public Range createRange(String rangeSpecification) throws UnrecognizedEntryException {
        String[] positions = rangeSpecification.split(":");

        switch (positions.length){
            case 1:
                return createPosition(positions[0]);
            case 2:
                return createInterval(positions[0], positions[1]);
            default:
                throw new UnrecognizedEntryException(rangeSpecification);
        }
    }

    public Position createPosition(String positionSpecification) throws UnrecognizedEntryException {
        String[] i = positionSpecification.split(";");
        if (i.length != 2)
            throw new UnrecognizedEntryException(positionSpecification);

        int line   = Integer.parseInt(i[0]);
        int column = Integer.parseInt(i[1]);
        if (0 >= line || line > _lines || 0 >= column || column > _columns)
            throw new UnrecognizedEntryException(positionSpecification);

        return new Position(line, column);
    }

    public Interval createInterval(String positionSpecification1, String positionSpecification2) throws UnrecognizedEntryException {
        String[] i = positionSpecification1.split(";");
        String[] j = positionSpecification2.split(";");
        if (i.length != 2 || j.length != 2)
            throw new UnrecognizedEntryException(positionSpecification1 + ":" + positionSpecification2);

        int ix = Integer.parseInt(i[0]);
        int iy = Integer.parseInt(i[1]);
        int jx = Integer.parseInt(j[0]);
        int jy = Integer.parseInt(j[1]);

        int lowerLine   = Math.min(ix, jx);
        int lowerColumn = Math.min(iy, jy);
        int upperLine   = Math.max(ix, jx);
        int upperColumn = Math.max(iy, jy);
        
        if (0 >= lowerLine || upperLine > _lines || 0 >= lowerColumn || upperColumn > _columns ||
            (lowerLine != upperLine && lowerColumn != upperColumn)) // only accepts one-dimensional intervals
            throw new UnrecognizedEntryException(positionSpecification1 + ":" + positionSpecification2);

        return new Interval(lowerLine, lowerColumn, upperLine, upperColumn);
    }

    public Content createContent(String contentSpecification) throws UnrecognizedEntryException {
        char firstChar = contentSpecification.charAt(0);
        Content content;

        switch (firstChar) {
            default:
                content = new IntegerLiteral(Integer.parseInt(contentSpecification));
                break;
            case '\'':
                content = new StringLiteral(contentSpecification.substring(1));
                break;
            case '=':
                contentSpecification = contentSpecification.substring(1);
                char nextChar = contentSpecification.charAt(0);
                if ('0' <= nextChar && nextChar <= '9')
                    content = createReference(contentSpecification);
                else
                    content = createFunction(contentSpecification);
            }
        return content;
    }

    public Content createReference(String contentSpecification) throws UnrecognizedEntryException {
        Position position = createPosition(contentSpecification);
        return new Reference(position, this);
    }

    public Content createFunction(String contentSpecification) throws UnrecognizedEntryException {
        try {
            String name = contentSpecification.split("\\(")[0];
            String[] operands = contentSpecification.split("\\(")[1].split("\\)")[0].split(",");
            if (operands.length == 2)
                return createBinaryFunction(contentSpecification, name, operands);
            return createIntervalFunction(contentSpecification, name, operands[0]);
        } catch (UnrecognizedEntryException e) {
            throw new UnrecognizedEntryException(contentSpecification);
        }
    }

    public Content createBinaryFunction(String contentSpecification, String name, String[] operands) throws UnrecognizedEntryException {
        for (int i = 0; i < operands.length; i++)
            if (operands[i].split(";").length > 1 || ('A' <= operands[i].charAt(0) && operands[i].charAt(0) <= 'Z'))
                operands[i] = "=" + operands[i];

        Content operand1 = createContent(operands[0]);
        Content operand2 = createContent(operands[1]);
        
        Content content = switch (name) {
            case "ADD" -> new Add(operand1, operand2);
            case "SUB" -> new Sub(operand1, operand2);
            case "MUL" -> new Mul(operand1, operand2);
            case "DIV" -> new Div(operand1, operand2);
            default -> throw new UnrecognizedEntryException(contentSpecification);
        };
        return content;
    }

    public Content createIntervalFunction(String contentSpecification, String name, String operands) throws UnrecognizedEntryException {
        
        Interval interval = (Interval) createRange(operands);
        
        Content content = switch (name) {
            case "AVERAGE" -> new Average(interval, this);
            case "PRODUCT" -> new Product(interval, this);
            case "CONCAT" -> new Concat(interval, this);
            case "COALESCE" -> new Coalesce(interval, this);
            default -> throw new UnrecognizedEntryException(contentSpecification);
        };
        return content;
    }

    public void copy(String rangeSpecification, CutBuffer cutBuffer) throws UnrecognizedEntryException {
        List<Position> positions = createRange(rangeSpecification).getPositions();

        int size = positions.size();
        int firstLine = positions.get(0).getLine();
        int firstColumn = positions.get(0).getColumn();

        cutBuffer.setDimensions(positions.get(size - 1).getLine() - firstLine + 1, positions.get(size - 1).getColumn() - firstColumn + 1);

        for (Position position : positions)
            if (!isEmpty(position))
                cutBuffer.put(new Position(position.getLine() - firstLine + 1, position.getColumn() - firstColumn + 1), new Cell(get(position).getContent()));
    }

    public void cut(String rangeSpecification, CutBuffer cutBuffer) throws UnrecognizedEntryException {
        copy(rangeSpecification, cutBuffer);
        delete(rangeSpecification);
    }
    
    public void paste(String rangeSpecification, CutBuffer cutBuffer) throws UnrecognizedEntryException {
        List<Position> allPositions = cutBuffer.getAllPositions();

        int size = allPositions.size();
        int singleCellFlag = 0;

        Range range = createRange(rangeSpecification);  
        if (range.size() == 1) 
            singleCellFlag = 1;

        Position start = createRange(rangeSpecification).getPositions().get(0);

        for (Position position : allPositions) {
            int nextLine = start.getLine() + position.getLine() - 1;
            int nextColumn = start.getColumn() + position.getColumn() - 1;
            if (singleCellFlag == 1 && (nextLine > _lines || nextColumn > _columns))
                break;
            Position addedPosition = new Position(nextLine, nextColumn);
            if (cutBuffer.isEmpty(position))
                remove(addedPosition);
            else
                put(addedPosition, new Cell(cutBuffer.get(position).getContent()));
        }
    }

    public List<Position> searchValue(String contentSpecification) throws UnrecognizedEntryException {
        Content toSearch = (Literal) createContent(contentSpecification);

        List<Position> res = new ArrayList<>();
        List<Position> allPositions = getAllPositions();

        for (Position position : allPositions)
            if (!isEmpty(position))
                if (toSearch.equals(get(position).value()))
                    res.add(position);              
        return res;
    }

    public List<Position> searchFunction(String functionSpecification) throws UnrecognizedEntryException {
        List<Position> res = new ArrayList<>();
        List<Position> allPositions = getAllPositions();

        for (Position position : allPositions)
            if (!isEmpty(position))
                if (get(position).getContent().equals(functionSpecification))
                    res.add(position);
        return res;
    }

}