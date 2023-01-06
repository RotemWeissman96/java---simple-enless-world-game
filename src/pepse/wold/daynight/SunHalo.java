package pepse.wold.daynight;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import java.awt.*;


public class SunHalo {
    private static final int HALO_SIZE = 400;
    private static final String SUN_HALO = "SUN HALO";

    /**
     * a static function to create the sun halo
     * @param gameObjects where we add all the game objects
     * @param layer  the layer where the sun halo should be
     * @param sun the sun in order to know where to put the halo
     * @param color the color we wnat fot the halo
     * @return the sun halo itself
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color){

        // we're creating the SunHalo in the background with all its parameters
        GameObject sunHalo = new GameObject(Vector2.ZERO,
                new Vector2(HALO_SIZE,HALO_SIZE),
                new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sunHalo, layer);
        sun.setTag(SUN_HALO);
        // we set the halo to be where the sun is
        sunHalo.addComponent(deltaTime ->
                sunHalo.setCenter(new Vector2(sun.getCenter().x(), sun.getCenter().y())));
        return sunHalo;
    }
}
