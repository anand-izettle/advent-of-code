package se.zettle.adventofcode;

import java.util.List;
import java.util.stream.IntStream;

public class StringUtilities {

    public static <T> List<List<T>> getSlidingWindows(List<T> candidates, int windowSize){
        if (candidates.isEmpty() || windowSize > candidates.size()){
            return List.of();
        }

        List<List<T>> windows = IntStream.range(0, candidates.size() - windowSize + 1)
            .mapToObj(start -> candidates.subList(start, start + windowSize))
            .toList();

        return windows;


    }
}
