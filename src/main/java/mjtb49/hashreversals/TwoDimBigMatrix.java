package mjtb49.hashreversals;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TwoDimBigMatrix {

    private BigDecimal[][] matrix;
    private final int SIZE = 2;
    private final int SCALE = 200;

    protected TwoDimBigMatrix() {
        matrix = new BigDecimal[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                matrix[row][col] = BigDecimal.ZERO;
    }

    protected TwoDimBigMatrix(BigDecimal[][] matrix) {
        this.matrix = matrix;
    }

    protected TwoDimBigMatrix(TwoDimBigMatrix matrix) {
        this.matrix = new BigDecimal[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                this.matrix[row][col] = matrix.getElement(row,col);
    }

    protected BigDecimal getElement(int row, int col) {
        return matrix[row][col];
    }

    protected void setElement(BigDecimal element,int row, int col) {
        matrix[row][col] = element;
    }

    protected void setElement(long element,int row, int col) {
        matrix[row][col] = BigDecimal.valueOf(element).setScale(SCALE,RoundingMode.HALF_UP);
    }

    protected void setElement(int element,int row, int col) {
        matrix[row][col] = BigDecimal.valueOf(element).setScale(SCALE,RoundingMode.HALF_UP);
    }

    protected TwoDimBigVector getRow(int i) {
        return new TwoDimBigVector(matrix[i][0], matrix[i][1]);
    }

    protected TwoDimBigVector getCol(int i) {
        return new TwoDimBigVector(matrix[0][i], matrix[1][i]);
    }

    protected void setRow(int row, TwoDimBigVector newRow) {
        for (int col = 0; col < SIZE; col++) {
            matrix[row][col] = newRow.getElement(col);
        }
    }

    protected void setCol(int col, TwoDimBigVector newCol) {
        for (int row = 0; row < SIZE; row++) {
            matrix[row][col] = newCol.getElement(row);
        }
    }

    protected TwoDimBigMatrix scale(BigDecimal scalar) {
        TwoDimBigMatrix result = new TwoDimBigMatrix();
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                result.setElement(this.getElement(row,col).multiply(scalar),row,col);
        return result;
    }

    protected TwoDimBigMatrix multiply(TwoDimBigMatrix rightHandMatrix) {
        TwoDimBigMatrix result = new TwoDimBigMatrix();
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                result.setElement(rightHandMatrix.getCol(col).dot(this.getRow(row)),row,col);
        return result;
    }

    protected TwoDimBigVector multiply(TwoDimBigVector vec) {
        TwoDimBigVector result = new TwoDimBigVector();
        result.setElement(0, getRow(0).dot(vec));
        result.setElement(1, getRow(1).dot(vec));
        return result;
    }

    protected TwoDimBigMatrix transpose() {
        TwoDimBigMatrix result = new TwoDimBigMatrix();
        result.setElement(getElement(0,0), 0,0);
        result.setElement(getElement(1,0), 0,1);
        result.setElement(getElement(0,1), 1,0);
        result.setElement(getElement(1,1), 1,1);
        return result;
    }

    protected BigDecimal det() {
        return matrix[0][0].multiply(matrix[1][1]).subtract(matrix[1][0].multiply(matrix[0][1]));
    }

    protected TwoDimBigMatrix inverse() {
        TwoDimBigMatrix inv = new TwoDimBigMatrix();
        BigDecimal det = this.det();
        assert (!det.equals(BigDecimal.ZERO));
        inv.setElement(matrix[1][1],0,0);
        inv.setElement(matrix[0][0],1,1);
        inv.setElement(matrix[1][0].negate(),1,0);
        inv.setElement(matrix[0][1].negate(),0,1);
        inv = inv.scale(BigDecimal.ONE.setScale(SCALE,RoundingMode.HALF_UP).divide(det, RoundingMode.HALF_UP));
        return inv;
    }

    protected TwoDimBigMatrix swapRows() {
        TwoDimBigMatrix result = new TwoDimBigMatrix();
        result.setRow(0, this.getRow(1));
        result.setRow(1, this.getRow(0));
        return result;
    }

    protected TwoDimBigMatrix swapCols() {
        TwoDimBigMatrix result = new TwoDimBigMatrix();
        result.setCol(0, this.getCol(1));
        result.setCol(1, this.getCol(0));
        return result;
    }

    @Override
    public String toString() {
        return "[ "+ matrix[0][0] +" " + matrix[0][1]+" ]\n" + "[ "+ matrix[1][0] +" " + matrix[1][1]+" ]";
    }
}
