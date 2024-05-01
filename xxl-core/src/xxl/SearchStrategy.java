package xxl;

import java.util.Map;

public abstract class SearchStrategy {

    private StorageUnit _storageUnit;

    public StorageUnit getStorageUnit() {
        return _storageUnit;
    }

    public void setStorageUnit(StorageUnit storageUnit) {
        _storageUnit = storageUnit;
    }

    public abstract Map<Position, Cell> search();

}