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
import methg.commonlib.file_checker.FileChecker;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ExeController extends ChildController{
    @FXML
    private Label pathLabel;

    @Override
    public final void init() {
        Logger.INST.debug("Exe init called");

        final Path exe = MainHandler.INST.getExe();
        if (exe == null) {
            pathLabel.setText("まだ指定されていません.");
        } else {
            pathLabel.setText(exe.toString());
            parentController.enableNextButton();
        }
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();
        final Path exePath = board.getFiles().get(0).toPath();

        MainHandler.INST.setExe(exePath);
        pathLabel.setText(exePath.toString());

        event.setDropCompleted(true);
        event.consume();

        parentController.enableNextButton();
    }

    @FXML private void onDragOver(DragEvent event){
        final Dragboard board = event.getDragboard();
        checkContent: if(board.hasFiles()){
            final List<File> fileList = board.getFiles();
            if(fileList.size() != 1)break checkContent;

            final Path exePath = fileList.get(0).toPath();

            final Optional<Path> exe = new FileChecker(exePath)
                    .onCannotRead(dummy -> true)
                    .onCannotWrite(dummy -> true)
                    .onCanExec(dummy -> true)
                    .check();

            if(exe.isPresent() && exe.get().startsWith(MainHandler.INST.getGameRootDir()))
                event.acceptTransferModes(TransferMode.LINK);
        }
        event.consume();
    }
}
