package enemdata;

import org.nd4j.linalg.activations.Activation;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class NeuralNet {
	public static MultiLayerConfiguration getNetConfiguration () {
		return new NeuralNetConfiguration.Builder()
			.seed(123L)
			// .iterations(10000)
			.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
			// .learningRate(0.01)
			.weightInit(WeightInit.XAVIER)
			// .updater(new Nesterovs(0.9))
			.list()
			.layer(0, new DenseLayer.Builder().nIn(45).nOut(80)
						 .activation(Activation.TANH).build())
			.layer(1, new DenseLayer.Builder().nIn(80).nOut(30)
						 .activation(Activation.TANH).build())
			.layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
						 .activation(Activation.IDENTITY)
						 .nIn(30).nOut(5).build())
			.pretrain(false).backprop(true).build();
	}
}
