package alternative;

import controller.SortAndGradeController;
import model.Phone;
import model.Sort;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

/** Реализует таблицу альтернатив, из которой можно получить вес альтернативы в отсортированном списке камер */
public class NormalizedTable {
    private final SortAndGradeController controller;
    private final HashMap<Phone,List<Double>> phones;
    double[] avgGeom;
    double sumOfAvgGeom;

    double[] alternativeWeight;

    public NormalizedTable(HashMap<Phone,List<Double>> phones, Sort type, String field) {
        this.controller = new SortAndGradeController(phones.keySet(), type,field);
        this.phones = phones;
        findAvgGeom();
        findAlternativeWeight();
    }



    private void findAvgGeom() {
        int length = controller.getMatrixOfAlternatives().length;
        avgGeom = new double[length];

        MathContext mc = new MathContext(10, RoundingMode.HALF_UP); // Устанавливаем точность

        for (int i = 0; i < length; i++) {
            BigDecimal product = BigDecimal.ONE;
            for (double num : controller.getMatrixOfAlternatives()[i]) {
                // Проверяем на ноль и отрицательные значения
                if (num <= 0) {
                    throw new IllegalArgumentException("Все числа должны быть положительными и ненулевыми.");
                }
                // Округляем до двух знаков после запятой
                BigDecimal bd = new BigDecimal(Double.toString(num)).setScale(2, RoundingMode.HALF_UP);
                product = product.multiply(bd, mc);
            }
            BigDecimal nthRoot = nthRoot(product, controller.getMatrixOfAlternatives()[i].length, mc);
            avgGeom[i] = nthRoot.doubleValue();
        }

        sumOfAvgGeom = 0;
        for (double a : avgGeom) {
            sumOfAvgGeom += a;
        }
    }

    private BigDecimal nthRoot(BigDecimal value, int n, MathContext mc) {
        BigDecimal x0 = new BigDecimal(Math.pow(value.doubleValue(), 1.0 / n), mc);
        BigDecimal x1;
        BigDecimal nBigDecimal = new BigDecimal(n);

        while (true) {
            x1 = x0.multiply(new BigDecimal(n - 1), mc)
                    .add(value.divide(x0.pow(n - 1, mc), mc), mc)
                    .divide(nBigDecimal, mc);
            if (x0.subtract(x1).abs().compareTo(new BigDecimal(1e-10)) < 0) {
                break;
            }
            x0 = x1;
        }

        return x1;
    }

    public HashMap<Phone,List<Double>> getMapWithAlternatives() {
        int i = 0;
        for(var phone : controller.getSortedPhones()) {
            phones.get(phone).add(alternativeWeight[i]);
            i++;
        }
        return phones;
    }

    private void findAlternativeWeight() {
        alternativeWeight = new double[avgGeom.length];

        for (int i = 0; i < alternativeWeight.length; i++) {
            alternativeWeight[i] = avgGeom[i] / sumOfAvgGeom;
        }
    }
}
