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

	public static void main(String[] args) throws FileNotFoundException {

		boolean iWantToGenerateNewData = false;
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

		boolean iWantToGenerateDataForDrawGraphics = false;
		if (iWantToGenerateDataForDrawGraphics) {
			// We training neural network with different number of neuron in hidden layer
			// and documenting the output
			for (int numOfHiddenNeurons = 4; numOfHiddenNeurons <= 10; numOfHiddenNeurons++) {
				String filename = "ActivationElliottSymmetric_" + numOfHiddenNeurons + "_HiddenNeurons.csv";
				PrintWriter pw = new PrintWriter(new File(filename));
				pw.printf("Epoch\tTraining Error\tValidation Error\n");

				BasicNetwork neuralNetwork = NeuralNetwork.createNeuralNewtwork(numOfHiddenNeurons,
						"ActivationElliottSymmetric");
				int MAX_ITERATIONS = 200;

				MLDataSet trainingSet = new BasicMLDataSet(trainingX, trainingY);

				Backpropagation train = new Backpropagation(neuralNetwork, trainingSet);
				for (int i = 1; i <= MAX_ITERATIONS; i++) {
					train.iteration();
//				System.out.println("Epoch " + (i + 1));
//				System.out.println("Training Error: " + train.getError());
					double errorOnValidationSet = NeuralNetwork.calcuateMedianSquaredError(neuralNetwork, validationX,
							validationY);
//				System.out.println("Validation Error: " + errorOnValidationSet);
//				System.out.println();
					pw.printf(Locale.FRENCH, "%d\t%f\t%f\n", i, train.getError(), errorOnValidationSet);
				}
				pw.flush();
				pw.close();
				train.finishTraining();
			}
			System.out.println("Data is Generated");
		}
		
		
		
		// Data analysis has shown that the best settings for the ANN is:
		// Activation function <xx>
		// Number of hidden layers is <x>
		// Number of iterations is <z>
		int numOfHiddenNeurons = 10;
		String activationFunction = "ActivationElliottSymmetric";
		int MAX_ITERATIONS = 40;
		
		BasicNetwork neuralNetwork = NeuralNetwork.createNeuralNewtwork(numOfHiddenNeurons, activationFunction);
		MLDataSet trainingSet = new BasicMLDataSet(trainingX, trainingY);
		
		Backpropagation train = new Backpropagation(neuralNetwork, trainingSet);
		// Training NN using analyzed results;
		PrintWriter consoleOutput = new PrintWriter(new File("ConsoleOutput.txt"));
		consoleOutput.printf("Training Neural Network using %s with %s neurons in hidden layer\n\n",activationFunction, numOfHiddenNeurons);
		consoleOutput.printf("Epoch\tError\n");
		for (int i = 1; i <= MAX_ITERATIONS; i++) {
			train.iteration();
			consoleOutput.printf(Locale.FRENCH, "%d\t%f\n", i, train.getError());
		}
		
		// Now we going to test resulted neural network with the test set
		consoleOutput.printf("\n\nWorking with the test set\n");
		consoleOutput.printf("x1\t\tx2\t\tOutput\tIdeal\n");
		PrintWriter testSet = new PrintWriter(new File("testSet.csv"));
		for (int i = 0; i < testX.length; i++) {
			MLDataPair pair = new BasicMLDataPair(new BasicMLData(testX[i]), new BasicMLData(testY[i]));
			MLData result = neuralNetwork.compute(pair.getInput());
			testSet.printf(Locale.ENGLISH, "%f\n", result.getData(0));
			consoleOutput.printf("%f\t%f\t%f\t%f\n",testX[i][0],testX[i][1],result.getData(0) ,testY[i][0]);
//			double[] in = pair.getInputArray();
//			System.out.printf("%f\t%f\tidl: %f\tout: %f\n", in[0], in[1],pair.getIdealArray()[0], result.getData(0)); 
		}
		consoleOutput.flush();
		testSet.flush();
		consoleOutput.close();
		testSet.close();
		System.out.println("done");
	}

}
