package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import methods.DataProcessing;
import methods.NeuralNetwork;

public class LabProject {
	// change this variable to true to generate new data
	static boolean iWantToGenerateNewData = false;
	
	// change this variable to true to generate new plot input data;
	static boolean iWantToGenerateDataForDrawGraphics = false;

	public static void main(String[] args) throws FileNotFoundException {

		if (iWantToGenerateNewData) {
			DataProcessing.generateData();
		}

		double[][][] data = DataProcessing.readData();
		double[][] trainingX = data[0];
		double[][] trainingY = data[1];

		double[][] validationX = data[2];
		double[][] validationY = data[3];

		double[][] testX = data[4];
		double[][] testY = data[5];

		PrintWriter consoleOutput = new PrintWriter(new File("output.txt"));

		if (iWantToGenerateDataForDrawGraphics) {
			// We training neural network with different number of neuron in hidden layer
			// and documenting the output
			String[] af = { "ActivationLOG", "ActivationElliottSymmetric" };
			for (int func = 0; func < af.length; func++) {
				for (int numOfHiddenNeurons = 4; numOfHiddenNeurons <= 10; numOfHiddenNeurons++) {
					String filename = af[func] + "_" + numOfHiddenNeurons + "_HiddenNeurons.csv";
					PrintWriter pw = new PrintWriter(new File(filename));
					pw.printf("sep=,\n");
					pw.printf("Epoch,Training Error,Validation Error\n");

					BasicNetwork neuralNetwork = NeuralNetwork.createNeuralNewtwork(numOfHiddenNeurons, af[func]);
					int MAX_ITERATIONS = 750;

					MLDataSet trainingSet = new BasicMLDataSet(trainingX, trainingY);
					Backpropagation train = new Backpropagation(neuralNetwork, trainingSet);

					for (int i = 1; i <= MAX_ITERATIONS; i++) {
						train.iteration();
						double errorOnValidationSet = NeuralNetwork.calcuateMedianSquaredError(neuralNetwork,
								validationX, validationY);
						pw.printf(Locale.ENGLISH, "%d,%f,%f\n", i, train.getError(), errorOnValidationSet);
					}
					pw.flush();
					pw.close();
					train.finishTraining();
				}
			}
			System.out.println("Data is Generated");
		}

		// Data analysis has shown that the best settings for the ANN is:

		int numOfHiddenNeurons = 10;
		int MAX_ITERATIONS = 750;
		String activationFunction = "ActivationLOG";

		BasicNetwork neuralNetwork = NeuralNetwork.createNeuralNewtwork(numOfHiddenNeurons, activationFunction);
		MLDataSet trainingSet = new BasicMLDataSet(trainingX, trainingY);

		Backpropagation train = new Backpropagation(neuralNetwork, trainingSet);
		// Training NN using analyzed results;

		consoleOutput.printf("Best results were achived using %s with %s neurons in hidden layer\n\n",
				activationFunction, numOfHiddenNeurons);
		consoleOutput.printf("Epoch\tError\n");
		
		System.out.printf("Best results were achived using %s with %s neurons in hidden layer\n\n",
				activationFunction, numOfHiddenNeurons);
		System.out.printf("Epoch\tError\n");
		for (int i = 1; i <= MAX_ITERATIONS; i++) {
			train.iteration();
			consoleOutput.printf(Locale.ENGLISH, "%d\t\t%f\n", i, train.getError());
			System.out.printf(Locale.ENGLISH, "%d\t\t%f\n", i, train.getError());
		}

		// Now we going to test resulted neural network with the test set
		consoleOutput.printf("\n\nWorking with the test set\n");
		consoleOutput.printf("x1\t\t\tx2\t\t\tOutput\t\tIdeal\n");
		System.out.printf("\n\nWorking with the test set\n");
		System.out.printf("x1\t\t\tx2\t\t\tOutput\t\tIdeal\n");
		PrintWriter testSet = new PrintWriter(new File("testSet.csv"));
		testSet.printf("sep=,\n");
		testSet.printf("x1,x2,resut,ideal\n");
		for (int i = 0; i < testX.length; i++) {
			MLDataPair pair = new BasicMLDataPair(new BasicMLData(testX[i]), new BasicMLData(testY[i]));
			MLData result = neuralNetwork.compute(pair.getInput());
			testSet.printf(Locale.ENGLISH, "%f,%f,%f,%f\n", testX[i][0], testX[i][1], result.getData(0), testY[i][0]);
			consoleOutput.printf("%f\t%f\t%f\t%f\n", testX[i][0], testX[i][1], result.getData(0), testY[i][0]);
			System.out.printf("%f\t%f\t%f\t%f\n", testX[i][0], testX[i][1], result.getData(0), testY[i][0]);
		}
		consoleOutput.flush();
		testSet.flush();
		consoleOutput.close();
		testSet.close();
		System.out.println("done");
	}

}
