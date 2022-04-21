import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class LabProject {

	private static final double PI = Math.PI;

	public static void main(String[] args) {

		double[][] trainingX = new double[1000][2];
		double[][] trainingY = new double[1000][1];

		double[][] validationX = new double[1000][2];
		double[][] validationY = new double[1000][1];

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
		int MAX_ITERATIONS = 100;
		int numOfHiddenNeurons = 10;
		BasicNetwork neuronalNetwork = new BasicNetwork();
		neuronalNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true, 2));
		neuronalNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true, numOfHiddenNeurons));
		neuronalNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true, 1));
		neuronalNetwork.getStructure().finalizeStructure();
		neuronalNetwork.reset();

		MLDataSet trainingSet = new BasicMLDataSet(trainingX, trainingY);

		Backpropagation train = new Backpropagation(neuronalNetwork, trainingSet);
		for(int i = 0; i<MAX_ITERATIONS ; i++) {
			train.iteration();
			System.out.println ("Error " + train.getError());
		}
		
		System.out.println("done");
		train.finishTraining();
		
		
		
		/*
		 * for(MLDataPair pair : trainingSet) { MLData result =
		 * neuronalNetwork.compute(pair.getInput()); double[] in = pair.getInputArray();
		 * System.out.printf("%f\t%f\tidl: %f\tout: %f\n", in[0], in[1],
		 * pair.getIdealArray()[0], result.getData()[0]); }
		 */
	}

	public static double getF(double x, double y) {
		return (Math.sin(x) * Math.cos(y));
	}
}
