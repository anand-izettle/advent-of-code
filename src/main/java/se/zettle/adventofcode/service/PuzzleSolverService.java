package se.zettle.adventofcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import se.zettle.adventofcode.puzzle.Day1;

@Service
public class PuzzleSolverService implements CommandLineRunner {

    private final Day1 day1;

    @Autowired
    public PuzzleSolverService(final Day1 day1) {
        this.day1 = day1;
    }

    @Override
    public void run(final String... args) {
        day1.solve();
    }
}
