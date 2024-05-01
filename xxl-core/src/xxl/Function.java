package xxl;

public abstract class Function extends Content {

    private String _name;

    public Function(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public abstract Literal value();

    @Override
    public boolean equals(Object o) {
        if (o instanceof String) {
            String name = (String) o;
            int minLength = Math.min(_name.length(), name.length());
            for (int i = 0; i < minLength; i++) 
                if (_name.charAt(i) != name.charAt(i)) 
                    return false;
            return true;
        }
        return false;
    }

}