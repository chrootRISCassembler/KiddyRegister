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

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import methg.commonlib.trivial_logger.Logger;

public class WriteController extends ChildController{

    @FXML private TableView<Field> tableView;
    @FXML private TableColumn keyCol;
    @FXML private TableColumn valueCol;
    @FXML private Button writeButton;

    private ObservableList<Field> data;

    public static class Field{
        private final SimpleStringProperty key;
        private final SimpleStringProperty value;

        private Field(String key, String value){
            this.key = new SimpleStringProperty(key);
            this.value = new SimpleStringProperty(value);
        }

        public String getKey() {
            return key.get();
        }

        public String getValue() {
            return value.get();
        }
    }


    @Override
    public final void init() {
        Logger.INST.debug("WriteController#init called");

        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        data = FXCollections.observableArrayList(
                new Field("ゲームの実行ファイル", MainHandler.INST.getExe().toString()),
                new Field("ゲーム名", MainHandler.INST.getName()),
                new Field("ゲームの説明", MainHandler.INST.getDesc()),
                new Field("パネル画像", MainHandler.INST.getPanel().toString()),
                new Field("紹介画像", MainHandler.INST.getImageList().toString()),
                new Field("紹介動画", MainHandler.INST.getMovieList().toString()),
                new Field("ゲームID", Integer.toString(MainHandler.INST.getID()))
        );
        tableView.setItems(data);

        if(MainHandler.INST.getMode() == Mode.UPDATE){
            parentController.enableNextButton();
        }
    }

    @FXML private void onWritePushed(ActionEvent event){
        writeButton.setDisable(true);

        MainHandler.INST.writeToJSON();
        MainHandler.INST.cacheGameRoot();

        parentController.enableNextButton();
    }
}
