
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Region {

		private int red;
		private int green;
		private int blue;
		private Color averageColor;
		private int numberOfPoints;
		private ArrayList<double[]> points;

		
		public ArrayList<double[]> getPoints(){
			return points;
		}
		
		public Region() {
			points = new ArrayList<>();
			red = 0;
			green = 0;
			blue = 0;
			
			numberOfPoints = 0;
		}
		
		public void incrementColors(int red, int green, int blue){
			this.red += red;
			this.green += green;
			this.blue += blue;
		}
		
		public void incrementPoints(){
			++numberOfPoints;
		}
		
		public Color getAverageColor(){
			return averageColor;
		}
		
		public void calculateAndSetAverageColor(){
		
			averageColor = new Color( (int)(red/numberOfPoints), (int)(green/numberOfPoints), (int)(blue/numberOfPoints));
			
		}

		public void writePointsToImage(BufferedImage img) {
			
			for(double[] arr : points){
				img.setRGB((int)arr[0], (int)arr[1], averageColor.getRGB());

			}
		}
		
	}

