
/**
 * @author Donnell R. Debnam Jr
 */

import java.util.Random;

public class Matrix {

    // Actual 2D matrix of the object.
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

    // Returns a Matrix object from an array object
    public static Matrix fromArray(float[] data) {
        Matrix m = new Matrix();
        float[][] values = new float[data.length][1];
        for (int i = 0; i < data.length; i++) 
            values[i][0] = data[i];
        m.matrix = values;
        return m;
    }

    /**
     * @param activations activations of neurons in layer(i)
     * @param weights     weight matrix of layer(i) and layer(j)
     * 
     * @return products of given vector and weight matrix
     */
    public static float[] multiply(float[] activations, float[][] weights) {
        float[] product = new float[weights[1].length];
        for (int i = 0; i < weights[1].length; i++) {
            float sum = 0;
            int Iterator = 0;
            for (float a : activations) {
                sum += (a * weights[Iterator][i]);
                Iterator++;
            }
            product[i] = (float) NN.sigmoid(sum);
        }
        return product;
    }

    /**
     * @param a first matrix
     * @param b second matrix
     * 
     * @return product of given matrices
     */
    public static Matrix multiply(Matrix a, Matrix b) {
        Matrix results = new Matrix();
        float[][] a_values = a.matrix, b_values = b.matrix;
        int r1 = a_values.length, c1 = b_values.length, c2 = b_values[1].length;
        float[][] r_values = new float[r1][c2];
        for (int i = 0; i < r1; i++)
            for (int j = 0; j < c2; j++) 
                for (int k = 0; k < c1; k++) 
                    r_values[i][j] += a_values[i][k] * b_values[k][j];
        results.matrix = r_values;
        return results;
    }

    /**
     * @param a Matrix a
     * @param b Matrix b
     * @return  result of a - b.
     */
    public static Matrix subtract(Matrix a, Matrix b) {
        Matrix results = new Matrix();
        float[][] a_values = a.matrix, b_values = b.matrix;
        float[][] results_values = new float[a_values.length][a_values[1].length];
        for (int i = 0; i < a_values.length; i++) 
            for (int j = 0; j < a_values[1].length; j++) 
                results_values[i][j] = a_values[i][j] - b_values[i][j];
        results.matrix = results_values;
        return results;
    } 

    /**
     * @param m matrix that will be transposed.
     * @return the transposition of original matrix.
     */
    static Matrix transpose(Matrix m) {
        Matrix result = new Matrix();
        int len = m.matrix.length, width = m.matrix[1].length;
        float[][] transposed = new float[width][len];
        for (int i = 0; i < len; i++)
            for (int j = 0; j < width; j++)
                transposed[j][i] = m.matrix[i][j];
        result.matrix = transposed;
        return result;
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