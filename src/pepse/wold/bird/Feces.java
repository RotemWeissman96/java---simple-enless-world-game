package pepse.wold.bird;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.util.Vector2;

import java.util.Objects;

public class Feces extends GameObject{
    private final GameObjectCollection gameObjects;


    public Feces(Bird bird, ImageReader imageReader, GameObjectCollection gameObjects) {
        super(bird.getCenter(),
                new Vector2(30,30),
                imageReader.readImage("assets/feces.png", true));
        this.setTag("feces");
        gameObjects.addGameObject(this, Layer.DEFAULT);
        this.setVelocity(new Vector2(0, 100));
        this.gameObjects = gameObjects;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(!Objects.equals(other.getTag(), "Bird")){
            gameObjects.removeGameObject(this,Layer.DEFAULT);
        }
    }
}
