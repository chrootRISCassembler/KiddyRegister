package capslock.kiddy_register.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import methg.commonlib.trivial_logger.Logger;

public enum RegisterState {
    INIT_GAME_ROOT_DIR("InitGameRootDir.fxml"),
    REGISTER_EXE("Exe.fxml"),
    REGISTER_NAME("Name.fxml"),
    REGISTER_DESC("Desc.fxml"),
    REGISTER_PANEL("Panel.fxml"),
    REGISTER_IMAGE("Image.fxml"),
    REGISTER_MOVIE("Movie.fxml"),
    REGISTER_GAME_ID("ID.fxml"),
    WRITE_JSON("Write.fxml");

    private IController controller;
    private Node rootNode;


    private RegisterState(String FXMLLocation){
        if(FXMLLocation.isEmpty())return;

        final FXMLLoader loader;

        loader = new FXMLLoader(getClass().getResource(FXMLLocation));

        final Parent root;

        try {
            rootNode = loader.load();
        } catch (IOException ex) {
            Logger.INST.critical("Failed to load InitGameRootDir.fxml");
            Logger.INST.logException(ex);
            return;
        }

        controller = (IController) loader.getController();
    }

    public final RegisterState next(){
        System.err.println(this.ordinal());
        return values()[this.ordinal() + 1];
    }

    public final RegisterState prev(){
        System.err.println(this.ordinal());
        return values()[this.ordinal() - 1];
    }

    public final IController getController(){
        return controller;
    }

    public final Node getRootNode(){
        return rootNode;
    }
}
