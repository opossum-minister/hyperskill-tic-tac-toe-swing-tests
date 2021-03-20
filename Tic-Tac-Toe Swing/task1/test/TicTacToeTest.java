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
import java.util.List;
import java.util.Map;

import static org.hyperskill.hstest.testcase.CheckResult.correct;

public class TicTacToeTest extends SwingTest {
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
            buttons.add(button.target());
        });
        return correct();
    }

    @DynamicTest(feedback = "The board should have exactly three rows and columns")
    CheckResult test2() {
        final var cols = buttons.stream().mapToInt(JButton::getX).distinct().sorted().toArray();
        final var rows = buttons.stream().mapToInt(JButton::getY).distinct().sorted().toArray();

        assertEquals(3, cols.length, "The board should have only 3 columns but buttons "
                + "have {0} different coordinates for columns", cols.length);

        assertEquals(3, rows.length, "The board should have only 3 rows but buttons "
                + "have {0} different coordinates for rows", rows.length);

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