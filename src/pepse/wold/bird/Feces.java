package pepse.wold.bird;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.util.Vector2;

import java.util.Objects;

public class Feces extends GameObject{
    private static final String BIRD = "BIRD";
    private static final String FECES = "FECES";
    private final GameObjectCollection gameObjects;

    /**
     *the construct for the feces
     */
    public Feces(Bird bird, ImageReader imageReader, GameObjectCollection gameObjects) {
        super(bird.getCenter(),
                new Vector2(30,30),
                imageReader.readImage("assets/feces.png", true));
        this.setTag(FECES);
        gameObjects.addGameObject(this, Layer.DEFAULT);
        this.setVelocity(new Vector2(0, 100));
        this.gameObjects = gameObjects;
    }


    /**
     * if there is a collision that not the bird we want the feces to be removed from the game
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(!other.getTag().equals(BIRD) && !other.getTag().equals(FECES)){
            gameObjects.removeGameObject(this,Layer.DEFAULT);
        }
    }
}