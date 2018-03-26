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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;

final class ImageContentEntry extends ContentEntry {
    private final ImageView imageView;

    ImageContentEntry(Path path) throws IllegalArgumentException {
        super(path);

        final Image image = new Image(path.toUri().toString());
        if(image.isError())throw new IllegalArgumentException("Failed to load \"" + path + "\" as a Image.");

        imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        setContentDisplayNode(imageView);
    }

    @Override
    public void resizeByWidth(double width) {
        imageView.setFitWidth(width);
    }

    @Override
    public void resizeByHeight(double height) {
        imageView.setFitHeight(height);
    }

    @Override
    public final boolean isMovie(){return false;}
}
