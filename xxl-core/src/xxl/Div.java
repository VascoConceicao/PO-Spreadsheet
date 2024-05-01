package xxl;

public class Div extends BinaryFunction {

    public Div(Content operand1, Content operand2) {
        super("DIV", operand1, operand2);
    }

    @Override
    public Literal value() {
        try {
            return new IntegerLiteral(((IntegerLiteral) getOperand1().value()).getInteger() / ((IntegerLiteral) getOperand2().value()).getInteger());
        } catch (NullPointerException | ClassCastException | ArithmeticException e) {
            return null;
        }
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
}