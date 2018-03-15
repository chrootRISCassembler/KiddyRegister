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

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import methg.commonlib.trivial_logger.Logger;

public class DescController extends ChildController{

    @FXML private TextArea textArea;

    @Override
    public final void init() {
        Logger.INST.debug("init called");

        final String desc = MainHandler.INST.getDesc();
        if(desc != null && !desc.isEmpty()){
            textArea.setText(desc);
            parentController.enableNextButton();
        }
    }

    @FXML private void onTextAreaReleased(KeyEvent event){
        if(textArea.getText().isEmpty()){
            parentController.disableNextButton();
        }else{
            parentController.enableNextButton();
        }
    }

    @Override
    public final void transition(){
        MainHandler.INST.setDesc(textArea.getText());
    }
}
