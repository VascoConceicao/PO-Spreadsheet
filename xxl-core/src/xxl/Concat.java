package xxl;

public class Concat extends IntervalFunction {

    public Concat(Interval interval, StorageUnit storageUnit) {
        super("CONCAT", interval, storageUnit);
    }

    @Override
    public Literal value() {
        String res = "";
        for (Position position : getInterval().getPositions())
            try {
                res += ((StringLiteral) getStorageUnit().get(position).value()).getString();
            } catch (NullPointerException | ClassCastException e) {
                continue;
            }
        return new StringLiteral(res);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}