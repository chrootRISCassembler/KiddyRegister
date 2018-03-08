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

import capslock.game_info.GameInfoBuilder;
import methg.commonlib.trivial_logger.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

enum MainHandler {
    INST;

    private MainController controller;
    private RegisterState state = RegisterState.INIT_GAME_ROOT_DIR;
    Path connectedJSON = null;

    private final GameInfoBuilder builder = new GameInfoBuilder();

    private Path gameRootDir;
    private Path exe;
    private String name;
    private String desc;
    private Path panel;
    private List<Path> imageList;
    private List<Path> movieList;
    private int id;

    final void setGameRootDir(Path path){ gameRootDir = path;}
    final Path getGameRootDir(){ return gameRootDir; }

    final void setExe(Path path){
        exe = gameRootDir.relativize(path);
        Logger.INST.debug(exe.toString());
    }

    final Path getExe(){return exe;}

    final void setName(String name){this.name = name;}
    final String getName(){return name;}

    final void setDesc(String desc){this.desc = desc;}
    final String getDesc(){return desc;}

    final void setPanel(Path path){
        panel = gameRootDir.relativize(path);
        Logger.INST.debug(panel.toString());
    }
    final Path getPanel(){return panel;}

    final void setImageList(List<Path> list){
        imageList = list.stream()
                .map(path -> gameRootDir.relativize(path))
                .collect(Collectors.toList());
    }
    final List<Path> getImageList(){return imageList;}

    final void setMovieList(List<Path> list) {
        movieList = list.stream()
                .map(path -> gameRootDir.relativize(path))
                .collect(Collectors.toList());
    }
    final List<Path> getMovieList(){return  movieList;}

    final void setID(int id){this.id = id;}
    final int getId(){return id;}

    final RegisterState nextState(){
        state.getController().transition();
        return state = state.next();
    }
    final RegisterState prevState(){
        return state.prev();
    }

    void setController(MainController controller){
        this.controller = controller;
    }
    final MainController getController(){
        return controller;
    }

    void run(){
        controller.replace(state);
    }
}
