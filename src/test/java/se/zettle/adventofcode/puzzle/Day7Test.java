package se.zettle.adventofcode.puzzle;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class Day7Test {

    @Autowired
    Day7 day7;

    @Test
    void solve_expect_OK() {

        Day7.Solution solution = day7.solve();
        assertThat(solution).isNotNull();

        log.info("Solution is: {}",solution);

    }

    @Test
    void getFleetFromSample_expect_10() {

        Day7.Fleet fleet = day7.getCrabFleet(getSample());

        assertThat(fleet).isNotNull();
        assertThat(fleet.getMinPosition()).isEqualTo(0);
        assertThat(fleet.getMaxPosition()).isEqualTo(16);

    }

    @Disabled
    @Test
    void calculate_position_part1_expect_2_asOptimal() {

        Day7.Fleet fleet = day7.getCrabFleet(getSample());

        Day7.Position position = fleet.getOptimalPosition();

        assertThat(position).isNotNull().isEqualTo(new Day7.Position(2,37L));

    }

    @Test
    void calculate_position_part2_expect_5_asOptimal() {

        Day7.Fleet fleet = day7.getCrabFleet(getSample());

        Day7.Position position = fleet.getOptimalPosition();

        assertThat(position).isNotNull().isEqualTo(new Day7.Position(5,168L));

    }

    @Test
    void fuelConsumption_16_to_5_expect_66() {

        Integer fuelRequired = Day7.Fleet.getFuelRequired(16, 5);
        assertThat(fuelRequired).isEqualTo(66);
    }

    private String getSample() {
        return "16,1,2,0,4,2,7,1,2,14";
    }
}