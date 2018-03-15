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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import methg.commonlib.trivial_logger.Logger;

public class NameController extends ChildController{

    @FXML private TextField textField;

    @Override
    public final void init() {
        Logger.INST.debug("init called");

        final String name = MainHandler.INST.getName();
        if (name == null || name.isEmpty()) {
            final String fileName = MainHandler.INST.getExe().getFileName().toString();
            final int dotPosition = fileName.lastIndexOf('.');
            if(dotPosition == -1){
                textField.setText(fileName);
            }else{
                textField.setText(fileName.substring(0, dotPosition));
            }
        }else{
            textField.setText(name);
        }
        parentController.enableNextButton();
    }

    @FXML private void onTextFieldReleased(KeyEvent event){
        if(textField.getText().isEmpty()){
            parentController.disableNextButton();
        }else{
            parentController.enableNextButton();
        }
    }

    @Override
    public final void transition(){
        MainHandler.INST.setName(textField.getText());
    }
}
