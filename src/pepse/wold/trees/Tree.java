package pepse.wold.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
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
    private final int seed;

    public Tree(GameObjectCollection gameObjectCollection,
                int layer,
                Function<Float, Float> heightFunction,
                int seed){
        this.gameObjectCollection = gameObjectCollection;
        treeLayer = layer;
        this.groundHeightFunction = heightFunction;
        this.seed = seed;
    }

    /**
     *
     * @return
     */
    private int randomTreePosition(int treesIndex,int adds){
//        for (int i = 0; i < 20; i++) {
//            System.out.println((Objects.hash(treesIndex, seed)*13)%40);
//        }
////        System.out.println(Objects.hash(treesIndex, seed));
//        Random random = new Random(Objects.hash(treesIndex, seed));
        return (Objects.hash(treesIndex, seed)*13)%40;
    }

    /**
     *
      * @return
     */
//    private int treesHeight(){
//        return rand.nextInt(MAX_TREE_HEIGHT - MIN_TREE_HEIGHT) + MIN_TREE_HEIGHT;
//    }

    /**
     *
     * @return
     */
    private boolean leafRandom(){
        Random rand =new Random();
        int adds = rand.nextInt(10);
        return (adds < 7);
    }

//    public void createInRange(int minX, int maxX) {
//         float indexTreeX = minX;
//         Vector2 height = Vector2.RIGHT;
//         while (indexTreeX < maxX){
//             if (randomCoin()){
//                 Block block = new Block(Vector2.ZERO, null);
//                 for (int i =1; i < treesHeight() + 1; i++) {
//                     block = new Block(new Vector2(indexTreeX, heightFunction.apply(indexTreeX)-i*Block.SIZE),
//                             new RectangleRenderable(ColorSupplier.approximateColor(TREE_COLOR)));//plant tree
//                     block.setTag("TREE");
//                     gameObjectCollection.addGameObject(block, treeLayer);
//                     height = block.getTopLeftCorner();
//                     }
//                 int leavesSize = treesHeight()/3;
//                 height = height.add(new Vector2(-Block.SIZE*leavesSize, -Block.SIZE*leavesSize));
//                 for (int i = 0; i < leavesSize*2; i++) {//the leaves size is to 1 dirction, to init need 2
//                     for (int j = 0; j < leavesSize*2; j++) {
//                         if (leafRandom()) {
////                             Leaf leaf = new Leaf(height, block, gameObjectCollection, leafLayer,
////                                     function.apply(height.x()), seed);
//                             Leaf leaf = new Leaf(height,
//                                     new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)),
//                                     gameObjectCollection,
//                                     treeLayer + 1);
//                             gameObjectCollection.addGameObject(leaf, treeLayer);
////                             block.addComponent(deltaTime -> leaf.update(deltaTime));
//                             }
//                         height = height.add(Vector2.RIGHT.mult(Block.SIZE));
//                         }
//                     height = height.add(new Vector2(0, Block.SIZE));
//                     height = height.add(Vector2.LEFT.mult(2*Block.SIZE*leavesSize));
//                     }
//                 }
////             else {
////                 createSkullPilot(indexTreeX);
////                 }
//             indexTreeX+=Block.SIZE;
//             }
//         }



    /**
     * h
     * @param minX
     * @param maxX
     */
    public void createInRange(int minX, int maxX){
        int treesIndex = minX;
        while (treesIndex < maxX){
            if(randomTreePosition(treesIndex,10) == 0){
                createTree(treesIndex);
            }
            treesIndex += Block.SIZE;
        }
    }

    /**
     *
     * @param treesIndex
     */
    private void createTree(int treesIndex){
        Vector2 height = Vector2.RIGHT;
        Block block = new Block(Vector2.RIGHT,null);
        int treesHeight = randomTreePosition(treesIndex, 15);
        for (int i = 0; i < treesHeight + 1 ; i++) {
            block = new Block(new Vector2(treesIndex,  groundHeightFunction.apply((float)treesIndex) - (i* Block.SIZE)),
                    new RectangleRenderable(ColorSupplier.approximateColor(TREE_COLOR)));
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
        int leafSize = randomTreePosition(treesIndex, 15)/3;
        Vector2 leafHeight = block.getTopLeftCorner().add(new
                Vector2(-Block.SIZE*leafSize,-Block.SIZE*leafSize));
        for (int i = 0; i < leafSize*2; i++) {
            for (int j = 0; j < leafSize*2; j++) {
                if(leafRandom()) {
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
            leafHeight = leafHeight.add(Vector2.LEFT.mult(2*Block.SIZE*leafSize));
        }
    }
}
