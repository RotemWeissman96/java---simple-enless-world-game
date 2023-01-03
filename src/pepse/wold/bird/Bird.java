package pepse.wold.bird;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.wold.Avatar;

public class Bird extends GameObject {
    private Avatar avatar;
    private AnimationRenderable animationRenderablee;

    public Bird(ImageReader imageReader, Avatar avatar, GameObjectCollection gameObjects) {
        super(new Vector2(100,70), new Vector2(35,35),
                imageReader.readImage("assets/birds1.png", true));
        this.avatar =avatar;
        gameObjects.addGameObject(this, Layer.STATIC_OBJECTS);
        createAnimationRenderablesBird(imageReader);
    }

    private void createAnimationRenderablesBird(ImageReader imageReader) {
        String[] animationPaths = new String[4];
        for (int j = 1; j < 5; j++) {
            animationPaths[j-1] = "assets/" + "birds" + (j) + ".png";
        }
        animationRenderablee = new AnimationRenderable(animationPaths,
                imageReader,
                true,
                0.5);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(this.getCenter().x() < avatar.getCenter().x()){
            this.renderer().setIsFlippedHorizontally(false);
        }else {
            this.renderer().setIsFlippedHorizontally(true);
        }
    }
}
