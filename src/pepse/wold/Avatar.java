package pepse.wold;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;


public class Avatar extends GameObject {
    private static final int STAND = 0;
    private static final int WALK = 1;
    private static final int FLY = 2;

    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final String[] motions = {"stand", "walk", "fly"};
    private static final int[] numOfImagesInMotion = {4, 4, 2};
    private static UserInputListener inputListener;
    private float energy;
    private static AnimationRenderable[] motionsRenderable;

    public Avatar(Vector2 topLeftCorner, Renderable renderable, UserInputListener inputListener,
                  ImageReader imageReader) {
        super(topLeftCorner, new Vector2(70, 120), renderable);

        this.energy = 100;

    }

    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader){
        Avatar.inputListener = inputListener;
        createAnimationRenderables(imageReader);
        Avatar avatar = new Avatar(topLeftCorner, motionsRenderable[0], inputListener, imageReader);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.transform().setAccelerationY(GRAVITY);
        gameObjects.addGameObject(avatar, layer);

        return avatar;
    }

    private static void createAnimationRenderables(ImageReader imageReader) {
        motionsRenderable = new AnimationRenderable[motions.length];
        for (int i = 0; i < motions.length; i ++) {
            String[] animationPaths = new String[numOfImagesInMotion[i]];
            for (int j = 0; j < numOfImagesInMotion[i]; j++) {
                String str = "src/external/" + motions[i] + (j + 1) + ".png";
                animationPaths[j] = str;
            }
            motionsRenderable[i] = new AnimationRenderable(animationPaths, imageReader, true, 0.5);
        }
    }

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
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            physics().preventIntersectionsFromDirection(null);
            new ScheduledTask(this, .5f, false,
                    () -> physics().preventIntersectionsFromDirection(Vector2.ZERO));
            return;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            if (getVelocity().y() == 0) {
                transform().setVelocityY(VELOCITY_Y);
            } else if (energy != 0) {
                transform().setVelocityY(Math.max(VELOCITY_Y - 100, getVelocity().y() - 30));
                this.energy -= 0.5f;
            }
        } else if (getVelocity().y() == 0) {
            this.energy += 0.5f;
        }

        if (getVelocity().y() == 0 && getVelocity().x() == 0){
            this.renderer().setRenderable(motionsRenderable[STAND]);
        }
        if (getVelocity().y() != 0){
            this.renderer().setRenderable(motionsRenderable[FLY]);
            this.renderer().setIsFlippedHorizontally(getVelocity().x() < 0);
        }
    }

    public float getEnergy(){
        return this.energy;
    }
}
