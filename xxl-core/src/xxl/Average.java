package xxl;

public class Average extends IntervalFunction {

    public Average(Interval interval, StorageUnit storageUnit) {
        super("AVERAGE", interval, storageUnit);
    }

    @Override
    public Literal value() {
        try {
            int sum = 0;
            Interval interval = getInterval();
            for (Position position : interval.getPositions())
                sum += ((IntegerLiteral) getStorageUnit().get(position).value()).getInteger();
            return new IntegerLiteral(sum / interval.size());
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}