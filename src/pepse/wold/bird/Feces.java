package pepse.wold.bird;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Feces extends GameObject{
    private Bird bird;
    private GameObjectCollection gameObjects;


    public Feces(Bird bird, ImageReader imageReader, GameObjectCollection gameObjects) {
        super(bird.getCenter(),
                new Vector2(30,30),
                imageReader.readImage("assets/feces.png", true));
        this.setTag("feces");
        gameObjects.addGameObject(this, Layer.DEFAULT);
        this.setVelocity(new Vector2(0, 100));
    }

//    @Override
//    public void onCollisionEnter(GameObject other, Collision collision) {
//        super.onCollisionEnter(other, collision);
//        gameObjects.removeGameObject(this,Layer.DEFAULT);
//    }
}
