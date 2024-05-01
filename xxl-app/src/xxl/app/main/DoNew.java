package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;
import xxl.Spreadsheet;

/**
 * Open a new file.
 */
class DoNew extends Command<Calculator> {

    DoNew(Calculator receiver) {
        super(Label.NEW, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        if (_receiver.hasStarted() && _receiver.hasChanged() && Form.confirm(Prompt.saveBeforeExit())) {
            DoSave cmd = new DoSave(_receiver);
            cmd.execute();
        }
        _receiver.reset(Form.requestInteger(Prompt.lines()), Form.requestInteger(Prompt.columns()));
    }

}
