package capslock.kiddy_register.main;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import methg.commonlib.file_checker.FileChecker;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.FutureTask;

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

        movieList.forEach(this::requestDisplay);

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

    final void requestDisplay(Path path){
        final Media media = new Media(path.toUri().toString());

        try {
            MediaException exception = media.getError();
            if(exception != null)throw exception;
        }catch (Exception ex){
            Logger.INST.logException(ex);
        }

        Logger.INST.debug("media created");

        media.setOnError(() -> Logger.INST.critical("MediaError"));


        if(media == null)throw new NullPointerException();

        final MediaPlayer player = new MediaPlayer(media);

        System.err.println(player.getTotalDuration());
        player.setOnError(() -> Logger.INST.critical("onError"));
        player.setOnStopped(() -> Logger.INST.critical("onStopped"));
        player.setOnHalted(() -> Logger.INST.critical("onHalted"));

        WritableImage snapshot = new WritableImage(100, 100);

        player.setOnError(() -> Logger.INST.critical("play error"));
        //player.setAutoPlay(true);
        player.setCycleCount(1);

        if(player == null)throw new NullPointerException();

        final MediaView mediaView = new MediaView(player);
        mediaView.setPreserveRatio(true);
        //mediaView.setFitWidth(100);
        final Scene scene = new Scene (new Group(mediaView), 100, 100);
        //player.play();
        //player.pause();

        if(mediaView == null)throw new NullPointerException();

        // player.setOnPlaying(futureTask);

        player.setOnReady(() -> {
            Logger.INST.debug("OnReady entry");
            final ImageView view = new ImageView(scene.snapshot(snapshot));
            flowPane.getChildren().add(view);
        });

        player.play();
    }
}
