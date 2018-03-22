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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;

public class PanelController extends ChildController{

    @FXML private ImageView imageView;
    @FXML private Label warnLabel;

    @Override
    public final void init() {
        Logger.INST.debug("panel init called");
        final Path panelFile = MainHandler.INST.getPanel();

        if(panelFile == null){
            warnLabel.setText("まだ登録されていません.");
            warnLabel.setTextFill(Color.RED);
            return;
        }

        if(Files.notExists(panelFile, LinkOption.NOFOLLOW_LINKS)){
            warnLabel.setText("ファイルが見つかりません.再登録してください.");
            warnLabel.setTextFill(Color.RED);
            return;
        }

        if(!Files.isRegularFile(panelFile, LinkOption.NOFOLLOW_LINKS)){
            warnLabel.setText("このファイルは通常ファイルではありません.");
            warnLabel.setTextFill(Color.RED);
            return;
        }

        if(!Files.isReadable(panelFile)){
            warnLabel.setText("このファイルはJavaから読み込むことができません.");
            warnLabel.setTextFill(Color.RED);
            return;
        }

        if(!panelFile.startsWith(MainHandler.INST.getGameRootDir())){
            warnLabel.setText("このファイルはゲームのルートディレクトリ外にあります.");
            warnLabel.setTextFill(Color.RED);
            return;
        }

        final Image image = new Image(panelFile.toUri().toString());

        if(image.isError()){
            warnLabel.setText("このファイルは画像として読み込めません.");
            warnLabel.setTextFill(Color.RED);
            return;
        }

        imageView.setImage(image);

        if(image.getWidth() == image.getHeight()){
            warnLabel.setText("登録内容は正常です.");
            warnLabel.setTextFill(Color.GREEN);
        }else{
            warnLabel.setText("画像は正方形ではありません.ランチャー上では正方形に正規化されます.");
            warnLabel.setTextFill(Color.YELLOW);
        }

        parentController.enableNextButton();
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();
        final Path panelPath = board.getFiles().get(0).toPath();

        final Image image = new Image(panelPath.toUri().toString());

        if(image.isError()){
            parentController.warn("ドロップされたファイルは画像として読み込むことができませんでした.", Color.RED);
        }else {
            parentController.warn("ドロップされた画像をパネル画像として登録しました.", Color.GREEN);

            if(image.getHeight() == image.getWidth()){
                warnLabel.setText("登録内容は正常です.");
                warnLabel.setTextFill(Color.GREEN);
            }else {
                warnLabel.setText("画像は正方形ではありません.ランチャー上では正方形に正規化されます.");
                warnLabel.setTextFill(Color.YELLOW);
            }

            imageView.setImage(image);
            MainHandler.INST.setPanel(panelPath);
            parentController.enableNextButton();
        }

        event.setDropCompleted(true);
        event.consume();
    }

    @FXML private void onDragOver(DragEvent event) {
        final Dragboard board = event.getDragboard();
        checkContent:
        if (board.hasFiles()) {
            final List<File> fileList = board.getFiles();
            if (fileList.size() != 1) {
                parentController.warn("パネル画像は一つのみ登録できます.", Color.RED);
                break checkContent;
            }

            final Path panelPath = fileList.get(0).toPath();

            if(!Files.isRegularFile(panelPath, LinkOption.NOFOLLOW_LINKS)){
                parentController.warn("ドロップしようとしているものは通常ファイルではありません.", Color.RED);
                break checkContent;
            }

            if(!Files.isReadable(panelPath)){
                parentController.warn("ドロップしようとしているファイルはJavaから読み込むことができません.", Color.RED);
                break checkContent;
            }

            if(panelPath.startsWith(MainHandler.INST.getGameRootDir())){
                event.acceptTransferModes(TransferMode.LINK);
            }else{
                parentController.warn("ドロップしようとしているファイルはゲームのルートディレクトリ外にあります.", Color.RED);
            }
        }
        event.consume();
    }

    @Override
    void transition() {
        imageView.setImage(null);//参照を外してGCによる回収を期待する.
    }
}
