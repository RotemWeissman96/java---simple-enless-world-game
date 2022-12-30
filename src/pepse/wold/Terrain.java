package pepse.wold;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.GroundHeightSupplier;

import java.awt.*;

public class Terrain {
    private static final float SCREEN_PERCENTAGE_FOR_GROUND_HEIGHT_AT_0 = 0.7f;
    private static final int MAX_VARIATION_HEIGHT_FROM_0 = 10;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private int groundHeightAtX0;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final GroundHeightSupplier heightSupplier;
    Vector2 windowDimensions;

    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        groundHeightAtX0 = (int)(windowDimensions.y() * SCREEN_PERCENTAGE_FOR_GROUND_HEIGHT_AT_0);
        groundHeightAtX0 = (groundHeightAtX0/Block.SIZE) * Block.SIZE;
        // make sure that the index is dividable by 30
        this.heightSupplier = new GroundHeightSupplier(seed);
        this.windowDimensions = windowDimensions;
    }

    public float groundHeightAt(float x) {
        System.out.println(heightSupplier.noise(x) * MAX_VARIATION_HEIGHT_FROM_0);
        float check = groundHeightAtX0 +
                (float) heightSupplier.noise(x) * MAX_VARIATION_HEIGHT_FROM_0 * Block.SIZE;
        return groundHeightAtX0 +
                (float) heightSupplier.noise(x) * MAX_VARIATION_HEIGHT_FROM_0 * Block.SIZE;
    }

    public void createInRange(int minX, int maxX) {
        int maxBlockIndexToSee = (int)windowDimensions.y()/Block.SIZE + MAX_VARIATION_HEIGHT_FROM_0;
        for (int col = minX; col <= maxX; col ++) {
            int groundHeightAtCol = (int)(groundHeightAt(col))/Block.SIZE;
//            System.out.println("groundHeight at " + col + " = " + groundHeightAtCol);
            addTerrainBlock(groundHeightAtCol, col, 1);
            for (int row = groundHeightAtCol + 1; row <= maxBlockIndexToSee; row ++) {
                addTerrainBlock(row, col, Layer.STATIC_OBJECTS);
            }
        }
    }

    private void addTerrainBlock(int row, int col, int layer) {
        Renderable blockRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        Block groundBlock = new Block(
                new Vector2(col*Block.SIZE, row*Block.SIZE),
                blockRenderable);
        groundBlock.setTag("ground");
        gameObjects.addGameObject(groundBlock, layer);
    }

    public double getSeed(){return heightSupplier.getSeed();}
}
