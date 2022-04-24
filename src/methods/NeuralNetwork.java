package methods;

import org.encog.engine.network.activation.ActivationElliottSymmetric;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationLOG;
import org.encog.engine.network.activation.ActivationLinear;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class NeuralNetwork {

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
	public static BasicNetwork createNeuralNewtwork(int numOfHiddenNeurons, String activationFunction) {
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
	public static double calcuateMedianSquaredError(BasicNetwork neuralNetwork, double[][] validationX,
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
}
