
/**
 * @author Donnell R. Debnam Jr
 */

import java.util.*;

public class NN {

    /* Attributes of the Network */
    public LinkedList<Layer> nn = new LinkedList<>();

    public InputLayer input;
    public OutputLayer output;

    public int size = 0;
    public float[][] pattern;

    /* Custom Constructor for NeuralNetwork. */
    public NN(int inputNeurons) {
        input = new InputLayer(inputNeurons);
        nn.add(input);
        size++;
    }

    /* Adds new layer to NN. */
    private void append(Layer layer) {
        nn.add(layer);
        size++;
    }

    /* Returns value crunched through sigmoid function */
    public double sigmoid(float x) {
        return 1 / (1 + Math.exp(-x));
    }

    /* Multiplies a vector by a weight matrix */
    private float[] multiply(float[] activations, float[][] weights) {
        float[] product = new float[weights[1].length];
        for (int i = 0; i < weights[1].length; i++) {
            float sum = 0;
            int Iterator = 0;
            for (float a : activations) {
                sum += (a * weights[Iterator][i]);
                Iterator++;
            }
            product[i] = (float) sigmoid(sum);
        }
        return product;
    }

    /* Sets pattern to the Network training ground. */
    private void setPattern(float[][] pattern) {
        this.pattern = pattern;
    }

    /* Imports pattern to training ground. */
    private float[][] importPattern() {
        return pattern;
    }

    /* Entire feedforward algorithm. */
    public void Train(int hiddenLayers, float[][] p) {

        /* Network with 25 input neurons */
        int s = p.length *  p[1].length;
        NN network = new NN(s);

        /* Number of Hidden Layers. */
        final int HIDDEN_LAYERS = hiddenLayers;

        /* Maps pairs of layers to a weight matrix. */
        HashMap<HashSet<Layer>, Matrix> map = new HashMap<>();

        /* Add (3) new HiddenLayers to the Network. */
        for (int i = 0; i < HIDDEN_LAYERS; i++) 
            network.append(new HiddenLayer(3)); // 3 neurons in each layer
        
        /* Add Output Layer. */
        network.append(new OutputLayer());

        /* Give each pair of parallel Layers a matrix. */
        for (int i = 0; i < network.nn.size()-1; i++) {
            HashSet<Layer> set = new HashSet<>();
            Layer layer_i, layer_j;
            layer_i = network.nn.get(i);
            layer_j = network.nn.get(i + 1);
            set.add(layer_i);
            set.add(layer_j);
            map.put(set, new Matrix(layer_i, layer_j));
        }

        /* Input Matrix for unique pattern */
        network.setPattern(p);
        float[][] MY_PATTERN = network.importPattern();

        Matrix pattern = new Matrix(); // Create new Matrix for pattern
        pattern.matrix = MY_PATTERN; // Set matrix to new pattern

        pattern.Flatten(); // Flatten Matrix to 1D
        network.input.vector = pattern; // Set pattern to input layer.

        /* Copy pattern to 1D input layer. */
        for (int i = 0; i < network.input.neurons; i++) 
            network.input.activations[i] = pattern.matrix[i][0];
    
            /**
             * Here, we actually feedforward through the network. Layers are interconnected through the
             * NN's LinkedList structure, in which each layer of activations and weight matrices
             * are multipled and passed along. Finally, the activations of our output layers are printed.
             */

        Matrix weightMatrix;            // Dynamic weight matrix from layer-to-layer
        float[] nextLayerActivations;   // Dynamic activations from layer-to-layer

        // Feed forward through the network.
        for (int i = 0; i < network.size-1; i++) {
            weightMatrix = new Matrix(network.nn.get(i), network.nn.get(i+1));
            if (i == 0) 
                nextLayerActivations = network.multiply(network.input.activations, weightMatrix.matrix);
            else 
                nextLayerActivations = network.multiply(network.nn.get(i).activations, weightMatrix.matrix);
            network.nn.get(i+1).activations = nextLayerActivations;
        }

            /** 
             * We now backpropagate through the network and update weights for each
             * set of parallel layers. The final hiddenlayer-to-outputlayer matrix will
             * be scored differently with respect to targetted outputs of that layer.
             */

        // Reverse Layers of Network
        Collections.reverse(network.nn);
        
        /* Go through each set of parallel layers and update matrices. */
        for (int k = 0; k < /*network.nn.size()-*/1; k++) {

            // Get matrix from output to last hidden layer
            HashSet<Layer> set = new HashSet<>(Arrays.asList(
                                    network.nn.get(k), network.nn.get(k+1)));
            Matrix weights = new Matrix();

            // Retreive weight matrix for the 2 parallel layers.
            weights = map.get(set);
            weights.matrix = Matrix.rotate(weights);

            System.out.println("Original Hidden to Output Layer Matrix \n\n" + weights);

            // Update entire weight matrix
            for (int i = 0; i < weights.matrix.length; i++) {

                for (int j = 0; j < weights.matrix[1].length; j++) {
                    
                    float w = weights.matrix[i][j]; // weight we are adjusting

                    // Attributes to calculate Delta of Weight w
                    float x = (network.nn.get(i+1)).activations[i];         // Neuron from previous layer (i+1 because of reverse)
                    float target = Math.round(Math.random());               // Random "target" output (0 or 1)
                    float out = network.nn.get(i).activations[i];           // Actual output neuron activation

                    float Δw = (x) * (out - target) * (out) * (out - 1);    // Delta (Rate of Change/Variation)
                    weights.matrix[i][j] = w + Δw;                          // New weight
                }
            }
            System.out.println("\nAfter Gradient Descent:\n\n" + weights);

        }

    }

}
