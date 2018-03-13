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
