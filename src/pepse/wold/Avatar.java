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
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;

    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private float energy;

    public Avatar(Vector2 topLeftCorner, Renderable renderable, UserInputListener inputListener,
                  ImageReader imageReader) {
        super(topLeftCorner, new Vector2(300, 300), renderable);
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.energy = 100;

    }

    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader){
        String[] stand = new String[1];
        stand[0] = "src/external/stand1_01.jpg";
        Renderable renderable =imageReader.readImage("src/external/Untitled-2_02.jpg", true);
        Avatar avatar = new Avatar(topLeftCorner, renderable, inputListener, imageReader);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.transform().setAccelerationY(GRAVITY);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            xVel -= VELOCITY_X;
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            xVel += VELOCITY_X;
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
    }

    public float getEnergy(){
        return this.energy;
    }
}
