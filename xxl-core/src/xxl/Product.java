package xxl;

public class Product extends IntervalFunction {

    public Product(Interval interval, StorageUnit storageUnit) {
        super("PRODUCT", interval, storageUnit);
    }

    @Override
    public Literal value() {
        try {
            int product = 1;
            for (Position position : getInterval().getPositions())
                product *= ((IntegerLiteral) getStorageUnit().get(position).value()).getInteger();
            return new IntegerLiteral(product);
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}