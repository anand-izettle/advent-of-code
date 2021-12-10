package se.zettle.adventofcode.puzzle;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class Day6Test {

    private static final String sample = "3,4,3,1,2";

    @Autowired
    Day6 day6;

    @Test
    void solve_puzzle_expect_OK() {

        Day6.Solution solution = day6.solve();

        assertThat(solution).isNotNull();

        log.info("Day6 Solution: {}", solution);

    }

    @Test
    void getSchool_FromSample_Expect_5() {

        Day6.FishSchool school = day6.getSchool(sample);

        assertThat(school).isNotNull();
        assertThat(school.getFishInAllWards()).isEqualTo(5);

    }

    @Test
    void spend_18_days_expect_26_fish() {
        Day6.FishSchool school = day6.getSchool(sample);

        day6.spendDays(school, 18);

        assertThat(school.getFishInAllWards()).isEqualTo(26);

    }

    @Test
    void spend_80_days_expect_5934_fish() {
        Day6.FishSchool school = day6.getSchool(sample);

        assertThat(school).isNotNull();

        day6.spendDays(school, 80);

        assertThat(school.getFishInAllWards()).isEqualTo(5934);

    }

    @Test
    void spend_256_days_expect_26984457539_fish() {
        Day6.FishSchool school = day6.getSchool(sample);

        assertThat(school).isNotNull();

        day6.spendDays(school, 256);

        assertThat(school.getFishInAllWards()).isEqualTo(26984457539L);

    }
}