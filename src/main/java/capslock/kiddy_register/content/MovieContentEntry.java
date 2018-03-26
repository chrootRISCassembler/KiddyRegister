package capslock.kiddy_register.content;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import methg.commonlib.trivial_logger.Logger;

import java.nio.file.Path;

final class MovieContentEntry extends ContentEntry {
    private final MediaPlayer player;
    private final MediaView mediaView;

    MovieContentEntry(Path path) throws IllegalArgumentException{
        super(path);

        try {
            final Media canFailMovie = new Media(path.toUri().toString());

            player = new MediaPlayer(canFailMovie);

            final MediaException exception = player.getError();
            if(exception != null)throw exception;

            player.setMute(true);
            player.setAutoPlay(true);
            player.setCycleCount(MediaPlayer.INDEFINITE);

            mediaView = new MediaView(player);
            mediaView.setPreserveRatio(true);

        }catch (MediaException ex){
            throw new IllegalArgumentException("Failed to load \"" + path + "\" as a Media.", ex);
        }

        setContentDisplayNode(mediaView);
        player.play();
    }

    @Override
    public final boolean isMovie(){return true;}

    @Override
    public final void destructor() {
        player.stop();
        player.dispose();
    }
}
