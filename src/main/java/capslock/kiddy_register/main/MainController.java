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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import methg.commonlib.trivial_logger.Logger;

public class MainController implements IController{

    @FXML private VBox rootVBox;
    @FXML private HBox bottomHBox;
    @FXML private Button prevButton;
    @FXML private Button nextButton;

    @FXML private void onNextClicked(ActionEvent event){
        replace(MainHandler.INST.nextState());
    }

    @FXML private void onPrevClicked(ActionEvent event){

    }

    void onCreate(WindowEvent event) {

    }

    void replace(RegisterState state) {
        Logger.INST.debug("replace called");

        final ObservableList<Node> nodeList = rootVBox.getChildren();

        if(nodeList.get(0).equals(bottomHBox)){
            nodeList.add(0, state.getRootNode());
        }else{
            nodeList.set(0, state.getRootNode());
        }

        state.getController().init();
    }

    /**
     * 設定項目が正常なとき以外にこのメソッドを呼んではならない
     */
    void enablePrevButton(){
        prevButton.setDisable(false);
    }

    void disablePrevButton(){
        prevButton.setDisable(true);
    }

    /**
     * ユーザーが次の設定項目に進めるようにする.
     * 設定項目が正常なとき以外にこのメソッドを呼んではならない
     */
    void enableNextButton(){
        nextButton.setDisable(false);
    }

    void disableNextButton(){
        nextButton.setDisable(true);
    }

}
