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
import javafx.scene.control.Label;
import methg.commonlib.trivial_logger.Logger;

public class PreviewController extends ChildController{
    @FXML private Label quitLabel;

    @Override
    public final void init() {
        Logger.INST.debug("PreviewController#init called");
        if(MainHandler.INST.getMode() == Mode.PREVIEW){
            quitLabel.setText("このウィンドウを閉じてプレビューを終了します.");
        }
    }

    @FXML private void onLaunchPushed(ActionEvent event){
        MainHandler.INST.launch();
    }
}
