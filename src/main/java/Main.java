import alternative.NormalizedTable;
import com.opencsv.exceptions.CsvValidationException;
import globalpriorities.GlobalPriorities;
import model.Phone;
import model.Sort;
import parce.Reader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) throws CsvValidationException, IOException {
        //читаем csv файл с данными
        HashMap<Phone, List<Double>> phones = Reader.read();

        //Вычисляем нормализованные оценки вектора приоритетов
        NormalizedTable camera = new NormalizedTable(phones, Sort.ASC,"Camera");
        phones = camera.getMapWithAlternatives();

        NormalizedTable storage = new NormalizedTable(phones, Sort.ASC,"StorageIncluded");
        phones = storage.getMapWithAlternatives();

        NormalizedTable workingTime = new NormalizedTable(phones, Sort.ASC,"WorkingTime");
        phones = workingTime.getMapWithAlternatives();

        NormalizedTable style = new NormalizedTable(phones, Sort.ASC,"Style");
        phones = style.getMapWithAlternatives();

        NormalizedTable price = new NormalizedTable(phones, Sort.ASC,"Price");
        phones = price.getMapWithAlternatives();

        //Вычисляем глобальные приоритеты
        GlobalPriorities gp = new GlobalPriorities(phones);
        //Ищем и выводим лучшую альтернативу
        System.out.println(gp.getBestPhone());
    }
}


