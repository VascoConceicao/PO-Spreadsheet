package xxl;

public abstract class BinaryFunction extends Function {

    private Content _operand1;
    private Content _operand2;

    public BinaryFunction(String name, Content operand1, Content operand2) {
        super(name);
        _operand1 = operand1;
        _operand2 = operand2;
    }

    @Override
    public abstract Literal value();

    public Content getOperand1() {
        return _operand1;
    }

    public void setOperand1(Content operand1) {
        _operand1 = operand1;
    }

    public Content getOperand2() {
        return _operand2;
    }

    public void setOperand2(Content operand2) {
        _operand2 = operand2;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
}