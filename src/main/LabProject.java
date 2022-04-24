package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

import org.encog.ml.data.MLDataSet;
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

		// We training neural network with different number of neuron in hidden layer
		// and documenting the output
		for (int numOfHiddenNeurons = 0; numOfHiddenNeurons < 10; numOfHiddenNeurons++) {
			String filename = "ActivationLOG_" + numOfHiddenNeurons + "_HiddenNeurons.csv";
			PrintWriter pw = new PrintWriter(new File(filename));
			pw.printf("Epoch,Training Error,Validation Error\n");

			BasicNetwork neuralNetwork = NeuralNetwork.createNeuralNewtwork(numOfHiddenNeurons, "ActivationLOG");
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
				pw.printf(Locale.ENGLISH, "%d,%f,%f\n", i, train.getError(), errorOnValidationSet);
			}
			pw.flush();
			pw.close();
			train.finishTraining();
		}
		System.out.println("done");

		/*
		 * for(MLDataPair pair : trainingSet) { MLData result =
		 * neuronalNetwork.compute(pair.getInput()); double[] in = pair.getInputArray();
		 * System.out.printf("%f\t%f\tidl: %f\tout: %f\n", in[0], in[1],
		 * pair.getIdealArray()[0], result.getData()[0]); }
		 */
	}

}
