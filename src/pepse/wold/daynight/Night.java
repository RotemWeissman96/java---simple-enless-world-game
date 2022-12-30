package pepse.wold.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {
    private static final String NIGHT = "Night";
    private static final float INITIALING_NIGHT= 0f;
    private static final Float MIDNIGHT_OPACITY = 0.5f;


    /**
     * a static function to create the night
     * @param gameObjects where we add all the game objects
     * @param layer  the layer where the night should be
     * @param windowDimensions the window itself
     * @param cycleLength the cycle of the night
     * @return the game object night
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        // we're creating the Night background with all its parameters
        GameObject night = new GameObject(Vector2.ZERO,
                                            windowDimensions,
                                            new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night,layer);
        night.setTag(NIGHT);
        // transitioning form light to dark and vice versa
        new Transition<Float>(night,
                night.renderer()::setOpaqueness,
                INITIALING_NIGHT,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                Night::print);
        return night;
    }

    public static void print() {
        System.out.println("mid day");
    }
}
