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
