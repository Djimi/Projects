package gamecamerashooter.example.com.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, GameTimer.GameClockListener,
                                                                            GameActivity.OnShotListener {

    private long lastTime;

    private Context context;
    private GameObject crossHair = new GameObject();
    private GameTimer timer;

    private PointF checkPoint = new PointF();
    private Paint crosshairPaint = new Paint();
    private Paint scorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int currentFPS;
    private int frames = 0;
    private Bitmap bulletBitmap; 

    GameAnimationObject animationObject;

    
    
    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
    	
    	this.context = context;

        setupScorePaint();

        createCrosshairBitmap(context);
        createBulletBitmap(context);


        getHolder().addCallback(this);

        setOnTouchListener(new MyTouchListener());
    }

    private void setupScorePaint() {
        scorePaint.setColor(Color.BLUE);
        scorePaint.setAlpha(50);
        scorePaint.setTextSize(300);
    }


    private void createBulletBitmap(Context context) {
        Bitmap tempBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        bulletBitmap = Bitmap.createScaledBitmap(tempBitmap, 20, 20, false);
        tempBitmap.recycle();
    }

    private void createCrosshairBitmap(Context context) {
        Bitmap tempBitmap =  BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        crossHair.setBitmap(Bitmap.createScaledBitmap(tempBitmap, 100, 100, false));
        tempBitmap.recycle();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        setupTargetPossition();
        setupTimer();

        PointF topLeft = new PointF(100, 200);
        PointF fromCenter = new PointF(topLeft.x + crossHair.getBitmap().getWidth() / 2, topLeft.y + crossHair.getBitmap().getHeight() / 2);
        PointF toCenter = new PointF(crossHair.getCenter().x, crossHair.getCenter().y);

        TransformationGenerator generator = new TransformationGenerator.Builder(topLeft, fromCenter, toCenter)
                .setDuration(200)
                .setFromAngle(0)
                .setToAngle(360)
                .setFromScaleX(3f)
                .setToScaleX(1f)
                .setToScaleY(3f)
                .build();

        animationObject = new GameAnimationObject(topLeft, crossHair.getBitmap(), generator);

//        animationObject.nextFrame();

        lastTime = System.currentTimeMillis();
        startPlaying();
    }

    private void setupTimer() {
        timer = new GameTimer(this);
        timer.registerGameClockListener(this);
    }

    private void setupTargetPossition() {
        float targetX = (getWidth() - Constants.CROSSFIRE_WIDTH)/2;
        float targetY = (getHeight() - Constants.CROSSFIRE_HEIGHT)/2;

        crossHair.setLocation(targetX, targetY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Canvas c = null;
        try {
            c = getHolder().lockCanvas(null);
            synchronized (getHolder()) {
                //call methods to draw and process nextFrame frame
                if(c!= null) draw(c);
            }
        } finally {
            if (c != null) {

                getHolder().unlockCanvasAndPost(c);
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        timer.stop();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(crossHair.getBitmap(), crossHair.getLocation().x, crossHair.getLocation().y, crosshairPaint);

        if(animationObject.hasNextFrame()) canvas.drawBitmap(animationObject.getBitmap(), animationObject.getMatrix(), crosshairPaint);

        canvas.drawText("" + currentFPS, Constants.scoreX, Constants.scoreY, scorePaint);

    }

    private boolean shouldChangeFPS() {
        long now = System.currentTimeMillis();
        long difference = now - lastTime;

        return  difference >= 1000 ;
    }

    /** This method starts the game action */
    public void startPlaying(){
        timer.start();
    }

    public void stopPlaying(){
        timer.stop();
    }

    @Override
    public void onShot() {
		
    }

    /**
     * Touch listener which handles all touch events in the game view
     */
    private class MyTouchListener implements OnTouchListener {

        @Override
		public boolean onTouch(View v, MotionEvent event) {

            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                // setup the pivot point
                checkPoint.set(event.getRawX(), event.getRawY());
            } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {

                // coordinates of the user`s finger
                float touchX = event.getRawX();
                float touchY = event.getRawY();

                // move target according to the user touch event
                float x = crossHair.getLocation().x + touchX - checkPoint.x;
                float y = crossHair.getLocation().y + touchY - checkPoint.y;

                crossHair.getLocation().set(x, y);
                checkPoint.set(touchX, touchY);
            }

            return true;
        }
    }

    @Override
    public void onGameEvent() {
        incrementFrames();

        if(shouldChangeFPS()){
            currentFPS = frames;
            lastTime = System.currentTimeMillis();
            frames = 1;
        }

        animationObject.nextFrame();

        Canvas c = getHolder().lockCanvas(null);
        synchronized (getHolder()) {
            draw(c);
        }

        getHolder().unlockCanvasAndPost(c);
    }

    private void incrementFrames() {
        ++frames;
    }


}
