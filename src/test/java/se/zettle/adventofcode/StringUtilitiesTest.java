package se.zettle.adventofcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StringUtilitiesTest {

    @Test
    void slidingWindows() {
        List<Integer> candidates = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        int windowSize = 3;
        List<List<Integer>> slidingWindows = StringUtilities.getSlidingWindows(candidates, windowSize);
        assertThat(slidingWindows).hasSize(candidates.size() - 2);
        slidingWindows
            .forEach(w -> assertThat(w).hasSize(windowSize));
    }
}