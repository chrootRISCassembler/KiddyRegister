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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import methg.commonlib.trivial_logger.LogLevel;
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;

public class Main extends Application {

    /**
     * エントリポイント
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {

        Logger.INST.setCurrentLogLevel(LogLevel.DEBUG);

        Logger.INST.info("fixer started.");

        launch(args);

        Logger.INST.info("fixer terminated.");
        Logger.INST.flush();
    }


    @Override
    public void start(Stage stage) {
        Logger.INST.debug("Application#start called.");

        final FXMLLoader loader;
        try{
            loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        }catch(Exception ex){
            Logger.INST.critical("Failed to get resource.");
            Logger.INST.logException(ex);
            return;
        }

        final Parent root;

        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.INST.critical("Failed to load Main.fxml");
            Logger.INST.logException(ex);
            return;
        }


        try {
            final MainController controller = (MainController) loader.getController();

            final Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ゲーム情報登録ツール");

            MainHandler.INST.setController(controller);

            Logger.INST.debug("Try to display Console window.");
            stage.show();

            MainHandler.INST.setController(controller);
        }catch (Exception ex){
            Logger.INST.logException(ex);
        }

        MainHandler.INST.run();
    }
}
