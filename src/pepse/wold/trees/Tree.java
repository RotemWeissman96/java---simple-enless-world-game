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
//    private final Random rand = new Random();
    private final static int MAX_TREE_HEIGHT = 15;
    private final static int MIN_TREE_HEIGHT = 8;
    private static final String TREE_HERE = "TREE";
    private static final String LEAF_HERE = "LEAF";
    private final GameObjectCollection gameObjectCollection;
    private final int treeLayer;
    private final Function<Float, Float> groundHeightFunction;
    private final ConstantRandomSupplier randomSupplier;
    private final int seed;

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
    private double randomTreePosition(int treesIndex){
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
        System.out.println("min = " + minX);
        System.out.println("max = " + maxX);
        int treesIndex = minX;
        while (treesIndex < maxX){
            if(randomTreePosition(treesIndex * seed) >= 0.5){
                createTree(treesIndex);
            }
            treesIndex += 1;
        }
    }

    /**
     *
     * @param treesIndex
     */
    private void createTree(int treesIndex){
//        Vector2 height = Vector2.RIGHT;
//        Block block = new Block(Vector2.RIGHT,null);
        double treesHeight =
                Math.abs(randomTreePosition(treesIndex))*(MAX_TREE_HEIGHT - MIN_TREE_HEIGHT) + MIN_TREE_HEIGHT;
        for (int i = 1; i < treesHeight + 1; i++) {
            Block block = new Block(new Vector2(treesIndex * Block.SIZE,
                    groundHeightFunction.apply((float)treesIndex) - (i * Block.SIZE)),
                    new RectangleRenderable(ColorSupplier.approximateColor(TREE_COLOR)));
            System.out.println(groundHeightFunction.apply((float)treesIndex) - (i * Block.SIZE));
            block.setTag(TREE_HERE);
            gameObjectCollection.addGameObject(block, treeLayer);
        }
//        createLeaf(treesIndex ,block);
    }

//    /**
//     *
//     * @param treesIndex
//     */
//    private void createLeaf(int treesIndex, Block block){
//        int leafSize = 0;
//        Vector2 leafHeight = block.getTopLeftCorner().add(new
//                Vector2(-Block.SIZE*leafSize,-Block.SIZE*leafSize));
//        for (int i = 0; i < leafSize*2; i++) {
//            for (int j = 0; j < leafSize*2; j++) {
//                if(leafRandom()) {
//                    Leaf leaf = new Leaf(leafHeight,
//                            new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)),
//                            gameObjectCollection,
//                            treeLayer + 1);
//                    leaf.setTag(LEAF_HERE);
//                    gameObjectCollection.addGameObject(leaf, treeLayer + 1);
//                    block.addComponent(deltaTime -> leaf.update(deltaTime));
//                }
//                leafHeight = leafHeight.add(Vector2.RIGHT.mult(Block.SIZE));
//            }
//            leafHeight = leafHeight.add(new Vector2(0, Block.SIZE));
//            leafHeight = leafHeight.add(Vector2.LEFT.mult(2*Block.SIZE*leafSize));
//        }
//    }
}
