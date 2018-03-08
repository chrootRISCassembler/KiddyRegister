package capslock.kiddy_register.main;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import methg.commonlib.trivial_logger.Logger;

public class NameController implements IController {

    @FXML private TextField textField;

    @Override
    public final void init() {
        Logger.INST.debug("init called");

        final String name = MainHandler.INST.getName();
        if (name == null || name.isEmpty()) {
            textField.setText(MainHandler.INST.getExe().getFileName().toString());
        }else{
            textField.setText(name);
        }

        MainHandler.INST.getController().enableNextButton();
    }

    @Override
    public final void transition(){
        MainHandler.INST.setName(textField.getText());
    }
}
