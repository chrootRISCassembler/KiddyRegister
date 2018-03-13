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

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import methg.commonlib.trivial_logger.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainController{
    private ListIterator<State> stateIte;
    private ChildController controller;

    @FXML private VBox rootVBox;
    @FXML private HBox bottomHBox;
    @FXML private Button prevButton;
    @FXML private Button nextButton;

    void start(List<State> stateList){
        stateIte = stateList.listIterator();

        final State state = stateIte.next();

        rootVBox.getChildren().add(0, state.getRootNode());

        controller = state.getController();
        controller.init();
    }

    @FXML private void onPrevClicked(ActionEvent event){
        controller.transition();
        final State state = stateIte.previous();
        if(!stateIte.hasPrevious())prevButton.setVisible(false);
        rootVBox.getChildren().set(0, state.getRootNode());
        controller = state.getController();
        controller.init();

        nextButton.setVisible(true);
    }

    @FXML private void onNextClicked(ActionEvent event){
        controller.transition();
        final State state = stateIte.next();
        if(!stateIte.hasNext())nextButton.setVisible(false);
        rootVBox.getChildren().set(0, state.getRootNode());
        controller = state.getController();
        controller.init();

        prevButton.setVisible(true);
    }

    void disableNextButton(){

    }

    void enableNextButton(){

    }
}
