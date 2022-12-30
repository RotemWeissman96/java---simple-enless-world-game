package pepse.wold.trees;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.wold.Block;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

/**
 *
 */
public class Leaf extends Block {
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private final static int MAX_TIME = 30;
    private final static int TRANSITION_TIME = 4;
    private final static float ANGEL_LEAF = 10;
    private final GameObjectCollection gameObjectCollection;
    private final Vector2 topLeftCorner;
    private final int leafLayer;
    private final Random rand = new Random();

    public Leaf(Vector2 topLeftCorner,
                Renderable renderable,
                GameObjectCollection gameObjectCollection,
                int layer) {
        super(topLeftCorner, renderable);
        this.gameObjectCollection = gameObjectCollection;
        this.topLeftCorner = topLeftCorner;
        this.leafLayer = layer;
        creatingScheduled(); //calculating leaf angel
    }

    /**
     *
     * @param angel
     */
    private void calculatingLeafAngel(float angel){
        this.renderer().setRenderableAngle((float)Math.sin(angel)*ANGEL_LEAF);
    }

    /**
     *
     */
    private void creatingScheduled(){
        new ScheduledTask(
                this,
                rand.nextInt(MAX_TIME),
                false,
                () ->{
//                    new Transition<Float>(
//                            this,
//                            leaf -> this.setDimensions(new Vector2(leaf,this.getDimensions().y())),
//                            (float) Block.SIZE,
//                            5f,
//                            Transition.LINEAR_INTERPOLATOR_FLOAT,
//                            10,
//                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
//                            null
//                    );
                    new Transition<Float>(this,
                            this::calculatingLeafAngel,
                            0f,
                            (float) (2 *Math.PI),
                            Transition.CUBIC_INTERPOLATOR_FLOAT,
                            TRANSITION_TIME,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null);
                }
        );
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        if(Objects.equals())
    }
}
