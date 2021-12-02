package se.zettle.adventofcode.puzzle;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Day1Test {

    @Autowired
    Day1 day1;

    @Test
    void solve_Puzzle_expect_OK() {
        day1.solve();

        assertThat(day1).isNotNull();
    }
}