import java.awt.image.BufferedImage;
import java.util.Arrays;

public class OrderStatisticFiltering {
	public static BufferedImage saltPepper(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[][][] ImageArray2 = Demo.convertToArray(originalImage);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double random = Math.random() * 1;

                if (random < 0.05) {
                    ImageArray2[x][y][0] = 255;
                    ImageArray2[x][y][1] = 0;
                    ImageArray2[x][y][2] = 0;
                    ImageArray2[x][y][3] = 0;
                } else if (random > 0.95) {
                    ImageArray2[x][y][0] = 255;
                    ImageArray2[x][y][1] = 255;
                    ImageArray2[x][y][2] = 255;
                    ImageArray2[x][y][3] = 255;
                }
            }
        }
        return Demo.convertToBimage(ImageArray2);
    }
	
	public static BufferedImage minFiltering(BufferedImage originalImage) {
    	int[][][] ImageArray1 = Demo.convertToArray(originalImage); 
    	int[][][] ImageArray2 = Demo.convertToArray(originalImage); 
    	int k;
    	int[] rWindow = new int[9];
    	int[] gWindow = new int[9];
    	int[] bWindow = new int[9];
    	for(int y=1; y<originalImage.getHeight()-1; y++){
    		for(int x=1; x<originalImage.getWidth()-1; x++){
	    		k = 0;
	    		for(int s=-1; s<=1; s++){
		    		for(int t=-1; t<=1; t++){
			    		rWindow[k] = ImageArray1[x+s][y+t][1]; //r
			    		gWindow[k] = ImageArray1[x+s][y+t][2]; //g
			    		bWindow[k] = ImageArray1[x+s][y+t][3]; //b
			    		k++;
		    		}
	    		}
	    		Arrays.sort(rWindow);
	    		Arrays.sort(gWindow);
	    		Arrays.sort(bWindow);
	    		ImageArray2[x][y][1] = rWindow[0]; //r
	    		ImageArray2[x][y][2] = gWindow[0]; //g
	    		ImageArray2[x][y][3] = bWindow[0]; //b
    		}
    	}
    	return Demo.convertToBimage(ImageArray2);
    }
	
	public static BufferedImage maxFiltering(BufferedImage originalImage) {
    	int[][][] ImageArray1 = Demo.convertToArray(originalImage); 
    	int[][][] ImageArray2 = Demo.convertToArray(originalImage); 
    	int k;
    	//3x3
    	int[] rWindow = new int[9];
    	int[] gWindow = new int[9];
    	int[] bWindow = new int[9];
    	for(int y=1; y<originalImage.getHeight()-1; y++){
    		for(int x=1; x<originalImage.getWidth()-1; x++){
	    		k = 0;
	    		for(int s=-1; s<=1; s++){
		    		for(int t=-1; t<=1; t++){
			    		rWindow[k] = ImageArray1[x+s][y+t][1]; //r
			    		gWindow[k] = ImageArray1[x+s][y+t][2]; //g
			    		bWindow[k] = ImageArray1[x+s][y+t][3]; //b
			    		k++;
		    		}
	    		}
	    		Arrays.sort(rWindow);
	    		Arrays.sort(gWindow);
	    		Arrays.sort(bWindow);
	    		ImageArray2[x][y][1] = rWindow[8]; //r
	    		ImageArray2[x][y][2] = gWindow[8]; //g
	    		ImageArray2[x][y][3] = bWindow[8]; //b
    		}
    	}
    	return Demo.convertToBimage(ImageArray2);
    }

    public static BufferedImage midpointFilter(BufferedImage originalImage) {
    	int[][][] ImageArray1 = Demo.convertToArray(originalImage);
    	int[][][] ImageArray2 = Demo.convertToArray(originalImage); 
    	int k;
    	//3x3
    	int[] rWindow = new int[9];
    	int[] gWindow = new int[9];
    	int[] bWindow = new int[9];
    	for(int y=1; y<originalImage.getHeight()-1; y++){
    		for(int x=1; x<originalImage.getWidth()-1; x++){
	    		k = 0;
	    		for(int s=-1; s<=1; s++){
		    		for(int t=-1; t<=1; t++){
			    		rWindow[k] = ImageArray1[x+s][y+t][1]; //r
			    		gWindow[k] = ImageArray1[x+s][y+t][2]; //g
			    		bWindow[k] = ImageArray1[x+s][y+t][3]; //b
			    		k++;
		    		}
	    		}
	    		Arrays.sort(rWindow);
	    		Arrays.sort(gWindow);
	    		Arrays.sort(bWindow);
	    		ImageArray2[x][y][1] = (rWindow[0] + rWindow[8])/2; //r
	    		ImageArray2[x][y][2] = (gWindow[0] + gWindow[8])/2; //g
	    		ImageArray2[x][y][3] = (bWindow[0] + bWindow[8])/2; //b
    		}
    	}
    	return Demo.convertToBimage(ImageArray2);
    }
    
    public static BufferedImage medianFiltering(BufferedImage originalImage) {
    	int[][][] ImageArray1 = Demo.convertToArray(originalImage); 
    	int[][][] ImageArray2 = Demo.convertToArray(originalImage); 
    	int k;
    	int[] rWindow = new int[9];
    	int[] gWindow = new int[9];
    	int[] bWindow = new int[9];
    	for(int y=1; y<originalImage.getHeight()-1; y++){
    		for(int x=1; x<originalImage.getWidth()-1; x++){
	    		k = 0;
	    		for(int s=-1; s<=1; s++){
		    		for(int t=-1; t<=1; t++){
			    		rWindow[k] = ImageArray1[x+s][y+t][1]; //r
			    		gWindow[k] = ImageArray1[x+s][y+t][2]; //g
			    		bWindow[k] = ImageArray1[x+s][y+t][3]; //b
			    		k++;
		    		}
	    		}
	    		Arrays.sort(rWindow);
	    		Arrays.sort(gWindow);
	    		Arrays.sort(bWindow);
	    		ImageArray2[x][y][1] = rWindow[4]; //r
	    		ImageArray2[x][y][2] = gWindow[4]; //g
	    		ImageArray2[x][y][3] = bWindow[4]; //b
    		}
    	}
    	return Demo.convertToBimage(ImageArray2);
    }
}
