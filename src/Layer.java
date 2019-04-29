
/**
 * @author Donnell R. Debnam Jr
 */

public class Layer {

    public int neurons = 0;     // size of layer
    public float[] activations; // array of activations (neurons)

    // Default Constructor
    public Layer() {
    }

    // Custom Constructor
    public Layer(int neurons) {
        this.neurons = neurons;
        activations = new float[neurons];
    }
    
}
