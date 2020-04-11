import java.awt.image.BufferedImage;
/**
 * When the �optimal� thresholding works? 
 * How can the edges be detected by thresholding? 
 * @author Corsair
 */
public class Thresholding {
	
	//Mean and Standard Deviation
	private static float rMean, gMean, bMean;
	private static float rSD, gSD, bSD;
	
	private static double[] HistNormR = new double[256];
	private static double[] HistNormG = new double[256];
	private static double[] HistNormB = new double[256];
    
	public static void Mean(BufferedImage originalImage) {
		Histogram.histogramNormalisation(originalImage);
		rMean = 0;
		gMean = 0;
		bMean = 0;
		// Cumulaive
		for (int k = 0; k <= 255; k++) { // Initialisation
			rMean = (float) (rMean + (k * HistNormR[k]));
			gMean = (float) (gMean + (k * HistNormG[k]));
			bMean = (float) (bMean + (k * HistNormB[k]));
		}
		// rMean = rMean/255;
		// gMean = gMean/255;
		// bMean = bMean/255;
		System.out.println(rMean + " " + gMean + " " + bMean);
	}
	
	public static void standardDeviation(BufferedImage originalImage) {
		Mean(originalImage);
		// Cumulative
		rSD = 0;
		gSD = 0;
		bSD = 0;
		for (int k = 0; k <= 255; k++) { // Initialisation
			rSD = (float) (rSD + (((k - rMean) * (k - rMean)) * HistNormR[k]));
			gSD = (float) (gSD + (((k - gMean) * (k - gMean)) * HistNormG[k]));
			bSD = (float) (bSD + (((k - bMean) * (k - bMean)) * HistNormB[k]));
		}
		rSD = (float) Math.sqrt(rSD);
		gSD = (float) Math.sqrt(gSD);
		bSD = (float) Math.sqrt(bSD);
		System.out.println(rSD + " " + gSD + " " + bSD);
	}
	
	public static BufferedImage simpleThresholding(BufferedImage originalImage, int threshold) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);

		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				if (ImageArray1[x][y][1] <= threshold || ImageArray1[x][y][2] <= threshold
						|| ImageArray1[x][y][3] <= threshold) {
					ImageArray1[x][y][1] = 0;
					ImageArray1[x][y][2] = 0;
					ImageArray1[x][y][3] = 0;
				} else {
					ImageArray1[x][y][1] = 255;
					ImageArray1[x][y][2] = 255;
					ImageArray1[x][y][3] = 255;
				}
			}
		}
		return Demo.convertToBimage(ImageArray1);
	}
	
	public static BufferedImage automatedThresholding(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);
		float backMean, objMean, backSum, objSum;
		backSum = 0;
		objSum = 0;
		
		int h = originalImage.getHeight();
		int w = originalImage.getWidth();
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (x == 0 && y == 0) {
					backSum = backSum + ((ImageArray1[x][y][1] + ImageArray1[x][y][2] + ImageArray1[x][y][3]) / 3);
				} else if (x == 0 && y == (h - 1)) {
					backSum = backSum + ((ImageArray1[x][y][1] + ImageArray1[x][y][2] + ImageArray1[x][y][3]) / 3);
				} else if (x == (w - 1) && y == 0) {
					backSum = backSum + ((ImageArray1[x][y][1] + ImageArray1[x][y][2] + ImageArray1[x][y][3]) / 3);
				} else if (x == (w - 1) && y == (h - 1)) {
					backSum = backSum + ((ImageArray1[x][y][1] + ImageArray1[x][y][2] + ImageArray1[x][y][3]) / 3);
				} else {
					objSum = objSum + ((ImageArray1[x][y][1] + ImageArray1[x][y][2] + ImageArray1[x][y][3]) / 3);
				}
			}
		}
		backMean = backSum / 4;
		objMean = objSum / ((w * h) - 4);
		int initialThreshold = (int) ((backMean + objMean) / 2);
		return automatedHelper(originalImage, initialThreshold);
	}
	
	public static BufferedImage automatedHelper(BufferedImage originalImage, int curThreshold) {

		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		float backMean, objMean, backSum, objSum, backCount, objCount;
		backSum = 0;
		objSum = 0;
		backCount = 0;
		objCount = 0;
		
		int h = originalImage.getHeight();
		int w = originalImage.getWidth();
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (ImageArray1[x][y][1] <= curThreshold || ImageArray1[x][y][2] <= curThreshold
						|| ImageArray1[x][y][3] <= curThreshold) {
					objSum = objSum + ((ImageArray1[x][y][1] + ImageArray1[x][y][2] + ImageArray1[x][y][3]) / 3);
					objCount++;
				} else {
					backSum = backSum + ((ImageArray1[x][y][1] + ImageArray1[x][y][2] + ImageArray1[x][y][3]) / 3);
					backCount++;
				}
			}
		}
		backMean = backSum / backCount;
		objMean = objSum / objCount;
		int nextThreshold = (int) ((backMean + objMean) / 2);
		// Till threshold doesnt change
		if (nextThreshold - curThreshold <= 0) {
			//System.out.println(curThreshold);
			return simpleThresholding(originalImage, curThreshold);
		} else {
			//System.out.println(curThreshold);
			return automatedHelper(originalImage, nextThreshold);
		}

	}
}
