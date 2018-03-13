package capslock.kiddy_register.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import methg.commonlib.trivial_logger.Logger;

public class SelectModeController{
    private Mode selecetedMode;

    @FXML private RadioButton register;
    @FXML private RadioButton update;
    @FXML private RadioButton preview;
    @FXML private Label detailLabel;
    @FXML private Button startButton;

    public final void init() {
        Logger.INST.debug("SelectModeController init called");
    }

    @FXML private void onStartPushed(ActionEvent event){
        MainHandler.INST.runAsMode(selecetedMode);
    }

    @FXML private void onRegisterSelected(ActionEvent event){
        selecetedMode = Mode.REGISTER;
        detailLabel.setText("新規登録モード\r\t一度も登録していないゲームを登録します." +
                "\n\t以前に登録したゲームに対してこのモードを使用しないでください." +
                "\n\tゲーム提出が認められない場合があります.");
        startButton.setText("新規登録モードで起動");
        startButton.setDisable(false);
    }

    @FXML private void onUpdateSelected(ActionEvent event){
        selecetedMode = Mode.UPDATE;
        detailLabel.setText("更新モード\n\t一度登録したゲーム情報を更新します." +
                "\n\t以下の場合は更新を行って下さい." +
                "\n\t\t+ ゲームの実行ファイルを変更,移動した" +
                "\n\t\t+ パネル画像を変更,移動した" +
                "\n\t\t+ ゲーム名,説明を変更したい" +
                "\n\t\t+ 紹介動画,紹介画像を削除,追加,変更,移動した");
        startButton.setText("更新モードで起動");
        startButton.setDisable(false);
    }

    @FXML private void onPreviewSelected(ActionEvent event){
        selecetedMode = Mode.PREVIEW;
        detailLabel.setText("プレビューモード" +
                "\n\t登録済みゲームがランチャーで正しく表示されるかチェックすることができます." +
                "\n\tこのモードでは登録情報を更新することができません.");
        startButton.setText("プレビューモードで起動");
        startButton.setDisable(false);
    }
}
