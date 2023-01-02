package pepse.wold.trees;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.wold.Block;

import java.util.Random;


public class Leaf extends Block {
    private final static int MAX_TIME = 30;
    private final static int TRANSITION_TIME = 4;
    private final static float ANGEL_LEAF = 10;
    private final static int FALLING_ANGEL = 30;
    private final static int FADE_OUT_TIME = 10;
    private final Vector2 topLeftCorner;
    private final Random rand = new Random();
    private Transition<Float> horizontalTransition;


    /**
     * this is the construct for the leaf
     * @param topLeftCorner the position where we want the leaf
     * @param renderable what color it should be
     */
    public Leaf(Vector2 topLeftCorner,
                Renderable renderable
                ) {
        super(topLeftCorner, renderable);
        this.topLeftCorner = topLeftCorner;
        creatingScheduled(); //calculating leaf angel
    }

    /**
     * this is when we create the leaf again on the tree
     */
    private void recreatingLeaf(){
        setTopLeftCorner(this.topLeftCorner);
        this.setVelocity(Vector2.ZERO);
        this.renderer().setOpaqueness(1f);
        creatingScheduled();
    }

    /**
     * we want to know the angle of the leaf at each moment
     * @param angel
     */
    private void calculatingLeafAngel(float angel){
        this.renderer().setRenderableAngle((float)Math.sin(angel)*ANGEL_LEAF);
    }

    /**
     * this function deals when the leaf is blown off
     */
    private void fallLeaves(){
        this.transform().setVelocity(new
                Vector2((rand.nextInt(FALLING_ANGEL*2)-FALLING_ANGEL),3*FALLING_ANGEL));
        this.renderer().fadeOut(FADE_OUT_TIME, this::recreatingLeaf);
    }

    /**
     *this is where we create the scheduled task for the leafs
     */
    private void creatingScheduled(){
        // the first ScheduledTask is for leafs to move in the wind
        int save =  rand.nextInt(MAX_TIME);
        new ScheduledTask(
                this,
                save,
                false,
                () ->{
                    horizontalTransition = new Transition<Float>(this,
                            this::calculatingLeafAngel,
                            0f,
                            (float) (2 *Math.PI),
                            Transition.CUBIC_INTERPOLATOR_FLOAT,
                            TRANSITION_TIME,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null);
                });

        // the second is for leafs to fall of the tree to the ground
        new ScheduledTask(
                this,
                (rand.nextInt(MAX_TIME-save)+save),
                false,
                this::fallLeaves
        );
    }


    /**
     * whene the leaf hits the ground we want it to stop moving in the wind
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.setVelocity(Vector2.ZERO);
        this.removeComponent(horizontalTransition);
    }
}
