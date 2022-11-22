package project1;


/*******************************************************
 CS4551 Multimedia Software Systems
 @ Author: Elaine Kang

MImage class is for a 24bit RGB image only.
MImage is a platform independent class definition and stores image data into a byte array.
MImage provides utility functions such as reading data from and writing data to a PPM file.
 *******************************************************/

import java.io.*;
import java.util.*;

public class MImage {
	private String fileName; // Input file name
	private int pixelDepth = 3; // pixel depth in bye
	private byte data[]; // raw data in byte array -- char is 16bits in Java
	private int width;
	private int height;

	public MImage(int w, int h)
	// create an empty image with w(idth) and h(eight)
	{
		fileName = "";
		width = w;
		height = h;
		data = new byte[w * h * pixelDepth];
		System.out.println("Created an empty image with size " + w + "x" + h);
	}

	public MImage(String fn)
	// Create an image and read the data from the file
	{
		fileName = fn;
		readPPM(fileName);
		System.out.println("Created an image from " + fileName + " with size " + getW() + "x" + getH());
	}

	public int getW() {
		return width;
	}

	public int getH() {
		return height;
	}

	public int getSize()
	// return the image size in byte
	{
		return getW() * getH() * pixelDepth;
	}

	public String getName()
	// return the image name
	{
		return fileName;
	}

	// (0,0) is the upper left corner of the image
	// (w-1,0) is the upper right corner of the image
	// (0, h-1) is the lower right corner of the image
	// (w-1, h-1) is the lower right corner of the image

	public void getPixel(int x, int y, byte[] rgb)
	// retrieve rgb values at (x,y) through rgb[] byte array
	{
		if (x >= 0 && x < width && y >= 0 && y < height) {
			rgb[0] = data[y * width * pixelDepth + x * pixelDepth];
			rgb[1] = data[y * width * pixelDepth + x * pixelDepth + 1];
			rgb[2] = data[y * width * pixelDepth + x * pixelDepth + 2];
		}
	}

	public void getPixel(int x, int y, int[] rgb)
	// retrieve rgb values at (x,y) through rgb[] int array
	{
		if (x >= 0 && x < width && y >= 0 && y < height) {
			byte r = data[y * width * pixelDepth + x * pixelDepth];
			byte g = data[y * width * pixelDepth + x * pixelDepth + 1];
			byte b = data[y * width * pixelDepth + x * pixelDepth + 2];

			// converts singed byte value (~128-127) to unsigned byte value (0~255)
			rgb[0] = (int) (0xFF & r);
			rgb[1] = (int) (0xFF & g);
			rgb[2] = (int) (0xFF & b);
		}
	}

	public void setPixel(int x, int y, byte[] rgb)
	// set byte rgb values at (x,y)
	{
		if (x >= 0 && x < width && y >= 0 && y < height) {
			data[y * width * pixelDepth + x * pixelDepth] = rgb[0];
			data[y * width * pixelDepth + x * pixelDepth + 1] = rgb[1];
			data[y * width * pixelDepth + x * pixelDepth + 2] = rgb[2];
		}
	}

	public void setPixel(int x, int y, int[] irgb)
	// set int rgb values at (x,y)
	{
		if (x >= 0 && x < width && y >= 0 && y < height) {
			data[y * width * pixelDepth + x * pixelDepth] = (byte) irgb[0];
			data[y * width * pixelDepth + x * pixelDepth + 1] = (byte) irgb[1];
			data[y * width * pixelDepth + x * pixelDepth + 2] = (byte) irgb[2];
		}
	}

	public void printPixel(int x, int y)
	// Print rgb pixel in unsigned (0~255)
	{
		if (x >= 0 && x < width && y >= 0 && y < height) {
			byte r = data[y * width * pixelDepth + x * pixelDepth];
			byte g = data[y * width * pixelDepth + x * pixelDepth + 1];
			byte b = data[y * width * pixelDepth + x * pixelDepth + 2];

			System.out.println("RGB Pixel value at (" + x + "," + y + "): (" + (0xFF & r) + "," + (0xFF & g) + ","
					+ (0xFF & b) + ")");
		}
	}

