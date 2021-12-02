package se.zettle.adventofcode.provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class InputProviderTest {

    @Test
    void inputProviderIsInitialized() {
        Map<String, String> inputMap = InputProvider.getInputMap();
        assertThat(inputMap).isNotEmpty();
    }

}