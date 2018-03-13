package capslock.kiddy_register.main;

import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import methg.commonlib.file_checker.FileChecker;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieController extends ChildController{

    @FXML private FlowPane flowPane;

    @Override
    public final void init() {
        Logger.INST.debug("movie init called");
        parentController.disableNextButton();
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();

        final List<Path> movieList = new ArrayList<>();

        for (final File file : board.getFiles()){
            final Optional<Path> validFile = new FileChecker(file.toPath())
                    .onCannotWrite(dummy -> true)
                    .onCanExec(dummy -> true)
                    .check();

            if(validFile.isPresent()){
                //画像として読み込めるかどうかのチェック
                movieList.add(validFile.get());
            }
        }

        MainHandler.INST.setMovieList(movieList);

        event.setDropCompleted(true);
        event.consume();

        parentController.enableNextButton();
    }

    @FXML private void onDragOver(DragEvent event) {
        final Dragboard board = event.getDragboard();

        if (board.hasFiles()) {
            for (final File file : board.getFiles()){
                final Optional<Path> validFile = new FileChecker(file.toPath())
                        .onCannotWrite(dummy -> true)
                        .onCanExec(dummy -> true)
                        .check();

                if(validFile.isPresent()){
                    if(!validFile.get().startsWith(MainHandler.INST.getGameRootDir()))continue;

                    //画像として読み込めるかどうかのチェック

                    event.acceptTransferModes(TransferMode.LINK);
                    break;
                }
            }
        }
        event.consume();
    }
}
