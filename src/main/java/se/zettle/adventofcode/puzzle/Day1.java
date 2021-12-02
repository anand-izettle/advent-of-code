package se.zettle.adventofcode.puzzle;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.zettle.adventofcode.StringUtilities;
import se.zettle.adventofcode.provider.InputProvider;

@Component
@Slf4j
public class Day1 {

    private static record MeasurementPair(Long previous, Long Current) {
    }

    private static record MeasurementWindowPair(List<Long> previous, List<Long> current) {
    }

    public Long solve() {

        log.info("*".repeat(50));
        log.info("Day1: puzzle start...");

        String puzzleInput = InputProvider.getPuzzleInput("day1");
        List<Long> measurements = Arrays.stream(puzzleInput.split("\n"))
            .map(Long::parseLong)
            .toList();

        List<MeasurementPair> increasedMeasurements = getIncreasedMeasurements(measurements);

        log.info("For the 1st (*); Found total: {} increased measurements", increasedMeasurements.size());

        List<MeasurementWindowPair> increasedMeasurementWindows =
            getIncreasedMeasurementWindows(measurements);
        log.info("For the 2nd (*); Found total: {} increased measurement windows", increasedMeasurementWindows.size());

        log.info("Day1: puzzle solved !!");
        log.info("*".repeat(50));

        return (long) increasedMeasurementWindows.size();
    }

    private static List<MeasurementWindowPair> getIncreasedMeasurementWindows(final List<Long> measurements) {
        List<List<Long>> windows = StringUtilities.getSlidingWindows(measurements, 3);
        return IntStream.range(1, windows.size())
            .mapToObj(i -> {
                List<Long> current = windows.get(i);
                List<Long> previous = windows.get(i - 1);
                if (sumList(current) > sumList(previous)) {
                    return new MeasurementWindowPair(previous, current);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();

    }

    private static Long sumList(final List<Long> measurements) {

        return measurements
            .stream()
            .mapToLong(m -> m)
            .sum();

    }

    private static List<MeasurementPair> getIncreasedMeasurements(final List<Long> measurements) {

        return IntStream.range(1, measurements.size())
            .mapToObj(i -> {
                Long current = measurements.get(i);
                Long previous = measurements.get(i - 1);
                if (current > previous) {
                    return new
                        MeasurementPair(previous, current);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();

    }
}
