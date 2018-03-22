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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import methg.commonlib.file_checker.FileChecker;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class PanelController extends ChildController{

    @FXML private ImageView imageView;

    @Override
    public final void init() {
        Logger.INST.debug("panel init called");
        final Path panelFile = MainHandler.INST.getPanel();

        parentController.warn("init", Color.GREEN);

        if (panelFile == null){
            Logger.INST.info("Panel is null");
            return;
        }

        imageView.setImage(new Image(panelFile.toUri().toString()));
        parentController.enableNextButton();
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();
        final Path panelPath = board.getFiles().get(0).toPath();

        MainHandler.INST.setPanel(panelPath);

        imageView.setImage(new Image(panelPath.toUri().toString()));

        event.setDropCompleted(true);
        event.consume();

        parentController.enableNextButton();
    }

    @FXML private void onDragOver(DragEvent event) {
        final Dragboard board = event.getDragboard();
        checkContent:
        if (board.hasFiles()) {
            final List<File> fileList = board.getFiles();
            if (fileList.size() != 1) break checkContent;

            final Path exePath = fileList.get(0).toPath();

            final Optional<Path> panel = new FileChecker(exePath)
                    .onCannotWrite(dummy -> true)
                    .onCanExec(dummy -> true)
                    .check();

            if (panel.isPresent() && panel.get().startsWith(MainHandler.INST.getGameRootDir()))
                event.acceptTransferModes(TransferMode.LINK);
        }
        event.consume();
    }

    @Override
    void transition() {
        imageView.setImage(null);//参照を外してGCによる回収を期待する.
    }
}
