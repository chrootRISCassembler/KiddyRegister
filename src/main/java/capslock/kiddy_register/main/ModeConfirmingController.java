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
    @FXML private Button selectButton;

    public final void init() {
        Logger.INST.debug("ModeConfirmingController#init");

        if(MainHandler.INST.getMode() == Mode.REGISTER){
            informLabel.setText("ゲーム情報を新たに登録します.違う場合はモード選択ボタンを押してください.");
        }else{
            informLabel.setText("登録済みのゲーム情報を更新します.違う場合はモード選択ボタンを押してください.");
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
