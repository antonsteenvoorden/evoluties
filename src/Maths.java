public class Maths {

   /** sqrt(a^2 + b^2) without under/overflow. **/

   public static double hypot(double a, double b) {
      double r;
      if (Math.abs(a) > Math.abs(b)) {
         r = b/a;
         r = Math.abs(a)*Math.sqrt(1+r*r);
      } else if (b != 0) {
         r = a/b;
         r = Math.abs(b)*Math.sqrt(1+r*r);
      } else {
         r = 0.0;
      }
      return r;
   }

   public static Matrix normalize(Matrix m) {
    int numRows = m.getRowDimension();
    int numCols = m.getColumnDimension();

    // compute the sum of the matrix
    double sum = 0;
    for (int i = 0; i < numRows; i++) {
        for (int j = 0; j < numCols; j++) {
            sum += m.get(i, j);
        }
    }

    // normalize the matrix
    Matrix normalizedM = new Matrix(numRows, numCols);
    for (int i = 0; i < numRows; i++) {
        for (int j = 0; j < numCols; j++) {
            normalizedM.set(i, j, m.get(i, j) / sum);
        }
    }

    return normalizedM;
  }

  public static double sum(Matrix m) {
    int numRows = m.getRowDimension();
    int numCols = m.getColumnDimension();
    double sum = 0;
    // loop through the rows and compute the sum
    for (int i = 0; i < numRows; i++) {
        for (int j = 0; j < numCols; j++) {
            sum += m.get(i, j);
        }
    }
    return sum;
  }

  public static Matrix ones(int numRows, int numCols) {
    return new Matrix(numRows, numCols, 1);
  }

  public static Matrix identity (int m) {
     Matrix A = new Matrix(m,m);
     double[][] X = A.getArray();
     for (int i = 0; i < m; i++) {
        for (int j = 0; j < m; j++) {
           X[i][j] = (i == j ? 1.0 : 0.0);
        }
     }
     return A;
  }
}
