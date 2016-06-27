package gamecamerashooter.example.com.myapplication;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class GameTimer {


    /**
     * This constant shows the interval between two consecutive refreshes.
     * In other words fps = 1000/FRAMERATE_CONSTANT
     */
    public static final int FRAMERATE_CONSTANT = 16;
    private final Handler handler = new Handler();

    /** This list holds subscribers for GameEvents */
    private final List<GameClockListener> gameClockListeners = new ArrayList<GameClockListener>();

    /** Runnable to be executed by handler */
    private final ClockRunnable clockRunnable;

    /** This interface is implemented by the types which are able to receive event on every refresh.
     * The refresh appears once per FRAMERATE_CONSTANT milliseconds
     */
    public static interface GameClockListener{
        public void onGameEvent();
    }

    /** The Runnable class which will be executed by handler */
    private class ClockRunnable implements Runnable{
        private volatile boolean running;

        @Override
        public void run() {
            if(running) {
                onTimerTIck();
                handler.postDelayed(this, FRAMERATE_CONSTANT);
            }
        }
    }


    public GameTimer(GameView surfaceView) {
        clockRunnable = new ClockRunnable();
    }

    public void registerGameClockListener(GameClockListener listener){
        gameClockListeners.add(listener);
    }

    /** Handler start executing its ClockRunnable */
    public void start() {
        clockRunnable.running = true;
        handler.post(clockRunnable);
    }

    /** Handler stop executing its ClockRunnable */
    public void stop(){
        clockRunnable.running = false;
    }

    /** Iterate over all GameClockListeners which are subscribed to receive onGameEvent */
    private void onTimerTIck() {
        for(GameClockListener g : gameClockListeners){
            g.onGameEvent();
        }
    }
}
