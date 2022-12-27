package pepse.wold.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.wold.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private static final Color TREE_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private final Random rand = new Random();
    private final static int MAX_TREE_HEIGHT = 20;
    private final static int MIN_TREE_HEIGHT = 8;
    private static final String TREE_HERE = "tree here";
    private final GameObjectCollection gameObjectCollection;
    private final int seed;
    private final int leafLayer;
    private final  Function<Float, Float> heightFunction;

    public Tree(GameObjectCollection gameObjectCollection,
                int seed,
                int layer,
                Function<Float, Float> heightFunction){
        this.gameObjectCollection = gameObjectCollection;
        this.seed =seed;
        leafLayer = layer;
        this.heightFunction = heightFunction;
    }

    private boolean randomCoin(){
        int adds = rand.nextInt(10);
        return (adds == 0);
    }

    private int treesHeight(){
        return rand.nextInt(MAX_TREE_HEIGHT - MIN_TREE_HEIGHT)+ MIN_TREE_HEIGHT;
    }


    /**
     * h
     * @param minX
     * @param maxX
     */
    public void createInRange(int minX, int maxX){
        int treesIndex =minX;
        while (treesIndex < maxX){
            if(randomCoin()){
                createTree(treesIndex);
            }
            treesIndex+= Block.SIZE;
        }

    }

    /**
     *
     * @param treesIndex
     */
    private void createTree(float treesIndex){
        Block block;
        for (int i = 0; i < treesHeight() + 1 ; i++) {
            block = new Block(new Vector2(treesIndex,  heightFunction.apply(treesIndex) -i* Block.SIZE),
                    new RectangleRenderable(ColorSupplier.approximateColor(TREE_COLOR)));
            block.setTag(TREE_HERE);
            gameObjectCollection.addGameObject(block, leafLayer);
        }
        createLeaf(treesIndex);
    }

    private void createLeaf(float treesIndex){

    }
}
