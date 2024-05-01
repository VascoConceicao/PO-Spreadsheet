package xxl;

public class Render extends Visitor {

    String _rendering = "";
    boolean _hasStarted = false;
    boolean _toSplit = true;
    boolean _toShowValue = true;

    public String render() {
        return _rendering;
    }

    public void started() {
        _hasStarted = true;
    }

    public void split() {
        _toSplit = true;
    }

    public void splited() {
        _toSplit = false;
    }

    public void show() {
        _toShowValue = true;
    }

    public void showed() {
        _toShowValue = false;
    }

    @Override
    public void visit(Position position) {
        if (_hasStarted && _toSplit)
            _rendering += "\n";
        _rendering += position.getLine() + ";" + position.getColumn();
        if (_toSplit)
            _rendering += "|";
        started();
    }

    @Override
    public void visit(Cell cell) {
        splited();
        show();
        cell.getContent().accept(this);
        split();
    }

    @Override
    public void visit(IntegerLiteral integerLiteral) {
        _rendering += integerLiteral.getInteger();
    }

    @Override
    public void visit(StringLiteral stringLiteral) {
        _rendering += "'" + stringLiteral.getString();
        showed();
    }

    @Override
    public void visit(Reference reference) {
        if (_toShowValue) {
            if (reference.value() != null)
                reference.value().accept(this);
            else
                _rendering += "#VALUE";
            showed();
            _rendering += "=";
        }
        reference.getPosition().accept(this);
    }

    @Override
    public void visit(Add add) {
        if (_toShowValue) {
            if (add.value() != null)
                add.value().accept(this);
            else
                _rendering += "#VALUE";
            showed();
            _rendering += "=";
        }
        _rendering += add.getName() + "(";
        add.getOperand1().accept(this);
        _rendering += ",";
        add.getOperand2().accept(this);
        _rendering += ")";
    }

    @Override
    public void visit(Sub sub) {
        if (_toShowValue) {
            if (sub.value() != null)
                sub.value().accept(this);
            else
                _rendering += "#VALUE";
            showed();
            _rendering += "=";
        }
        _rendering += sub.getName() + "(";
        sub.getOperand1().accept(this);
        _rendering += ",";
        sub.getOperand2().accept(this);
        _rendering += ")";
    }

    @Override
    public void visit(Mul mul) {
        if (_toShowValue) {
            if (mul.value() != null)
                mul.value().accept(this);
            else
                _rendering += "#VALUE";
            showed();
            _rendering += "=";
        }
        _rendering += mul.getName() + "(";
        mul.getOperand1().accept(this);
        _rendering += ",";
        mul.getOperand2().accept(this);
        _rendering += ")";
    }

    @Override
    public void visit(Div Div) {
        if (_toShowValue) {
            if (Div.value() != null)
                Div.value().accept(this);
            else
                _rendering += "#VALUE";
            showed();
            _rendering += "=";
        }
        _rendering += Div.getName() + "(";
        Div.getOperand1().accept(this);
        _rendering += ",";
        Div.getOperand2().accept(this);
        _rendering += ")";
    }

    @Override
    public void visit(Average average) {
        if (_toShowValue) {
            if (average.value() != null)
                average.value().accept(this);
            else
                _rendering += "#VALUE";
            showed();
            _rendering += "=";
        }
        _rendering += average.getName() + "(";
        average.getInterval().getPositions().get(0).accept(this);
        _rendering += ":";
        average.getInterval().getPositions().get(average.getInterval().getPositions().size() - 1).accept(this);
        _rendering += ")";
    }

    @Override
    public void visit(Product product) {
        if (_toShowValue) {
            if (product.value() != null)
                product.value().accept(this);
            else
                _rendering += "#VALUE";
            showed();
            _rendering += "=";
        }
        _rendering += product.getName() + "(";
        product.getInterval().getPositions().get(0).accept(this);
        _rendering += ":";
        product.getInterval().getPositions().get(product.getInterval().getPositions().size() - 1).accept(this);
        _rendering += ")";
    }

    @Override
    public void visit(Concat concat) {
        if (_toShowValue) {
            if (concat.value() != null)
                concat.value().accept(this);
            else
                _rendering += "#VALUE";
            showed();
            _rendering += "=";
        }
        _rendering += concat.getName() + "(";
        concat.getInterval().getPositions().get(0).accept(this);
        _rendering += ":";
        concat.getInterval().getPositions().get(concat.getInterval().getPositions().size() - 1).accept(this);
        _rendering += ")";
    }

    @Override
    public void visit(Coalesce coalesce) {
        if (_toShowValue) {
            if (coalesce.value() != null)
                coalesce.value().accept(this);
            showed();
            _rendering += "=";
        }
        _rendering += coalesce.getName() + "(";
        coalesce.getInterval().getPositions().get(0).accept(this);
        _rendering += ":";
        coalesce.getInterval().getPositions().get(coalesce.getInterval().getPositions().size() - 1).accept(this);
        _rendering += ")";
    }

}