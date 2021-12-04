package se.zettle.adventofcode.puzzle;

import static java.util.function.Predicate.not;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.zettle.adventofcode.provider.InputProvider;

@Component
@Slf4j
public class Day4 {

    protected static record Solution(Long partOne, Long partTwo) {
    }

    protected static record BingoInput(String draw, String boards) {
    }

    protected static record BingoCell(Integer number, Integer row, Integer column, boolean found) {
    }

    protected static record BingoBoard(List<BingoCell> cells) {
    }

    @AllArgsConstructor
    @Builder(toBuilder = true)
    @Value
    protected static class BingoGame {

        List<Integer> numbers;

        Map<Integer, BingoBoard> boards;

    }

    public Solution solve() {
        log.info("*".repeat(50));
        log.info("Day4: puzzle start...");

        String puzzleInput = InputProvider.getPuzzleInput("Day4");

        BingoInput bingoInput = getBingoInput(puzzleInput);

        BingoGame bingoGame = getBingoGame(bingoInput);

        Solution solution = runBingoGame(bingoGame);

        log.info("Day4: solution: {}, puzzle solved !!",solution);
        log.info("*".repeat(50));

        return solution;

    }

    protected Solution runBingoGame(final BingoGame bingoGame) {
        throw new UnsupportedOperationException("WIP.. ");
    }

    protected BingoGame getBingoGame(BingoInput bingoInput) {

        return new BingoGame(getBingoDraw(bingoInput.draw()), getBingoBoards(bingoInput.boards()));

    }

    private Map<Integer, BingoBoard> getBingoBoards(final String boardsInput) {

        Map<Integer, List<String>> boards = new HashMap<>();
        Integer boardNumber = 1;

        List<String> strings = boardsInput.lines().toList();

        for (String line : strings) {
            if (line.isBlank()) {
                boardNumber++;
                continue;
            }

            List<String> boardLines = boards.computeIfAbsent(boardNumber, mf -> new ArrayList<>());
            boardLines.add(line);
        }
        return boards.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> toBoard(e.getValue())
            ));
    }

    protected BingoBoard toBoard(List<String> input) {

        List<BingoCell> cells = IntStream.range(0, input.size())
            .mapToObj(row -> toRowCells(row, input.get(row)))
            .flatMap(Collection::stream)
            .toList();

        return new BingoBoard(cells);

    }

    private List<BingoCell> toRowCells(final int row, final String columnInput) {
        List<Integer> columns = Arrays.stream(columnInput.trim().split(" "))
            .filter(not(String::isBlank))
            .map(Integer::valueOf)
            .toList();
        return IntStream.range(0, columns.size())
            .mapToObj(i -> new BingoCell(columns.get(i), row, i, false))
            .toList();

    }

    protected List<Integer> getBingoDraw(final String draw) {
        return Arrays.stream(draw.split(","))
            .map(Integer::valueOf)
            .toList();
    }

    protected BingoInput getBingoInput(final String puzzleInput) {

        String bingoDraw = puzzleInput.stripIndent().lines()
            .takeWhile(s -> s.contains(","))
            .collect(Collectors.joining())
            .trim();

        String bingoBoards = puzzleInput.stripIndent().lines()
            .filter(not(s -> s.contains(",")))
            .collect(Collectors.joining("\n"))
            .trim();

        return new BingoInput(bingoDraw, bingoBoards);

    }

}
