package capslock.kiddy_register.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class SetGameRootDirController extends ChildController {
    @FXML private Label pathLabel;

    @Override
    public final void init() {
        final Path gameRootDir = MainHandler.INST.getGameRootDir();
        if (gameRootDir == null) {
            pathLabel.setText("まだ指定されていません.");
        } else {
            pathLabel.setText(gameRootDir.toString());
            mainController.enableNextButton();
        }
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();
        final Path rootPath = board.getFiles().get(0).toPath();

        MainHandler.INST.setGameRootDir(rootPath);
        pathLabel.setText(rootPath.toString());

        event.setDropCompleted(true);
        event.consume();

        mainController.enableNextButton();
    }

    @FXML private void onDragOver(DragEvent event){
        final Dragboard board = event.getDragboard();
        checkContent: if(board.hasFiles()){
            final List<File> fileList = board.getFiles();
            if(fileList.size() != 1)break checkContent;

            final Path rootDir = fileList.get(0).toPath();
            if(!Files.isDirectory(rootDir))break checkContent;

            event.acceptTransferModes(TransferMode.LINK);
        }
        event.consume();
    }
}
