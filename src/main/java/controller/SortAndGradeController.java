package controller;

import lombok.Getter;
import model.Phone;
import model.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Класс нужен чтобы отсортировать список по необходимому признаку, для составления матрицы альтернатив */
@Getter
public class SortAndGradeController {

    private List<Phone> sortedPhones;

    private final Sort sort;

    private final String field;

    private final double[] grade;

    double[][] matrixOfAlternatives;

    public SortAndGradeController(Set<Phone> phones, Sort sort, String field) {
        this.sort = sort;
        this.grade = new double[10];
        this.field = field;

        findGrade(phones, field);
        doMatrixOfAlternatives();
    }

    private void findGrade(Set<Phone> phones, String field) {
        Comparator<Phone> comparator = (c1, c2) -> {
            try {
                Comparable value1 = (Comparable) c1.getClass().getMethod("get" + field).invoke(c1);
                Comparable value2 = (Comparable) c2.getClass().getMethod("get" + field).invoke(c2);
                return value1.compareTo(value2);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        };

        if (sort == Sort.ASC) {
            sortedPhones = phones.stream()
                    .sorted(comparator)
                    .toList();
        } else {
            sortedPhones = phones.stream()
                    .sorted(comparator.reversed())
                    .toList();
        }

        double minPrise = (double) getFieldValue(sortedPhones.get(0));
        double maxPrise = (double) getFieldValue(sortedPhones.get(sortedPhones.size() - 1));
        double d = (maxPrise - minPrise) / 8;
        grade[1] = minPrise;

        for (int i = 2; i < grade.length; i++) {
            grade[i] = grade[i - 1] + d;
        }

        if (grade[9] != maxPrise) {
            throw new RuntimeException("Ошибка в нахождении макс суммы");
        }
    }

    private void doMatrixOfAlternatives() {
        matrixOfAlternatives = new double[sortedPhones.size()][sortedPhones.size()];

        for (int i = 0; i < sortedPhones.size(); i++) {
            for (int j = i; j < sortedPhones.size(); j++) {
                if (i == j) {
                    matrixOfAlternatives[i][j] = 1;
                } else {
                    for (int g = 1; g < grade.length; g++) {
                        if (Objects.equals(getFieldValue(sortedPhones.get(j)), grade[g])) {
                            matrixOfAlternatives[i][j] = g;
                        } else if ((double) getFieldValue(sortedPhones.get(j)) < grade[g] && g != 1) {
                            matrixOfAlternatives[i][j] = g - 1;
                            break;
                        }
                    }
                    matrixOfAlternatives[j][i] = matrixOfAlternatives[i][i] / matrixOfAlternatives[i][j];
                }
            }
        }
    }

    private Object getFieldValue(Phone phone) {
        try {
            return phone.getClass().getMethod("get" + field).invoke(phone);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
