
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

    /* NeuralNet Builder */
    public NN(int inputNeurons) {
        input = new InputLayer(inputNeurons);
        nn.add(input);
        size++;
    }

    /** 
     * @param layer new layer to be added to NeuralNet
     */
    private void append(Layer layer) {
        nn.add(layer);
        size++;
    }

    /** 
     * @param x any numeric value [activation]
     * @return 'x' value crunched through sigmoid function 
     */
    public static double sigmoid(float x) {
        return 1 / (1 + Math.exp(-x));
    }

    /** 
     * @param pattern input pattern to be set to the nn training ground
     */
    private void setPattern(float[][] pattern) {
        this.pattern = pattern;
    }

    /** 
     * @return pattern attribute of matrix that is being used 
     *         to train the network. 
     */
    private float[][] importPattern() {
        return pattern;
    }

    /** 
     * Entire training algorithm that takes in an input pattern and through multiple
     * layers of the network, feeds forward and backpropagates to correct errors in
     * each weight matrix.
     * 
     * @param hiddenLayers  number of hidden layers used to train the network
     * @param p input pattern that will be trained by the network
     */
    public void Train(float[][] input, float[] targets) {

        /* Network with L x W input neurons */
        int s = input.length *  input[1].length;
        NN network = new NN(s);

        /* Number of Hidden Layers. */
        final int HIDDEN_LAYERS = 24;

        /* Maps pairs of layers to a weight matrix. */
        HashMap<HashSet<Layer>, Matrix> map = new HashMap<>();

        /* Add n new HiddenLayers to the Network. */
        for (int i = 0; i < HIDDEN_LAYERS; i++) 
            network.append(new HiddenLayer(3));
        
        /* Add Output Layer. */
        network.append(new OutputLayer(targets.length));

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
        network.setPattern(input);
        float[][] MY_PATTERN = network.importPattern();

        Matrix pattern = new Matrix();  // Create new Matrix for input.
        pattern.matrix = MY_PATTERN;    // Set matrix to input pattern.

        pattern.Flatten();              // Flatten Matrix to single dimension.
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
                nextLayerActivations = Matrix.multiply(network.input.activations, weightMatrix.matrix);
            else 
                nextLayerActivations = Matrix.multiply(network.nn.get(i).activations, weightMatrix.matrix);
            network.nn.get(i+1).activations = nextLayerActivations;
        }

        // Print ouput neuron activations for this training example
        System.out.println("Output neuron 1: " + network.nn.get(network.size-1).activations[0]);
        System.out.println("Output neuron 2: " + network.nn.get(network.size-1).activations[1]);
        System.out.println("Output neuron 3: " + network.nn.get(network.size-1).activations[2]);
        System.out.println();

            /** 
             * We now backpropagate through the network and update weights for each
             * set of parallel layers. The final hiddenlayer-to-outputlayer matrix will
             * be scored differently with respect to targetted outputs of that layer.
             */

        // Reverse Layers of Network
        Collections.reverse(network.nn);
        
        // Convert actual & target arrays to matrices.
        Matrix output_vector = Matrix.fromArray(network.nn.get(0).activations);
        Matrix target_vector = Matrix.fromArray(targets);

        // Calculate errors from last hidden to output layer.
        Matrix output_errors = Matrix.subtract(target_vector, output_vector);

        // Get weight matrix from these layers.
        HashSet<Layer> t = new HashSet<>(Arrays.asList(
                            network.nn.get(0), network.nn.get(1)));
        Matrix who = map.get(t);  // weights (w) from hidden (h) to output (o)

        // Map to associate each weight matrix with an error matrix.
        HashMap<Layer, Matrix> error_map = new HashMap<>();

        // Map output layer to output errors.
        error_map.put(network.nn.get(0), output_errors);

        /**
         * In order to calculate all errors for each layer in the network, we
         * must loop through the layers, store previous errors, and recurse.
         */
        for (int i = 0; i < network.nn.size()-1; i++) {

            // Get weight matrix of current layer and its successor.
            HashSet<Layer> set = new HashSet<>();
            Layer layer_i, layer_j;
            layer_i = network.nn.get(i);
            layer_j = network.nn.get(i + 1);
            set.add(layer_i);
            set.add(layer_j);

            // Get the weight matrix and error of current layer.
            Matrix m = map.get(set);
            Matrix e = error_map.get(network.nn.get(i));

            // Calculate new error for next layer
            Matrix z = Matrix.multiply(m, e);

            // Store layer with its error.
            error_map.put(network.nn.get(i+1), z);
        }

        /* Go through each set of parallel layers and update matrices. 
        for (int k = 0; k < network.nn.size()-1; k++) {

            // Get matrix from output to last hidden layer
            HashSet<Layer> set = new HashSet<>(Arrays.asList(
                                    network.nn.get(k), network.nn.get(k+1)));

            // Retreive weight matrix for the 2 parallel layers
            // WM is rotated so that the FROM vertices are now the TO vertices; vice versa
            Matrix weights = map.get(set);
            weights.matrix = Matrix.rotate(weights);

            System.out.println("Original Hidden to Output Layer Matrix \n\n" + weights);

            // Update entire weight matrix
            for (int i = 0; i < weights.matrix.length; i++) {

                for (int j = 0; j < weights.matrix[1].length; j++) {
                    
                    float w = weights.matrix[i][j];                         // Weight we are adjusting

                    // Attributes to calculate Delta of Weight w
                    float x = (network.nn.get(i+1)).activations[i];         // Neuron from previous layer (i+1 because of reverse)
                    float target = Math.round(Math.random());               // Random "target" output (0 or 1)
                    float out = network.nn.get(i).activations[i];           // Actual output neuron activation

                    float Δw = (x) * (out - target) * (out) * (out - 1);    // Calculate Delta
                    weights.matrix[i][j] = w + Δw;                          // New weight
                }
            }
            System.out.println("\nAfter Gradient Descent:\n\n" + weights);

        }*/

    }

}
