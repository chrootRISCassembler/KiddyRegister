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
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

enum MainHandler {
    INST;

    private final Path JSON_PATH = Paths.get("./sign.json");

    private MainController controller;
    private RegisterState state = RegisterState.INIT_GAME_ROOT_DIR;

    private Path gameRootDir;
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

    final RegisterState nextState(){
        state.getController().transition();
        return state = state.next();
    }
    final RegisterState prevState(){
        return state = state.prev();
    }

    private Path toPortablePath(Path realPath){
        Logger.INST.debug(() -> "Real path is \"" + realPath + '\"');
        final Path realRelativePath = gameRootDir.relativize(realPath);
        Logger.INST.debug(() -> "Real relative path is \"" + realRelativePath + '\"');
        final Path portableRelativePath = Paths.get("Games/" + gameRootDir.getFileName() + '/' + realRelativePath);
        Logger.INST.debug(() -> "portable path is \"" + portableRelativePath + '\"');
        return portableRelativePath;
    }

    void setController(MainController controller){
        this.controller = controller;
    }
    final MainController getController(){
        return controller;
    }

    final void writeToJSON(){
        try {
            new JSONDBWriter(JSON_PATH).add(doc).flush();
        }catch (IOException ex){
            Logger.INST.logException(ex);
        }
    }

    void run(){
        controller.replace(state);
    }
}
