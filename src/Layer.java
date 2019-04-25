
/**
 * @author Donnell R. Debnam Jr
 */

public class Layer {

    // Number of neurons in this layer.
    public int neurons = 0;
    public float[] activations;

    // Default Constructor
    public Layer() {
    }

    // Custom Constructor
    public Layer(int neurons) {
        this.neurons = neurons;
        activations = new float[neurons];
    }

    // Set/change the number of neurons in layer.
    void setNeurons(int neurons) {
        this.neurons = neurons;
    }
}