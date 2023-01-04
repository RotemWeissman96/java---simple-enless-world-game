package pepse.wold.bird;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.wold.Avatar;

import java.util.Random;

public class Bird extends GameObject {
    private final Avatar avatar;
    private final int BIRD_PHOTO = 4;
    private final int SPACE_FROM_AVATAR = 5;
    private final int VELOCITY_BIRD = 300;
    private final double SPEED_PHOTO = 0.5;
    private final String BIRD ="Bird";
    private final Random rand = new Random();
    private final ImageReader imageReader;
    private final GameObjectCollection gameObjects;

    public Bird(ImageReader imageReader,
                Avatar avatar,
                GameObjectCollection gameObjects,
                WindowController windowController) {
        super(new Vector2(windowController.getWindowDimensions().x()/5,
                        windowController.getWindowDimensions().y()/15),
                new Vector2(50,50),
                imageReader.readImage("assets/birds1.png", true));
        this.imageReader = imageReader;
        this.gameObjects =gameObjects;
        this.avatar =avatar;
        gameObjects.addGameObject(this, Layer.STATIC_OBJECTS);
        createAnimationRenderablesBird(imageReader);
        this.setTag(BIRD);
    }

    private void createAnimationRenderablesBird(ImageReader imageReader) {
        String[] animationPaths = new String[BIRD_PHOTO];
        for (int j = 1; j < BIRD_PHOTO+1; j++) {
            animationPaths[j-1] = "assets/birds" + (j) + ".png";
        }
        AnimationRenderable animationRenderablee = new AnimationRenderable(animationPaths,
                imageReader,
                true,
                SPEED_PHOTO);
        this.renderer().setRenderable(animationRenderablee);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(this.getCenter().x() < avatar.getCenter().x() - SPACE_FROM_AVATAR){
            this.setVelocity(new Vector2(VELOCITY_BIRD,0));
            this.renderer().setIsFlippedHorizontally(true);
        }else if(this.getCenter().x() > avatar.getCenter().x() + SPACE_FROM_AVATAR) {
            this.setVelocity(new Vector2(-VELOCITY_BIRD,0));
            this.renderer().setIsFlippedHorizontally(false);
        }else {
            setVelocity(Vector2.ZERO);
        }
        if(rand.nextInt(100) ==0){
            new Feces(this,imageReader, gameObjects);
        }
    }
}
