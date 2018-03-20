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

/**
 * ゲームの紹介動画と紹介画像を登録させる.
 */
public class ContentController extends ChildController{

    private final List<MediaPlayer> playerList = new ArrayList<>(3);
    private final List<Path> imageList = new ArrayList<>(3);
    private final List<Path> movieList = new ArrayList<>(3);

    @FXML private FlowPane flowPane;

    @Override
    public final void init() {
        Logger.INST.debug("ContentController#init called");

        MainHandler.INST.getImageList().stream()
                .map(path -> new ImageView(new Image(path.toUri().toString())))
                .forEach(imageView -> {
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(flowPane.getPrefWidth() / 3.5);
                    flowPane.getChildren().add(imageView);
                });

        for(final Path moviePath : MainHandler.INST.getMovieList()){
            final MediaPlayer player = new MediaPlayer(new Media(moviePath.toUri().toString()));
            player.setMute(true);
            player.setAutoPlay(true);
            player.setCycleCount(MediaPlayer.INDEFINITE);

            playerList.add(player);

            final MediaView mediaView = new MediaView(player);
            mediaView.setPreserveRatio(true);
            mediaView.setFitWidth(flowPane.getPrefWidth() / 3.5);

            flowPane.getChildren().add(mediaView);
        }

        parentController.enableNextButton();
    }

    @FXML private void onDragDropped(DragEvent event){
        imageList.clear();
        movieList.clear();

        transition();

        final Dragboard board = event.getDragboard();

        for (final File file : board.getFiles()){
            final Optional<Path> validFile = new FileChecker(file.toPath())
                    .onCannotWrite(dummy -> true)
                    .onCanExec(dummy -> true)
                    .check();

            if(!validFile.isPresent())continue;

            try {
                final Media canFailMovie = new Media(validFile.get().toUri().toString());

                final MediaPlayer player = new MediaPlayer(canFailMovie);

                player.setMute(true);
                player.setAutoPlay(true);
                player.setCycleCount(MediaPlayer.INDEFINITE);

                playerList.add(player);

                final MediaView mediaView = new MediaView(player);
                mediaView.setPreserveRatio(true);
                mediaView.setFitWidth(flowPane.getPrefWidth() / 3.5);

                flowPane.getChildren().add(mediaView);

                movieList.add(validFile.get());

                continue;

            }catch (MediaException ex){
                Logger.INST.debug("This file isn't a movie.");
            }catch (Exception ex){
                Logger.INST.logException(ex);
            }

            try {
                final Image canFailImage = new Image(validFile.get().toUri().toString());

                final Exception exception = canFailImage.getException();
                if(exception != null)continue;

                final ImageView imageView = new ImageView(canFailImage);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(flowPane.getPrefWidth() / 3.5);

                flowPane.getChildren().add(imageView);

                imageList.add(validFile.get());

                continue;

            }catch (Exception ex){
                Logger.INST.logException(ex);
            }

            Logger.INST.debug("This file is probably neither image nor movie.");
        }

        MainHandler.INST.setMovieList(movieList);
        MainHandler.INST.setImageList(imageList);

        if(!movieList.isEmpty() || !imageList.isEmpty()) parentController.enableNextButton();

        event.setDropCompleted(true);
        event.consume();
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

                    //動画または画像として読み込めるかどうかのチェック

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
        playerList.forEach(player -> {
            player.stop();
            player.dispose();
        });
        playerList.clear();
    }
}
