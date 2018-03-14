package capslock.kiddy_register.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import methg.commonlib.trivial_logger.Logger;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IDController extends ChildController{

    @FXML private ChoiceBox<Integer> choiceBox;
    @Override
    public final void init() {

        Logger.INST.debug("ID init called");

        parentController.disableNextButton();

        choiceBox.setItems(FXCollections.observableList(IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList())));

        choiceBox.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                        MainHandler.INST.setID(number2.intValue());
                        parentController.enableNextButton();
                    }
                });


    }
}
