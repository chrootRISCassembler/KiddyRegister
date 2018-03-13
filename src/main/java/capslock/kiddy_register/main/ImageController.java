package capslock.kiddy_register.main;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class ImageController extends ChildController{

    @FXML private FlowPane flowPane;

    @Override
    public final void init() {
        Logger.INST.debug("image init called");
        MainHandler.INST.getImageList().stream()
                .map(path -> new ImageView(new Image(path.toUri().toString())))
                .forEach(imageView -> {
                    imageView.setPreserveRatio(true);
                    imageView.setFitHeight(flowPane.getHeight() / 3.0);
                    flowPane.getChildren().add(imageView);
                });
        parentController.enableNextButton();
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();

        final List<Path> imageList = new ArrayList<>();

        for (final File file : board.getFiles()){
            final Optional<Path> validFile = new FileChecker(file.toPath())
                    .onCannotWrite(dummy -> true)
                    .onCanExec(dummy -> true)
                    .check();

            if(validFile.isPresent()){
                //画像として読み込めるかどうかのチェック
                imageList.add(validFile.get());
            }
        }

        imageList.stream()
                .map(path -> new ImageView(new Image(path.toUri().toString())))
                .forEach(imageView -> {
                    imageView.setPreserveRatio(true);
                    imageView.setFitHeight(flowPane.getHeight() / 3.0);
                    flowPane.getChildren().add(imageView);
                });

        MainHandler.INST.setImageList(imageList);

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

    @Override
    final void transition() {
        flowPane.getChildren().clear();
    }
}
