package pepse.wold.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    private static final String SUN = "Sun";
    private static final int SUN_SIZE = 150;
    private static final float INITIALING_SUN = 0f;
    private static final float MAX_ANGLE = (float) (2 * Math.PI);
    private static final float CENTER_Of_WINDOW = 3f;
    private static final float RADIUS_RATIO = 3f/6f;


    /**
     * we find where the sun will be at the next moment
     * @param sun the sun itself
     * @param windowDimensions whe window dimensions
     * @param angle the angle where the sun needs to be from the center of the window dimensions
     */
    private static void calculatingSunNextPosition(GameObject sun,Vector2 windowDimensions, float angle){
        float focalX = windowDimensions.x() / 2;
        float focalY = windowDimensions.y() * 0.7f;
        float radiusA = windowDimensions.x() * RADIUS_RATIO;
        float radiusB = windowDimensions.y() * RADIUS_RATIO;
        float xParemeter = (float) (focalX + radiusA * Math.cos(angle - 0.5 * Math.PI));
        float yParemeter = (float) (focalY + radiusB * Math.sin(angle - 0.5 * Math.PI));
        sun.setCenter(new Vector2(xParemeter,yParemeter));
    }

    /**
     * a static function to create the sun
     * @param gameObjects where we add all the game objects
     * @param layer the layer where the sun should be
     * @param windowDimensions the window itself
     * @param cycleLength the cycle of the sun
     * @return the sun itself
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){

        // we're creating the Sun in the background with all its parameters
        GameObject sun = new GameObject(Vector2.ZERO,
                                        new Vector2(SUN_SIZE,SUN_SIZE),
                                        new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(SUN);
        new Transition<Float>(sun,
                angle -> calculatingSunNextPosition(sun, windowDimensions, angle),
                INITIALING_SUN,
                MAX_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                Sun::print);
        return sun;
    }

    public static void print(){
        System.out.println("sun is up");
    }
}
