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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class ExeController extends ChildController{
    @FXML private Label pathLabel;
    @FXML private Label warnLabel;

    @Override
    public final void init() {
        Logger.INST.debug("Exe init called");

        final Path exe = MainHandler.INST.getExe();

        if (exe == null)return;

        pathLabel.setText(exe.toString());

        if(Files.notExists(exe)) {
            warnLabel.setText("ファイルが見つかりません.指定し直して下さい.");
            warnLabel.setTextFill(Color.RED);
            return;
        }

        if(validateExe(exe, false)){
            warnLabel.setText("登録内容は正常です.");
            warnLabel.setTextFill(Color.GREEN);
            parentController.enableNextButton();
        }
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();
        final Path exePath = board.getFiles().get(0).toPath();

        MainHandler.INST.setExe(exePath);
        pathLabel.setText(exePath.toString());
        parentController.warn("表示されているパスをゲームの実行ファイルとして登録しました.", Color.GREEN);
        warnLabel.setText("登録内容は正常です.");
        warnLabel.setTextFill(Color.GREEN);
        parentController.enableNextButton();

        event.setDropCompleted(true);
        event.consume();
    }

    @FXML private void onDragOver(DragEvent event){
        final Dragboard board = event.getDragboard();
        checkContent: if(board.hasFiles()){
            final List<File> fileList = board.getFiles();
            if(fileList.size() != 1){
                parentController.warn("ゲームの実行ファイルは1つのみ登録できます.", Color.RED);
                break checkContent;
            }

            if(validateExe(fileList.get(0).toPath(), true)) {
                parentController.warn("ドロップして登録します.", Color.GREEN);
                event.acceptTransferModes(TransferMode.LINK);
            }
        }
        event.consume();
    }

    /**
     * ファイルがゲームの実行ファイルとしての要件を満たすかどうか, つまりランチャーで正常に起動できるかどうか検証する.
     * <p>副作用で{@link #warnLabel}にエラー内容を表示する.</p>
     * @param path 検証対象のファイルパス
     * @param isBeingDragged ドラッグ&ドロップ中かどうか
     * @return {@code true}引数がゲームの実行ファイルとしての要件を満たす. {@code false}それ以外
     */
    private boolean validateExe(Path path, boolean isBeingDragged) {
        if(!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)){
            Logger.INST.debug("It isn't a regular file.");
            if(isBeingDragged){
                parentController.warn("ドロップしようとしているものは通常ファイルではありません.", Color.RED);
            }else{
                warnLabel.setTextFill(Color.RED);
                warnLabel.setText("このファイルは通常ファイルではありません.");
            }
            return false;
        }

        if(!path.startsWith(MainHandler.INST.getGameRootDir())){
            Logger.INST.debug("Exe file is at the outside of the root directory of the game.");
            if(isBeingDragged){
                parentController.warn("ドロップしようとしているファイルはゲームのルートディレクトリの中にありません.", Color.RED);
            }else{
                warnLabel.setTextFill(Color.RED);
                warnLabel.setText("このファイルはゲームのルートディレクトリの中にありません.");
            }
            return false;
        }

        if(!Files.isExecutable(path)){
            Logger.INST.debug("Not executable.");
            if(isBeingDragged){
                parentController.warn("ドロップしようとしているファイルには実行権限が与えられていません.", Color.RED);
            }else {
                warnLabel.setTextFill(Color.RED);
                warnLabel.setText("このファイルには実行権限が与えられていません.");
            }
            return false;
        }

        //先頭の2byteを読み出して,MSDOSのEXEファイルフォーマットのマジックナンバー"MZ"と一致するか検証する.
        try (final InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ)){
            byte[] magicNumber = new byte[2];
            inputStream.read(magicNumber);
            System.err.println("[0] = " + magicNumber[0] + ", [1] = " + magicNumber[1]);
            if(Arrays.equals(magicNumber, new byte[]{'M', 'Z'})){
                Logger.INST.debug("Ok, this is exe format file.");
                return true;
            }else{
                if(isBeingDragged){
                    parentController.warn("ドロップしようとしているファイルは実行できません.", Color.RED);
                }else {
                    warnLabel.setTextFill(Color.RED);
                    warnLabel.setText("このファイルは実行できません.");
                }
                return false;
            }
        }catch (IOException ex){
            Logger.INST.logException(ex);
            warnLabel.setTextFill(Color.YELLOW);
            warnLabel.setText("このファイルは実行できない可能性があります.");
            return true;
        }
    }
}
