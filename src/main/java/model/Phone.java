package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Phone {
    private String model; //модель
    private double camera; //камера в мп
    private double storageIncluded; // встроенная память
    private double workingTime; //время работы
    private double style;
    private double price; // цена

    private double globalPriorityValue; //оценка глобального приоритета
}
