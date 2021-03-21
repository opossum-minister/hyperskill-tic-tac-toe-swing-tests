package tictactoe;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class TicTacToe extends JFrame implements ActionListener {
    private static final Logger log = Logger.getLogger(TicTacToe.class.getName());
    private static final Pattern PLAYERS = Pattern.compile("(?<Player1>Human|Robot).+(?<Player2>Human|Robot)");

    private final Board board = new Board(this);
    private final StatusBar statusBar = new StatusBar();
    private final Toolbar toolbar = new Toolbar(this);

    private int currentPlayer;

    {
        log.info("tictactoe.TicTacToe is started.");
        add(board, BorderLayout.CENTER);
        add(toolbar, BorderLayout.NORTH);
        add(statusBar, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setBackground(Color.WHITE);
        setTitle("Tic Tac Toe");
        setResizable(false);
        setVisible(true);
        setJMenuBar(new AppMenu(this::processMenu));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        log.entering(TicTacToe.class.getName(), "actionPerformed", e);
        final var button = (JButton) e.getSource();
        if (button.getText().equals("Reset")) {
            reset();
        } else if (button.getText().equals("Start")) {
            start();
        } else {
            move((Cell) button);
        }
        log.exiting(TicTacToe.class.getName(), "actionPerformed", board.getGameState());
    }

    void processMenu(final ActionEvent e) {
        log.entering(TicTacToe.class.getName(), "processMenu", e.getSource());
        final var item = (JMenuItem) e.getSource();
        final var matcher = PLAYERS.matcher(item.getText());
        if (matcher.matches()) {
            toolbar.players[0].setText(matcher.group("Player1"));
            toolbar.players[1].setText(matcher.group("Player2"));
            reset();
            start();
        } else {
            this.dispose();
        }
        log.exiting(TicTacToe.class.getName(), "processMenu", board.getGameState());
    }

    public void move(final Cell cell) {
        log.log(Level.INFO, "Index: {0}, Status: {1}",
                new Object[]{cell.getIndex(), board.getGameState().getMessage()});

        if (cell.isEmpty() && board.isPlaying()) {
            cell.setMark(currentPlayer == 0 ? Cell.Mark.X : Cell.Mark.O);
            statusBar.setMessage(board.getGameState());
            currentPlayer = 1 - currentPlayer;
        }
        if (board.getGameState() != Board.State.PLAYING) {
            board.setPlaying(false);
        }
        checkRobot();
    }

    public void start() {
        toolbar.startGame();
        statusBar.setMessage(Board.State.PLAYING);
        board.setPlaying(true);
        checkRobot();
    }

    private void checkRobot() {
        if (isRobotsTurn() && board.isPlaying()) {
            board.getRandomFreeCell().doClick();
        }
    }

    private boolean isRobotsTurn() {
        return toolbar.players[currentPlayer].getText().equals("Robot");
    }

    public void reset() {
        board.clear();
        currentPlayer = 0;
        toolbar.resetGame();
        statusBar.setMessage(Board.State.NOT_STARTED);
        board.setPlaying(false);
    }
}