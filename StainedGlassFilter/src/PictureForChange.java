
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSize.Other;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

public class PictureForChange {

	private final int DIMENTION = 2;
	
	
	private Region[] regions;
	private KDTree<Integer> tree;
	
	private final int POINTS_COUNT ;
	private double[][] centerPoints ;
	private BufferedImage img ;
	
	private int addedPointsCount ;
	
	// otherPointsToGenerate shows how many of the smallest rectangles, that we generate in generatePoints method, 
	//will have 2 points
	private int otherPointsToGenerateCount;
	
	
	
	public PictureForChange(String imagePath, int pointsCount) {
		
		try {
			img = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		tree = new KDTree<>(DIMENTION);
		
		POINTS_COUNT = pointsCount;
		addedPointsCount = 0;
		
		centerPoints = new double[pointsCount][DIMENTION];
		regions = new Region[POINTS_COUNT];
		
		for(int i = 0; i < regions.length; ++i){
			regions[i] = new Region();
		}
		
		
		setOtherPointsToGenerageCount();
	}
	
	private void setOtherPointsToGenerageCount(){
		int maxDegreeOf2LowerThanPointsCount = (int)(Math.log(POINTS_COUNT)/Math.log(2));
		otherPointsToGenerateCount = (int)( POINTS_COUNT - Math.pow(2,  maxDegreeOf2LowerThanPointsCount) );
	}
	
	public BufferedImage getImage(){
		return img;
	}
	
	public int getAddedPointsCount(){
		return addedPointsCount;
	}
	
	public Region getRegion(int index){
		return regions[index];
	}
	
	public KDTree<Integer> getTree(){
		return tree;
	}
	
	// The idea of generatePoints is to start splitting image in four rectangulars, after that every piece will be split in four new pieces
	// because of this we have two functions - for even max depth and for odd max depth
	public void generatePoints(){

		int maxtDepth = (int)(Math.log(POINTS_COUNT)/Math.log(2));
		
		if(maxtDepth%2 == 1)
			try {
				generatePointsHelperForOddMaxDepth(0, maxtDepth, 0, img.getHeight() - 1, 0, img.getWidth() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			try {
				generatePointsHelperForEvenMaxDepth(0, maxtDepth, 0, img.getHeight() - 1, 0, img.getWidth() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	
	public void paintRegionsToImage(){
		for(Region region: regions){
			region.writePointsToImage(img);
		}
	}

	public void setAverageColorsForRegions(){
		for(Region region: regions){
			region.calculateAndSetAverageColor();
		}
	}
	
	private void generatePointsHelperForOddMaxDepth(int depth, int maxDepth, int startHeight, int endHeight, int startWidth, int endWidth) throws KeySizeException, KeyDuplicateException{
		
		if(depth > maxDepth) return;
		
		if(depth == maxDepth - 1){
			Random rand = new Random();

			// generating point of the left half of the rectangle
			int firstX = startWidth + rand.nextInt( (endWidth - startWidth)/ 2);
			int firstY = startHeight + rand.nextInt((endHeight - startHeight));
			
			// generating point of the right half of the rectangle
			int secondX = (startWidth + endWidth)/2 + rand.nextInt(endWidth - startWidth)/2 ;
			int secondY = startHeight + rand.nextInt((endHeight - startHeight));
			
			addPoint(firstX, firstY);
			addPoint(secondX, secondY);
			
			
			if(otherPointsToGenerateCount > 0){
				//we have to generate point in this square, but we have to be sure that we would not select point that already has been added
				
				int x = 0;
				int y = 0;
				
				if(secondX < endWidth - 1){ 
					x = secondX + 1 + rand.nextInt((endWidth - secondX)/2);
				} else {
					x = firstX + 1 + rand.nextInt((secondX - firstX)/ 2);
				}
				
				y = startHeight + rand.nextInt((endHeight - startHeight));
				
				addPoint(x, y);
				--otherPointsToGenerateCount;
			}
			
			return;
		}

		generatePointsHelperForOddMaxDepth(depth + 2, maxDepth, startHeight, (endHeight + startHeight)/2, startWidth, (endWidth + startWidth)/2); 
		generatePointsHelperForOddMaxDepth(depth + 2, maxDepth, startHeight, (endHeight + startHeight)/2, (startWidth + endWidth)/2, endWidth);
		generatePointsHelperForOddMaxDepth(depth + 2, maxDepth, (startHeight + endHeight)/2, endHeight, startWidth, (startWidth + endWidth)/2);
		generatePointsHelperForOddMaxDepth(depth + 2, maxDepth, (startHeight + endHeight)/2, endHeight, (startWidth + endWidth)/2, endWidth);
	}
	
	private void generatePointsHelperForEvenMaxDepth(int depth, int maxDepth, int startHeight, int endHeight, int startWidth, int endWidth) throws KeySizeException, KeyDuplicateException{
		if(depth > maxDepth) return;
		
		if(depth == maxDepth){
			Random rand = new Random();

			// generating point of the left half of the rectangle
			int firstX = startWidth + rand.nextInt( (endWidth - startWidth)/ 2);
			int firstY = startHeight + rand.nextInt((endHeight - startHeight));
			
			
			addPoint(firstX, firstY);
			
			if(otherPointsToGenerateCount > 0){
				//we have to generate point in this square, but we have to be sure that we would not select point that already has been added
				
				int x = 0;
				int y = 0;
				
				if(firstX < (endWidth + startWidth)/2 ){ 
					x = firstX + 1 + rand.nextInt((endWidth - firstX)/2);
				} else {
					x = firstX + 1  - rand.nextInt((endWidth - firstX)/ 2);
				}
				
				y = startHeight + rand.nextInt((endHeight - startHeight));
				
				addPoint(x, y);
				--otherPointsToGenerateCount;
			}
			
			return;
		}

		generatePointsHelperForEvenMaxDepth(depth + 2, maxDepth, startHeight, (endHeight + startHeight)/2, startWidth, (endWidth + startWidth)/2); 
		generatePointsHelperForEvenMaxDepth(depth + 2, maxDepth, startHeight, (endHeight + startHeight)/2, (startWidth + endWidth)/2, endWidth);
		generatePointsHelperForEvenMaxDepth(depth + 2, maxDepth, (startHeight + endHeight)/2, endHeight, startWidth, (startWidth + endWidth)/2);
		generatePointsHelperForEvenMaxDepth(depth + 2, maxDepth, (startHeight + endHeight)/2, endHeight, (startWidth + endWidth)/2, endWidth);
	}
	
	public void addPoint(int x, int y) throws KeySizeException, KeyDuplicateException{
		centerPoints[addedPointsCount][0] = x;
		centerPoints[addedPointsCount][1] = y;
		
		tree.insert(centerPoints[addedPointsCount], addedPointsCount);
		
		++addedPointsCount;
	}
	

}
