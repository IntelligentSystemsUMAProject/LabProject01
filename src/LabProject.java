
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

import org.encog.engine.network.activation.ActivationElliottSymmetric;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationLOG;
import org.encog.engine.network.activation.ActivationLinear;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class LabProject {

	private static final double PI = Math.PI;

	public static void main(String[] args) throws FileNotFoundException {

		double[][] trainingX = new double[1000][2];
		double[][] trainingY = new double[1000][1];

		double[][] validationX = new double[1000][2];
		double[][] validationY = new double[1000][1];

		double[][] testX = new double[10000][2];
		double[][] testY = new double[10000][1];

		for (int i = 0; i < 1000; i++) {
			double x = Math.random() * (PI + PI) - PI;
			double y = Math.random() * (PI + PI) - PI;
			trainingX[i][0] = x;
			trainingX[i][1] = y;
			trainingY[i][0] = getF(x, y);

			double x1 = Math.random() * (PI + PI) - PI;
			double y1 = Math.random() * (PI + PI) - PI;
			validationX[i][0] = x1;
			validationX[i][1] = y1;
			validationY[i][0] = getF(x1, y1);
		}

		double step = 2 * PI / 99;
		double initX = -PI - step;
		double initY = -PI - step;
		double x = initX;
		double y = initY;
		PrintWriter writer = new PrintWriter(new File("test.txt"));
		for (int i = 0; i < 100; i++) {
			y += step;
			for (int j = 0; j < 100; j++) {
				x += step;
				testX[i * 100 + j][0] = x;
				testX[i * 100 + j][1] = y;
				testY[i * 100 + j][0] = getF(x, y);
				writer.printf("%f\t\t%f\t\t%f\n", x, y, testY[i * 100 + j][0]);
			}
			x = initX;
		}
		writer.close();

		// We training neural network with different number of neuron in hidden layer
		// and documenting the output
		for (int numOfHiddenNeurons = 0; numOfHiddenNeurons < 10; numOfHiddenNeurons++) {

			String filename = "ActivationElliottSymmetricWith_" + numOfHiddenNeurons + "_HiddenNeurons.csv";
			File out = new File(filename);
			PrintWriter pw = new PrintWriter(out);
			pw.printf("%s\n", "Epoch,Training Error,Validation Error");

			BasicNetwork neuralNetwork = createNeuralNewtwork(numOfHiddenNeurons, "ActivationElliottSymmetric");
			int MAX_ITERATIONS = 200;

			MLDataSet trainingSet = new BasicMLDataSet(trainingX, trainingY);

			Backpropagation train = new Backpropagation(neuralNetwork, trainingSet);
			for (int i = 1; i <= MAX_ITERATIONS; i++) {
				train.iteration();
//				System.out.println("Epoch " + (i + 1));
//				System.out.println("Training Error: " + train.getError());
				double errorOnValidationSet = calcuateMedianSquaredError(neuralNetwork, validationX, validationY);
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

	/**
	 * Manual research showed that the best activation function is logarithmic and
	 * Elliott Symmetrical function. Both of this functions has range (-1,1) which
	 * is theoretical output of our F function. Sigmoid activation function, on the
	 * other hand in unable to represent all possible values, its range is [0, 1]
	 * and the empirical results show at best, 25 % percents of error.
	 * 
	 * @param numOfHiddenNeurons Number of neurons in hidden layer of the neural
	 *                           network. Currently only 1 hidden layer networks are
	 *                           taken in consideration.
	 * @param activationFunction Name of activation function we want to use in our
	 *                           neural network; currently possible values are
	 *                           "ActivationElliottSymmetric" and "ActivationLOG"
	 * @return A neural network that has 2 input, numOfHiddenNeurons and 1 output
	 *         neuron;
	 */
	private static BasicNetwork createNeuralNewtwork(int numOfHiddenNeurons, String activationFunction) {
		BasicNetwork neuralNetwork = new BasicNetwork();
		ActivationFunction af = getAF(activationFunction);

		neuralNetwork.addLayer(new BasicLayer(new ActivationLinear(), false, 2));
		if (numOfHiddenNeurons > 0) {
			neuralNetwork.addLayer(new BasicLayer(af, true, numOfHiddenNeurons));
		}
		neuralNetwork.addLayer(new BasicLayer(af, true, 1));

		neuralNetwork.getStructure().finalizeStructure();
		neuralNetwork.reset();
		return neuralNetwork;
	}

	/**
	 * 
	 * @param activationFunction Name of activation function we want to use in our
	 *                           neural network; currently possible values are
	 *                           "ActivationElliottSymmetric" and "ActivationLOG"
	 * @return An object activation function of corresponding class or null value
	 */
	private static ActivationFunction getAF(String activationFunction) {
		ActivationFunction af = null;
		switch (activationFunction) {
		case "ActivationElliottSymmetric":
			af = new ActivationElliottSymmetric();
			break;

		case "ActivationLOG":
			af = new ActivationLOG();
			break;

		default:
			throw new IllegalArgumentException("Error in naming activation function.");
		}

		return af;
	}

	/**
	 * Median Squared error computation.
	 * 
	 * @param neuralNetwork A neural network object of class BasicNetwork
	 * @param validationX   Java array of double[][], Input in neural network
	 * @param validationY   Java array of double[][], Expected value of the given
	 *                      input neural network
	 * @return A median squared error MSE between actual output of neural network
	 *         and expected value
	 */
	private static double calcuateMedianSquaredError(BasicNetwork neuralNetwork, double[][] validationX,
			double[][] validationY) {
		double error = 0;
		for (int i = 0; i < validationX.length; i++) {
			MLDataPair pair = new BasicMLDataPair(new BasicMLData(validationX[i]), new BasicMLData(validationY[i]));
			MLData result = neuralNetwork.compute(pair.getInput());
			error += Math.pow((result.getData(0) - pair.getIdealArray()[0]), 2);
//			double[] in = pair.getInputArray();
//			System.out.printf("%f\t%f\tidl: %f\tout: %f\n", in[0], in[1],pair.getIdealArray()[0], result.getData(0)); 
		}
		return error / validationX.length;
	}

	public static double getF(double x, double y) {
		return (Math.sin(x) * Math.cos(y));
	}
}
