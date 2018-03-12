package capslock.kiddy_register.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import methg.commonlib.trivial_logger.Logger;

public class WriteController extends ChildController{

    @FXML private TableView<Field> tableView;
    @FXML private TableColumn keyCol;
    @FXML private TableColumn valueCol;
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
    }

    @FXML private void onWritePushed(ActionEvent event){
        MainHandler.INST.writeToJSON();
        MainHandler.INST.cacheGameRoot();
    }
}
