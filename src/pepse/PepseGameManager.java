package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.wold.Block;
import pepse.wold.Sky;
import pepse.wold.Terrain;
import pepse.wold.daynight.Night;
import pepse.wold.daynight.Sun;
import danogl.util.Vector2;
import pepse.wold.daynight.SunHalo;
import pepse.wold.trees.Tree;

import java.awt.*;
import java.util.Random;

public class PepseGameManager extends GameManager {
    private static final float NIGHT_CYCLE = 10;
    private static final float SUN_CYCLE = 500;
    private static final int OUT_OF_WINDOW_BLOCKS = 5;
    private static final int SKY_AND_NIGHT_BACKGROUND= Layer.BACKGROUND;
    private static final int SUN_BACKGROUND = SKY_AND_NIGHT_BACKGROUND + 1;
    private static final int HALO_SUN_BACKGROUND = SUN_BACKGROUND + 1;
    private static final int TERRAIN_BACKGROUND = HALO_SUN_BACKGROUND + 1;
    private static final int TREE_BACKGROUND = HALO_SUN_BACKGROUND + 5;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sky.create(this.gameObjects(), windowController.getWindowDimensions(), SKY_AND_NIGHT_BACKGROUND);
        Random rand = new Random();
        int seed = rand.nextInt(1000);

        // this is where we add the terrain
        Terrain terrain = new Terrain(gameObjects(),
                TERRAIN_BACKGROUND,
                windowController.getWindowDimensions(),
                seed);
        terrain.createInRange(-OUT_OF_WINDOW_BLOCKS,
                        (int)windowController.getWindowDimensions().x()/Block.SIZE + OUT_OF_WINDOW_BLOCKS);

        // this is where we add the background for night and day
        Night.create(this.gameObjects(),
                SKY_AND_NIGHT_BACKGROUND,
                windowController.getWindowDimensions(),
                NIGHT_CYCLE);

        // this is where we add the sun to the window.
        GameObject sun = Sun.create(this.gameObjects(),
                SUN_BACKGROUND,
                windowController.getWindowDimensions(),
                SUN_CYCLE);

        // this is where we add the sunHalo to the window.
        SunHalo.create(this.gameObjects(),
                HALO_SUN_BACKGROUND,
                sun,
                HALO_COLOR
                );
        // this is where we add the tree and the leaf.
        Tree tree = new Tree(this.gameObjects(), TREE_BACKGROUND, terrain::groundHeightAt,seed);
        int rightBoarder = 0;
        while (rightBoarder < (windowController.getWindowDimensions().x()/2)+ (5* Block.SIZE)){
            rightBoarder += Block.SIZE;
        }
        tree.createInRange(-rightBoarder,rightBoarder);
        gameObjects().layers().shouldLayersCollide(TREE_BACKGROUND, TREE_BACKGROUND +1, true);
    }


}
