/*
    Copyright (C) 2018 RISCassembler

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package capslock.kiddy_register.main;

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

        if (MainHandler.INST.getID() == 0) {
            parentController.disableNextButton();
            choiceBox.setItems(FXCollections.observableList(IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList())));
            choiceBox.setStyle("-fx-font-size: 20");

            choiceBox.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        MainHandler.INST.setID(newValue);
                        parentController.enableNextButton();
                    });
        } else {
            choiceBox.setValue(MainHandler.INST.getID());
            parentController.enableNextButton();
        }
    }
}
