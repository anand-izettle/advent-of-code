package se.zettle.adventofcode.puzzle;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class Day5Test {

    @Autowired
    Day5 day5;

    @Test
    void solve_expect_OK() {
        Day5.Solution solution = day5.solve();

        assertThat(solution).isNotNull();
        log.info("Day5 solution: {}", solution);

    }

    @Test
    void line_coveredPointsForDiagonal_79_97_expect_798897() {

        Day5.Line line = Day5.Line.fromString("7,9 -> 9,7");
        assertThat(line.isDiagonal()).isTrue();
        List<Day5.Point> points =
            List.of(Day5.Point.fromString("7,9"), Day5.Point.fromString("8,8"), Day5.Point.fromString("9,7"));
        assertThat(line.getCoveredPoints(Day5.PuzzlePart.PART2))
            .hasSameSizeAs(points)
            .hasSameElementsAs(points);

        Day5.Line line3773 = Day5.Line.fromString("7,3 -> 3,7");
        assertThat(line3773.isDiagonal()).isTrue();
        List<Day5.Point> points37 = List.of(
            Day5.Point.fromString("7,3"),
            Day5.Point.fromString("6,4"),
            Day5.Point.fromString("5,5"),
            Day5.Point.fromString("4,6"),
            Day5.Point.fromString("3,7")
        );
        assertThat(line3773.getCoveredPoints(Day5.PuzzlePart.PART2))
            .hasSameSizeAs(points37)
            .hasSameElementsAs(points37);

        //0,6 1,5 2,4 3,3, 4,2
        Day5.Line line0642 = Day5.Line.fromString("0,6 -> 4,2");
        assertThat(line0642.isDiagonal()).isTrue();
        List<Day5.Point> points0642 = List.of(
            Day5.Point.fromString("0,6"),
            Day5.Point.fromString("1,5"),
            Day5.Point.fromString("2,4"),
            Day5.Point.fromString("3,3"),
            Day5.Point.fromString("4,2")
        );

        assertThat(line0642.getCoveredPoints(Day5.PuzzlePart.PART2))
            .hasSameSizeAs(points0642)
            .hasSameElementsAs(points0642);

        Day5.Line line0088 = Day5.Line.fromString("0,0 -> 8,8");
        assertThat(line0088.isDiagonal()).isTrue();
        List<Day5.Point> points0088 = List.of(
            Day5.Point.fromString("0,0"),
            Day5.Point.fromString("1,1"),
            Day5.Point.fromString("2,2"),
            Day5.Point.fromString("3,3"),
            Day5.Point.fromString("4,4"),
            Day5.Point.fromString("5,5"),
            Day5.Point.fromString("6,6"),
            Day5.Point.fromString("7,7"),
            Day5.Point.fromString("8,8")
        );

        assertThat(line0088.getCoveredPoints(Day5.PuzzlePart.PART2))
            .hasSameSizeAs(points0088)
            .hasSameElementsAs(points0088);

    }

    @Test
    void getOverLappingPoints_Part1_FromSample_expect_5() {
        List<Day5.Point> overLappingPoints = day5.getOverLappingPoints(getSample(), Day5.PuzzlePart.PART1);
        assertThat(overLappingPoints.size()).isEqualTo(5L);
    }

    @Test
    void getOverLappingPoints_Part2_FromSample_expect_12() {
        List<Day5.Point> overLappingPoints = day5.getOverLappingPoints(getSample(), Day5.PuzzlePart.PART2);
        assertThat(overLappingPoints.size()).isEqualTo(12L);
    }

    @Test
    void getLinesFrom_Sample_expect_10() {

        String sample = getSample();
        Day5.Readings readings = day5.getReadings(sample);

        Map<Integer, Day5.Line> readout = readings.readout();
        assertThat(readout).hasSize(10);

        assertThat(readout.values().stream()
            .map(Day5.Line::toInput).
            collect(Collectors.joining("\n")))
            .isEqualTo(sample);

    }

    @Test
    void getGraph_expect_100_points_onGrid() {

        Day5.Readings readings = day5.getReadings(getSample());
        Day5.Graph graph = Day5.Graph.fromReadings(readings, Day5.PuzzlePart.PART1);
        assertThat(graph).isNotNull();
        assertThat(graph.getStart()).isEqualTo(new Day5.Point(0, 0));
        assertThat(graph.getEnd()).isEqualTo(new Day5.Point(9, 9));
        assertThat(graph.getGrid()).hasSize(100);
        log.info("graph done. start: {}, end: {}", graph.getStart(), graph.getEnd());

    }

    @Test
    void validReadOut_LinesMustHaveMultiplePoints() {

        Day5.Readings readings = day5.getReadings(getSample());
        Map<Integer, Day5.Line> validReadout = readings.getValidReadout(Day5.PuzzlePart.PART1);
        assertThat(validReadout).hasSize(6);

        validReadout
            .forEach((lineNumber, line) -> {
                List<Day5.Point> coveredPoints = line.getCoveredPoints(Day5.PuzzlePart.PART1);
                log.info("coveredPoints.size(): {}", coveredPoints.size());
                if (line.isVertical()) {
                    assertThat(coveredPoints).hasSize(Math.abs(line.a().y() - line.b().y()) + 1);
                } else {
                    assertThat(coveredPoints).hasSize(Math.abs(line.a().x() - line.b().x()) + 1);

                }
            });

    }

    @Test
    void plotReadings_expect_plottedPointsEqualTo21() {

        Day5.Readings readings = day5.getReadings(getSample());
        Map<Integer, Day5.Line> validReadout = readings.getValidReadout(Day5.PuzzlePart.PART1);
        assertThat(validReadout).hasSize(6);
        Day5.Graph graph = Day5.Graph.fromReadings(readings, Day5.PuzzlePart.PART1);
        graph.plotReadings(readings, Day5.PuzzlePart.PART1);

        assertThat(graph).isNotNull();

        List<List<String>> plottedPoints =
            graph.getGrid().values().stream().filter(Predicate.not(Collection::isEmpty)).toList();
        assertThat(plottedPoints).hasSize(21);

        String display = graph.getDisplay();
        log.info(display);

        assertThat(display).isEqualTo(getSampleDisplay());
        log.info("Done");

    }

    @Test
    void getGraphAndPlot_Expect_part2Display() {

        Day5.Readings readings = day5.getReadings(getSample());
        Day5.Graph graph = Day5.Graph.fromReadings(readings, Day5.PuzzlePart.PART2);
        Map<Integer, Day5.Line> validReadout = readings.getValidReadout(Day5.PuzzlePart.PART2);

        log.info("readout: \n {}", validReadout);
        graph.plotReadings(readings, Day5.PuzzlePart.PART2);

        String display = graph.getDisplay();

        log.info(display);

        assertThat(display).isEqualTo(getPart2SampleDisplay());

        log.info("Done!!");

    }

    private String getSampleDisplay() {
        return """
            .......1..
            ..1....1..
            ..1....1..
            .......1..
            .112111211
            ..........
            ..........
            ..........
            ..........
            222111....""".stripIndent();

    }

    private String getPart2SampleDisplay() {
        return """
            1.1....11.
            .111...2..
            ..2.1.111.
            ...1.2.2..
            .112313211
            ...1.2....
            ..1...1...
            .1.....1..
            1.......1.
            222111....""".stripIndent();
    }

    private String getSample() {

        return """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2""".stripIndent();
    }
}