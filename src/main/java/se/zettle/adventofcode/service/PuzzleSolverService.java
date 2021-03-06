package se.zettle.adventofcode.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.zettle.adventofcode.puzzle.Day1;
import se.zettle.adventofcode.puzzle.Day2;
import se.zettle.adventofcode.puzzle.Day3;
import se.zettle.adventofcode.puzzle.Day4;
import se.zettle.adventofcode.puzzle.Day5;
import se.zettle.adventofcode.puzzle.Day6;
import se.zettle.adventofcode.puzzle.Day7;

@Service
@Slf4j
public class PuzzleSolverService {

    private final Day1 day1;
    private final Day2 day2;
    private final Day3 day3;
    private final Day4 day4;
    private final Day5 day5;
    private final Day6 day6;
    private final Day7 day7;

    @Autowired
    public PuzzleSolverService(
        final Day1 day1,
        final Day2 day2,
        final Day3 day3,
        final Day4 day4,
        final Day5 day5,
        final Day6 day6,
        final Day7 day7
    ) {
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
        this.day4 = day4;
        this.day5 = day5;
        this.day6 = day6;
        this.day7 = day7;
    }

    public void run() {
        log.info("==".repeat(20));
        log.info("Here come the Answers...");
        log.info("Day1: {}", day1.solve());
        log.info("Day2: {}", day2.solve());
        log.info("Day3: {}", day3.solve());
        log.info("Day4: {}", day4.solve());
        log.info("Day5: {}", day5.solve());
        log.info("Day6: {}", day6.solve());
        log.info("Day7: {}", day7.solve());
        log.info("Done !!");
        log.info("==".repeat(20));
    }
}
