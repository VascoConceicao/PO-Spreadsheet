package xxl;

public abstract class Literal extends Content {

    public abstract Literal value();

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
}