package se.zettle.adventofcode.puzzle;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static se.zettle.adventofcode.puzzle.Day5.PuzzlePart.PART1;
import static se.zettle.adventofcode.puzzle.Day5.PuzzlePart.PART2;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.zettle.adventofcode.provider.InputProvider;

@Component
@Slf4j
public class Day5 {

    protected enum PuzzlePart {
        PART1, PART2
    }

    protected static record Solution(Long part1, Long part2) {
    }

    protected static record Point(@NonNull Integer x, @NonNull Integer y) {
        static Point fromString(String input) {
            String[] pos = input.strip().split(",");
            return new Point(Integer.valueOf(pos[0]), Integer.valueOf(pos[1]));
        }

    }

    protected static record Line(@NonNull Point a, @NonNull Point b) {

        static Line fromString(String lineString) {
            String[] points = lineString.strip().split("->");
            return new Line(Point.fromString(points[0]), Point.fromString(points[1]));
        }

        String toInput() {
            return String.format("%d,%d -> %d,%d", a.x(), a.y(), b().x(), b().y());
        }

        Point left() {
            return a.x() <= b.x() ? a : b;
        }

        Point top() {
            return a.y() <= b.y() ? a : b;
        }

        List<Point> getCoveredPoints(final PuzzlePart part) {
            if (isVertical()) {
                return IntStream.rangeClosed(getMinY(), getMaxY())
                    .mapToObj(y -> new Point(a.x(), y))
                    .toList();
            }

            if (isHorizontal()) {
                return IntStream.rangeClosed(getMinX(), getMaxX())
                    .mapToObj(x -> new Point(x, a.y()))
                    .toList();
            }

            if (part == PART1) {
                throw new IllegalArgumentException("Not considering diagonals in part1");
            }

            if (isDiagonal()) { // 0,6 1,5 2,4 3,3 4,2 5,1
                int dx = abs(a().x() - b().x());
                if (a == left() && a == top()) {
                    return IntStream.rangeClosed(0, dx)
                        .mapToObj(i -> new Point(a.x()+i,a.y() + i))
                        .toList();
                }

                if (a == left() && a != top()) {
                    return IntStream.rangeClosed(0, dx)
                        .mapToObj(i -> new Point(a.x()+i,a.y() - i))
                        .toList();
                }

                if (a != left() && a == top() ) {
                    return IntStream.rangeClosed(0, dx)
                        .mapToObj(i -> new Point(a.x() - i,a.y() + i))
                        .toList();
                }

                if (a != left() && a != top()) {
                    return IntStream.rangeClosed(0, dx)
                        .mapToObj(i -> new Point(a.x() - i,a.y() - i))
                        .toList();
                }

            }
            throw new IllegalArgumentException("Not a valid line");
        }

        boolean isHorizontal() {
            return a.y().equals(b.y());
        }

        boolean isVertical() {
            return a.x().equals(b.x());
        }

        boolean isDiagonal() { //0,6 -> 4,2
            int dx = abs(a.x() - b.x());
            int dy = abs(a.y() - b.y());
            return dx == dy;
        }

        boolean isValid(final PuzzlePart part) {
            if (part == PART1) {
                return isVertical() || isHorizontal();
            }
            return isHorizontal() || isVertical() || isDiagonal();
        }

        Integer getMinX() {
            return IntStream.of(a.x(), b.x())
                .min()
                .getAsInt();
        }

        Integer getMaxX() {
            return IntStream.of(a.x(), b.x())
                .max()
                .getAsInt();
        }

        Integer getMinY() {
            return IntStream.of(a.y(), b.y())
                .min()
                .getAsInt();
        }

        Integer getMaxY() {
            return IntStream.of(a.y(), b.y())
                .max()
                .getAsInt();
        }

    }

    protected static record Readings(Map<Integer, Line> readout) {

