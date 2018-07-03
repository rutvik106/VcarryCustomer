package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Weights {

    private final List<String> weights;

    private static final String weightsString = "0-500 Kg\n" +
            "500-800 Kg\n" +
            "800-1000 Kg\n" +
            "1200-1300 Kg\n" +
            "1300-1400 Kg\n" +
            "1400-1500 Kg\n" +
            "1500-1600 Kg\n" +
            "1600-1700 Kg\n" +
            "1800-1900 Kg\n" +
            "1900-2000 Kg\n" +
            "2 - 2.5 Tonne\n" +
            "2.5 - 3 Tonne\n" +
            "3 - 3.5 Tonne\n" +
            "3.5 - 4.0 Tonne\n" +
            "4 - 4.5 Tonne\n" +
            "4.5 - 5.0 Tonne\n" +
            "5 - 5.5 Tonne\n" +
            "5.5 - 6.0 Tonne\n" +
            "6.0 - 6.5 Tonne \n" +
            "6.5 - 7.0 Tonne\n" +
            "7.0 - 7.5 Tonne\n" +
            "7.5 - 8.0 Tonne\n" +
            "8.0 - 8.5 Tonne\n" +
            "8.5 - 9.0 Tonne\n" +
            "9.0 - 9.5 Tonne\n" +
            "9.5 - 10.0 Tonne";

    public Weights() {
        weights = new ArrayList<>();

        final String[] list = weightsString.split("\n");

        Collections.addAll(weights, list);
    }

    public List<String> getWeights() {
        return weights;
    }
}
