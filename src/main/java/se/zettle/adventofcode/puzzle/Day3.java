package se.zettle.adventofcode.puzzle;

import static java.util.stream.Collectors.toMap;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.zettle.adventofcode.provider.InputProvider;

@Component
@Slf4j
public class Day3 {

    protected static record Solution(Long gammaEpsilon, Long oxygenCO2) {
    }

    protected static record Rate(String gammaOrOxygen, String epsilonOrCO2) {
        Long gamaOrOxygenDecimal() {
            return Long.parseLong(gammaOrOxygen(), 2);
        }

        Long epsilonOrCo2Decimal() {
            return Long.parseLong(epsilonOrCO2(), 2);
        }

        Long getProduct() {
            return gamaOrOxygenDecimal() * epsilonOrCo2Decimal();
        }
    }

    public Solution solve() {

        log.info("*".repeat(50));
        log.info("Day3: puzzle start...");

        String puzzleInput = InputProvider.getPuzzleInput("day3");
        List<String> readings = getReadings(puzzleInput);
        Map<Integer, List<Integer>> positionReadings = getPositionReadings(readings);
        Map<Integer, Integer> mostCommonBits = getMostCommonBits(positionReadings);

        Rate gammaEpsilonRate = getGammaEpsilon(mostCommonBits);

        Long gammaEpsilon = gammaEpsilonRate.getProduct();
        log.info("gammaEpsilon rates are: {}, product is: {}", gammaEpsilonRate, gammaEpsilon);

        Rate oxygenCo2Rate = getOxygenCO2(readings);

        Long oxygenCo2 = oxygenCo2Rate.getProduct();
        log.info("oxygenCo2 Rates are: {}, product is: {}",oxygenCo2Rate, oxygenCo2);

        log.info("Day3: puzzle solved !!");
        log.info("*".repeat(50));


        return new Solution(gammaEpsilon, oxygenCo2);

    }

    protected Rate getGammaEpsilon(final Map<Integer, Integer> mostCommonBits) {

        String gamma = mostCommonBits.values()
            .stream()
            .map(String::valueOf)
            .collect(Collectors.joining());

        String espilon = gamma.replace("0", "2")
            .replace("1", "0")
            .replace("2", "1");

        return new Rate(gamma, espilon);

    }

    protected Day3.Rate getOxygenCO2(final List<String> readings) {

        List<String> tempOxygen = new ArrayList<>(readings);
        List<String> tempCo2 = new ArrayList<>(readings);
        String oxygen;
        String reading = readings.get(0);

        for (int i = 0; i < reading.length(); i++) {
            if (tempOxygen.size() == 1 && tempCo2.size() == 1) {
                break;
            }
            if (tempOxygen.size() > 1) {
                tempOxygen = onlyCommonBits(tempOxygen, i, true);
            }
            if (tempCo2.size() > 1) {
                tempCo2 = onlyCommonBits(tempCo2, i, false);
            }
        }

        oxygen = tempOxygen.get(0);

        String co2 = tempCo2.get(0);

        return new Day3.Rate(oxygen, co2);
    }

    private List<String> onlyCommonBits(final List<String> tempReadings, final int position, final boolean keep) {

        Integer common = getMostCommonBitOnPosition(tempReadings, position);

        return filterMostCommon(tempReadings, position, common, keep);

    }

    protected List<String> filterMostCommon(List<String> readings, Integer position, Integer bit, final boolean keep) {
        if (readings.size() == 1) {
            return readings;
        }

        return readings.stream()
            .filter(s -> {
                String bitAtPos = s.substring(position, position + 1);
                String bitValue = String.valueOf(bit);
                boolean found = bitAtPos.equalsIgnoreCase(bitValue);
                return keep == found;

            })
            .toList();
    }

    protected Map<Integer, List<Integer>> getPositionReadings(List<String> readings) {
        Map<Integer, List<Integer>> bitsByPosition = new HashMap<>();

        readings
            .forEach(s -> {
                char[] bits = s.toCharArray();
                IntStream.range(0, bits.length)
                    .forEach(i -> {
                        List<Integer> positionBits = bitsByPosition.computeIfAbsent(i, mf -> new ArrayList<>());
                        positionBits.add(Integer.valueOf(String.valueOf(bits[i])));
                    });
            });

        return bitsByPosition;
    }

    protected Map<Integer, Integer> getMostCommonBits(final Map<Integer, List<Integer>> positionReadings) {
        return positionReadings.entrySet()
            .stream()
            .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), getMostCommonBit(e.getValue())))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Integer getMostCommonBit(final List<Integer> bits) {
        Map<Integer, Long> bitMap = bits.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Long zeroCount = Optional.ofNullable(bitMap.get(0)).orElse(0L);
        Long oneCount = Optional.ofNullable(bitMap.get(1)).orElse(0L);
        return zeroCount > oneCount ? 0 : 1;

    }

    protected List<String> getReadings(final String puzzleInput) {
        return Arrays.stream(puzzleInput.stripIndent().split("\n"))
            .toList();
    }

    protected Integer getMostCommonBitOnPosition(List<String> readings, Integer position) {

        Map<String, Long> bitMap = readings.stream()
            .map(s -> s.substring(position, position + 1))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Long zeroCount = Optional.ofNullable(bitMap.get("0")).orElse(0L);
        Long oneCount = Optional.ofNullable(bitMap.get("1")).orElse(0L);

        return zeroCount > oneCount ? 0 : 1;

    }
}
