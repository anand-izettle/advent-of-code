package se.zettle.adventofcode.puzzle;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class Day2Test {

    @Autowired
    Day2 day2;

    @Test
    void test_Movements() {

        List<Day2.Direction> directions = getDirections();
        Day2.Submarine submarine = day2.navigateSubmarine(directions);
        Day2.Bearing finalPosition = new Day2.Bearing(12L,108L, 12L);
        assertThat(submarine.getBearing()).isEqualTo(finalPosition);

    }

    @Test
    void solve_expect_OK() {
        Long answer = day2.solve();
        assertThat(answer).isNotNegative().isNotNull();
        log.info("Puzzle Answer is: {}",answer);
    }

    private List<Day2.Direction> getDirections() {
        String directionCodes = """
            forward 1
            forward 2
            down 5
            down 5
            down 4
            down 9
            up 6
            up 7
            down 2
            forward 9
            """;
        return day2.decodeDirections(directionCodes);
    }

    /* // x=0, depth=0, aim=0
    * forward 1 // x=1 , depth=0 , aim=0
      forward 2 // x=3 , depth=0 , aim=0
      down 5 // x=3 , depth=0 , aim=5
      down 5 // x=3 , depth=0 , aim=10
      down 4 // x=3 , depth=0 , aim=14
      down 9 // x=3 , depth=0 , aim=23
      up 6 // x=3 , depth=0 , aim=17
      up 7 // x=3 , depth=0 , aim=10
      down 2 // x=3 , depth=0 , aim=12
      forward 9 // x=12, depth=108, aim=12
    * */
}