import java.awt.image.BufferedImage;

/*
 * What�s the difference between shifting and absolute value conversion? 
 * In what cases, the absolute value conversion is needed after convolution? 
 */
public class LinearFiltering {
	
	private static float[][] Mask = new float[3][3];
	private static float[][] aMask = { { 1f, 1f, 1f }, { 1f, 1f, 1f }, { 1f, 1f, 1f } };
	private static float[][] wAMask = { { 1f, 2f, 1f }, { 2f, 4f, 2f }, { 1f, 2f, 1f } };
	private static float[][] fourNLMask = { { 0f, -1f, 0f }, { -1f, 4f, -1f }, { 0f, -1f, 0f } };
	private static float[][] eightNLMask = { { -1f, -1f, -1f }, { -1f, 8f, -1f }, { -1f, -1f, -1f } };
	private static float[][] fourNLEnhancedMask = { { 0f, -1f, 0f }, { -1f, 5f, -1f }, { 0f, -1f, 0f } };
	private static float[][] eightNLEnhancedMask = { { -1f, -1f, -1f }, { -1f, 9f, -1f }, { -1f, -1f, -1f } };
	private static float[][] robertsAMask = { { 0f, 0f, 0f }, { 0f, 0f, -1f }, { 0f, 1f, 0f } };
	private static float[][] robertsBMask = { { 0f, 0f, 0f }, { 0f, -1f, 0f }, { 0f, 0f, 1f } };
	private static float[][] sobelXMask = { { -1f, 0f, 1f }, { -2f, 0f, 2f }, { -1f, 0f, 1f } };
	private static float[][] sobelYMask = { { -1f, -2f, -1f }, { 0f, 0f, 0f }, { 1f, 2f, 1f } };
	
	public static BufferedImage average(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		Mask = aMask;
		float total = 0;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				total = total + Mask[row][col];
			}
		}
		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r/total)); // g
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g/total)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b/total)); // b
			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}
	
	public static BufferedImage weightedAvg(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		Mask = wAMask;
		float total = 0;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				total = total + Mask[row][col];
			}
		}
		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r/total)); // g
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g/total)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b/total)); // b
			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}
	
	public static BufferedImage fourNL(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);
		Mask = fourNLMask;

		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}		
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r)); // g
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b)); // b
			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}
	
	public static BufferedImage eightNL(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		Mask = eightNLMask;

		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r)); // r
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b)); // b
			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}
	
	public static BufferedImage fourNLEnhanced(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		Mask = fourNLEnhancedMask;

		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r)); // r
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b)); // b

			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}

	public static BufferedImage eightNLEnhanced(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		Mask = eightNLEnhancedMask;

		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r)); // r
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b)); // b
			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}

	public static BufferedImage robertsA(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		Mask = robertsAMask;

		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r)); // r
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b)); // b
			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}

	public static BufferedImage robertsB(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		Mask = robertsBMask;

		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r)); // r
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b)); // b
			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}

	public static BufferedImage sobelX(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		Mask = sobelXMask;

		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r)); // r
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b)); // b
			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}

	public static BufferedImage sobelY(BufferedImage originalImage) {
		int[][][] ImageArray1 = Demo.convertToArray(originalImage);
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		Mask = sobelYMask;

		float r, g, b;
		for (int y = 1; y < originalImage.getHeight() - 1; y++) {
			for (int x = 1; x < originalImage.getWidth() - 1; x++) {
				r = 0;
				g = 0;
				b = 0;
				for (int s = -1; s <= 1; s++) {
					for (int t = -1; t <= 1; t++) {
						r = r + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][1]; // r
						g = g + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][2]; // g
						b = b + Mask[1 - s][1 - t] * ImageArray1[x + s][y + t][3]; // b
					}

				}
				ImageArray2[x][y][1] = (int) Math.round(Math.abs(r)); // r
				ImageArray2[x][y][2] = (int) Math.round(Math.abs(g)); // g
				ImageArray2[x][y][3] = (int) Math.round(Math.abs(b)); // b
			}
		}
		return Demo.findShiftAndScale((ImageArray2));
	}
}