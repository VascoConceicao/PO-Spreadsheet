package xxl;

import java.util.Collection;

public class User {

    private String _name = "root";
    private Collection<Spreadsheet> _spreadsheets;

    public User () {}

    public User(String name) {
        _name = name;
    }

}