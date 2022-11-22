package project1;

import java.io.File;
import java.util.Scanner;

/*******************************************************
 * CS4551 Multimedia Software Systems @ Author: Elaine Kang
 * 
 * 
 * Template Code - demonstrate how to use MImage class
 *******************************************************/

public class CS4551_Tobar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// the program expects one command line argument
		// if there is no command line argument, exit the program
		if (args.length != 1) {
			usage();
			System.exit(1);
		}

		System.out.println("--Welcome to Multimedia Software System--");
		
		Scanner input = new Scanner(System.in);
		
		boolean terminate = false;
		
		int choice = 0;
		
		while(!terminate) {
			System.out.println("\nMain Menu-----------------------------------");
			System.out.println("1. Conversion to Gray-scale Image (24bits->8bits)");
			System.out.println("2. Conversion to Binary Image using Ordered Dithering (k=4)");
			System.out.println("3. Conversion to 8bit Indexed Color Image using Uniform Color Quantization (24bits->8bits)");
			System.out.println("4. Quit");
			System.out.println("Please enter the task number [1-4]:");
			
			choice = input.nextInt();
			
			if(choice == 1) {
				MImage img = new MImage(args[0]);
				img.convertToGrayScale();
				img.write2PPM(img.getName() + "-gray.ppm");
			}
			else if (choice == 2) {
				MImage img = new MImage(args[0]);
				img.convertToGrayScale();
				img.orderedDithering();
				img.write2PPM(img.getName() + "-OD4.ppm");
			}
			else if (choice == 3) {
				MImage img = new MImage(args[0]);
				img.uniformColorQuantizationIndex();
				img.write2PPM(img.getName() + "-index.ppm");
				img.uniformColorQuantization();
				img.write2PPM(img.getName() + "-QT8.ppm");
			}
			else if (choice == 4) {
				terminate = true;
			}
			else {
				System.out.println("ERROR: Enter correct choice from menu. Try again.");
			}
		}
		
		input.close();
		System.out.println("Program terminated...");
		System.exit(0);
	}

	public static void usage() {
		System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
	}
}
