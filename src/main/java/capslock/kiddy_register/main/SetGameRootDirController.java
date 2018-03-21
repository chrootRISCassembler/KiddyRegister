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
import javafx.scene.paint.Paint;
import methg.commonlib.file_checker.FDSkimmer;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;


public class SetGameRootDirController extends ChildController {
    @FXML private Label pathLabel;
    @FXML private Label warnLabel;

    @Override
    public final void init() {
        final Path gameRootDir = MainHandler.INST.getGameRootDir();

        if(gameRootDir == null)return;

        pathLabel.setText(gameRootDir.toString());

        if(Files.notExists(gameRootDir)) {
            warnLabel.setText("ディレクトリが見つかりません.指定し直して下さい.");
            warnLabel.setTextFill(Paint.valueOf("red"));
            return;
        }

        if(validateRootDir(gameRootDir, false)) {
            warnLabel.setText("設定内容は正常です.");
            warnLabel.setTextFill(Paint.valueOf("green"));
            parentController.enableNextButton();
        }
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();
        final Path rootPath = board.getFiles().get(0).toPath();

        MainHandler.INST.setGameRootDir(rootPath);
        pathLabel.setText(rootPath.toString());

        event.setDropCompleted(true);
        event.consume();

        parentController.enableNextButton();
    }

    @FXML private void onDragOver(DragEvent event){
        final Dragboard board = event.getDragboard();
        checkContent: if(board.hasFiles()){
            final List<File> fileList = board.getFiles();

            if(fileList.size() != 1){
                warnLabel.setText("ルートディレクトリは1つのみです.");
                warnLabel.setTextFill(Paint.valueOf("red"));
                break checkContent;
            }

            if(validateRootDir(fileList.get(0).toPath(), true)) {
                warnLabel.setText("設定可能です.");
                warnLabel.setTextFill(Paint.valueOf("green"));
                event.acceptTransferModes(TransferMode.LINK);
            }
        }
        event.consume();
    }

    @FXML private void onDragExited(DragEvent event){
        final Path rootDir = MainHandler.INST.getGameRootDir();
        if(rootDir == null){
            warnLabel.setText("まだ設定されていません.");
            warnLabel.setTextFill(Paint.valueOf("red"));
            return;
        }

        if(validateRootDir(rootDir, false)){
            warnLabel.setText("設定内容は正常です.");
            warnLabel.setTextFill(Paint.valueOf("green"));
        }
    }

    private boolean validateRootDir(Path path, boolean isBeingDragged){
        final String demonstrative = isBeingDragged ? "その" : "この";
        if(!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)){
            Logger.INST.debug("It's not a directory.");
            warnLabel.setTextFill(Paint.valueOf("red"));
            warnLabel.setText(demonstrative + "ファイルはディレクトリではありません.");
            return false;
        }

        final FDSkimmer skimmer = new FDSkimmer(path);
        if(!skimmer.permCheck(FDSkimmer.Permission.NEED, FDSkimmer.Permission.NEED, FDSkimmer.Permission.NEED)){
            Logger.INST.debug("Permission is not \"rwx\".");
            warnLabel.setTextFill(Paint.valueOf("red"));
            warnLabel.setText(demonstrative + "ディレクトリには不適切なアクセス権限が設定されています.");
            return false;
        }

        try {
            if(Files.list(path).count() == 0){
                Logger.INST.debug("This dir is empty.");
                warnLabel.setTextFill(Paint.valueOf("red"));
                warnLabel.setText(demonstrative + "ディレクトリには中身がありません.");
                return false;
            }
        }catch (IOException ex){
            Logger.INST.logException(ex);
        }

        return true;
    }
}
