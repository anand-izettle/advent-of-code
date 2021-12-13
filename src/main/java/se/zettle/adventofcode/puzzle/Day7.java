package se.zettle.adventofcode.puzzle;

import static java.lang.Math.abs;
import static java.util.function.Predicate.not;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.zettle.adventofcode.provider.InputProvider;

@Component
@Slf4j
public class Day7 {


    protected static record Position(Integer position, Long fuelConsumption){}

    protected static record Solution(Long answer) {}

    @Value
    protected static class Fleet{

        Map<Integer,Long> crabPostions;

        Integer minPosition;
        Integer maxPosition;
        Integer crabTotal;

        @Builder
        public Fleet(final List<Integer> positions) {

            final List<Integer> orderedPositions = positions.stream()
                .sorted(Comparator.comparingInt(i -> i))
                .toList();

            crabTotal = positions.size();
            minPosition = orderedPositions.get(0);
            maxPosition = orderedPositions.get(positions.size()-1);
            Map<Integer, Long> crabsByPos =
                positions.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            crabPostions = IntStream.rangeClosed(minPosition,maxPosition)
                .boxed()
                .collect(Collectors.toMap(Function.identity(),
                   i -> Optional.ofNullable(crabsByPos.get(i)).orElse(0L) ));
        }

        public Position getOptimalPosition(){

            Position fc = null;

            for (Integer position : crabPostions.keySet()) {

                Long fuel = crabPostions.entrySet()
                    .stream()
                    .filter(not(e -> e.getKey().equals(position)))
                    .mapToLong(e -> getFuelRequired(e.getKey(), position) * e.getValue())
                    .sum();

                if (Objects.isNull(fc) || fc.fuelConsumption() > fuel) {
                    fc = new Position(position,fuel);
                }
            }

            return fc;

        }

        protected static Integer getFuelRequired(final Integer fromPos, Integer toPos) {
            int steps = abs(fromPos - toPos);
            return IntStream.rangeClosed(1, steps)
                .sum();
        }

    }

    public Solution solve() {

        log.info("*".repeat(50));
        log.info("Day7: puzzle start...");

        String puzzleInput = InputProvider.getPuzzleInput("Day7");

        Solution solution = getSolution(puzzleInput);
        log.info("Day7: solution: {}, puzzle solved !!", solution);
        log.info("*".repeat(50));

        return solution;


    }

    protected Fleet getCrabFleet(final String puzzleInput) {
        List<Integer> positions = Arrays.stream(puzzleInput.split(","))
            .map(Integer::valueOf)
            .toList();
        return Fleet.builder()
            .positions(positions)
            .build();
    }

    protected Solution getSolution(final String puzzleInput) {
        Fleet fleet = getCrabFleet(puzzleInput);
        Position position = fleet.getOptimalPosition();
        log.info("Optimal Position is: {}",position);
        return new Solution(position.fuelConsumption());
    }
}
