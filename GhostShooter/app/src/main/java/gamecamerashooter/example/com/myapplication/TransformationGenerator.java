package gamecamerashooter.example.com.myapplication;

import android.graphics.Matrix;
import android.graphics.PointF;


/**
 * This class is helper for transformations. With the information in it, it generates
*  frames for animation. To get nextFrame frame use {@link #nextFrame() nextFrame} method. Before using
 *  {@link #nextFrame() nextFrame} you have to check, whether the generator can generate more frames,
 *  with {@link #hasNextFrame() hasNextFrame} method. Usually is it recommended to set at least fromTopLeft point,
 *  fromCenter point and toCenter point. The class must be instantiated with
 *  {@link TransformationGenerator.Builder} Builder.
 */
public class TransformationGenerator {

    private Matrix matrix;

    private PointF topLeft;
    private PointF center;

    private int framesLeft;

    private float moveXSpeed = 0;
    private float moveYSpeed = 0;

    private float angle = 0;
    private float angleDifference = 0;

    private float scaleXSpeed = 0;
    private float scaleYSpeed = 0;

    private float scaleX = 1;
    private float scaleY = 1;

    private TransformationGenerator(PointF fromTopLeft, PointF fromCenter, PointF toCenter, float fromScaleX, float fromScaleY,
                                    float toScaleX, float toScaleY, Float fromAngle, Float toAngle, int duration) {

        setTopLeft(fromTopLeft);
        setCenter(fromCenter);
        setScale(fromScaleX, fromScaleY);
        setAngle(fromAngle);
        setFramesLeft(duration);

        setupAngleDifference(fromAngle, toAngle, duration);
        setupMoveSpeed(fromCenter, toCenter, duration);
        setupScaleSpeed(fromScaleX, fromScaleY, toScaleX, toScaleY, duration);

        matrix = new Matrix();
        applyTransformations();
    }

    private float calculateOnePart(Float from, Float to, int duration) {
        return (to - from)/duration;
    }


    public void setAngleDifference(float angleDifference) {
        this.angleDifference = angleDifference;
    }

    private void setAngle(Float angle) {
        this.angle = angle;
    }

    private void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    private void setCenter(PointF center) {
        this.center = center;
    }

    private void setTopLeft(PointF fromTopLeft) {
        this.topLeft = fromTopLeft;
    }

    private void setFramesLeft(int framesLeft) {
        this.framesLeft = framesLeft;
    }

    private void setupAngleDifference(Float fromAngle, Float toAngle, int duration) {
        setAngleDifference(calculateOnePart(fromAngle, toAngle, duration));
    }

    private void setupScaleSpeed(float fromScaleX, float fromScaleY, float toScaleX, float toScaleY, int duration) {
        scaleXSpeed = calculateOnePart(fromScaleX, toScaleX, duration);
        scaleYSpeed = calculateOnePart(fromScaleY, toScaleY, duration);
    }

    private void setupMoveSpeed(PointF fromLocation, PointF toLocation, int duration) {
        moveXSpeed = calculateOnePart(fromLocation.x, toLocation.x, duration);
        moveYSpeed = calculateOnePart(fromLocation.y, toLocation.y, duration);
    }



    /**
     * This method generates the matrix for nextFrame transformation
     * @return The matrix of transformation
     */
    public Matrix nextFrame(){

        decrementFrames();

        nextCoordinates();
        nextAngle();
        nextScaleValues();

        applyTransformations();

        return matrix;
    }

    private void applyTransformations() {

        matrix.setScale(scaleX, scaleY, center.x - topLeft.x, center.y - topLeft.y);
        matrix.postTranslate(topLeft.x, topLeft.y);
        matrix.postRotate(angle, center.x, center.y);
    }

    private void nextCoordinates() {
        nextTopLeft();
        nextCenter();
    }

    private void nextCenter() {
        center.x += moveXSpeed;
        center.y += moveYSpeed;
    }

    private void nextTopLeft() {
        topLeft.x += moveXSpeed;
        topLeft.y += moveYSpeed;
    }

    private void nextScaleValues() {
        scaleX += scaleXSpeed;
        scaleY += scaleYSpeed;
    }

    private void nextAngle() {
        angle += angleDifference;
    }

    private void decrementFrames() {
        --framesLeft;
    }


    /** Shows whether the Generator at least one more frame */
    public boolean hasNextFrame(){
        return framesLeft > 0;
    }

    public Matrix getMatrix(){
        return matrix;
    }

    /**
     *The Builder class is a utility class to facilitate adding transformations
     */
    public static class Builder{

        private PointF fromTopLeft = new PointF(0, 0);
        private PointF fromCenter = new PointF(0, 0);
        private PointF toLocation = new PointF(0, 0);
        private float fromAngle = 0;
        private float toAngle = 0;
        private float fromScaleX = 1;
        private float fromScaleY = 1;
        private float toScaleX = 1;
        private float toScaleY = 1;
        private int duration = 1;

        public Builder(PointF fromTopLeft, PointF fromCenter, PointF toLocation) {
            this.fromTopLeft = fromTopLeft;
            this.fromCenter = fromCenter;
            this.toLocation = toLocation;
        }

        public TransformationGenerator build(){

            TransformationGenerator generator = new TransformationGenerator(fromTopLeft, fromCenter, toLocation, fromScaleX, fromScaleY, toScaleX,
                                            toScaleY, fromAngle, toAngle, duration);

            fromTopLeft = null;
            fromCenter = null;
            toLocation = null;

            return generator;
        }

        public Builder setFromTopLeft(PointF fromTopLeft) {
            this.fromTopLeft = fromTopLeft;
            return this;
        }

        public Builder setFromCenter(PointF fromCenter){
            this.fromCenter = fromCenter;

            return this;
        }

        public Builder setToLocation(PointF toLocation) {
            this.toLocation = toLocation;
            return this;
        }

        public Builder setFromAngle(float fromAngle) {
            this.fromAngle = fromAngle;
            return this;
        }

        public Builder setToAngle(float toAngle) {
            this.toAngle = toAngle;
            return this;
        }

        public Builder setFromScaleX(float fromScaleX) {
            this.fromScaleX = fromScaleX;
            return this;
        }

        public Builder setFromScaleY(float fromScaleY) {
            this.fromScaleY = fromScaleY;
            return this;
        }

        public Builder setToScaleX(float toScaleX) {
            this.toScaleX = toScaleX;
            return this;
        }

        public Builder setToScaleY(float toScaleY) {
            this.toScaleY = toScaleY;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }
    }
}
