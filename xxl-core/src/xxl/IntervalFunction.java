package xxl;

public abstract class IntervalFunction extends Function {

    private Interval _interval;
    private StorageUnit _storageUnit;

    public IntervalFunction(String name, Interval interval, StorageUnit storageUnit) {
        super(name);
        _interval = interval;
        _storageUnit = storageUnit;
    }

    @Override
    public abstract Literal value();

    public Interval getInterval() {
        return _interval;
    }

    public StorageUnit getStorageUnit() {
        return _storageUnit;
    }

    @Override
    public String toString() {
        return (isValid() ? value() : "#VALUE") + "=" + getName() + "(" + _interval + ")";
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
}