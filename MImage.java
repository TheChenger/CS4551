
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
} // MImage class
