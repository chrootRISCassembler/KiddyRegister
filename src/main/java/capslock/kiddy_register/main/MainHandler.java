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

import capslock.capslock.main.CapsLock;
import capslock.game_info.GameDocument;
import capslock.game_info.JSONDBReader;
import capslock.game_info.JSONDBWriter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import methg.commonlib.trivial_logger.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

enum MainHandler {
    INST;

    /**
     * ゲームのルートディレクトリをキャッシュしておくプロパティファイルのパス
     */
    private static final Path PATH_CACHE = Paths.get("./.PathCache.properties");
    private static final Path JSON_PATH = Paths.get(".signature.json");

    private Mode mode;
    private Path gameRootDir;

    private Stage stage;

    private MainController controller;

    private GameDocument doc;

    final void setGameRootDir(Path path){
        gameRootDir = path;
    }
    final Path getGameRootDir(){ return gameRootDir; }

    final Path getExe(){ return toRealPath(doc.getExe()); }
    final String getName(){ return doc.getName(); }
    final String getDesc(){ return doc.getDesc(); }
    final Path getPanel(){ return toRealPath(doc.getPanel()); }
    final List<Path> getImageList(){
        return doc.getImageList().stream()
                .map(this::toRealPath)
                .collect(Collectors.toList());
    }
    final List<Path> getMovieList(){
        return doc.getMovieList().stream()
            .map(this::toRealPath)
            .collect(Collectors.toList());
    }
    final int getID(){ return doc.getGameID(); }

    final void setExe(Path path){
        doc.setExe(toPortablePath(path));
        Logger.INST.info(() -> "Exe will save as " + doc.getExe().toString());
    }

    final void setName(String name){ doc.setName(name); }
    final void setDesc(String desc){ doc.setDesc(desc); }

    final void setPanel(Path path){
        doc.setPanel(toPortablePath(path));
        Logger.INST.info(() -> "Panel will save as " + doc.getPanel().toString());
    }
    final void setImageList(List<Path> list){
        doc.setImageList(list.stream()
                .map(this::toPortablePath)
                .collect(Collectors.toList()));
    }
    final void setMovieList(List<Path> list) {
        doc.setMovieList(list.stream()
                .map(this::toPortablePath)
                .collect(Collectors.toList()));
    }

    final void setID(int id){ doc.setGameID(id); }

    /**
     * 実際のパスから本番環境で稼働可能な相対パスを作る.
     * @param realPath 実在するパス
     * @return 仮想的な相対パス
     */
    private Path toPortablePath(Path realPath){
        Logger.INST.debug(() -> "Real path is \"" + realPath + '\"');
        final Path realRelativePath = gameRootDir.relativize(realPath);
        Logger.INST.debug(() -> "Real relative path is \"" + realRelativePath + '\"');
        final Path portableRelativePath = Paths.get("Games/" + gameRootDir.getFileName() + '/' + realRelativePath);
        Logger.INST.debug(() -> "portable path is \"" + portableRelativePath + '\"');
        return portableRelativePath;
    }

    /**
     * JSONファイルに書き込まれている仮想的な相対パスから本当のファイルパスを作る.
     * @param phantomPath JSONファイルに書き込まれている仮想的な相対パス
     * @return 実在するファイルのパス, phantomPathに{@code null}を渡すとnullを返す.
     */
    private Path toRealPath(Path phantomPath){
        if (phantomPath == null)return null;

        Logger.INST.debug(() -> "portable path is \"" + phantomPath + '\"');
        final Path realPath = Paths.get(gameRootDir + "/" + phantomPath.subpath(2, phantomPath.getNameCount()));
        Logger.INST.debug(() -> "Real path is \"" + realPath + '\"');
        return realPath;
    }


    final void writeToJSON(){
        Logger.INST.debug(doc.getExe().toString());

        final Path writePath = Paths.get(gameRootDir + "/" + JSON_PATH);

        doc.setLastMod(Instant.now());
        try {
            new JSONDBWriter().add(doc).flush(writePath);
        }catch (IOException ex){
            Logger.INST.logException(ex);
        }
    }

    final void launch(){
        final Stage launcherStage = new Stage();
        final CapsLock launcher = new CapsLock();
        launcherStage.initModality(Modality.APPLICATION_MODAL);
        launcher.InjectionPoint(launcherStage, Paths.get(gameRootDir + "/" + JSON_PATH), gameRootDir.toString());
    }

    final void init(Stage stage){
        Logger.INST.debug("MainHandler#init");
        this.stage = stage;

        try {
            final BufferedReader reader = Files.newBufferedReader(PATH_CACHE);
            final Properties properties = new Properties();
            properties.load(reader);
            reader.close();

            final String cachedGameRoot = properties.getProperty("cachedGameRoot");
            gameRootDir = Paths.get(cachedGameRoot);
            final Path signatureJSONPath = Paths.get(cachedGameRoot + '/' + JSON_PATH);
            if(Files.isReadable(signatureJSONPath)) {
                mode = Mode.UPDATE;
                Logger.INST.info("Run as UPDATE mode.");
                doc = new JSONDBReader(signatureJSONPath).getDocumentList().get(0);

            }else{
                Logger.INST.info(signatureJSONPath + " isn't readable.");
                throw new NullPointerException();
            }

        }catch (IOException | NullPointerException ex){
            Logger.INST.debug("Failed to get InputStream from " + PATH_CACHE + ".");
            mode = Mode.REGISTER;
            doc = new GameDocument();
        }
    }

    final void cacheGameRoot(){
        final Properties properties = new Properties();
        properties.setProperty("cachedGameRoot", gameRootDir.toString());

        try(final BufferedWriter writer = Files.newBufferedWriter(PATH_CACHE,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)){
            properties.store(writer, null);
        }catch (IOException ex){
            Logger.INST.critical("Failed to write path of gameRootDir to " + PATH_CACHE)
                    .logException(ex);
        }
    }

    final Mode getMode(){
        return mode;
    }

    final Stage getStage(){
        return stage;
    }

    /**
     * モードを変更して起動する.
     */
    final void runAsMode(Mode mode){
        this.mode = mode;
        if(mode == Mode.REGISTER){
            doc = new GameDocument();
            gameRootDir = null;
        }
        runAsMode();
    }

    /**
     * 現在設定されているモードで起動する.
     */
    final void runAsMode(){
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));

        try {
            loader.load();
            stage.setScene(new Scene(loader.getRoot()));
        } catch (IOException ex) {
            Logger.INST.critical("Failed to load Main.fxml");
            Logger.INST.logException(ex);
        }

        final MainController controller = (MainController) loader.getController();
        ChildController.parentController = controller;
        controller.start(mode.getStateList());
    }
}
