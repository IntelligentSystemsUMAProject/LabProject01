package methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

public class DataProcessing {
	private static final double PI = Math.PI;

	/**
	 * Read stored data from files "training.csv", "validation.csv" and "test.csv"
	 * 
	 * @return a triple array of data where position 0 and 2 stores training set
	 *         data, 2 and 3 stores validation set data, 4 and 5 test set data
	 */
	public static double[][][] readData() {
		double[][][] data = new double[6][][];
		double[][][] tmp = new double[2][][];

		tmp = readFile("training.csv", 1000);
		data[0] = tmp[0]; // training
		data[1] = tmp[1]; // training

		tmp = readFile("validation.csv", 1000);
		data[2] = tmp[0]; // validation
		data[3] = tmp[1]; // validation

		tmp = readFile("test.csv", 10000);
		data[4] = tmp[0]; // test
		data[5] = tmp[1]; // test

		return data;
	}

	/**
	 * 
	 * @param filename to be data read from.
	 * @param size     number of lines of the dataset.
	 * @return stored data in triple array where in position 0 stored input data and
	 *         in positon 1 stored ideal output.
	 */
	private static double[][][] readFile(String filename, int size) {
		double data[][][] = new double[2][][];
		data[0] = new double[size][2];
		data[1] = new double[size][1];
		try (Scanner sc = new Scanner(new File(filename))) {
			int cnt = 0;
			while (sc.hasNextLine()) {
				try (Scanner line = new Scanner(sc.nextLine())) {
					line.useLocale(Locale.ENGLISH);
					String[] s = line.nextLine().split(",");
					data[0][cnt][0] = Double.parseDouble(s[0]);
					data[0][cnt][1] = Double.parseDouble(s[1]);
					data[1][cnt][0] = Double.parseDouble(s[2]);
					cnt++;
				} catch (NumberFormatException e) {
					// ingore;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
		return data;
	}

	/**
	 * Method made for generate training, validation, and test sets To invoke this
	 * method and regenerate the input data you must toggle boolean value at the
	 * beginning of the main function.
	 */
	public static void generateData() {
		generateDataAndSaveToFile("training.csv");
		generateDataAndSaveToFile("validation.csv");

		double step = 2 * PI / 99;
		double initX = -PI - step;
		double initY = -PI - step;
		double x = initX;
		double y = initY;
		try (PrintWriter writer = new PrintWriter(new File("test.csv"))) {
			writer.printf("x1,x2,f\n");
			for (int i = 0; i < 100; i++) {
				y += step;
				for (int j = 0; j < 100; j++) {
					x += step;
					/*
					 * //In case I want to generate directly 
					 * testX[i * 100 + j][0] = x; 
					 * testX[i * 100 + j][1] = y; 
					 * testY[i * 100 + j][0] = getF(x, y);
					 */
					writer.printf("%f,%f,%f\n", x, y, getF(x, y));
				}
				x = initX;
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}

	
	
	/**
	 * Auxiliary method that abstract writing data to the file.
	 * 
	 * @param filename of the output file.
	 */
	private static void generateDataAndSaveToFile(String filename) {
		try (PrintWriter writer = new PrintWriter(new File(filename))) {
			writer.printf("x1,x2,f\n");
			for (int i = 0; i < 1000; i++) {
				double x = Math.random() * (PI + PI) - PI;
				double y = Math.random() * (PI + PI) - PI;
				writer.printf("%f,%f,%f\n", x, y, getF(x, y));
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}

	}
	
	
	/**
	 * Calculates the desire output of the neural network
	 * @param x double precision floating point number
	 * @param y double precision floating point number
	 * @return sin(x) * cos(y)
	 */
	private static double getF(double x, double y) {
		return (Math.sin(x) * Math.cos(y));
	}
}
