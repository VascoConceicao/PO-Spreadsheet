package xxl;

import java.io.Serial;
import java.io.Serializable;

public abstract class Content implements Serializable {
    
    public abstract void accept(Visitor v);

    public abstract Literal value();

    public boolean isValid() {
        try {
            return value() != null;
        } catch (NullPointerException e) {
            return false;
        }
    }

}