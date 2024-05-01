package xxl;

public class IntegerLiteral extends Literal {
    
    private int _i;

    public IntegerLiteral(int i) {
        _i = i;
    }

    public int getInteger() {
        return _i;
    }

    @Override
    public Literal value() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntegerLiteral) {
            IntegerLiteral integerLiteral = (IntegerLiteral) o;
            return _i == integerLiteral.getInteger();
        }
        return false;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}