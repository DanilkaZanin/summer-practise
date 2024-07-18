package parce;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import exceptions.NoSuchStyleException;
import model.Phone;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Reader {
    public static HashMap<Phone, List<Double>> read() throws IOException, CsvValidationException {
        HashMap<Phone, List<Double>> phones = new HashMap<>();
        CSVReader csvReader = new CSVReaderBuilder(new FileReader("phone.csv")).build();

        String[] nextLine;

        while ((nextLine = csvReader.readNext()) != null) {
            Phone phone = new Phone();
            phone.setModel(nextLine[0]);
            phone.setCamera(Double.parseDouble(nextLine[1]));
            phone.setStorageIncluded(Double.parseDouble(nextLine[2]));
            phone.setWorkingTime(Double.parseDouble(nextLine[3]));
            phone.setStyle(parseStyle(nextLine[4]));

            phones.put(phone, new ArrayList<>());
        }


        csvReader.close();
        return phones;
    }

    private static int parseStyle(String style) {
        return switch (style) {
            case "Плохой" -> 1;
            case "Средний" -> 2;
            case "Хороший" -> 3;
            case "Очень хороший" -> 4;
            case "Лучший" -> 5;
            default -> throw new NoSuchStyleException(style);
        };
    }
}
