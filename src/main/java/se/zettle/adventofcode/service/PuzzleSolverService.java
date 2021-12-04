package se.zettle.adventofcode.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.zettle.adventofcode.puzzle.Day1;
import se.zettle.adventofcode.puzzle.Day2;
import se.zettle.adventofcode.puzzle.Day3;
import se.zettle.adventofcode.puzzle.Day4;

@Service
@Slf4j
public class PuzzleSolverService {

    private final Day1 day1;
    private final Day2 day2;
    private final Day3 day3;
    private final Day4 day4;

    @Autowired
    public PuzzleSolverService(
        final Day1 day1,
        final Day2 day2,
        final Day3 day3,
        final Day4 day4
    ) {
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
        this.day4 = day4;
    }

    public void run() {
        log.info("==".repeat(20));
        log.info("Here come the Answers...");
        log.info("Day1: {}", day1.solve());
        log.info("Day2: {}", day2.solve());
        log.info("Day3: {}", day3.solve());
        log.info("Day4: {}", day4.solve());
        log.info("Done !!");
        log.info("==".repeat(20));
    }
}
