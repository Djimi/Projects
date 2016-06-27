package gamecamerashooter.example.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;

/**
 * This class is {@link GameObject}, but has one additional property - instance of {@link TransformationGenerator}.
 * In that way it is convenience for animations.
 */
public class GameAnimationObject extends GameObject{

    private TransformationGenerator generator;

    public GameAnimationObject(PointF startLocation, Bitmap bitmap) {
        super(startLocation, bitmap);
    }

    public GameAnimationObject(PointF startLocation, Bitmap bitmap, TransformationGenerator generator) {
        super(startLocation, bitmap);
        this.generator = generator;
    }

    public void setGenerator(TransformationGenerator generator) {
        this.generator = generator;
    }

    public Matrix getMatrix() {
        return generator.getMatrix();
    }

    public Matrix nextFrame(){
        return generator.nextFrame();
    }

    public boolean hasNextFrame() {
        return generator.hasNextFrame();
    }
}