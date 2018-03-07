package capslock.kiddy_register.main;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import methg.commonlib.file_checker.FileChecker;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

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
}
