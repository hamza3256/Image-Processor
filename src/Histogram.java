import java.awt.image.BufferedImage;

/**
 * What happens if we apply histogram equalisation twice to the same image? - They are the same
 * Are the transforms the same if we apply histogram equalisation to different regions of interest (ROI) in an image?
 */

public class Histogram {
	
    private static double[]  HistR = new double[256];
    private static double[]  HistG = new double[256];
    private static double[]  HistB = new double[256];
	
	/**
	 * Exercise 1
	 * To find the histogram of an image by counting the numbers of the pixel values in the image.
	 * @param originalImage
	 */
	public static void createHistogram(BufferedImage originalImage) {
    	int[][][] ImageArray1 = Demo.convertToArray(originalImage); 
    	for(int k=0; k<=255; k++){ // Initialisation
    		HistR[k] = 0;
    		HistG[k] = 0;
    		HistB[k] = 0;
    	}
    	int r, b, g;
    	for(int y=0; y<originalImage.getHeight(); y++){
    		for(int x=0; x<originalImage.getWidth(); x++){
    			r = ImageArray1[x][y][1]; //r
    			g = ImageArray1[x][y][2]; //g
    			b = ImageArray1[x][y][3]; //b
    			HistR[r]++;
    			HistG[g]++;
    			HistB[b]++;
    		}
    	}
    }

	private static double[] HistRNorm = new double[256];
	private static double[] HistGNorm = new double[256];
	private static double[] HistBNorm = new double[256];

	/**
	 * Exercise 2
	 * To normalise a histogram by dividing by the number of the counted pixels.
	 *
	 */
	public static void histogramNormalisation(BufferedImage originalImage) {
		createHistogram(originalImage);

		int w = originalImage.getWidth();
		int h = originalImage.getHeight();

		for (int k = 0; k <= 255; k++) {
			HistRNorm[k] = HistR[k] / (w * h);
			HistGNorm[k] = HistG[k] / (w * h);
			HistBNorm[k] = HistB[k] / (w * h);
		}
	}

	private static double[] HistREq = new double[256];
	private static double[] HistGEq = new double[256];
	private static double[] HistBEq = new double[256];

	/**
	 * Exercise 3
	 * To equalise a histogram of an image and to find the transform (corresponding gray levels for mapping)
	 * for histogram equalisation and then apply the transform to the image.
	 *
	 */
	//Equalises histogram and then uses equalised histogram to display equalised image
	public static BufferedImage histogramEqualisation(BufferedImage originalImage) {
		histogramNormalisation(originalImage);
		HistREq[0] = HistRNorm[0];
		HistGEq[0] = HistGNorm[0];
		HistBEq[0] = HistBNorm[0];

		for (int k = 1; k <= 255; k++) { // Initialisation
			HistREq[k] = HistREq[k - 1] + HistRNorm[k];
			HistGEq[k] = HistGEq[k - 1] + HistGNorm[k];
			HistBEq[k] = HistBEq[k - 1] + HistBNorm[k];
		}
		for (int k = 0; k <= 255; k++) {
			HistREq[k] = Math.round(HistREq[k] * 255);
			HistGEq[k] = Math.round(HistGEq[k] * 255);
			HistBEq[k] = Math.round(HistBEq[k] * 255);
		}
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				ImageArray1[x][y][1] = (int) HistREq[ImageArray1[x][y][1]]; // r
				ImageArray1[x][y][2] = (int) HistGEq[ImageArray1[x][y][2]]; // g
				ImageArray1[x][y][3] = (int) HistBEq[ImageArray1[x][y][3]]; // b
			}
		}
		return Demo.convertToBimage(ImageArray1);
	}
}
