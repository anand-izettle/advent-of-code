package se.zettle.adventofcode.puzzle;

import static java.util.function.Predicate.not;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.zettle.adventofcode.provider.InputProvider;

@Component
@Slf4j
public class Day4 {

    protected enum PuzzlePart {PART1, PART2}

    protected static record Solution(Long partOne, Long partTwo) {
    }

    protected static record BingoInput(String draw, String boards) {
    }

    @Data
    @AllArgsConstructor
    protected static class BingoCell {
        Integer number;
        Integer row;
        Integer column;
        boolean marked;
    }

    @AllArgsConstructor
    @Data
    protected static class BingoBoard {
        List<BingoCell> cells;

        public void markNumber(final Integer bingoNumber) {
            for (BingoCell cell : cells) {
                if (cell.getNumber().equals(bingoNumber)) {
                    cell.setMarked(true);
                }
            }
        }

        public Integer getCompletedRow() {
            return cells.stream()
                .filter(BingoCell::isMarked)
                .collect(Collectors.groupingBy(BingoCell::getRow))
                .entrySet().stream()
                .filter(e -> e.getValue().size() == (5L))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        }

        public Integer getCompletedColumn() {
            return cells.stream()
                .filter(BingoCell::isMarked)
                .collect(Collectors.groupingBy(BingoCell::getColumn))
                .entrySet().stream()
                .filter(e -> e.getValue().size() == (5L))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        }
    }

    protected static record BingoWinner(Integer boardNumber,
                                        BingoBoard bingoBoard,
                                        Integer lastDrawnNumber,
                                        Integer completedRow,
                                        Integer completedColumn) {
    }

    @AllArgsConstructor
    @Builder(toBuilder = true)
    @Value
    protected static class BingoGame {

        List<Integer> numbers;

        Map<Integer, BingoBoard> boards;

        public BingoWinner runFTW() {

            for (Integer bingoNumber : getNumbers()) {
                BingoWinner winner = markBoardsAndCheckForWinner(bingoNumber);
                if (Objects.nonNull(winner)) {
                    return winner;
                }
            }
            throw new IllegalStateException("No winner !!");

        }

        public BingoWinner runFTL() {

            for (Integer bingoNumber : numbers) {
                Set<Integer> boardPositions = getBoards().keySet();

                List<BingoWinner> winners = markAllBoardsAndGetWinners(bingoNumber);

                if (winners.isEmpty()) {
                    continue;
                }

                Set<Integer> boardsWon = winners.stream().map(BingoWinner::boardNumber)

                    .collect(Collectors.toSet());
                log.info("Found winner!! board number: {}", boardsWon);

                if (boardPositions.size() == 1) {
                    return winners.get(0);
                }

                boardsWon
                    .forEach(boardPositions::remove);
            }
            throw new IllegalStateException("Could not determine last winner");
        }

        private BingoWinner markBoardsAndCheckForWinner(final Integer bingoNumber) {
            for (Map.Entry<Integer, BingoBoard> boardEntry : getBoards().entrySet()) {
                BingoBoard bingoBoard = boardEntry.getValue();
                bingoBoard.markNumber(bingoNumber);
                BingoWinner winner = checkCompletedRowOrColumn(boardEntry, bingoNumber);
                if (Objects.nonNull(winner)) {
                    return winner;
                }
            }
            return null;
        }

        private List<BingoWinner> markAllBoardsAndGetWinners(final Integer bingoNumber) {
            for (Map.Entry<Integer, BingoBoard> boardEntry : getBoards().entrySet()) {
                BingoBoard bingoBoard = boardEntry.getValue();
                bingoBoard.markNumber(bingoNumber);
            }

            return getBoards().entrySet().stream()
                .map(e -> checkCompletedRowOrColumn(e, bingoNumber))
                .filter(Objects::nonNull)
                .toList();

        }

        private BingoWinner checkCompletedRowOrColumn(
            final Map.Entry<Integer, BingoBoard> boardEntry,
            final Integer bingoNumber
        ) {
            BingoBoard bingoBoard = boardEntry.getValue();
            Integer completedRow = bingoBoard.getCompletedRow();
            if (Objects.nonNull(completedRow)) {
                return new BingoWinner(boardEntry.getKey(), bingoBoard, bingoNumber, completedRow, null);
            }

            Integer completedColumn = bingoBoard.getCompletedColumn();
            if (Objects.nonNull(completedColumn)) {
                return new BingoWinner(boardEntry.getKey(), bingoBoard, bingoNumber, null, completedColumn);
            }
            return null;
        }
    }

    public Solution solve() {
        log.info("*".repeat(50));
        log.info("Day4: puzzle start...");

        String puzzleInput = InputProvider.getPuzzleInput("Day4");

        BingoInput bingoInput = getBingoInput(puzzleInput);

        BingoGame bingoGame = getBingoGame(bingoInput);

        Solution s1 = findWinner(bingoGame, PuzzlePart.PART1);
        log.info("Day4: part1 solution: {}, puzzle solved !!", s1.partOne());

        Solution s2 = findWinner(bingoGame, PuzzlePart.PART2);

        log.info("Day4: part2 solution: {}, puzzle solved !!", s2.partTwo());
        log.info("*".repeat(50));

        return new Solution(s1.partOne(), s2.partTwo());

    }

    protected Solution findWinner(BingoGame bingoGame, final PuzzlePart puzzlePart) {
        BingoWinner winner = puzzlePart == PuzzlePart.PART1 ?
            bingoGame.runFTW() :
            bingoGame.runFTL();

        if (Objects.nonNull(winner)) {
            log.info(
                "we have a Winner!! details: boardNumber: {}, completed row/column: {}   ",
                winner.boardNumber(),
                Optional.ofNullable(winner.completedRow())
                    .orElseGet(winner::completedColumn)
            );

            // Board score: sum(unmarked numbers) * lastDrawnNumber
            Long score = winner.bingoBoard().getCells().stream()
                .filter(not(BingoCell::isMarked))
                .mapToLong(BingoCell::getNumber)
                .sum() * winner.lastDrawnNumber();

            return puzzlePart == PuzzlePart.PART1 ?
                new Solution(score, null) :
                new Solution(null, score);
        }

        throw new IllegalStateException("could not find a winner");

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
