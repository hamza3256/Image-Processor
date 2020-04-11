import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Demo extends Component implements ActionListener {


	// ************************************
	// List of the options(Original, Negative); correspond to the cases:
	// ************************************

	String descs[] = { "Original", "Negative",

			"Image Pixel Value Re-Scaling", "Image Pixel Value Shifting", "Image Pixel Value Shifting and Re-Scaling",
			"Addition", "Subtraction", "Multiplication", "Division",
			"NOT",
			"AND", "OR", "XOR",
			"ROI AND", "ROI MULTIPLY", "ROI NOT",
			"Negative Linear Transform",
			"Logarithmic Function",
			"Power-Law",
			"Random Look-up Table",
			"Bit-Plane Slicing",
			"Histogram Equalization",
			"Averaging", "Weighted Averaging", "4-Neighbour Laplacian", "8-Neighbour Laplacian",
			"4-Neighbour Laplacian Enhancement", "8-Neighbour Laplacian Enhancement", "Roberts 1", "Roberts 2",
			"Sobel X", "Sobel Y",
			"Salt-and-Pepper Noise",
			"Min Filter",
			"Max Filter",
			"Midpoint Filter",
			"Median Filter",
			"Mean and Standard Deviation",
			"Simple thresholding",
			"Automated Thresholding"
	};

	int opIndex; // option index for
	int lastOp;
	public static BufferedImage biFiltered, bi; // the input image saved as bi;//
	BufferedImage bi3;
	BufferedImage roi;

	int w, h;

	public static List<BufferedImage> imageList;

	private static JButton addButton, saveButton, removeButton, undoButton;
	private static JComboBox formats;

	JFileChooser jfc;



	public Demo() {
		jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir") + System.getProperty("file.separator") +"/images"));

		imageList = new ArrayList<BufferedImage>();

		//mem = new Stack();
		try {

			// Load bi (file1)
			File f = new File("images\\BaboonRGB.bmp");
			System.out.println("File we are trying to load: " + f);
			System.out.println(f.canRead());
			System.out.println(f.getAbsolutePath());
			bi = ImageIO.read(f);
			System.out.println("ImageIO file returned: " + bi);
			w = bi.getWidth(null);
			h = bi.getHeight(null);
			System.out.println(bi.getType());

			// Load bi3 (file2)
			bi3 = ImageIO.read(new File("images\\LenaRGB.bmp"));

			roi = ImageIO.read(new File("images\\roi.bmp"));

			if (bi.getType() != BufferedImage.TYPE_INT_RGB) {
				BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics big = bi2.getGraphics();
				big.drawImage(bi, 0, 0, null);
//				big.drawImage(bi3, 0, h, null);
//				big.drawImage(roi, w, h, null);
				biFiltered = bi = bi2;
			}
		} catch (IOException e) { // deal with the situation that th image has problem;/
			System.out.println(e.getMessage());
			System.out.println("Image could not be read");

			System.exit(1);
		}


	}
	public BufferedImage rawToBmp(String pt) throws IOException {
		Path fl= Paths.get(pt);
		byte[] bytes = Files.readAllBytes(fl);
		int guessX = 0, guessY = 0, diff = bytes.length;

		//guess height and width by the 2 closest factors
		for (int i = 2; i < bytes.length; i++) {
			if (bytes.length % i == 0) {
				int x = bytes.length / i;
				if (i > x) {
					break;
				}

				if (Math.abs(x - i) < diff) {
					diff = x - i;
					guessX = i;
					guessY = x;
				}
			}
		}
		//create array of x, y, a, r, g, b values
		int[][][] result = new int[guessX][guessY][4];
		int x = 0;
		int y = 0;
		for (int i = 0; i < bytes.length; i++) {
			if (x == guessX) {
				x = 0;
				y++;
			}
			int grey = bytes[i];
			result[x][y][0] = 255;
			result[x][y][1] = grey;
			result[x][y][2] = grey;
			result[x][y][3] = grey;
			x++;
		}

		//convert bytes to buffered image(bmp)
		BufferedImage tmpimg = new BufferedImage(guessX, guessY, BufferedImage.TYPE_USHORT_GRAY);
		for (int y1 = 0; y1 < guessX; y1++) {
			for (int x1 = 0; x1 < guessY; x1++) {
				int a = 255;
				int r = result[x1][y1][1];
				int g = result[x1][y1][2];
				int b = result[x1][y1][3];

				//set RGB value
				int p = (a << 24) | (r << 16) | (g << 8) | b;
				tmpimg.setRGB(x1, y1, p);

			}
		}
		return tmpimg;
	}


	public Dimension getPreferredSize() {
		return new Dimension(w, h);
	}

	String[] getDescriptions() {
		return descs;
	}

	// Return the formats sorted alphabetically and in lower case
	public String[] getFormats() {
		String[] formats = { "bmp", "gif", "jpeg", "jpg", "png" };
		TreeSet<String> formatSet = new TreeSet<String>();
		for (String s : formats) {
			formatSet.add(s.toLowerCase());
		}
		return formatSet.toArray(new String[0]);
	}

	void setOpIndex(int i) {
		opIndex = i;
	}


	public void paint(Graphics g) { // Repaint calls this function so the image will change.
		filterImage();
		g.drawImage(biFiltered, 0, 0, null);
		g.drawImage(biFiltered, biFiltered.getWidth(), 0, null);
		if (!imageList.isEmpty()) {
			g.drawImage(imageList.get(imageList.size()-1), biFiltered.getWidth(), 0, null);
		} else {
			imageList.add(biFiltered);
		}

	}

	// ************************************
	// Convert the Buffered Image to Array
	// ************************************
	public static int[][][] convertToArray(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		int[][][] result = new int[width][height][4];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = image.getRGB(x, y);
				int a = (p >> 24) & 0xff;
				int r = (p >> 16) & 0xff;
				int g = (p >> 8) & 0xff;
				int b = p & 0xff;

				result[x][y][0] = a;
				result[x][y][1] = r;
				result[x][y][2] = g;
				result[x][y][3] = b;
			}
		}
		return result;
	}

	// ************************************
	// Convert the Array to BufferedImage
	// ************************************
	public static BufferedImage convertToBimage(int[][][] TmpArray) {

		int width = TmpArray.length;
		int height = TmpArray[0].length;

		BufferedImage tmpimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int a = TmpArray[x][y][0];
				int r = TmpArray[x][y][1];
				int g = TmpArray[x][y][2];
				int b = TmpArray[x][y][3];

				int p = (a << 24) | (r << 16) | (g << 8) | b;
				tmpimg.setRGB(x, y, p);

			}
		}
		return tmpimg;
	}

	// ************************************
	// Example: Image Negative
	// ************************************
	public static BufferedImage ImageNegative(BufferedImage timg) {
		int width = timg.getWidth();
		int height = timg.getHeight();

		int[][][] ImageArray = convertToArray(timg); // Convert the image to array

		// Image Negative Operation:
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ImageArray[x][y][1] = 255 - ImageArray[x][y][1]; // r
				ImageArray[x][y][2] = 255 - ImageArray[x][y][2]; // g
				ImageArray[x][y][3] = 255 - ImageArray[x][y][3]; // b
			}
		}

		return convertToBimage(ImageArray); // Convert the array to BufferedImage
	}

	// ************************************
	// Your turn now: Add more function below
	// ************************************

	public static BufferedImage findShiftAndScale(int[][][] ImageArray1) {
		BufferedImage originalImage = Demo.convertToBimage(ImageArray1);
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		int min = ImageArray1[0][0][1];
		int max = ImageArray1[0][0][1];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				if (ImageArray1[x][y][1] < min) {
					min = ImageArray1[x][y][1];
				}
				if (ImageArray1[x][y][2] < min) {
					min = ImageArray1[x][y][2];
				}
				if (ImageArray1[x][y][3] < min) {
					min = ImageArray1[x][y][3];
				}
				if (ImageArray1[x][y][1] > max) {
					max = ImageArray1[x][y][1];
				}
				if (ImageArray1[x][y][2] > max) {
					max = ImageArray1[x][y][2];
				}
				if (ImageArray1[x][y][3] > max) {
					max = ImageArray1[x][y][3];
				}
			}
		}

		int shift = min * -1;
		float scale = 255f / (max + shift);
		// System.out.println(scale+ " " + shift);
		return shiftAndScale(ImageArray1, scale, shift);
	}

	// Helper method called by findShiftAndScale() to return the actual array as the
	// aforementioned method only finds the shift and scale values
	public static BufferedImage shiftAndScale(int[][][] ImageArray1, float s, int t) {
		BufferedImage originalImage = Demo.convertToBimage(ImageArray1);
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		int[][][] ImageArray2 = Demo.convertToArray(originalImage);

		int rmin, bmin, gmin;
		int rmax, bmax, gmax;
		rmin = Math.round(s * (ImageArray2[0][0][1] + t));
		rmax = rmin;
		gmin = Math.round(s * (ImageArray2[0][0][2] + t));
		gmax = gmin;
		bmin = Math.round(s * (ImageArray2[0][0][3] + t));
		bmax = bmin;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ImageArray2[x][y][1] = Math.round(s * (ImageArray1[x][y][1] + t)); // r
				ImageArray2[x][y][2] = Math.round(s * (ImageArray1[x][y][2] + t)); // g
				ImageArray2[x][y][3] = Math.round(s * (ImageArray1[x][y][3] + t)); // b
				if (rmin > ImageArray2[x][y][1]) {
					rmin = ImageArray2[x][y][1];
				}
				if (gmin > ImageArray2[x][y][2]) {
					gmin = ImageArray2[x][y][2];
				}
				if (bmin > ImageArray2[x][y][3]) {
					bmin = ImageArray2[x][y][3];
				}
				if (rmax < ImageArray2[x][y][1]) {
					rmax = ImageArray2[x][y][1];
				}
				if (gmax < ImageArray2[x][y][2]) {
					gmax = ImageArray2[x][y][2];
				}
				if (bmax < ImageArray2[x][y][3]) {
					bmax = ImageArray2[x][y][3];
				}
			}
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (rmax - rmin == 0) {
					ImageArray2[x][y][1] = 255 * (ImageArray2[x][y][1] - rmin);
				} else {
					ImageArray2[x][y][1] = 255 * (ImageArray2[x][y][1] - rmin) / (rmax - rmin);
				}
				if (gmax - gmin == 0) {
					ImageArray2[x][y][2] = 255 * (ImageArray2[x][y][2] - gmin);
				} else {
					ImageArray2[x][y][2] = 255 * (ImageArray2[x][y][2] - gmin) / (gmax - gmin);
				}
				if (bmax - bmin == 0) {
					ImageArray2[x][y][3] = 255 * (ImageArray2[x][y][3] - bmin);
				} else {
					ImageArray2[x][y][3] = 255 * (ImageArray2[x][y][3] - bmin) / (bmax - bmin);
				}
			}
		}
		return Demo.convertToBimage(ImageArray2);
	}



	public void filterImage() {
		String sc;
		String sh;
		boolean isValid = false;
		int i = 3;
		float f = 3;
		if (opIndex == lastOp) {
			return;
		}
		lastOp = opIndex;
		switch (opIndex) {

		// Lab 1
		case 0:
			biFiltered = bi; /* original */
			return;
		case 1:
			biFiltered = ImageNegative(imageList.get(0)); /* Image Negative */
			imageList.add(biFiltered);
			//repaint();
			return;

		// Lab 2
		case 2:
			while (f > 2 || f < -2) {
				try {
					sc = JOptionPane.showInputDialog("Enter a scale factor between -2 and 2, up to one decimal point allowed.");
					f = Float.parseFloat(sc);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Number not within specified range, please try again.");
				}
			}
			biFiltered = RescaleAndShift.rescale(bi, f); /* Image Rescale */
			imageList.add(biFiltered);
			return;
		case 3:
			while (!isValid) {
				try {
					sh = JOptionPane.showInputDialog("Enter a value between -255 and 255:");
					i = Integer.parseInt(sh);
					if (i >= -255 && i <= 255) {
						isValid = true;
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Number not within specified range, please try again.");
				}
			}
			biFiltered = RescaleAndShift.shifting(bi, i); /* Image shift */
			imageList.add(biFiltered);
			return;
		case 4:
			biFiltered = RescaleAndShift.randomShiftScale(bi); /* Random shift and scale */
			imageList.add(biFiltered);
			return;

		// Lab 3
		case 5:
			float s = 1;
			int q = 1;
			biFiltered = ArithmeticAndBool.Addition(bi, bi3);
			biFiltered = RescaleAndShift.shifting(biFiltered, q);
			biFiltered = RescaleAndShift.rescale(biFiltered, s);
			imageList.add(biFiltered);
			return;
		case 6:
			s = 1.0f;
			q = 1;
			biFiltered = ArithmeticAndBool.Subtraction(bi, bi3);
			biFiltered = RescaleAndShift.shifting(biFiltered, q);
			biFiltered = RescaleAndShift.rescale(biFiltered, s);
			imageList.add(biFiltered);
			return;
		case 7:
			biFiltered = ArithmeticAndBool.Multiplication(bi, bi3);
			imageList.add(biFiltered);
			return;
		case 8:
			biFiltered = ArithmeticAndBool.Division(bi, bi3);
			imageList.add(biFiltered);
			return;
		case 9:
			biFiltered = ArithmeticAndBool.NOT(bi);
			imageList.add(biFiltered);
			return;
		case 10:
			biFiltered = ArithmeticAndBool.AND(bi, bi3);
			imageList.add(biFiltered);
			return;
		case 11:
			biFiltered = ArithmeticAndBool.OR(bi, bi3);
			imageList.add(biFiltered);
			return;
		case 12:
			biFiltered = ArithmeticAndBool.XOR(bi, bi3);
			imageList.add(biFiltered);
			return;
		case 13:
			biFiltered = ArithmeticAndBool.roiAND(bi, roi);
			imageList.add(biFiltered);
			return;
		case 14:
			biFiltered = ArithmeticAndBool.roiMultiplication(bi, roi);
			imageList.add(biFiltered);
			return;
		case 15:
			biFiltered = ArithmeticAndBool.roiNOT(bi, roi);
			imageList.add(biFiltered);
			return;

		// Lab 4
		case 16:
			biFiltered = PointProcessing.negLinearTransform(bi);
			imageList.add(biFiltered);
			return;
		case 17:
			biFiltered = PointProcessing.logFunction(bi);
			imageList.add(biFiltered);
			return;
		case 18:
			// Value between 0.01 and 25
			while (!isValid) {
				try {
					sh = JOptionPane.showInputDialog("Enter a number between 0.01 and 25:");
					f = Float.parseFloat(sh);
					if (f >= 0.01f && f <= 25) {

						isValid = true;
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Number not within specified range, please try again.");
				}
			}
			biFiltered = PointProcessing.powerLaw(bi, f);
			imageList.add(biFiltered);
			return;
		case 19:
			biFiltered = PointProcessing.randomLUT(bi);
			imageList.add(biFiltered);
			return;
		case 20:
			while (!isValid) {
				try {
					sh = JOptionPane.showInputDialog("Enter a value between 0 and 7:");
					i = Integer.parseInt(sh);
					if (i >= 0 && i <= 7) {
						isValid = true;
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Number not within specified range, please try again.");
				}
			}
			biFiltered = PointProcessing.bitplaneSlicing(bi, i);
			imageList.add(biFiltered);
			return;
		// Lab 5
		case 21:
			biFiltered = Histogram.histogramEqualisation(bi);
			imageList.add(biFiltered);
			return;
		// Lab 6
		case 22:
			biFiltered = LinearFiltering.average(bi);
			imageList.add(biFiltered);
			return;
		case 23:
			biFiltered = LinearFiltering.weightedAvg(bi);
			imageList.add(biFiltered);
			return;
		case 24:
			biFiltered = LinearFiltering.fourNL(bi);
			imageList.add(biFiltered);
			return;
		case 25:
			biFiltered = LinearFiltering.eightNL(bi);
			imageList.add(biFiltered);
			return;
		case 26:
			biFiltered = LinearFiltering.fourNLEnhanced(bi);
			imageList.add(biFiltered);
			return;
		case 27:
			biFiltered = LinearFiltering.eightNLEnhanced(bi);
			imageList.add(biFiltered);
			return;
		case 28:
			biFiltered = LinearFiltering.robertsA(bi);
			imageList.add(biFiltered);
			return;
		case 29:
			biFiltered = LinearFiltering.robertsB(bi);
			imageList.add(biFiltered);
			return;
		case 30:
			biFiltered = LinearFiltering.sobelX(bi);
			imageList.add(biFiltered);
			return;
		case 31:
			biFiltered = LinearFiltering.sobelY(bi);
			imageList.add(biFiltered);
			return;
		// Lab 7
		case 32:
			biFiltered = OrderStatisticFiltering.saltPepper(bi);
			imageList.add(biFiltered);
			return;
		case 33:
			biFiltered = OrderStatisticFiltering.minFiltering(bi);
			imageList.add(biFiltered);
			return;
		case 34:
			biFiltered = OrderStatisticFiltering.maxFiltering(bi);
			imageList.add(biFiltered);
			return;
		case 35:
			biFiltered = OrderStatisticFiltering.midpointFilter(bi);
			imageList.add(biFiltered);
			return;
		case 36:
			biFiltered = OrderStatisticFiltering.medianFiltering(bi);
			imageList.add(biFiltered);
			return;
		// Lab 8
		case 37: 
			Thresholding.standardDeviation(bi);
			return;
		case 38:
			// Threshold between 0 and 255
			while (!isValid) {
				try {
					sh = JOptionPane.showInputDialog("Enter a value between 0 and 255:");
					i = Integer.parseInt(sh);
					if (i >= 0 && i <= 255) {
						isValid = true;
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Number not within specified range, please try again.");
				}
			}
			biFiltered = Thresholding.simpleThresholding(bi, i);
			imageList.add(biFiltered);
			return;
		case 39:
			biFiltered = Thresholding.automatedThresholding(bi);
			imageList.add(biFiltered);
		}
	}
	private static void dialogDisplay(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Image processing coursework", JOptionPane.ERROR_MESSAGE);
	}


	public void actionPerformed(ActionEvent e) {

		if(e.getSource() instanceof JComboBox) {
			JComboBox cb = (JComboBox) e.getSource();
			if (cb.getActionCommand().equals("SetFilter")) {
				setOpIndex(cb.getSelectedIndex());

				repaint();}
//
		}

		if (e.getSource() == addButton) {

			int returnVal = jfc.showOpenDialog(Demo.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				if (FileSystemView.getFileSystemView().getSystemTypeDescription(file).contains("RAW")) {
					try {
						bi3 = rawToBmp("./images/" + file.getName());
						imageList.add(bi3);
						repaint();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}else if(FileSystemView.getFileSystemView().getSystemTypeDescription(file).contains("ROI")){
					try {
						roi = ImageIO.read(file);
						imageList.add(roi);
						repaint();
					} catch (IOException exc) {
						System.out.println("Cannot read Image");
						System.exit(1);
					}
				}else {
					try {
						bi3 = ImageIO.read(file);
						imageList.add(bi3);
						repaint();
					} catch (IOException exc) {
						System.out.println("Cannot read Image");
						System.exit(1);
					}
				}
			}
			bi = imageList.get(0);

		} else if (e.getSource() == removeButton) {
			if (imageList.size() >= 2) {
				imageList.remove(imageList.size() - 1);
				repaint();
			}
		} else if (e.getSource() == saveButton) {
			File file = new File("savedimage." + formats);
			jfc.setSelectedFile(file);
			int returnVal = jfc.showSaveDialog(Demo.this);
			JFileChooser chooser = new JFileChooser();
			chooser.setSelectedFile(file);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Formats", "bmp", "gif", "jpeg", "jpg", "png");
			chooser.setFileFilter(filter);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File saveFile = jfc.getSelectedFile();
				try {
					ImageIO.write(biFiltered, String.valueOf(formats), saveFile);
				} catch (IOException ex) {

				}
			}
		} else if (e.getSource() == undoButton) {
			System.out.println("clicked undo");
			if (!imageList.isEmpty()) {
				System.out.println("here");
				biFiltered = imageList.get(imageList.size() - 1);
				imageList.remove(imageList.size()-1);
				repaint();
				if (imageList.isEmpty()) { imageList.add(bi);}
			} else {
				dialogDisplay("Image cannot be undone.");
			}
			System.out.println(imageList);
		}
		
	};

	static final int WINDOW_WIDTH = 1280;
	static final int WINDOW_HEIGHT = 720;


	public static void createDemo() {
		// Open the JFrame
		JFrame f = new JFrame("Image Processing Coursework " );
		f.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		Demo de = new Demo();
		f.add("Center", de);
		JComboBox choices = new JComboBox(de.getDescriptions());

		choices.setActionCommand("SetFilter");
		choices.addActionListener(de);

		addButton = new JButton("Open an image");
		addButton.addActionListener(de);

		saveButton = new JButton("Save a file");
		saveButton.addActionListener(de);

		removeButton = new JButton("Remove previously opened image");
		removeButton.addActionListener(de);

		undoButton = new JButton("Undo");

		undoButton.addActionListener(de);

		formats = new JComboBox(de.getFormats());

		formats.addActionListener(de);

		JPanel panel = new JPanel();



		panel.add(choices);
		panel.add(addButton);
		panel.add(saveButton);
		panel.add(removeButton);

		panel.add(new JLabel("Save As"));
		panel.add(formats);
		panel.add(undoButton);


		f.add("North", panel);
		f.pack();
		f.setVisible(true);
	}
	public static void main(String s[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createDemo();
			}});

	}
}