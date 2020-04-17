import java.awt.image.BufferedImage;
/*
 * What is the difference between image negative (as in the template code) and image bit-wise NOT?
 */
public class ArithmeticAndBool {
	
	/*
     * Add method
     */
    public static BufferedImage Addition(BufferedImage originalImage, BufferedImage image2){
    	int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[][][] ImageArray1 = Demo.convertToArray(originalImage); //image to array
        int[][][] ImageArray2 = Demo.convertToArray(image2);  
    	
        for(int y=0; y<height; y++){
	    	for(int x=0; x<width; x++){
		    	ImageArray2[x][y][1] = ImageArray1[x][y][1] + ImageArray2[x][y][1];
		    	ImageArray2[x][y][2] = ImageArray1[x][y][2] + ImageArray2[x][y][2];
		    	ImageArray2[x][y][3] = ImageArray1[x][y][3] + ImageArray2[x][y][3];
	    		if (ImageArray2[x][y][1]<0) { ImageArray2[x][y][1] = 0; }
	    		if (ImageArray2[x][y][2]<0) { ImageArray2[x][y][2] = 0; }
	    		if (ImageArray2[x][y][3]<0) { ImageArray2[x][y][3] = 0; }
	    		if (ImageArray2[x][y][1]>255) { ImageArray2[x][y][1] = 255; }
	    		if (ImageArray2[x][y][2]>255) { ImageArray2[x][y][2] = 255; }
	    		if (ImageArray2[x][y][3]>255) { ImageArray2[x][y][3] = 255; }
		    }
	    }
        //return findShiftAndScale(ImageArray2);
    	return Demo.convertToBimage(ImageArray2);
    }
    
    
    /*
     * Subtract method
     */
    public static BufferedImage Subtraction(BufferedImage originalImage, BufferedImage image2){
    	int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[][][] ImageArray1 = Demo.convertToArray(originalImage);//image to array
        int[][][] ImageArray2 = Demo.convertToArray(image2);  
    	
        for(int y=0; y<height; y++){
	    	for(int x=0; x<width; x++){
		    	ImageArray2[x][y][1] = Math.abs(ImageArray1[x][y][1] - ImageArray2[x][y][1]);
		    	ImageArray2[x][y][2] = Math.abs(ImageArray1[x][y][2] - ImageArray2[x][y][2]);
		    	ImageArray2[x][y][3] = Math.abs(ImageArray1[x][y][3] - ImageArray2[x][y][3]);
	    		if (ImageArray2[x][y][1]<0) { ImageArray2[x][y][1] = 0; }
	    		if (ImageArray2[x][y][2]<0) { ImageArray2[x][y][2] = 0; }
	    		if (ImageArray2[x][y][3]<0) { ImageArray2[x][y][3] = 0; }
	    		if (ImageArray2[x][y][1]>255) { ImageArray2[x][y][1] = 255; }
	    		if (ImageArray2[x][y][2]>255) { ImageArray2[x][y][2] = 255; }
	    		if (ImageArray2[x][y][3]>255) { ImageArray2[x][y][3] = 255; }
	    		ImageArray2[x][y][1] = (ImageArray2[x][y][1] + 255)/2;
		    	ImageArray2[x][y][2] = (ImageArray2[x][y][2] + 255)/2;
		    	ImageArray2[x][y][3] = (ImageArray2[x][y][3] + 255)/2;
	    		
	    	}
	    }
    	return Demo.convertToBimage(ImageArray2);
    }
    
    /* Multiply method */
    
