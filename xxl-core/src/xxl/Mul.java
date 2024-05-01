package xxl;

public class Mul extends BinaryFunction {

    public Mul(Content operand1, Content operand2) {
        super("MUL", operand1, operand2);
    }

    @Override
    public Literal value() {
        try {
            return new IntegerLiteral(((IntegerLiteral) getOperand1().value()).getInteger() * ((IntegerLiteral) getOperand2().value()).getInteger());
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}