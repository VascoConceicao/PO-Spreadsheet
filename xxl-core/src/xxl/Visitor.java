package xxl;

public abstract class Visitor {

    public void visit(Cell cell) {}
    public void visit(Position position) {}
    public void visit(Literal literal) {}
    public void visit(IntegerLiteral integerLiteral) {}
    public void visit(StringLiteral stringLiteral) {}
    public void visit(Reference reference) {}
    public void visit(BinaryFunction binaryFunction) {}
    public void visit(IntervalFunction intervalFunction) {}
    public void visit(Add add) {}
    public void visit(Sub sub) {}
    public void visit(Mul mul) {}
    public void visit(Div div) {}
    public void visit(Average average) {}
    public void visit(Product product) {}
    public void visit(Concat concat) {}
    public void visit(Coalesce coalesce) {}

}