package capslock.kiddy_register.main;

import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import methg.commonlib.file_checker.FileChecker;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieController extends ChildController{

    private final List<MediaPlayer> playerList = new ArrayList<>(3);

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

            if(!validFile.isPresent())continue;

            movieList.add(validFile.get());

            final Media media = new Media(validFile.get().toUri().toString());
            final MediaPlayer player = new MediaPlayer(media);
            player.setMute(true);
            player.setAutoPlay(true);
            player.setCycleCount(MediaPlayer.INDEFINITE);

            playerList.add(player);

            final MediaView mediaView = new MediaView(player);
            mediaView.setPreserveRatio(true);
            mediaView.setFitWidth(flowPane.getWidth() / 3.0);

            flowPane.getChildren().add(mediaView);
        }


        MainHandler.INST.setMovieList(movieList);

        event.setDropCompleted(true);
        event.consume();

        parentController.enableNextButton();
        Logger.INST.debug("drop quit");
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
    void transition() {
        flowPane.getChildren().clear();
        playerList.forEach(player -> {
            player.stop();
            player.dispose();
        });
        playerList.clear();
    }
}
