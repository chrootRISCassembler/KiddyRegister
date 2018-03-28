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
    public final void resizeByWidth(double width) {
        mediaView.setFitWidth(width);
    }

    @Override
    public final void resizeByHeight(double height) {
        mediaView.setFitHeight(height);
    }

    @Override
    public final boolean isMovie(){return true;}

    @Override
    public final void destructor() {
        player.stop();
        player.dispose();
    }
}
