import java.awt.Color;
import java.awt.image.BufferedImage;

import edu.wlu.cs.levy.CG.KeySizeException;


public class CoreStarter implements Runnable{

	PictureForChange img;
	
	int heightStart;
	int heightEnd;
	
	int widthEnd;
	
	
	public CoreStarter(int iStart, int iEnd, int jEnd, PictureForChange img) {
		this.img = img;
		
		this.heightStart = iStart;
		this.heightEnd = iEnd;
		this.widthEnd = jEnd;
	}

	@Override
	public void run() {
		for(int i = heightStart; i < heightEnd; ++i){
			for(int j = 0; j < widthEnd; ++j){
				
				Color color = new Color(img.getImage().getRGB(j, i));
				
				int regionNumber = 0;
				
				try {
					regionNumber = img.getTree().nearest(new double[]{j, i});
				} catch (KeySizeException e) {
					e.printStackTrace();
				}
				
				Region currentRegion = img.getRegion(regionNumber);
				
				synchronized(this){
					currentRegion.incrementColors(color.getRed(), color.getGreen(), color.getBlue());
					currentRegion.incrementPoints();
					currentRegion.getPoints().add(new double[]{j, i});
				}
				
			} // end inner for
		} //end outer for
	} // end run
}
