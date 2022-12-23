package pepse;

import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.wold.Sky;
import pepse.wold.daynight.Night;

public class PepseGameManager extends GameManager {
    private static final float NIGHT_CYCLE = 10;
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sky.create(this.gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);
        // this is where we add the background for night and day
        Night.create(this.gameObjects(),Layer.BACKGROUND,windowController.getWindowDimensions(),NIGHT_CYCLE);
    }


}
