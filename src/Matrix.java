
/**
 * @author Donnell R. Debnam Jr
 */

import java.util.Random;

public class Matrix {

    /*
     * 2-D Array backend implementation of our neural network weight matrix.
     */
    public float[][] matrix;

    public Matrix() {}

    // Matrix Builder
    public Matrix(Layer L0, Layer L1) {
        Random r = new Random();
        matrix = new float[L0.neurons][L1.neurons];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[1].length; j++)
                matrix[i][j] = 0 + r.nextFloat() * (1 - 0);
    }

    /* Turns 2D matrix into 1D vector. */
    void Flatten() {
        float[][] flat = new float[matrix.length * matrix[1].length][1];
        int iterator = 0;
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[1].length; j++) {
                flat[iterator][0] = matrix[i][j];
                iterator++;
            }
        this.matrix = flat;
    }

    /* Rotates matrix for backpropagation. */
    static float[][] rotate(Matrix m) {
        int len = m.matrix.length, width = m.matrix[1].length;
        float[][] rotated = new float[width][len];
        for (int i = 0; i < len; i++)
            for (int j = 0; j < width; j++)
                rotated[j][i] = m.matrix[i][j];
        return rotated;
    }

    @Override
    public String toString() {
        if (matrix.length == 0 && matrix[1].length == 0)
            return "[]";
        String str = "[";
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[1].length; j++) {
                str += matrix[i][j];
                if (j + 1 == matrix[1].length) {
                    if (i + 1 == matrix.length)
                        str += "]";
                    str += "\n ";
                } else
                    str += ", ";
            }
        }
        return str;
    }
}