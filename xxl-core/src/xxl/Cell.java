package xxl;

import java.io.Serial;
import java.io.Serializable;

public class Cell implements Serializable {

    private Content _content;

    public Cell (Content content) {
        _content = content;
    }

    public Content getContent() {
        return _content;
    }

    public Literal value() {
        return _content.value();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}