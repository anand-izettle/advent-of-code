package se.zettle.adventofcode.puzzle;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class Day4Test {

    @Autowired
    Day4 day4;

    @Test
    void inputParser_expect_correct_result() {

        Day4.BingoInput bingoInput = getSample();
        assertThat(bingoInput.draw()).isEqualTo(sampleDraw);
        assertThat(bingoInput.boards()).isEqualTo(sampleBoards);

    }

    @Test
    void getBoard_expect_3Boards_27Draw_fromSample() {

        Day4.BingoGame bingoGame = day4.getBingoGame(getSample());

        assertThat(bingoGame.getNumbers()).hasSize(27);

        Map<Integer, Day4.BingoBoard> boards = bingoGame.getBoards();

        assertThat(boards).hasSize(3);
        boards.values()
            .forEach(boardRows -> assertThat(boardRows.cells()).hasSize(25));

    }

    private Day4.BingoInput getSample() {

        return day4.getBingoInput("""
            7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
                        
            22 13 17 11  0
             8  2 23  4 24
            21  9 14 16  7
             6 10  3 18  5
             1 12 20 15 19
                        
             3 15  0  2 22
             9 18 13 17  5
            19  8  7 25 23
            20 11 10 24  4
            14 21 16 12  6
                        
            14 21 17 24  4
            10 16 15  9 19
            18  8 23 26 20
            22 11 13  6  5
             2  0 12  3  7
            """);

    }

    private final static String sampleDraw = """
        7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1""".stripIndent();
    private final static String sampleBoards = """
        22 13 17 11  0
         8  2 23  4 24
        21  9 14 16  7
         6 10  3 18  5
         1 12 20 15 19
                    
         3 15  0  2 22
         9 18 13 17  5
        19  8  7 25 23
        20 11 10 24  4
        14 21 16 12  6
                    
        14 21 17 24  4
        10 16 15  9 19
        18  8 23 26 20
        22 11 13  6  5
         2  0 12  3  7""".stripIndent();

}