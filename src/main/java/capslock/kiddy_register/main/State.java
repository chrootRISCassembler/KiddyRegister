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
    REGISTER_IMAGE("Image.fxml"),
    REGISTER_MOVIE("Movie.fxml"),
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
