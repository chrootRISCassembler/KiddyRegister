package capslock.kiddy_register.main;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
        mainController.disableNextButton();
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();
        final Path panelPath = board.getFiles().get(0).toPath();

        MainHandler.INST.setPanel(panelPath);

        imageView.setImage(new Image(panelPath.toUri().toString()));

        event.setDropCompleted(true);
        event.consume();

        mainController.disableNextButton();
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
}
