import java.util.ArrayList;
import java.util.List;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;


public class LabProject {
	
	private static final double PI = Math.PI;
	
	public static void main(String[] args) {
		
		List<Point> training = new ArrayList<Point>();
		List<Point> validation = new ArrayList<Point>();
		
		for (int i = 0; i < 1000; i++) {
			double x = Math.random() * (PI + PI) -PI;
			double y = Math.random() * (PI + PI) -PI ;
			training.add(new Point(x,y));
			double x1 = Math.random() * (PI + PI) -PI;
			double y1 = Math.random() * (PI + PI) -PI ;
			validation.add(new Point(x1,y1)) ;
		}
		int numOfHiddenNeurons = 1;
		BasicNetwork neuronalNetwork = new BasicNetwork();
		neuronalNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true, 2));
		neuronalNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true, numOfHiddenNeurons));
		neuronalNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true, 1));
		neuronalNetwork.getStructure().finalizeStructure();
		neuronalNetwork.reset();
		

		// need to learn how to create a dataset;
		Backpropagation train = new Backpropagation(neuronalNetwork, null, 0.1, 0.3);
		
		//System.out.println(training);	
	}
}
