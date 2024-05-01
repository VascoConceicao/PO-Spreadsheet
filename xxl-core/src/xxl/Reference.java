package xxl;

public class Reference extends Content {

    private Position _position;
    private StorageUnit _storageUnit;

    public Reference(Position position, StorageUnit storageUnit) {
        _position = position;
        _storageUnit = storageUnit;
    }

    public Reference(int line, int column, StorageUnit storageUnit){
        _position = new Position(line, column);
        _storageUnit = storageUnit;
    }

    public Position getPosition() {
        return _position;
    }

    public StorageUnit getStorageUnit() {
        return _storageUnit;
    }

    @Override
    public Literal value() {
        return getStorageUnit().get(_position) == null ? null : getStorageUnit().get(_position).value();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}