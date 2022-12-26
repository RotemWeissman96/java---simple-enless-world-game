package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.wold.Sky;
import pepse.wold.Terrain;
import pepse.wold.daynight.Night;
import pepse.wold.daynight.Sun;
import danogl.util.Vector2;
import pepse.wold.daynight.SunHalo;

import java.awt.*;

public class PepseGameManager extends GameManager {
    private static final float NIGHT_CYCLE = 10;
    private static final float SUN_CYCLE = 500;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sky.create(this.gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);
        Terrain terrain = new Terrain(gameObjects(), Layer.BACKGROUND,
                windowController.getWindowDimensions(), 50);
        terrain.createInRange(-5 ,(int)windowController.getWindowDimensions().x()/30 + 5);
        // this is where we add the background for night and day
        Night.create(this.gameObjects(),Layer.BACKGROUND,windowController.getWindowDimensions(),NIGHT_CYCLE);


        // this is where we add the background for night.
        Night.create(this.gameObjects(),
                Layer.BACKGROUND,
                windowController.getWindowDimensions(),
                NIGHT_CYCLE);
        // this is where we add the sun to the window.
        GameObject sun = Sun.create(this.gameObjects(),
                Layer.BACKGROUND + 1,
                windowController.getWindowDimensions(),
                SUN_CYCLE);
        SunHalo.create(this.gameObjects(),
                Layer.BACKGROUND + 2,
                sun,
                HALO_COLOR
                );
    }


}
