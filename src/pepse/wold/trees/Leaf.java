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
        creatingScheduled();
    }

    /**
     *
     */
    private void creatingScheduled(){
        new ScheduledTask(
                this,
                rand.nextInt(30),
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
                            this.renderer()::setRenderableAngle,
                            1f,
                            45f,
                            Transition.LINEAR_INTERPOLATOR_FLOAT,
                            10,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null);
                }
        );
    }
//

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        if(Objects.equals())
    }
}
