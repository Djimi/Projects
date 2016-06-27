package gamecamerashooter.example.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class GameActivity extends Activity implements View.OnClickListener{


    GameView gameView;
    Button shotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAppWindow();

        setContentView(R.layout.game_layout);
        gameView = (GameView)findViewById(R.id.surfaceView);
        shotButton = (Button)findViewById(R.id.shotButton);
        shotButton.setOnClickListener(this);
        
    }

    @Override
    protected void onStart() {
        super.onStart();
//        gameView.startPlaying();
    }

    @Override
    protected void onStop() {
        gameView.stopPlaying();
        super.onStop();
    }

    private void setupAppWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // enable full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == shotButton.getId()){
            gameView.onShot();
        }
    }

    /** Interface which is implement by classes that are able to receive onShot events */
    public interface OnShotListener {
        public void onShot();
    }
}
