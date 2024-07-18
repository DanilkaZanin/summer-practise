package globalpriorities;

import model.NormalVectorGrades;
import model.Phone;

import java.util.*;

public class GlobalPriorities {
    private final HashMap<Phone, List<Double>> phones;

    public GlobalPriorities(HashMap<Phone, List<Double>> phones) {
        this.phones = phones;
        putGlobalPriority();
    }

    private void putGlobalPriority() {
        for (Map.Entry<Phone, List<Double>> entry : phones.entrySet()) {
            double globalPriorityValue = 0;
            for (int i = 0; i < entry.getValue().size(); i++) {
                globalPriorityValue += entry.getValue().get(i) * NormalVectorGrades.vector.get(i);
            }
            entry.getKey().setGlobalPriorityValue(globalPriorityValue);
        }
    }

    public Phone getBestPhone() {
        Comparator<Phone> comparator = (c1, c2) -> (int) (c1.getGlobalPriorityValue() - c2.getGlobalPriorityValue());
        return phones.keySet().stream().max(comparator).orElse(null);
    }
}
