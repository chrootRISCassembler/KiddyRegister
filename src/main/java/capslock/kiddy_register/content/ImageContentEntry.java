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
