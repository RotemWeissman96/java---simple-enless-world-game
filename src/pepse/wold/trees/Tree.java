package pepse.wold.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.ConstantRandomSupplier;
import pepse.wold.Block;

import java.awt.*;
import java.util.Objects;
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
    private static final String TREE_HERE = "TREE"; // unfair
    private static final String LEAF_HERE = "LEAF";
    private final GameObjectCollection gameObjectCollection;
    private final int treeLayer;
    private final Function<Float, Float> groundHeightFunction;
    private final ConstantRandomSupplier randomSupplier;
    private final int seed;

    /**
     *
     * @param gameObjectCollection
     * @param layer
     * @param heightFunction
     */
    public Tree(GameObjectCollection gameObjectCollection,
                int layer,
                Function<Float, Float> heightFunction){
        this.gameObjectCollection = gameObjectCollection;
        treeLayer = layer;
        this.groundHeightFunction = heightFunction;
        Random random = new Random();
        this.seed = random.nextInt(1000);
        this.randomSupplier = new ConstantRandomSupplier(seed);
    }


    /**
     *
     * @return
     */
    private double randomTreeLeafPosition(int treesIndex){
        return randomSupplier.noise(treesIndex);
    }

    /**
     *
     * @return
     */
    private boolean leafRandom(){
        Random rand =new Random();
        int adds = rand.nextInt(10);
        return (adds < 7);
    }

    /**
     * h
     * @param minX
     * @param maxX
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
     *
     * @param treesIndex
     */
    private void createTree(int treesIndex){
        Vector2 height = Vector2.RIGHT;
        Block block = new Block(Vector2.RIGHT,null);
        double treesHeight =
                Math.abs(randomTreeLeafPosition(treesIndex))*
                        (MAX_TREE_HEIGHT - MIN_TREE_HEIGHT) + MIN_TREE_HEIGHT;
        for (int i = 1; i < treesHeight + 1; i++) {
             block = new Block(new Vector2(treesIndex * Block.SIZE,
                    groundHeightFunction.apply((float)treesIndex) - (i * Block.SIZE)),
                    new RectangleRenderable(ColorSupplier.approximateColor(TREE_COLOR)));
            System.out.println(groundHeightFunction.apply((float)treesIndex) - (i * Block.SIZE));
            block.setTag(TREE_HERE);
            gameObjectCollection.addGameObject(block, treeLayer);
        }
        createLeaf(treesIndex ,block);
    }

    /**
     *
     * @param treesIndex
     */
    private void createLeaf(int treesIndex, Block block){
        Vector2 leafHeight = block.getTopLeftCorner().add(new
                Vector2(-Block.SIZE*LEAF_SIZE,-Block.SIZE*LEAF_SIZE));
        for (int i = 0; i < LEAF_SIZE*2; i++) {
            for (int j = 0; j < LEAF_SIZE*2; j++) {
                if(randomTreeLeafPosition(treesIndex*(i+1)*(j+1)) >= FAIR_ODDS) {
                    Leaf leaf = new Leaf(leafHeight,
                            new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)),
                            gameObjectCollection,
                            treeLayer + 1);
                    leaf.setTag(LEAF_HERE);
                    gameObjectCollection.addGameObject(leaf, treeLayer + 1);
                    block.addComponent(deltaTime -> leaf.update(deltaTime));
                }
                leafHeight = leafHeight.add(Vector2.RIGHT.mult(Block.SIZE));
            }
            leafHeight = leafHeight.add(new Vector2(0, Block.SIZE));
            leafHeight = leafHeight.add(Vector2.LEFT.mult(2*Block.SIZE*LEAF_SIZE));
        }
    }
}
