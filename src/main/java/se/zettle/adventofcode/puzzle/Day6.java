package se.zettle.adventofcode.puzzle;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.zettle.adventofcode.provider.InputProvider;

@Component
@Slf4j
public class Day6 {

    protected record Solution(Long answer) {
    }

    @AllArgsConstructor
    @Getter
    protected enum SpawnClock {
        NEW_BORN(8),
        SPAWNED(6),
        MIN_VALUE(0);
        Integer days;
    }

    @AllArgsConstructor
    @Data
    protected static class LanternFish {
        private Integer daysToSpawn;

    }

    @AllArgsConstructor
    @Data
    protected static class FishSchool {

        Map<Integer, Long> wards;

        public void maintainWards() {
            Map<Integer, Long> temp = new HashMap<>();
            Long babiesAndMums = getWards().get(0);

            wards
                .forEach((day, fishCount) -> {
                    if (!List.of(8, 6).contains(day)) {
                        temp.put(day, wards.get(day + 1));
                    }
                    if (day == 8) {
                        temp.put(day, babiesAndMums);
                    }
                    if (day == 6) {
                        temp.put(day, wards.get(day + 1) + babiesAndMums);
                    }
                });

            setWards(temp);

        }

        public Long getFishInAllWards() {
            return wards.values().stream()
                .mapToLong(Long::longValue)
                .sum();
        }

    }

    public Solution solve() {
        log.info("*".repeat(50));
        log.info("Day6: puzzle start...");

        String puzzleInput = InputProvider.getPuzzleInput("Day6");

        FishSchool school = getSchool(puzzleInput);

        // spendDays(school, 80); part1
        spendDays(school, 256); // part2

        Long fishInAllWards = school.getFishInAllWards();
        log.info("Day6: answer total fish in school : {}", fishInAllWards);
        Solution solution = new Solution(fishInAllWards);

        log.info("Day6: solution: {}, puzzle solved !!", solution);
        log.info("*".repeat(50));

        return solution;

    }

    protected void spendDays(final FishSchool school, final Integer days) {

        IntStream.range(0, days)
            .forEachOrdered(i -> school.maintainWards());

    }

    protected FishSchool getSchool(final String puzzleInput) {

        Map<Integer, Long> fishInWards = Arrays.stream(puzzleInput.split(","))
            .map(Integer::valueOf)
            .collect(groupingBy(Function.identity(), counting()));

        Map<Integer, Long> wards = IntStream.rangeClosed(0, 8)
            .boxed()
            .toList().stream()
            .collect(toMap(
                Function.identity(),
                i -> Optional.ofNullable(fishInWards.get(i))
                    .orElse(0L)
            ));

        return new FishSchool(wards);
    }
}
