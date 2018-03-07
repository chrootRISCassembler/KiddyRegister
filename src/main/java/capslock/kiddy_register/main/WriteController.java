package capslock.kiddy_register.main;

import capslock.game_info.GameInfoBuilder;
import methg.commonlib.trivial_logger.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteController implements IController {


    @Override
    public final void init() {
        Logger.INST.debug("Write init called");

        final GameInfoBuilder builder = new GameInfoBuilder();
        final MainHandler handler = MainHandler.INST;

        final capslock.game_info.GameRecord record = builder.setExe(handler.getExe())
                .setName(handler.getName())
                .setDesc(handler.getDesc())
                .setPanel(handler.getPanel())
                .setImageList(handler.getImageList())
                .setMovieList(handler.getMovieList())
                .setGameID(handler.getId())
                .buildGameRecord();

        try {
            final Path target = Paths.get("./sign.json");
            Files.createFile(target);
            new capslock.game_info.JSONDBWriter(target).add(record).flush();
        }catch (IOException ex){
            Logger.INST.logException(ex);
        }
    }
}
