package capslock.kiddy_register.main;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import methg.commonlib.trivial_logger.Logger;

public class DescController implements IController {

    @FXML private TextArea textArea;

    @Override
    public final void init() {
        Logger.INST.debug("init called");

        textArea.setText(MainHandler.INST.getDesc());

        MainHandler.INST.getController().enableNextButton();
    }
}
