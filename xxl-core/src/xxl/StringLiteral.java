package xxl;

public class StringLiteral extends Literal {

    private String _s;

    public StringLiteral(String s) {
        _s = s;
    }

    public String getString() {
        return _s;
    }
    
    @Override
    public Literal value() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StringLiteral) {
            StringLiteral stringLiteral = (StringLiteral) o;
            return _s.equals(stringLiteral.getString());
        }
        return false;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}