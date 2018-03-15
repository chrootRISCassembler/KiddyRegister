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

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;

enum  State {
    SET_GAME_ROOT_DIR("SetGameRootDir.fxml"),
    REGISTER_EXE("Exe.fxml"),
    REGISTER_NAME("Name.fxml"),
    REGISTER_DESC("Desc.fxml"),
    REGISTER_PANEL("Panel.fxml"),
    REGISTER_CONTENT("Content.fxml"),
    REGISTER_GAME_ID("ID.fxml"),
    WRITE_JSON("Write.fxml"),
    PREVIEW_LAUNCHER("Preview.fxml");

    private ChildController controller;
    private Node rootNode;

    State(String FXMLLocation){
        if(FXMLLocation.isEmpty())return;

        final FXMLLoader loader;

        loader = new FXMLLoader(getClass().getResource(FXMLLocation));

        final Parent root;

        try {
            rootNode = loader.load();
        } catch (IOException ex) {
            Logger.INST.critical("Failed to load " + FXMLLocation);
            Logger.INST.logException(ex);
            return;
        }

        controller = (ChildController) loader.getController();
    }

    final ChildController getController(){
        return controller;
    }

    final Node getRootNode(){
        return rootNode;
    }
}
