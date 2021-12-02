package se.zettle.adventofcode.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.zettle.adventofcode.puzzle.Day1;
import se.zettle.adventofcode.puzzle.Day2;

@Service
@Slf4j
public class PuzzleSolverService {

    private final Day1 day1;
    private final Day2 day2;

    @Autowired
    public PuzzleSolverService(final Day1 day1, final Day2 day2) {
        this.day1 = day1;
        this.day2 = day2;
    }

    public void run() {
        log.info("==".repeat(20));
        log.info("Here come the Answers...");
        log.info("Day1: {}", day1.solve());
        log.info("Day2: {}", day2.solve());
        log.info("Done !!");
        log.info("==".repeat(20));
    }
}