        Map<Integer, Line> getValidReadout(final PuzzlePart part) {
            return readout().entrySet()
                .stream()
                .filter(e -> e.getValue().isValid(part))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Point getEndPoint(final PuzzlePart part) {
            List<Line> validLines = getValidReadout(part).values()
                .stream()
                .toList();

            int x = validLines.stream()
                .mapToInt(Line::getMaxX)
                .max()
                .getAsInt();

            int y = validLines.stream()
                .mapToInt(Line::getMaxY)
                .max()
                .getAsInt();

            return new Point(x, y);

        }

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Point getStartPoint(final PuzzlePart part) {
            List<Line> validLines = getValidReadout(part).values()
                .stream()
                .toList();

            int x = validLines.stream()
                .mapToInt(Line::getMinX)
                .min()
                .getAsInt();

            int y = validLines.stream()
                .mapToInt(Line::getMinY)
                .min()
                .getAsInt();

            return new Point(x, y);

        }
    }

    @Data
    protected static class Graph {

        private final Point start;
        private final Point end;

        private Map<Point, List<String>> grid = new HashMap<>();

        public static Graph fromReadings(Readings readings, final PuzzlePart part) {
            return new Graph(readings.getStartPoint(part), readings.getEndPoint(part));
        }

        public void plotReadings(Readings readings, final PuzzlePart part) {
            readings.getValidReadout(part).values()
                .forEach(line -> line.getCoveredPoints(part)
                    .forEach(point -> grid.get(point).add(line.toInput())));

        }

        public List<Point> getOverLappingPoints() {
            return getGrid().entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .toList();
        }

        public String getDisplay() {
            Map<Integer, Map<Integer, Integer>> yPosxPosCount = getGrid().entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().size()))
                .sorted(Comparator.comparingInt(e -> e.getKey().y()))
                .collect(Collectors.groupingBy(
                    e -> e.getKey().y(),
                    Collectors.collectingAndThen(
                        toList(),
                        l -> l.stream()
                            .collect(toMap(e -> e.getKey().x(), AbstractMap.SimpleEntry::getValue))
                    )
                ));

            return yPosxPosCount.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(e -> e.getValue().entrySet()
                    .stream()
                    .sorted(Comparator.comparingInt(Map.Entry::getKey))
                    .map(Map.Entry::getValue)
                    .map(String::valueOf)
                    .collect(Collectors.joining())
                    .replace("0", ".")
                )
                .collect(Collectors.joining("\n"));

        }

        private Graph(final Point start, final Point end) {
            this.start = start;
            this.end = end;
            generateGrid();
        }

        private void generateGrid() {
            IntStream.range(start.x(), end.x() + 1)
                .mapToObj(xpos ->
                    IntStream.range(start.y(), end.y() + 1)
                        .mapToObj(ypos -> new Point(xpos, ypos))
                        .toList()
                )
                .flatMap(List::stream)
                .forEach(point -> grid.computeIfAbsent(point, mf -> new ArrayList<>()));

        }
    }

    public Solution solve() {
        log.info("*".repeat(50));
        log.info("Day5: puzzle start...");

        String puzzleInput = InputProvider.getPuzzleInput("Day5");

        List<Point> part1Points = getOverLappingPoints(puzzleInput, PART1);

        log.info("Day5: part1 overlapping points: {}", part1Points.size());

        List<Point> part2Points = getOverLappingPoints(puzzleInput, PART2);
        log.info("Day5: part2 overlapping points: {}", part2Points.size());
        Solution solution = new Solution((long) part1Points.size(), (long) part2Points.size());

        log.info("Day5: solution: {}, puzzle solved !!", solution);
        log.info("*".repeat(50));

        return solution;

    }

    protected Readings getReadings(final String puzzleInput) {

        List<Line> lines = puzzleInput.lines()
            .map(Line::fromString)
            .toList();
        Map<Integer, Line> lineReads = IntStream.range(0, lines.size())
            .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, lines.get(i)))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Readings(lineReads);

    }

    protected List<Point> getOverLappingPoints(
        final String puzzleInput,
        final PuzzlePart part
    ) {
        Readings readings = getReadings(puzzleInput);
        Graph graph = Graph.fromReadings(readings, part);
        graph.plotReadings(readings, part);
        return graph.getOverLappingPoints();

    }

}
