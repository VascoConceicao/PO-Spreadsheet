package xxl;

public class Coalesce extends IntervalFunction {

    public Coalesce(Interval interval, StorageUnit storageUnit) {
        super("COALESCE", interval, storageUnit);
    }

    @Override
    public Literal value() {
        String res = "";
        for (Position position : getInterval().getPositions())
            try {
                if (!getStorageUnit().isEmpty(position)) {
                    res += ((StringLiteral) getStorageUnit().get(position).value()).getString();
                    break;
                }
            } catch (NullPointerException | ClassCastException e) {
                continue;
            }
        return new StringLiteral(res);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}