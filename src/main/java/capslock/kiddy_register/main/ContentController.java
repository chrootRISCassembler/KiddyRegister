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

import capslock.kiddy_register.content.ContentEntry;
import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import methg.commonlib.trivial_logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * ゲームの紹介動画と紹介画像を登録させる.
 */
public class ContentController extends ChildController{
    static private final int CONTENT_HARD_MAX = 20;

    private List<ContentEntry> contentEntryList;

    @FXML private FlowPane flowPane;

    @Override
    public final void init() {
        Logger.INST.debug("ContentController#init called");
        ContentEntry.setOnUnregisterButtonPushed(instance -> {
            flowPane.getChildren().remove(instance.getPane());
            contentEntryList.remove(instance);
            instance.destructor();

            final var imageEntry = contentEntryList.parallelStream()
                    .filter(entry -> !entry.isMovie())
                    .findAny();
            if(!imageEntry.isPresent())parentController.disableNextButton();
        });

        contentEntryList = new ArrayList<>();

        boolean hasImage = false;

        for (final Path path : MainHandler.INST.getImageList()){
            if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS))continue;
            if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) continue;
            if (!Files.isReadable(path)) continue;
            if (!path.startsWith(MainHandler.INST.getGameRootDir())) continue;

            final ContentEntry entry = ContentEntry.asImage(path);
            entry.resizeByWidth(flowPane.getPrefWidth() / 3.5);
            contentEntryList.add(entry);
            flowPane.getChildren().add(entry.getPane());
            hasImage = true;
        }

        for(final Path path : MainHandler.INST.getMovieList()){
            if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS))continue;
            if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) continue;
            if (!Files.isReadable(path)) continue;
            if (!path.startsWith(MainHandler.INST.getGameRootDir())) continue;

            final ContentEntry entry = ContentEntry.asMovie(path);
            entry.resizeByWidth(flowPane.getPrefWidth() / 3.5);
            contentEntryList.add(entry);
            flowPane.getChildren().add(entry.getPane());
        }

        if(hasImage)parentController.enableNextButton();
    }

    @FXML private void onDragDropped(DragEvent event){
        final Dragboard board = event.getDragboard();

        for (final File file : board.getFiles()){
            if(contentEntryList.size() >= CONTENT_HARD_MAX){
                parentController.warn("コンテンツの最大数 : " + CONTENT_HARD_MAX +
                        " を超えたため,一部のファイルを無視します.", Color.RED);
                break;
            }

            final Path path = file.toPath();

            if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) continue;
            if (!Files.isReadable(path)) continue;
            if (!path.startsWith(MainHandler.INST.getGameRootDir())) continue;

            if(isDuplicated(path)) {
                parentController.warn("登録済みのファイルのドロップを無視しました.", Color.YELLOW);
                continue;
            }

            final ContentEntry entry;

            try {
                entry = ContentEntry.infer(path);
            }catch (IllegalArgumentException ex){
                Logger.INST.debug("This file is probably neither image nor movie.");
                parentController.warn(path.getFileName() + "は動画,画像として読み込めません.", Color.RED);
                continue;
            }

            contentEntryList.add(entry);
            entry.resizeByWidth(flowPane.getPrefWidth() / 3.5);
            flowPane.getChildren().add(entry.getPane());
            if(!entry.isMovie()) parentController.enableNextButton();
        }

        event.setDropCompleted(true);
        event.consume();
    }

    /**
     * 既に同じコンテンツが{@link #contentEntryList}に存在するかどうかチェックする.
     * @return 既に存在する,又は例外で正常な検査ができなかったとき {@code true}, まだ存在しないとき{@code false}
     */
    private boolean isDuplicated(Path path){
        for(final ContentEntry entry : contentEntryList){
            try {
                if(Files.isSameFile(entry.getPath(), path))return true;
            }catch (IOException | SecurityException ex){
                Logger.INST.warn("Failed to check for duplicate.").logException(ex);
                return true;
            }
        }
        return false;
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

        final List<Path> movieList = new ArrayList<>();
        final List<Path> imageList = new ArrayList<>();

        for(final ContentEntry content : contentEntryList){
            (content.isMovie() ? movieList : imageList).add(content.getPath());
            content.destructor();
        }
        contentEntryList = null;

        MainHandler.INST.setMovieList(movieList);
        MainHandler.INST.setImageList(imageList);
    }
}
