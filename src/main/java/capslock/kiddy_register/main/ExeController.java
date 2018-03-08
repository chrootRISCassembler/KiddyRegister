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

public class ExeController implements IController{
    @FXML
    private Label pathLabel;

    @Override
    public final void init() {
        Logger.INST.debug("Exe init called");

        final Path exe = MainHandler.INST.getExe();
        if (exe == null) {
            pathLabel.setText("まだ指定されていません.");
            MainHandler.INST.getController().disableNextButton();
        } else {
            pathLabel.setText(exe.toString());
            MainHandler.INST.getController().enableNextButton();
        }
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();
        final Path exePath = board.getFiles().get(0).toPath();

        MainHandler.INST.setExe(exePath);
        pathLabel.setText(exePath.toString());

        event.setDropCompleted(true);
        event.consume();

        MainHandler.INST.getController().enableNextButton();
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
