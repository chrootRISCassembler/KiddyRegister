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

import capslock.game_info.GameDocument;
import capslock.game_info.JSONDBWriter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import methg.commonlib.file_checker.FileChecker;
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

enum MainHandler {
    INST;

    /**
     * 自身のjarに保存するファイルパスのキャッシュ
     */
    private static final String PATH_CACHE = "PathCache.properties";
    private static final Path JSON_PATH = Paths.get(".signature.json");

    private Mode mode;
    private Path gameRootDir;

    private Stage stage;

    private MainController controller;

    private GameDocument doc = new GameDocument();

    final void setGameRootDir(Path path){
        gameRootDir = path;
    }
    final Path getGameRootDir(){ return gameRootDir; }

    final Path getExe(){ return doc.getExe(); }
    final String getName(){ return doc.getName(); }
    final String getDesc(){ return doc.getDesc(); }
    final Path getPanel(){ return doc.getPanel(); }
    final List<Path> getImageList(){ return doc.getImageList(); }
    final List<Path> getMovieList(){ return doc.getMovieList(); }
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

//    final RegisterState nextState(){
//        state.getController().transition();
//        return state = state.next();
//    }
//    final RegisterState prevState(){
//        return state = state.prev();
//    }



    private Path toPortablePath(Path realPath){
        Logger.INST.debug(() -> "Real path is \"" + realPath + '\"');
        final Path realRelativePath = gameRootDir.relativize(realPath);
        Logger.INST.debug(() -> "Real relative path is \"" + realRelativePath + '\"');
        final Path portableRelativePath = Paths.get("Games/" + gameRootDir.getFileName() + '/' + realRelativePath);
        Logger.INST.debug(() -> "portable path is \"" + portableRelativePath + '\"');
        return portableRelativePath;
    }


    final void writeToJSON(){
        Logger.INST.debug(doc.getExe().toString());
        doc.setLastMod(Instant.now());

        try {
            new JSONDBWriter(JSON_PATH).add(doc).flush();
        }catch (IOException ex){
            Logger.INST.logException(ex);
        }
    }

    final void init(Stage stage){
        this.stage = stage;

        try {
            final InputStream inputStream = getClass().getResourceAsStream(PATH_CACHE);
            final Properties properties = new Properties();
            properties.load(inputStream);
            inputStream.close();

            final String cachedGameRoot = properties.getProperty("cachedGameRoot");
            final Path signatureJSONPath = new FileChecker(cachedGameRoot + '/' + JSON_PATH)
                    .onCanExec(dummy -> true)
                    .check()
                    .get();
            mode = Mode.UPDATE;

        }catch (IOException | NullPointerException ex){
            Logger.INST.debug("Failed to get InputStream from " + PATH_CACHE + ".");
            mode = Mode.REGISTER;
        }
    }

    final Mode getMode(){
        return mode;
    }

    final Stage getStage(){
        return stage;
    }

    /**
     * 現在設定されているモードで起動する.
     */
    final void confirm(){
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));

        try {
            loader.load();
            stage.setScene(new Scene(loader.getRoot()));
        } catch (IOException ex) {
            Logger.INST.critical("Failed to load Main.fxml");
            Logger.INST.logException(ex);
        }

        final MainController controller = (MainController) loader.getController();
        ChildController.mainController = controller;
        controller.start(mode.getStateList());
    }
}
