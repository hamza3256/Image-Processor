import java.awt.image.BufferedImage;

/*
 * Questions: 
 * 
 * What we need do if c is 1 in the logarithmic function and power law? 
 * For c=1, what power makes power law similar to logarithm function for image processing?
 * How can we find c automatically if we wish to map 0 to 0 and 255 to 255 after transform? 
 */
public class PointProcessing {

	// Look up tables
	private static int[] LUT = new int[256];

	public static BufferedImage negLinearTransform(BufferedImage originalImage) {
		for (int k = 0; k <= 255; k++) {
			LUT[k] = 256 - 1 - k; // <--- main stuff here -> Equation
		}
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);
		int r, b, g;
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				r = ImageArray1[x][y][1]; // r
				g = ImageArray1[x][y][2]; // g
				b = ImageArray1[x][y][3]; // b
				ImageArray2[x][y][1] = LUT[r]; // r
				ImageArray2[x][y][2] = LUT[g]; // g
				ImageArray2[x][y][3] = LUT[b]; // b
			}
		}
		return Demo.convertToBimage(ImageArray2);
	}

	public static BufferedImage logFunction(BufferedImage originalImage) {
		for (int k = 0; k <= 255; k++) {
			LUT[k] = (int) (Math.log(1 + k) * 255 / Math.log(256)); // <--- main stuff here -> Equation
		}
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);
		int r, b, g;
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				r = ImageArray1[x][y][1]; // r
				g = ImageArray1[x][y][2]; // g
				b = ImageArray1[x][y][3]; // b
				ImageArray2[x][y][1] = LUT[r]; // r
				ImageArray2[x][y][2] = LUT[g]; // g
				ImageArray2[x][y][3] = LUT[b]; // b
			}
		}
		return Demo.convertToBimage(ImageArray2);
	}

	public static BufferedImage powerLaw(BufferedImage originalImage, float p) {
		for (int k = 0; k <= 255; k++) {
			LUT[k] = (int) (Math.pow(255, 1 - p) * Math.pow(k, p)); // <--- main stuff here -> Equation
		}
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);
		int r, b, g;
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				r = ImageArray1[x][y][1]; // r
				g = ImageArray1[x][y][2]; // g
				b = ImageArray1[x][y][3]; // b
				ImageArray2[x][y][1] = LUT[r]; // r
				ImageArray2[x][y][2] = LUT[g]; // g
				ImageArray2[x][y][3] = LUT[b]; // b
			}
		}
		return Demo.convertToBimage(ImageArray2);
	}

	public static BufferedImage randomLUT(BufferedImage originalImage) {
		for (int k = 0; k <= 255; k++) {
			LUT[k] = (int) (Math.random() * 256);
		}
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);
		int r, b, g;
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				r = ImageArray1[x][y][1]; // r
				g = ImageArray1[x][y][2]; // g
				b = ImageArray1[x][y][3]; // b
				ImageArray2[x][y][1] = LUT[r]; // r
				ImageArray2[x][y][2] = LUT[g]; // g
				ImageArray2[x][y][3] = LUT[b]; // b
			}
		}
		return Demo.convertToBimage(ImageArray2);
	}

	public static BufferedImage bitplaneSlicing(BufferedImage originalImage, int k) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);
		int r, b, g;
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				r = ImageArray1[x][y][1]; // r
				g = ImageArray1[x][y][2]; // g
				b = ImageArray1[x][y][3]; // b
				ImageArray2[x][y][1] = (r >> k) & 1; // r
				ImageArray2[x][y][2] = (g >> k) & 1; // g
				ImageArray2[x][y][3] = (b >> k) & 1; // b
				
				// Binary so value is 0 or 1 so make 1 = 255
				if (ImageArray2[x][y][1] == 1) {
					ImageArray2[x][y][1] = 255;
				}
				if (ImageArray2[x][y][2] == 1) {
					ImageArray2[x][y][2] = 255;
				}
				if (ImageArray2[x][y][3] == 1) {
					ImageArray2[x][y][3] = 255;
				}
			}
		}
		return Demo.convertToBimage(ImageArray2);
	}

}
