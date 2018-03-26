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

package capslock.kiddy_register.content;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import methg.commonlib.trivial_logger.Logger;

import java.nio.file.Path;
import java.util.function.Consumer;

public abstract class ContentEntry{

    static private final String UNREGISTER_ICON_NAME = "UnregisterIcon.png";
    static private final Image unregisterIcon;
    static private final Tooltip tooltip = new Tooltip("クリックすると登録を解除できます.");

    static private Consumer<ContentEntry> unregisterEventHandler;

    private final StackPane stackPane;
    private final ImageView unregisterButton;
    private final Path path;

    static {
        unregisterIcon = new Image(ContentEntry.class.getClassLoader().getResourceAsStream(UNREGISTER_ICON_NAME),
                32, 32, true, true);
    }

    protected ContentEntry(Path path){
        this.path = path;
        unregisterButton = new ImageView(unregisterIcon);
        Tooltip.install(unregisterButton, tooltip);
        unregisterButton.setEffect(new DropShadow());
        unregisterButton.setOnMouseClicked(dummy -> unregisterEventHandler.accept(this));

        //イベント発生毎にオブジェクトを生成している　重たければEffectを再利用するコードに書き換える
        unregisterButton.setOnMousePressed(dummy -> unregisterButton.setEffect(new ColorAdjust(0, 0, -0.4, 0)));
        unregisterButton.setOnMouseReleased(dummy -> unregisterButton.setEffect(new DropShadow()));

        stackPane = new StackPane(unregisterButton);
        stackPane.setAlignment(Pos.TOP_RIGHT);
    }

    protected final void setContentDisplayNode(Node contentDisplayNode){
        stackPane.getChildren().add(0, contentDisplayNode);
    }

    /**
     * 引数のパスが動画か画像か分からないときのstatic factory method
     * <p>この処理はユーザーが感知できるほど遅い.パスが示すコンテンツが動画または画像だと分かっている場合はより高速な
     * {@link #asMovie(Path)}, {@link #asImage(Path)}を使うべき.</p>
     * @param path 動画か画像か分からないコンテンツのファイルパス.
     * @return コンテンツを表示する {@link ContentEntry}のインスタンス.
     */
    public static ContentEntry infer(Path path) throws IllegalArgumentException{
        ContentEntry entry;
        try {
            entry = new MovieContentEntry(path);
            return entry;
        }catch (IllegalArgumentException ex){
            Logger.INST.debug("It's not movie.");
        }

        try {
            entry = new ImageContentEntry(path);
        }catch (IllegalArgumentException ex){
            throw ex;
        }
        return entry;
    }

    /**
     * 引数のパスが動画だとわかるときのstatic factory method
     * @param moviePath 動画のファイルパス.
     * @return 動画を表示する {@link ContentEntry}のインスタンス.
     */
    public static ContentEntry asMovie(Path moviePath) throws IllegalArgumentException {
        final MovieContentEntry movieEntry = new MovieContentEntry(moviePath);
        return movieEntry;
    }

    /**
     * 引数のパスが画像だとわかるときのstatic factory method
     * @param imagePath 画像のファイルパス.
     * @return 画像を表示する {@link ContentEntry}のインスタンス.
     * @throws IllegalArgumentException
     */
    public static ContentEntry asImage(Path imagePath) throws IllegalArgumentException{
        final ImageContentEntry imageEntry = new ImageContentEntry(imagePath);
        return imageEntry;
    }

    public static void setOnUnregisterButtonPushed(Consumer<ContentEntry> lambda){
        unregisterEventHandler = lambda;
    }

    public final Pane getPane(){return stackPane;}

    public abstract void resizeByWidth(double width);
    public abstract void resizeByHeight(double height);

    /**
     * コンテンツが動画かどうかを返す
     * @return {@code true}動画のとき, {@code false}画像のとき
     */
    public abstract boolean isMovie();

    /**
     * 表示されているコンテンツのファイルパスを返す.
     * @return ファイルパス
     */
    public final Path getPath(){return path;}

    public void destructor(){}
}
