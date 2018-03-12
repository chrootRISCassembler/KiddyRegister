package capslock.kiddy_register.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import methg.commonlib.file_checker.FileChecker;
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

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
        }
    }

    @FXML
    private void onConfirmPushed(ActionEvent event){
        MainHandler.INST.confirm();
    }

    @FXML
    private void onSelectPushed(ActionEvent event){
        //MainHandler.INST.getStage().setScene(new Scene((Parent) State.SELECT_MODE.getRootNode()));

        //SELECT_MODE("SelectMode.fxml"),
    }
}
