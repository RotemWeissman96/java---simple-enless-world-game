package pepse.wold.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.ConstantRandomSupplier;
import pepse.wold.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private static final Color TREE_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);

    private final static int MAX_TREE_HEIGHT = 15;
    private final static int MIN_TREE_HEIGHT = 8;
    private final static int LEAF_SIZE = 3;
    private final static double UNFAIR_ODDS = 0.4;
    private final static double FAIR_ODDS = -0.1;
    private final static int SEED_RANDOM = 1000;
    private static final String TREE_HERE = "TREE"; // unfair
    private static final String LEAF_HERE = "LEAF";
    private final GameObjectCollection gameObjectCollection;
    private final int treeLayer;
    private final Function<Float, Float> groundHeightFunction;
    private final ConstantRandomSupplier randomSupplier;
    private final int seed;

    /**
     *this is the constructor for the tree
     */
    public Tree(GameObjectCollection gameObjectCollection,
                int treeLayer,
                Function<Float, Float> heightFunction){
        this.gameObjectCollection = gameObjectCollection;
        this.treeLayer = treeLayer;
        // this is the function from the Terrain in order to know what is the ground at a certain index
        this.groundHeightFunction = heightFunction;
        Random random = new Random();
        this.seed = random.nextInt(SEED_RANDOM);
        this.randomSupplier = new ConstantRandomSupplier(seed);
    }


    /**
     * this is the random function for the tree and leaf position based on seed
     * @return a number based on the seed
     */
    private double randomTreeLeafPosition(int treesIndex){
        return randomSupplier.noise(treesIndex);
    }




    /**
     * the main function where we get the full screen and decide randomly where the tree will be
     * @param minX the min value
     * @param maxX the max value
     */
    public void createInRange(int minX, int maxX){
        int treesIndex = minX;
        while (treesIndex < maxX){
            if(randomTreeLeafPosition(treesIndex * seed) >= UNFAIR_ODDS){
                createTree(treesIndex);
                // in order not to have 2 trees one next to each other
                treesIndex += 2;
            }
            treesIndex += 1;
        }
    }


    /**
     * this is where we plant the tree
     * @param treesIndex the tree is being planted
     */
    private void createTree(int treesIndex){
        Block treeBlock = new Block(Vector2.RIGHT,null);
        // random go decide what hight the tree will be
        double treesHeight =
                Math.abs(randomTreeLeafPosition(treesIndex))*
                        (MAX_TREE_HEIGHT - MIN_TREE_HEIGHT) + MIN_TREE_HEIGHT;
        for (int i = 1; i < treesHeight + 1; i++) {
             treeBlock = new Block(new Vector2(treesIndex * Block.SIZE,
                    groundHeightFunction.apply((float)treesIndex) - (i * Block.SIZE)),
                    new RectangleRenderable(ColorSupplier.approximateColor(TREE_COLOR)));
            treeBlock.setTag(TREE_HERE);
            gameObjectCollection.addGameObject(treeBlock, treeLayer);
        }
        createLeaf(treesIndex , treeBlock);
    }



    /**
     * this is where we decide where the leaf is going to be
     * @param treesIndex where the tree wwas decided to be
     * @param treeBlock the tree itself
     */
    private void createLeaf(int treesIndex, Block treeBlock){
        Vector2 leafHeight = treeBlock.getTopLeftCorner().add(new
                Vector2(-Block.SIZE*LEAF_SIZE,-Block.SIZE*LEAF_SIZE));
        for (int i = 0; i < LEAF_SIZE*2; i++) {
            for (int j = 0; j < LEAF_SIZE*2; j++) {
                if(randomTreeLeafPosition(treesIndex*(i+1)*(j+1)) >= FAIR_ODDS) {
                    Leaf leaf = new Leaf(leafHeight,
                            new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR))
                           );
                    leaf.setTag(LEAF_HERE);
                    gameObjectCollection.addGameObject(leaf, treeLayer + 1);
                }
                leafHeight = leafHeight.add(Vector2.RIGHT.mult(Block.SIZE));
            }
            leafHeight = leafHeight.add(new Vector2(0, Block.SIZE));
            leafHeight = leafHeight.add(Vector2.LEFT.mult(2*Block.SIZE*LEAF_SIZE));
        }
    }
}
