package xxl;

import java.io.Serial;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private boolean _changed = false;
    private Collection<User> _users;
    private StorageUnit _storageUnit;
    private CutBuffer _cutBuffer;

    public Spreadsheet(int lines, int columns) {
        _storageUnit = new StorageUnit(lines, columns);
        _cutBuffer = new CutBuffer();
    }

    public boolean hasChanged() {
        return _changed;
    }

    public void setChanged(boolean changed) {
        _changed = changed;
    }

    public void changed() {
        setChanged(true);
    }

    public StorageUnit getStorageUnit() {
        return _storageUnit;
    }

    public CutBuffer getCutBuffer() {
        return _cutBuffer;
    }

    public Cell getContent(Position position) {
        return _storageUnit.get(position);
    }

    public String showContents(String rangeSpecification) throws UnrecognizedEntryException {
        Render renderer = new Render();
        for (Position position : _storageUnit.createRange(rangeSpecification).getPositions()) {
            position.accept(renderer);
            if (_storageUnit.get(position) != null)
                _storageUnit.get(position).accept(renderer);
        }
        return renderer.render();
    }

    public String showCutBuffer() {
        Render renderer = new Render();
        for (Position position : new Interval(1, 1, _cutBuffer.getLines(), _cutBuffer.getColumns()).getPositions()) {
            position.accept(renderer);
            if (_cutBuffer.get(position) != null)
                _cutBuffer.get(position).accept(renderer);
        }
        return renderer.render();
    }

    public void insertContents(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException {
        _storageUnit.insert(rangeSpecification, contentSpecification);
        changed();
    }

    public void deleteContents(String rangeSpecification) throws UnrecognizedEntryException {
        _storageUnit.delete(rangeSpecification);
    }

    public void copyContents(String rangeSpecification) throws UnrecognizedEntryException {
        _storageUnit.copy(rangeSpecification, _cutBuffer);
    }

    public void cutContents(String rangeSpecification) throws UnrecognizedEntryException {
        _storageUnit.cut(rangeSpecification, _cutBuffer);
    }

    public void pasteContents(String rangeSpecification) throws UnrecognizedEntryException {
        _storageUnit.paste(rangeSpecification, _cutBuffer);
    }

    public String search(SearchStrategy searchStrategy) {
        searchStrategy.setStorageUnit(_storageUnit);
        Map<Position, Cell> map = searchStrategy.search();
        Render renderer = new Render();
        for (Position position : map.keySet()) {
            position.accept(renderer);
            if (map.get(position) != null)
                map.get(position).accept(renderer);
        }
        return renderer.render();
    }

}