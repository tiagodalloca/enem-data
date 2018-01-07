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
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.learning.config.AdaDelta;
import org.nd4j.linalg.learning.config.AdaMax;

public class NeuralNet {

	public static double momentum = 0.7;

	public static double learning_rate = 0.0000001;

	public static MultiLayerConfiguration getNetConfiguration () {
		return new NeuralNetConfiguration.Builder()
			.seed(123L)
			.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
			.updater(new Nesterovs(learning_rate, momentum))
			.weightInit(WeightInit.XAVIER)
			.l2(0.001)
			.list()
			.layer(0, new DenseLayer.Builder().nIn(45).nOut(500)
						 .activation(Activation.RELU).build())
			// .layer(1, new DenseLayer.Builder().nIn(100).nOut(200)
			// 			 .activation(Activation.RELU).build())
			// .layer(2, new DenseLayer.Builder().nIn(200).nOut(400)
			// 			 .activation(Activation.RELU).build())
			.layer(1, new DenseLayer.Builder().nIn(500).nOut(1000)
						 .activation(Activation.RELU).build())
			.layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
						 .activation(Activation.IDENTITY)
						 .nIn(1000).nOut(5).build())
			.pretrain(false).backprop(true).build();
	}
}
