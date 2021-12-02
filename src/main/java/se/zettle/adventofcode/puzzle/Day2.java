package se.zettle.adventofcode.puzzle;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.zettle.adventofcode.provider.InputProvider;

@Component
@Slf4j
public class Day2 {

    protected enum Command {
        FORWARD, DOWN, UP
    }

    protected record Direction(Command command, Long units) {

        public Bearing apply(Bearing bearing) {
            return switch (command()) {
                case UP -> bearing.up(units());
                case DOWN -> bearing.down(units());
                case FORWARD -> bearing.forward(units());
            };
        }

    }

    protected record Bearing(Long xPos, Long depth, Long aim) {

        public Bearing down(Long units) {
            return new Bearing(xPos(), depth(), aim() + units);
        }

        public Bearing up(Long units) {
            return new Bearing(xPos(), depth(), aim() - units);
        }

        public Bearing forward(Long units) {
            Long depth = depth() + aim() * units;
            return new Bearing(xPos() + units, depth, aim());
        }

        public Long getProduct() {
            return xPos() * depth();
        }

    }

    @AllArgsConstructor
    @Getter
    @Setter
    protected static class Submarine {
        private Bearing bearing;

        public void navigate(Direction direction) {
            setBearing(direction.apply(getBearing()));
        }
    }

    public Long solve() {
        log.info("*".repeat(50));
        log.info("Day2: puzzle start...");
        String puzzleInput = InputProvider.getPuzzleInput("day2");

        List<Direction> directions = decodeDirections(puzzleInput);

        Bearing finalBearing = navigateSubmarine(directions).getBearing();

        log.info("Final Bearing is: {}, product: {}", finalBearing, finalBearing.getProduct());
        log.info("Day2: puzzle solved !!");
        log.info("*".repeat(50));

        return finalBearing.getProduct();

    }

    protected List<Direction> decodeDirections(final String puzzleInput) {
        return Arrays.stream(puzzleInput.split("\n"))
            .map(this::getDirection)
            .toList();
    }

    protected Submarine navigateSubmarine(final List<Direction> directions) {
        Submarine submarine = new Submarine(new Bearing(0L, 0L, 0L));

        directions
            .forEach(submarine::navigate);

        return submarine;
    }

    private Direction getDirection(final String cmd) {
        String[] components = cmd.split(" ");
        return new Direction(Command.valueOf(components[0].toUpperCase()), Long.parseLong(components[1]));

    }

}
