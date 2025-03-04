import org.assertj.swing.fixture.JButtonFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import tictactoe.TicTacToe;

import javax.swing.JButton;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;
import static org.hyperskill.hstest.testcase.CheckResult.correct;

public class TicTacToeTest extends SwingTest {
    private static final int BOARD_SIZE = 3;

    public TicTacToeTest() {
        super(new TicTacToe());
    }

    @SwingComponent
    private JButtonFixture buttonA1;
    @SwingComponent
    private JButtonFixture buttonA2;
    @SwingComponent
    private JButtonFixture buttonA3;
    @SwingComponent
    private JButtonFixture buttonB1;
    @SwingComponent
    private JButtonFixture buttonB2;
    @SwingComponent
    private JButtonFixture buttonB3;
    @SwingComponent
    private JButtonFixture buttonC1;
    @SwingComponent
    private JButtonFixture buttonC2;
    @SwingComponent
    private JButtonFixture buttonC3;

    private final List<JButton> buttons = new ArrayList<>();

    @DynamicTest(feedback = "Buttons should have a name buttonA1..buttonC3, be visible and have labels 'A1'...'C3'")
    CheckResult test1() {
        final var board = Map.of(
                "A3", buttonA3, "B3", buttonB3, "C3", buttonC3,
                "A2", buttonA2, "B2", buttonB2, "C2", buttonC2,
                "A1", buttonA1, "B1", buttonB1, "C1", buttonC1);

        board.forEach((label, button) -> {
            requireVisible(button);
            button.requireText(label);
        });

        Stream.of("A3", "B3", "C3", "A2", "B2", "C2", "A1", "B1", "C1")
                .map(board::get)
                .map(JButtonFixture::target)
                .forEach(buttons::add);

        return correct();
    }

    private int[] cols;
    private int[] rows;

    @DynamicTest(feedback = "The board should have exactly three rows and columns")
    CheckResult test2() {
        cols = buttons.stream().mapToInt(JButton::getX).distinct().sorted().toArray();
        rows = buttons.stream().mapToInt(JButton::getY).distinct().sorted().toArray();

        assertEquals(BOARD_SIZE, cols.length,
                "The board should have only 3 columns. "
                        + "The coordinates for columns are {0}, "
                        + "the buttons have {1} different coordinates for columns",
                Arrays.toString(cols), cols.length);

        assertEquals(BOARD_SIZE, rows.length,
                "The board should have only 3 rows. "
                        + "The coordinates for rows are {0}, "
                        + "The buttons have {0} different coordinates for rows",
                Arrays.toString(rows), rows.length);

        return correct();
    }

    private static final String[] ROW_NAME = new String[]{"top", "middle", "bottom"};
    private static final String[] COL_NAME = new String[]{"left", "middle", "right"};

    @DynamicTest(feedback = "The buttons are incorrectly placed on the board")
    CheckResult test3() {
        range(0, BOARD_SIZE * BOARD_SIZE).forEach(index -> {

            assertEquals(rows[index / BOARD_SIZE], buttons.get(index).getY(),
                    "The button {0} should be located on the {1} row",
                    buttons.get(index).getText(), ROW_NAME[index / BOARD_SIZE]);

            assertEquals(cols[index % BOARD_SIZE], buttons.get(index).getX(),
                    "The button {0} should be located on the {1} column",
                    buttons.get(index).getText(), COL_NAME[index % BOARD_SIZE]);
        });

        return correct();
    }

    private static void assertEquals(
            final Object expected,
            final Object actual,
            final String error,
            final Object... args) {

        if (!expected.equals(actual)) {
            final var feedback = MessageFormat.format(error, args);
            throw new WrongAnswer(feedback);
        }
    }
}