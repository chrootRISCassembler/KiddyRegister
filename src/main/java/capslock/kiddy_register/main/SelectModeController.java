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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import methg.commonlib.trivial_logger.Logger;

public class SelectModeController{
    private Mode selectedMode;

    @FXML private RadioButton register;
    @FXML private RadioButton update;
    @FXML private RadioButton preview;
    @FXML private Label detailLabel;
    @FXML private Button startButton;

    public final void init() {
        Logger.INST.debug("SelectModeController init called");
    }

    @FXML private void onStartPushed(ActionEvent event){
        MainHandler.INST.runAsMode(selectedMode);
    }

    @FXML private void onRegisterSelected(ActionEvent event){
        selectedMode = Mode.REGISTER;
        detailLabel.setText("新規登録モード\r\t一度も登録していないゲームを登録します." +
                "\n\t以前に登録したゲームに対してこのモードを使用しないでください." +
                "\n\tゲーム提出が認められない場合があります.");
        startButton.setText("新規登録モードで起動");
        startButton.setDisable(false);
    }

    @FXML private void onUpdateSelected(ActionEvent event){
        selectedMode = Mode.UPDATE;
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
        selectedMode = Mode.PREVIEW;
        detailLabel.setText("プレビューモード" +
                "\n\t登録済みゲームがランチャーで正しく表示されるかチェックすることができます." +
                "\n\tこのモードでは登録情報を更新することができません.");
        startButton.setText("プレビューモードで起動");
        startButton.setDisable(false);
    }
}
