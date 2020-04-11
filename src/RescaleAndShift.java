import java.awt.image.BufferedImage;
/*
 * What happened if image pixel values are rescaled by 2, rounded to integers, 
 * then rescaled by 1/2, and finally rounded to integers again? 
 * 
 * What happened if image pixel values are rescaled by 1/2, rounded to integers, then rescaled by 2, and finally
 * rounded to integers again?
 */
public class RescaleAndShift {
	public static BufferedImage rescale(BufferedImage originalImage, float s){
    	int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[][][] ImageArray1 = Demo.convertToArray(originalImage); //  Convert the image to array
        int[][][] ImageArray2 = Demo.convertToArray(originalImage);  
    	
    	for(int y=0; y<height; y++){
    		for(int x=0; x<width; x++){
    			ImageArray2[x][y][1] =  Math.round((s*(ImageArray1[x][y][1]))); //r
	    		ImageArray2[x][y][2] = Math.round((s*(ImageArray1[x][y][2]))); //g
	    		ImageArray2[x][y][3] = Math.round((s*(ImageArray1[x][y][3]))); //b
	    		if (ImageArray2[x][y][1]<0) { ImageArray2[x][y][1] = 0; }
	    		if (ImageArray2[x][y][2]<0) { ImageArray2[x][y][2] = 0; }
	    		if (ImageArray2[x][y][3]<0) { ImageArray2[x][y][3] = 0; }
	    		if (ImageArray2[x][y][1]>255) { ImageArray2[x][y][1] = 255; }
	    		if (ImageArray2[x][y][2]>255) { ImageArray2[x][y][2] = 255; }
	    		if (ImageArray2[x][y][3]>255) { ImageArray2[x][y][3] = 255; }
    		}
    	}

    	return Demo.convertToBimage(ImageArray2);
    }

  	public static BufferedImage shifting(BufferedImage originalImage, int s){
    	int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[][][] ImageArray1 = Demo.convertToArray(originalImage); //image to array
        int[][][] ImageArray2 = Demo.convertToArray(originalImage);  
    	
    	for(int y=0; y<height; y++){
    		for(int x=0; x<width; x++){
    			ImageArray2[x][y][1] =  Math.round(((ImageArray1[x][y][1]+s))); //r
	    		ImageArray2[x][y][2] = Math.round(((ImageArray1[x][y][2]+s))); //g
	    		ImageArray2[x][y][3] = Math.round(((ImageArray1[x][y][3]+s))); //b
	    		if (ImageArray2[x][y][1]<0) { ImageArray2[x][y][1] = 0; }
	    		if (ImageArray2[x][y][2]<0) { ImageArray2[x][y][2] = 0; }
	    		if (ImageArray2[x][y][3]<0) { ImageArray2[x][y][3] = 0; }
	    		if (ImageArray2[x][y][1]>255) { ImageArray2[x][y][1] = 255; }
	    		if (ImageArray2[x][y][2]>255) { ImageArray2[x][y][2] = 255; }
	    		if (ImageArray2[x][y][3]>255) { ImageArray2[x][y][3] = 255; }
    		}
    	}

    	return Demo.convertToBimage(ImageArray2);
    }

  	private static int randomiser() {
    	int r =(int)( Math.random()*256);
		double posNeg = Math.random();
		if (posNeg>0.5) {
			r = r * -1;
		}
		return r;
    }
  	
  	public static BufferedImage randomShiftScale(BufferedImage originalImage) {
    	int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[][][] ImageArray1 = Demo.convertToArray(originalImage);          //  Convert the image to array
        int[][][] ImageArray2 = Demo.convertToArray(originalImage); 
        
        for(int y=0; y<height; y++){
        	for(int x=0; x<width; x++){
        		int r1 = randomiser();
        		ImageArray2[x][y][1] = (ImageArray1[x][y][1])+r1; //r
        		int r2 = randomiser();
            	ImageArray2[x][y][2] = (ImageArray1[x][y][2])+r2; //g
            	int r3 = randomiser();
            	ImageArray2[x][y][3] = (ImageArray1[x][y][3])+r3; //b
        	}
        } 
    	return Demo.findShiftAndScale((ImageArray2));
    }
}