	public void readPPM(String fileName)
	// read data from a PPM file
	{
		File fIn = null;
		FileInputStream fis = null;
		BufferedReader in = null;
		DataInputStream din = null;

		try {
			fIn = new File(fileName);
			in = new BufferedReader(new FileReader(fIn));
			din = new DataInputStream(new FileInputStream(fIn));
			int headerLength = 0;

			System.out.println("Reading " + fileName + "...");

			// read Identifier
			String header = in.readLine();
			if (!header.equals("P6")) {
				System.err.println("This is NOT P6 PPM. Wrong Format.");
				System.exit(0);
			}

			headerLength += header.getBytes().length;

			// read Comment line
			header = in.readLine();
			headerLength += header.getBytes().length;

			// read width & height
			header = in.readLine();
			headerLength += header.getBytes().length;
			String[] WidthHeight = header.split(" ");
			width = Integer.parseInt(WidthHeight[0]);
			height = Integer.parseInt(WidthHeight[1]);

			// read maximum value
			header = in.readLine();
			headerLength += header.getBytes().length;
			int maxVal = Integer.parseInt(header);

			if (maxVal != 255) {
				System.err.println("Max val is not 255");
				System.exit(0);
			}

			// read pixel data and store it into data array
			data = new byte[width * height * pixelDepth];
			din.skipBytes(headerLength + 4);
			din.read(data);

			din.close();
			in.close();

			System.out.println("Read " + fileName + " Successfully.");

		} // try
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void write2PPM(String fileName)
	// write the image data into a PPM file
	{
		FileOutputStream fos = null;
		PrintWriter dos = null;

		try {
			fos = new FileOutputStream(fileName);
			dos = new PrintWriter(fos);

			System.out.println("Writing the Image buffer into " + fileName + "...");

			// write header
			dos.print("P6" + "\n");
			dos.print("#CS451" + "\n");
			dos.print(getW() + " " + getH() + "\n");
			dos.print(255 + "\n");
			dos.flush();

			// write data
			fos.write(data);
			fos.flush();

			dos.close();
			fos.close();

			System.out.println("Wrote into " + fileName + " Successfully.");

		} // try
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public String toString() {
		return getName() + " : " + getW() + " X " + getH() + " : " + getSize() + " bytes";
	}

	public void convertToGrayScale() {
		// TODO Auto-generated method stub
		int[] rgbValues = new int [3];
		
		for (int y = 0; y < getH(); y++) {
			for (int x = 0; x < getW(); x++) {
				getPixel(x, y, rgbValues);
				grayScalePixel(x, y, rgbValues);
			}
		}
	}

	private void grayScalePixel(int x, int y, int[] rgbValues) {
		// TODO Auto-generated method stub
		int[] rgb = new int[3];
		
		int gray = (int) Math.round(0.299 * rgbValues[0] + 0.587 * rgbValues[1] + 0.114 * rgbValues[2]);
		
		if (gray < 0) {
			gray = 0;
		}
		if (gray > 255) {
			gray = 255;
		}
		
		for (int i = 0; i < 3; i++) {
			rgb[i] = gray;
			setPixel(x, y, rgb);
		}
		
	}

	public void orderedDithering() {
		// TODO Auto-generated method stub
		int[] rgbValues = new int[3];
		
		int[][] ditheringMatrix = { {  0,  8,  2, 10 }, 
									{ 12,  4, 14,  6 }, 
									{  3, 11,  1,  9 }, 
									{ 15,  7, 13,  5 } };
		
		for (int y = 0; y < getH(); y++) {
			for (int x = 0; x < getW(); x++) {
				getPixel(x, y, rgbValues);
				
				int intensity = (int) (rgbValues[0] * ((Math.pow(4, 2) + 1) / (256)));
				
				int[] black = { 0, 0, 0 };
				int[] white = { 255, 255, 255 };
				
				if (intensity > ditheringMatrix[y % 4][x % 4]) {
					setPixel(x, y, white);
				} else {
					setPixel(x, y, black);
				}
			}
		}
	}

	public void uniformColorQuantizationIndex() {
		// TODO Auto-generated method stub
		int[][] LUT = createLUT();
		
		printLUT(LUT);
		
		int[] rgbValues = new int[3];
		
		for (int y = 0; y < getH(); y++) {
			for (int x = 0; x < getW(); x++) {
				getPixel(x, y, rgbValues);
				
				int red = rgbValues[0] >> 5;
				int green = rgbValues[1] >> 5;
				int blue = rgbValues[2] >> 6;
			
				int bit = (red << 5) | (green << 2) | blue;
				
				rgbValues[0] = rgbValues[1] = rgbValues[2] = bit;
				setPixel(x, y, rgbValues);
			}
		}
	}

	private void printLUT(int[][] lUT) {
		// TODO Auto-generated method stub
		System.out.println("LUT by UCQ");
		System.out.println("Index R G B");
		System.out.println("____________________________");
		
		for (int i = 0; i < lUT.length; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < lUT[i].length; j++) {
				System.out.print(lUT[i][j] + " ");
			}
			System.out.println();
		}
	}

	private int[][] createLUT() {
		// TODO Auto-generated method stub
		int[][] LUT = new int[256][3];
		
		for (int i = 0; i < 256; i++) {
			int red = i >> 5;
			int green = (i >> 2) & 7;
			int blue = i & 3;
			
			int redComponent = red * 32 + 16;
			int greenComponent = green * 32 + 16;
			int blueComponent = blue * 64 + 32;
			
			LUT[i][0] = redComponent;
			LUT[i][1] = greenComponent;
			LUT[i][2] = blueComponent;
		}
		
		return LUT;
	}

	public void uniformColorQuantization() {
		// TODO Auto-generated method stub
		int[] rgbValues = new int[3];
		int[][] LUT = createLUT();
		
		for (int y = 0; y < getH(); y++) {
			for (int x = 0; x < getW(); x++) {
				getPixel(x, y, rgbValues);
				
				int index = rgbValues[0];
				int[] rgbColor = getLUTColor(index, LUT);
				setPixel(x, y, rgbColor);
			}
		}
	}

	private int[] getLUTColor(int index, int[][] lUT) {
		// TODO Auto-generated method stub
		int[] result = new int[3];
		
		for (int i = 0; i < lUT.length; i++) {
			for (int j = 0; j < lUT[j].length; j++) {
				if (index == i) {
					result[0] = lUT[i][0];
					result[1] = lUT[i][1];
					result[2] = lUT[i][2];
				}
			}
		}
		
		return result;
	}	
} // MImage class
