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
import javafx.scene.paint.Color;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * ゲームの紹介動画と紹介画像を登録させる.
 */
public class ContentController extends ChildController{

    static private final int IMAGE_HARD_MAX = 10;
    static private final int MOVIE_HARD_MAX = 10;

    private final List<MediaPlayer> playerList = new ArrayList<>(3);
    private final List<Path> imageList = new ArrayList<>(3);
    private final List<Path> movieList = new ArrayList<>(3);

    @FXML private FlowPane flowPane;

    @Override
    public final void init() {
        Logger.INST.debug("ContentController#init called");

        for (final Path path : MainHandler.INST.getImageList()){
            if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS))continue;
            if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) continue;
            if (!Files.isReadable(path)) continue;
            if (!path.startsWith(MainHandler.INST.getGameRootDir())) continue;

            final Image image = new Image(path.toUri().toString());
            if(image.isError())continue;

            final ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(flowPane.getPrefWidth() / 3.5);
            flowPane.getChildren().add(imageView);
        }

        for(final Path path : MainHandler.INST.getMovieList()){
            if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS))continue;
            if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) continue;
            if (!Files.isReadable(path)) continue;
            if (!path.startsWith(MainHandler.INST.getGameRootDir())) continue;

            final MediaPlayer player = new MediaPlayer(new Media(path.toUri().toString()));
            player.setMute(true);
            player.setAutoPlay(true);
            player.setCycleCount(MediaPlayer.INDEFINITE);

            playerList.add(player);

            final MediaView mediaView = new MediaView(player);
            mediaView.setPreserveRatio(true);
            mediaView.setFitWidth(flowPane.getPrefWidth() / 3.5);

            flowPane.getChildren().add(mediaView);
        }

        if(!movieList.isEmpty() || !imageList.isEmpty()){
            parentController.enableNextButton();
        }
    }

    @FXML private void onDragDropped(DragEvent event){
        imageList.clear();
        movieList.clear();

        transition();

        final Dragboard board = event.getDragboard();

        for (final File file : board.getFiles()){
            final Path path = file.toPath();

            if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) continue;
            if (!Files.isReadable(path)) continue;
            if (!path.startsWith(MainHandler.INST.getGameRootDir())) continue;

            try {
                final Media canFailMovie = new Media(path.toUri().toString());

                final MediaPlayer player = new MediaPlayer(canFailMovie);

                final MediaException exception = player.getError();
                if(exception == null && movieList.size() > MOVIE_HARD_MAX){
                    parentController.warn("紹介動画の数は" + MOVIE_HARD_MAX +
                            "以下でなければなりません.", Color.RED);

                    player.dispose();
                    continue;
                }

                player.setMute(true);
                player.setAutoPlay(true);
                player.setCycleCount(MediaPlayer.INDEFINITE);

                playerList.add(player);

                final MediaView mediaView = new MediaView(player);
                mediaView.setPreserveRatio(true);
                mediaView.setFitWidth(flowPane.getPrefWidth() / 3.5);

                flowPane.getChildren().add(mediaView);

                movieList.add(path);

                continue;

            }catch (MediaException ex){
                Logger.INST.debug("This file isn't a movie.");
            }catch (Exception ex){
                Logger.INST.logException(ex);
            }

            try {
                final Image canFailImage = new Image(path.toUri().toString());

                final Exception exception = canFailImage.getException();
                if(exception == null){
                    if(imageList.size() > IMAGE_HARD_MAX){
                        parentController.warn("紹介画像の数は" + IMAGE_HARD_MAX +
                                "以下でなければなりません.", Color.RED);
                        continue;
                    }
                }else{
                    continue;
                }

                final ImageView imageView = new ImageView(canFailImage);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(flowPane.getPrefWidth() / 3.5);

                flowPane.getChildren().add(imageView);

                imageList.add(path);

                continue;

            }catch (Exception ex){
                Logger.INST.logException(ex);
            }

            Logger.INST.debug("This file is probably neither image nor movie.");
        }

        MainHandler.INST.setMovieList(movieList);
        MainHandler.INST.setImageList(imageList);

        if(movieList.isEmpty() && imageList.isEmpty()){
            parentController.warn("ドロップされたファイル中に登録可能な画像,動画は存在しません.", Color.RED);
            parentController.disableNextButton();
        }else{
            parentController.warn("表示されている画像,動画を登録しました.", Color.GREEN);
            parentController.enableNextButton();
        }

        event.setDropCompleted(true);
        event.consume();
    }

    /**
     * 一つでも登録できそうなものがあれば受け付ける.
     * <p>重たいからここでは画像,動画として読み込めるかどうかのチェックは行わない.</p>
     * @param event ドラッグイベント
     */
    @FXML private void onDragOver(DragEvent event) {
        final Dragboard board = event.getDragboard();

        if (!board.hasFiles()) {
            event.consume();
            return;
        }

        for (final File file : board.getFiles()) {
            final Path path = file.toPath();

            if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) continue;
            if (!Files.isReadable(path)) continue;
            if (!path.startsWith(MainHandler.INST.getGameRootDir())) continue;

            event.acceptTransferModes(TransferMode.LINK);
            event.consume();
            return;
        }

        parentController.warn("登録できる動画,画像が一つも含まれていません.", Color.RED);
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
