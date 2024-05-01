package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
import xxl.SearchFunction;

import xxl.exceptions.UnrecognizedEntryException;

/**
 * Command for searching function names.
 */
class DoShowFunctions extends Command<Spreadsheet> {

    DoShowFunctions(Spreadsheet receiver) {
        super(Label.SEARCH_FUNCTIONS, receiver);
        addStringField("name", Prompt.searchFunction());
    }

    @Override
    protected final void execute() {
        _display.popup(_receiver.search(new SearchFunction(stringField("name"))));
    }

}
