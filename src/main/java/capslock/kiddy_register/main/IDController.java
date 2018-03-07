package capslock.kiddy_register.main;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import methg.commonlib.trivial_logger.Logger;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IDController implements IController {

    @FXML private ChoiceBox<Integer> choiceBox;
    @Override
    public final void init() {
        Logger.INST.debug("ID init called");

        choiceBox.setItems(FXCollections.observableList(IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList())));

        MainHandler.INST.getController().enableNextButton();
    }
}
