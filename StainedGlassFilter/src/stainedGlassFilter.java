

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class stainedGlassFilter{

	
	public static void main(String[] args)  {
		
//		if(args.length < 3){
//			throw new IllegalArgumentException("You have to enter at least 3 arguments !"); 
		
//		}
//		
//		if(Integer.parseInt(args[2]) < 0){
//			throw new IllegalArgumentException("Enter a non-negative number !");
//		}
//		
//		final String path = args[0];
//		final String newPath = args[1];
//		
		
		final String path = "input.jpg";
		final String newPath = "output.jpg";
		
		long start = System.currentTimeMillis();
		
		final PictureForChange picture = new PictureForChange(path, 10000 );
		
		try {
			picture.generatePoints();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		final int threadCount = Runtime.getRuntime().availableProcessors();
		
		ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
	
		
		threadPoolSetup(picture, threadPool, threadCount, picture.getImage().getWidth());

		threadPool.shutdown();
		
		
		while(!threadPool.isTerminated()){
			try {
				threadPool.awaitTermination(60, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		picture.setAverageColorsForRegions();

		picture.paintRegionsToImage();
		
		// Writing the new image
		File output = new File(newPath);
		
		try {
			ImageIO.write(picture.getImage(), "jpg", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println((end - start)/1000);
	
	}
	
		
	public static void threadPoolSetup(PictureForChange picture, ExecutorService threadPool, int threadCount, int width){
		
		int startRow = 0;
		int endRow = picture.getImage().getHeight();
		final int rowsInPart = endRow/threadCount;
		int to = rowsInPart;
		
		
		for(int i = 0; i < threadCount - 1; ++i){
			
			threadPool.submit(new CoreStarter(startRow, to, width, picture) );
			
			startRow += rowsInPart;
			to += rowsInPart;
		}
		
		threadPool.submit(new CoreStarter(startRow, endRow, width, picture));
		
	}
}




