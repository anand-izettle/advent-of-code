package se.zettle.adventofcode.puzzle;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class Day3Test {

    @Autowired
    Day3 day3;

    @Test
    void solve_Expect_ok() {

        Day3.Solution answer = day3.solve();

        log.info("Answer is: {}",answer);
        assertThat(answer.gammaEpsilon()).isNotNegative().isGreaterThan(0L);


    }

    @Test
    void read_As_bits() {

        List<String> readings = getReadings();
        assertThat(readings).hasSize(6);

        Map<Integer, List<Integer>> positionReadings = day3.getPositionReadings(readings);

        assertThat(positionReadings).hasSize(12);
        positionReadings.values()
            .forEach(v -> assertThat(v).hasSize(6));

        Map<Integer, Integer> mostCommonBits = day3.getMostCommonBits(positionReadings);

        assertThat(mostCommonBits).hasSameSizeAs(positionReadings);

        Day3.Rate rate = day3.getGammaEpsilon(mostCommonBits);

        assertThat(rate.gammaOrOxygen()).isEqualTo("101100110100");

        assertThat(rate.gamaOrOxygenDecimal()).isEqualTo(2868L);

        assertThat(rate.epsilonOrCO2()).isEqualTo("010011001011");

        assertThat(rate.epsilonOrCo2Decimal()).isEqualTo(1227L);

        assertThat(rate.getProduct()).isEqualTo(3519036L);


    }

    @Test
    void getOxygetCo2_expect_OK() {

        List<String> readings = getSample();

        Day3.Rate oxygenCO2 = day3.getOxygenCO2(readings);

        assertThat(oxygenCO2.gammaOrOxygen()).isEqualTo("10111");
        assertThat(oxygenCO2.epsilonOrCO2()).isEqualTo("01010");
        assertThat(oxygenCO2.getProduct()).isEqualTo(230L);
    }



    private List<String> getReadings() {
        return day3.getReadings("""
            101000111100
            000011111101
            011100000100
            100100010000
            011110010100
            101001100000
            """);
    }

    private List<String> getSample() {
        return day3.getReadings("""
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010
            """);
    }
}