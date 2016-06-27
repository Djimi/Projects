package gamecamerashooter.example.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class GameObject{
	private PointF location;
	private Bitmap bitmap;
	
	public GameObject() {}

	public GameObject(PointF location, Bitmap bitmap) {
		this.location = location;
		this.bitmap = bitmap;
	}

    public PointF getCenter(){
        return new PointF(location.x + bitmap.getWidth()/2, location.y + bitmap.getHeight()/2);
    }

	public PointF getLocation() {
		return location;
	}

    public void setLocation(PointF point){
        location = point;
    }

	public void setLocation(float x, float y) {
        if(location == null) {
            location = new PointF();
        }

		location.set(x, y);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}