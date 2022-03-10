import java.util.Scanner;


public class CS4551_Cheng {
	public static void main(String[]args) {
		
		System.out.println("Main Menu-----------------------------------");
		System.out.println("1. Conversion to Gray-scale Image (24bits->8bits)");
		System.out.println("2. Conversion to Binary Image using Ordered Dithering(k=4)");
		System.out.println("3. Conversion to 8bit Indexed Color Image using Uniform Color Quantization(24bits->8bits)");
		System.out.println("4. Quit");
		System.out.println("Please enter the task number[1-4]");

		Scanner myObj = new Scanner(System.in);
		
		String tasknum = myObj.nextLine();
		System.out.println("The last input was " + tasknum);
		
		while (tasknum.length()== 1) {
			switch (tasknum) {
				case "1":
					System.out.println("This is case 1");
					toGrayScale();
					System.out.println("Enter a new task number: ");
					tasknum = myObj.nextLine();
					break;
					
				case "2":
					System.out.println("This is case 2");
					orderedDithering();
					System.out.println("Enter a new task number: ");
					tasknum = myObj.nextLine();
					break;
				
				case "3":
					System.out.println("This is case 3");
					uniformColorQuantization();
					System.out.println("Enter a new task number: ");
					tasknum = myObj.nextLine();
					break;
					
				case "4":
					System.exit(0);
					
				default:
					System.out.println("Sorry that was an invalid task number, try again");
					System.out.println("Main Menu-----------------------------------");
					System.out.println("1. Conversion to Gray-scale Image (24bits->8bits)");
					System.out.println("2. Conversion to Binary Image using Ordered Dithering(k=4)");
					System.out.println("3. Conversion to 8bit Indexed Color Image using Uniform Color Quantization(24bits->8bits)");
					System.out.println("4. Quit");
					System.out.println("Please enter the task number[1-4]");
					tasknum = myObj.nextLine();
			}
						
		}
		if (tasknum.length() != 1) {
			usage();
			System.exit(0);
		}
		
		
	}
	public static void usage()	{
		System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
	}
	
	public static void toGrayScale() {
		Scanner obj1 = new Scanner(System.in);
		System.out.println("Enter the image file name: ");
		MImage img = new MImage(obj1.nextLine());
		System.out.println("The name of the file is " + img.getName());
		System.out.println(img.toString());
		img.printPixel(155, 166);
		
		img.write2PPM("DuckyTest.ppm");
		int[] rgb = new int[3];
		
		
		for (int x = 0; x<img.getW()+1; x++) {
			for (int y = 0;y<img.getH()+1; y++) {
				
				img.getPixel(x, y, rgb);
				int grayVal = (int) (rgb[0]*.299 + rgb[1]*.587 + rgb[2]*.114);
				if (grayVal > 255) {
					grayVal = 255;
				}
				rgb[0]=grayVal;
				rgb[1]=grayVal;
				rgb[2]=grayVal;
				img.setPixel(x, y, rgb);
			}
		}
		img.write2PPM("Ducky-gray.ppm");
		System.out.println("++Good Bye++");
		
		
	}

	public static void orderedDithering() {
		Scanner obj1 = new Scanner(System.in);
		System.out.println("Enter the image file name: ");
		MImage img = new MImage(obj1.nextLine());
		System.out.println("The name of the file is " + img.getName());
		System.out.println(img.toString());
		img.printPixel(155, 166);
		
		img.write2PPM("DuckyTestO.ppm");
		int[] rgb = new int[3];
		int[][] dMatrix = {{0,8,2,10},{12,4,14,6},{3,11,1,9},{15,7,13,5}};
		
		
		for (int x = 0; x<img.getW()+1; x++) {
			for (int y = 0;y<img.getH()+1; y++) {
				
				img.getPixel(x, y, rgb);
				int d = rgb[0]*17/256;
				
				if (d <= dMatrix[x%4][y%4]) {
				
					rgb[0]=0;
					rgb[1]=0;
					rgb[2]=0;
				} else {
					rgb[0]=255;
					rgb[1]=255;
					rgb[2]=255;
				}
				img.setPixel(x, y, rgb);
			}
		}
		img.write2PPM("Ducky-gray-OD4.ppm");
		System.out.println("++Good Bye++");
	}
	
	public static void uniformColorQuantization() {
		Scanner obj1 = new Scanner(System.in);
		System.out.println("Enter the image file name: ");
		MImage img = new MImage(obj1.nextLine());
		System.out.println("The name of the file is " + img.getName());
		System.out.println(img.toString());
		img.printPixel(155, 166);
		
		img.write2PPM("DuckyTestUC.ppm");

		int lut [][] = new int[256][3];
		int index = 0;
		System.out.println("LUT by UCQ");
		System.out.println("Index R G B");
		System.out.println("------------");
		for(int r = 0; r < 8; r++) {
			for(int g = 0; g < 8; g++) {
				for(int b = 0; b < 4; b++) {
					System.out.println(index + " "+(r*32+16) + " " + (g*32+16) + " " + (b*64+32));
					lut[index][0]=r*32+16;
					lut[index][1]=g*32+16;
					lut[index][2]=b*64+32;
					index++;
		}}}
		
		
		int[]rgb = new int[3];
		for (int x = 0; x<img.getW(); x++) {
			for (int y = 0;y<img.getH(); y++) {
				
				img.getPixel(x, y, rgb);
				int r = rgb[0]/32;
				int g = rgb[1]/32;
				int b = rgb[2]/64;
				
				int indexvalue = r*32 + g*4 + b;
				
				rgb[0] = indexvalue;
				rgb[1] = indexvalue;
				rgb[2] = indexvalue;

				
				img.setPixel(x, y, rgb);
			}
		}
		img.write2PPM("Ducky-index.ppm");
		System.out.println("++Good Bye++");
		
		
		for (int x = 0; x<img.getW(); x++) {
			for (int y = 0;y<img.getH(); y++) {
				int rgb1[]=new int[3];
				img.getPixel(x, y, rgb1);
				int r = rgb1[0]/32;
				int g = rgb1[1]/32;
				int b = rgb1[2]/64;
				
				
				int indexvalue = r*32 + g*4 + b;
				int rlut = lut[indexvalue][0];
				int glut = lut[indexvalue][1];
				int blut = lut[indexvalue][2];
				
				
				rgb1[0] = rlut;
				rgb1[1] = glut;
				rgb1[2] = blut;
				
				
				img.setPixel(x, y, rgb1);
			}
		}
		img.write2PPM("Ducky-QT8.ppm");
		System.out.println("++Good Bye pt2++");
		
		
		
		
		
	}
}