    public static BufferedImage Multiplication(BufferedImage originalImage, BufferedImage image2) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		int[][][] ImageArray1 = Demo.convertToArray(originalImage); // Convert the image to array
		int[][][] ImageArray2 = Demo.convertToArray(image2);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ImageArray2[x][y][1] = ImageArray1[x][y][1] * ImageArray2[x][y][1];
				ImageArray2[x][y][2] = ImageArray1[x][y][2] * ImageArray2[x][y][2];
				ImageArray2[x][y][3] = ImageArray1[x][y][3] * ImageArray2[x][y][3];
			}
		}
		return Demo.findShiftAndScale(ImageArray2);
	}
    
    public static BufferedImage Division(BufferedImage originalImage, BufferedImage image2) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		int[][][] ImageArray1 = Demo.convertToArray(originalImage); // Convert the image to array
		int[][][] ImageArray2 = Demo.convertToArray(image2);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(ImageArray2[x][y][1]==0) {ImageArray2[x][y][1] = ImageArray1[x][y][1];} else {ImageArray2[x][y][1] = ImageArray1[x][y][1] / ImageArray2[x][y][1];}
				if(ImageArray2[x][y][2]==0) {ImageArray2[x][y][2] = ImageArray1[x][y][2];} else {ImageArray2[x][y][2] = ImageArray1[x][y][2] / ImageArray2[x][y][2];}
				if(ImageArray2[x][y][3]==0) {ImageArray2[x][y][3] = ImageArray1[x][y][3];} else {ImageArray2[x][y][3] = ImageArray1[x][y][3] / ImageArray2[x][y][3];}
			}
		}
		return Demo.findShiftAndScale(ImageArray2);
	}

    // Nots the image 
    // From 11 on lecture 3 - same as negative
    // Logical NOT or invert is an operator which takes a binary or gray level
    // image as input and produces its photographic negative, i.e. 
    // dark areas in the input image become light and light areas become dark.
  	public static BufferedImage NOT(BufferedImage originalImage) {
  		int[][][] ImageArray1 = Demo.convertToArray(originalImage); // Convert the image to array
  		int[][][] ImageArray2 = Demo.convertToArray(originalImage);
  		int r, b, g;
  		for (int y = 0; y < originalImage.getHeight(); y++) {
  			for (int x = 0; x < originalImage.getWidth(); x++) {
  				r = ImageArray1[x][y][1]; // r
  				g = ImageArray1[x][y][2]; // g
  				b = ImageArray1[x][y][3]; // b
  				ImageArray2[x][y][1] = (~r) & 0xFF; // r
  				ImageArray2[x][y][2] = (~g) & 0xFF; // g
  				ImageArray2[x][y][3] = (~b) & 0xFF; // b
  			}
  		}
  		return Demo.convertToBimage(ImageArray2);
  	}

  	public static BufferedImage AND(BufferedImage originalImage, BufferedImage image2) {
    	int[][][] ImageArray1 = Demo.convertToArray(originalImage); //image to array
        int[][][] ImageArray2 = Demo.convertToArray(image2);
    	int r, b, g;
        for(int y=0; y<originalImage.getHeight(); y++){
    		for(int x=0; x<originalImage.getWidth(); x++){
	    		r = ImageArray1[x][y][1]  & ImageArray2[x][y][1]; //r
	    		g = ImageArray1[x][y][2]  & ImageArray2[x][y][2]; //g
	    		b = ImageArray1[x][y][3]  & ImageArray2[x][y][3]; //b
	    		ImageArray2[x][y][1] = r; //r
	    		ImageArray2[x][y][2] = g; //g
	    		ImageArray2[x][y][3] = b; //b
	    	}
    	}
        return Demo.convertToBimage(ImageArray2);
    }
  	
  	public static BufferedImage OR(BufferedImage originalImage, BufferedImage image2) {
    	int[][][] ImageArray1 = Demo.convertToArray(originalImage);// image to array
        int[][][] ImageArray2 = Demo.convertToArray(image2);
    	int r, b, g;
        for(int y=0; y<originalImage.getHeight(); y++){
    		for(int x=0; x < originalImage.getWidth(); x++){
	    		r = ImageArray1[x][y][1] | ImageArray2[x][y][1]; //r
	    		g = ImageArray1[x][y][2] | ImageArray2[x][y][2]; //g
	    		b = ImageArray1[x][y][3] | ImageArray2[x][y][3]; //b
	    		ImageArray2[x][y][1] = r; //r
	    		ImageArray2[x][y][2] = g; //g
	    		ImageArray2[x][y][3] = b; //b
	    	}
    	}
        return Demo.convertToBimage(ImageArray2);
    }
  	
  	public static BufferedImage XOR(BufferedImage originalImage, BufferedImage image2) {
    	int[][][] ImageArray1 = Demo.convertToArray(originalImage);// image to array
        int[][][] ImageArray2 = Demo.convertToArray(image2);
    	int r, b, g;
        for(int y=0; y<originalImage.getHeight(); y++){
    		for(int x=0; x<originalImage.getWidth(); x++){
	    		r = ImageArray1[x][y][1] ^ ImageArray2[x][y][1]; //r
	    		g = ImageArray1[x][y][2] ^ ImageArray2[x][y][2]; //g
	    		b = ImageArray1[x][y][3] ^ ImageArray2[x][y][3]; //b
	    		ImageArray2[x][y][1] = r; //r
	    		ImageArray2[x][y][2] = g; //g
	    		ImageArray2[x][y][3] = b; //b
	    	}
    	}
        return Demo.convertToBimage(ImageArray2);
    }
  	
  	public static BufferedImage roiAND(BufferedImage originalImage, BufferedImage image2) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage); // Convert the image to array
		int[][][] ImageArray2 = Demo.convertToArray(image2);
		int r, b, g;
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				r = ImageArray1[x][y][1] & ImageArray2[x][y][1]; // r
				g = ImageArray1[x][y][2] & ImageArray2[x][y][2]; // g
				b = ImageArray1[x][y][3] & ImageArray2[x][y][3]; // b
				ImageArray2[x][y][1] = r; // r
				ImageArray2[x][y][2] = g; // g
				ImageArray2[x][y][3] = b; // b
			}
		}
		return Demo.convertToBimage(ImageArray2);
	}
  	
  	public static BufferedImage roiMultiplication(BufferedImage originalImage, BufferedImage image2) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage); // Convert the image to array
		int[][][] ImageArray2 = Demo.convertToArray(image2);
		int r, b, g;
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				if (ImageArray2[x][y][1] == 255) {
					r = ImageArray1[x][y][1];
				} else {
					r = 0;
				} // r
				if (ImageArray2[x][y][2] == 255) {
					g = ImageArray1[x][y][2];
				} else {
					g = 0;
				} // g
				if (ImageArray2[x][y][3] == 255) {
					b = ImageArray1[x][y][3];
				} else {
					b = 0;
				} // b
				ImageArray2[x][y][1] = r; // r
				ImageArray2[x][y][2] = g; // g
				ImageArray2[x][y][3] = b; // b
			}
		}

		return Demo.convertToBimage(ImageArray2);
	}
  	
  	public static BufferedImage roiNOT(BufferedImage originalImage, BufferedImage image2) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage); // Convert the image to array
		int[][][] ImageArray2 = Demo.convertToArray(NOT(image2));
		int r, b, g;
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				r = ImageArray1[x][y][1] & ImageArray2[x][y][1]; // r
				g = ImageArray1[x][y][2] & ImageArray2[x][y][2]; // g
				b = ImageArray1[x][y][3] & ImageArray2[x][y][3]; // b
				ImageArray2[x][y][1] = r; // r
				ImageArray2[x][y][2] = g; // g
				ImageArray2[x][y][3] = b; // b
			}
		}
		return Demo.convertToBimage(ImageArray2);
	}
}
