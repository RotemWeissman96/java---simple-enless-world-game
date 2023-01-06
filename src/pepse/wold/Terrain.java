package pepse.wold;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.ConstantRandomSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Terrain {
    private static final float SCREEN_PERCENTAGE_FOR_GROUND_HEIGHT_AT_0 = 0.7f;
    private static final int MAX_VARIATION_HEIGHT_FROM_0 = 10;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final String GROUND = "GROUND";
    private final HashMap<Integer, ArrayList<Block>> blockColHashMap;
    private final int maxBlockIndexToSee;
    private int groundHeightAtX0;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final ConstantRandomSupplier heightSupplier;
    Vector2 windowDimensions;

    /**
     * constructor for terrain
     * @param groundLayer the layer to display the top ground in
     * @param seed the terrain "randomness" will be determined by this seed
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        groundHeightAtX0 = (int)(windowDimensions.y() * SCREEN_PERCENTAGE_FOR_GROUND_HEIGHT_AT_0);
        groundHeightAtX0 = (groundHeightAtX0/Block.SIZE) * Block.SIZE;
        // make sure that the index is dividable by 30
        this.heightSupplier = new ConstantRandomSupplier(seed);
        this.windowDimensions = windowDimensions;
        this.blockColHashMap = new HashMap<>();
        this.maxBlockIndexToSee = (int)windowDimensions.y()/Block.SIZE + MAX_VARIATION_HEIGHT_FROM_0 + 1;
    }

    /**
     * return the ground height at block column x
     * @param x column index of blocks
     * @return height in pixels
     */
    public float groundHeightAt(float x) {
        return groundHeightAtX0 +
                (float) Math.floor(heightSupplier.noise(x) * MAX_VARIATION_HEIGHT_FROM_0) * Block.SIZE;
    }

    /**
     * generate a "randomly" terrain in a range based of blocks index
     * @param minX start of the range
     * @param maxX end of range (included in the creation)
     */
    public void createInRange(int minX, int maxX) {
        for (int col = minX; col <= maxX; col ++) {
            this.blockColHashMap.put(col, addTerrainCol(col, maxBlockIndexToSee));
        }
    }

    /**
     * create a single column of terrain blocks
     * @param col index
     * @param maxBlockIndexToSee what is the lowest block to generate
     * @return an arrayList of all blocks added
     */
    private ArrayList<Block> addTerrainCol(int col, int maxBlockIndexToSee) {
        int groundHeightAtCol = (int)(groundHeightAt(col))/Block.SIZE;
        ArrayList<Block> blocksInCol = new ArrayList<>();
        blocksInCol.add(addTerrainBlock(groundHeightAtCol, col, groundLayer));
        for (int row = groundHeightAtCol + 1; row <= maxBlockIndexToSee; row ++) {
            blocksInCol.add(addTerrainBlock(row, col, Layer.STATIC_OBJECTS));
        }
        return blocksInCol;
    }

    /**
     * create a single Terrain block
     * @param row index
     * @param col index
     * @param layer the layer to display the first block
     * @return the Block
     */
    private Block addTerrainBlock(int row, int col, int layer) {
        Renderable blockRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        Block groundBlock = new Block(
                new Vector2(col*Block.SIZE, row*Block.SIZE),
                blockRenderable);
        groundBlock.setTag(GROUND);
        this.gameObjects.addGameObject(groundBlock, layer);
        return groundBlock;
    }

    /**
     *  remove a range of columns from gameObjects and from the Terrain class HashMap
     * @param minX start of the range
     * @param maxX end of range (included in the remove)
     */
    public void removeFromRange(int minX, int maxX) {
        ArrayList<Block> blocksInCol;
        for (int col = minX; col <= maxX; col ++){
            blocksInCol = this.blockColHashMap.remove(col);
            this.gameObjects.removeGameObject(blocksInCol.get(0), groundLayer);
            for (int row = 1; row < blocksInCol.size(); row++){
                gameObjects.removeGameObject(blocksInCol.get(row), Layer.STATIC_OBJECTS);
            }
        }
    }

    public double getSeed(){return heightSupplier.getSeed();}
}


