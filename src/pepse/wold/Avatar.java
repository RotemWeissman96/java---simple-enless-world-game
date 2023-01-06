package pepse.wold;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;


public class Avatar extends GameObject {
    private static final int STAND = 0;
    private static final int WALK = 1;
    private static final int FLY = 2;
    private static final int FALL = 3;
    private static final float AVATAR_HEIGHT = 120;
    private static final float AVATAR_WIDTH = 60;
    private static final float MAX_ENERGY = 100;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final float FLIGHT_SLOWER_THEN_JUMP = 100;
    private static final float FLIGHT_VELOCITY_BONUS = 30;
    private static final float FLIGHT_POWER_REDUCTION = 0.5f;
    private static final String FECES = "FECES";
    private static final String TREE = "TREE";
    private static final String[] motions = {"stand", "walk", "fly", "fall"};
    private static final int[] numOfImagesInMotion = {4, 4, 2, 3};
    private static final String GROUND = "GROUND";
    private static final String AVATAR = "AVATAR";
    private static UserInputListener inputListener;
    private float energy;
    private static AnimationRenderable[] motionsRenderable;
    private boolean falling = false;

    /**
     * Avatar constructor is called from Avatar.create
     */
    public Avatar(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, new Vector2(AVATAR_WIDTH, AVATAR_HEIGHT), renderable);
        this.energy = MAX_ENERGY;

    }

    /**
     * create a new avatar
     * @param gameObjects GameObjectCollection of the current game
     * @param layer the layer to assign the avatar to
     * @param topLeftCorner where the avatar should be rendered at first
     * @param inputListener user input listener
     * @param imageReader renderables creator
     * @return the avatar game object
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader){
        Avatar.inputListener = inputListener;
        createAnimationRenderables(imageReader);
        Avatar avatar = new Avatar(topLeftCorner, motionsRenderable[0]);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.transform().setAccelerationY(GRAVITY);
        gameObjects.addGameObject(avatar, layer);
        avatar.setTag(AVATAR);
        return avatar;
    }

    /**
     * create all animation renderables for avatar
     * @param imageReader renderables creator
     */
    private static void createAnimationRenderables(ImageReader imageReader) {
        motionsRenderable = new AnimationRenderable[motions.length];
        for (int i = 0; i < motions.length; i ++) {
            String[] animationPaths = new String[numOfImagesInMotion[i]];
            for (int j = 0; j < numOfImagesInMotion[i]; j++) {
                String str = "assets/" + motions[i] + (j + 1) + ".png";
                animationPaths[j] = str;
            }
            motionsRenderable[i] = new AnimationRenderable(animationPaths, imageReader, true, 0.3);
        }
    }

    /**
     * update avatar renderable based on the game stutus and user input
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            if (getVelocity().y() == 0) {
                this.renderer().setRenderable(motionsRenderable[WALK]);
                this.renderer().setIsFlippedHorizontally(true);
            }
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            xVel += VELOCITY_X;
            if (getVelocity().y() == 0) {
                this.renderer().setRenderable(motionsRenderable[WALK]);
                this.renderer().setIsFlippedHorizontally(false);
            }
        }
        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            if (getVelocity().y() == 0) {
                transform().setVelocityY(VELOCITY_Y);
            } else if (energy != 0) {
                transform().setVelocityY(Math.max(VELOCITY_Y - FLIGHT_SLOWER_THEN_JUMP,
                                                  getVelocity().y() - FLIGHT_VELOCITY_BONUS));
                this.energy -= FLIGHT_POWER_REDUCTION;
            }
        } else if (getVelocity().y() == 0) {
            this.energy += FLIGHT_POWER_REDUCTION;
        }
        if (getVelocity().y() == 0 && getVelocity().x() == 0){
            this.renderer().setRenderable(motionsRenderable[STAND]);
        }
        if (getVelocity().y() != 0){
            if (falling){
                this.renderer().setRenderable(motionsRenderable[FALL]);
                this.renderer().setIsFlippedHorizontally(getVelocity().x() < 0);
            } else {
                this.renderer().setRenderable(motionsRenderable[FLY]);
                this.renderer().setIsFlippedHorizontally(getVelocity().x() < 0);
            }
        }
    }

    /**
     * dictates what to do upon collision
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(GROUND) || other.getTag().equals(TREE)) {
            this.falling = false;
            if (other.getTag().equals(GROUND)) {
                this.setVelocity(new Vector2(this.getVelocity().x(), 0));
            }
        }
        if (other.getTag().equals(FECES)) {
            this.energy = 0;
            this.falling = true;
        }
    }
}
