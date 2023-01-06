package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import pepse.wold.*;
import pepse.wold.bird.Bird;
import pepse.wold.daynight.Night;
import pepse.wold.daynight.Sun;
import danogl.util.Vector2;
import pepse.wold.daynight.SunHalo;
import pepse.wold.trees.Tree;
import java.awt.*;
import java.util.Random;

public class PepseGameManager extends GameManager {
    private static final float NIGHT_CYCLE = 20;
    private static final float SUN_CYCLE = 2*NIGHT_CYCLE;
    private static final int SKY_AND_NIGHT_BACKGROUND = Layer.BACKGROUND;
    private static final int SUN_BACKGROUND = SKY_AND_NIGHT_BACKGROUND + 1;
    private static final int HALO_SUN_BACKGROUND = SUN_BACKGROUND + 1;
    private static final int TERRAIN_BACKGROUND = HALO_SUN_BACKGROUND + 20;
    private static final int TREE_BACKGROUND = TERRAIN_BACKGROUND + 20;
    private static final int LEAF_BACKGROUND = TREE_BACKGROUND + 1;
    private static final int AVATAR_HEIGHT = 120;
    private static final int SEEDS_RANDOM = 1000;
    private static final int RANDOM_POSITION = 1000;
    private static final String NAME_RANDOM_POSITION = "extra layer";
    private static final double PERCENT_SCREEN = 0.75;
    private static final double PERCENT_SCREEN_CREATING = 0.6f;
    private static final double PERCENT_SCREEN_FIFTH = 0.2;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private Terrain terrain;
    private int maxIndexRendered;
    private int minIndexRendered;
    private  Avatar avatar;
    private  int windowDimensionBlocksX;
    private  Tree tree;
    private  WindowController windowController;
    private  ImageReader imageReader;
    private  UserInputListener inputListener;
    private final static Random rand = new Random();
    private final static int  SEED = rand.nextInt(SEEDS_RANDOM) + 100;
    private Enemy enemy;

    public PepseGameManager() {
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        // creating all the elements will need in creating the game objects
        this.windowDimensionBlocksX = (int) (windowController.getWindowDimensions().x() / Block.SIZE);
        this.minIndexRendered = -(int) (windowDimensionBlocksX * PERCENT_SCREEN);
        this.maxIndexRendered = - minIndexRendered;
        this.windowController = windowController;
        this.imageReader =imageReader;
        this.inputListener =inputListener;

        // this creates the daylight
        creatingDaylight();

        //this creates everything inheritance from blocks
        creatingBlocks();

        //this creates all the figures in the game
        creatingFigures();
        settingCollisions(windowController);

    }

    /**
     * in this function we create all the game objects that are connected to daylight
     */
    private void creatingDaylight(){

        // we create the sky
        Sky.create(this.gameObjects(),
                windowController.getWindowDimensions(),
                SKY_AND_NIGHT_BACKGROUND);

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
                HALO_COLOR);
    }

    /**
     * this is where will create all the game objects that are built from block
     */
    private void creatingBlocks(){
        this.terrain = new Terrain(gameObjects(),
                TERRAIN_BACKGROUND,
                windowController.getWindowDimensions(),
                SEED);
        terrain.createInRange(minIndexRendered, maxIndexRendered);

        // this is where we add the tree and the leaf.
        this.tree = new Tree(this.gameObjects(),
                TREE_BACKGROUND,
                terrain::groundHeightAt);
        tree.createInRange(minIndexRendered, maxIndexRendered);
    }

    /**
     * we create here all the figures in the game
     */
    private void creatingFigures(){
        // creating the avatar to the window controller
        this.avatar = Avatar.create(gameObjects(),
                                    Layer.DEFAULT,
                                    new Vector2(0, terrain.groundHeightAt(0) - AVATAR_HEIGHT),
                                    inputListener,
                                    imageReader);
        this.setCamera(new Camera(avatar,
                                  Vector2.ZERO,
                                  windowController.getWindowDimensions(),
                                  windowController.getWindowDimensions()));
        // creating the bird for the window controller
        new Bird(imageReader, avatar, gameObjects(), windowController);
        float enemyStartX = (float) (((int)(SEED/Block.SIZE))*Block.SIZE);
        this.enemy = Enemy.create(gameObjects(),
                                  new Vector2(enemyStartX,
                                           terrain.groundHeightAt(enemyStartX) - AVATAR_HEIGHT),
                                  imageReader,
                                  avatar);
    }


    /**
     * this function makes sure no layer is empty due to randomly generated world with no starting trees.
     */
    private void settingCollisions(WindowController windowController) {
        Renderable blockRenderable = new RectangleRenderable(Color.BLACK);
        Vector2 possition = new Vector2(windowController.getWindowDimensions().add(
                new Vector2(RANDOM_POSITION, RANDOM_POSITION)));
        gameObjects().addGameObject(new Block(possition, blockRenderable), LEAF_BACKGROUND);
        gameObjects().addGameObject(new Block(possition, blockRenderable), TREE_BACKGROUND);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, TERRAIN_BACKGROUND, true);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, TREE_BACKGROUND, true);
        gameObjects().layers().shouldLayersCollide(LEAF_BACKGROUND, TERRAIN_BACKGROUND, true);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.UI, true);
    }


    /**
     * to create the world from the left based on the Avatar position
     */
    void addingLeft() {
        if (avatar.getCenter().x() / Block.SIZE
                - windowDimensionBlocksX*PERCENT_SCREEN_CREATING < minIndexRendered) {
            int numOfBlocks =  (int) (windowDimensionBlocksX *PERCENT_SCREEN_FIFTH);
            terrain.createInRange(minIndexRendered - numOfBlocks, minIndexRendered - 1);
            terrain.removeFromRange(maxIndexRendered - numOfBlocks + 1, maxIndexRendered);
            tree.createInRange(minIndexRendered - numOfBlocks, minIndexRendered - 1);
            tree.removeFromRange(maxIndexRendered - numOfBlocks + 1, maxIndexRendered);
            minIndexRendered -= numOfBlocks;
            maxIndexRendered -= numOfBlocks;
        }
    }


    /**
     * to create the world from the right based on the Avatar position
     */
    void addingRight() {
        if (avatar.getCenter().x() / Block.SIZE
                + windowDimensionBlocksX*PERCENT_SCREEN_CREATING > maxIndexRendered) {
            int numOfBlocks =  (int) (windowDimensionBlocksX * PERCENT_SCREEN_FIFTH);
            terrain.createInRange(maxIndexRendered + 1, maxIndexRendered + numOfBlocks);
            terrain.removeFromRange(minIndexRendered, minIndexRendered + numOfBlocks - 1);
            tree.createInRange(maxIndexRendered + 1, maxIndexRendered + numOfBlocks);
            tree.removeFromRange(minIndexRendered, minIndexRendered + numOfBlocks - 1);
            maxIndexRendered += numOfBlocks;
            minIndexRendered += numOfBlocks;
        }
    }


    /**
     * checking each moment where the avatar is
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        addingRight();
        addingLeft();
        if (enemy.getCoughtAvatar()){
            if(windowController.openYesNoDialog("Game Over!"))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }
}

