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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;

public class ModeConfirmingController{


    @FXML private Label informLabel;
    @FXML private Button confirmButton;
    @FXML private Label selectWarnLabel;
    @FXML private Button selectButton;

    public final void init() {
        Logger.INST.debug("ModeConfirmingController#init");

        if(MainHandler.INST.getMode() == Mode.REGISTER){
            selectWarnLabel.setVisible(false);
            selectButton.setVisible(false);
        }else{
            informLabel.setText("登録済みのゲーム情報を更新します.");
            confirmButton.setText("情報を更新する");
        }
    }

    @FXML
    private void onConfirmPushed(ActionEvent event){
        MainHandler.INST.runAsMode();
    }

    @FXML
    private void onSelectPushed(ActionEvent event){
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectMode.fxml"));

        try {
            MainHandler.INST.getStage().setScene(new Scene(loader.load()));
        } catch (IOException ex) {
            Logger.INST.critical("Failed to load SelectMode.fxml").logException(ex);
        }
    }
}
