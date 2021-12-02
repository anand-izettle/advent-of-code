package se.zettle.adventofcode.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PuzzleSolverServiceTest {

    @Autowired
    PuzzleSolverService puzzleSolverService;

    @Test
    void serviceRunsCorrectly() {

        puzzleSolverService.run();

        assertThat(puzzleSolverService).isNotNull();
    }
}