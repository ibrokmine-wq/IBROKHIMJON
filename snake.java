import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private final int BOARD_WIDTH = 400;
    private final int BOARD_HEIGHT = 400;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = (BOARD_WIDTH * BOARD_HEIGHT) / (DOT_SIZE * DOT_SIZE);

    // Iloncha koordinatalari
    private final ArrayList<Point> snake = new ArrayList<>();
    // Olma koordinatasi
    private Point apple;

    // Harakat yo'nalishlari
    private char direction = 'R'; // R = O'ng, L = Chap, U = Tepaga, D = Pastga
    private boolean inGame = true;
    private Timer timer;
    private int score = 0;

    public SnakeGame() {
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        addKeyListener(new FieldKeyListener());
        initGame();
    }

    private void initGame() {
        snake.clear();
        // Ilonchaning boshlang'ich tanasi (3 ta bo'g'in)
        snake.add(new Point(100, 100));
        snake.add(new Point(80, 100));
        snake.add(new Point(60, 100));

        locateApple();

        // O'yin tezligi (har 140 millisekundda yangilanadi)
        timer = new Timer(140, this);
        timer.start();
    }

    private void locateApple() {
        Random rand = new Random();
        int rX = rand.nextInt(BOARD_WIDTH / DOT_SIZE) * DOT_SIZE;
        int rY = rand.nextInt(BOARD_HEIGHT / DOT_SIZE) * DOT_SIZE;
        apple = new Point(rX, rY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            // Olmani chizish
            g.setColor(Color.RED);
            g.fillRect(apple.x, apple.y, DOT_SIZE - 2, DOT_SIZE - 2);

            // Ilonchani chizish
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // Boshi
                } else {
                    g.setColor(new Color(45, 180, 0)); // Tanasi
                }
                g.fillRect(snake.get(i).x, snake.get(i).y, DOT_SIZE - 2, DOT_SIZE - 2);
            }

            // Ballni ko'rsatish
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Ball: " + score, 10, 20);

        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "O'yin Tugadi!";
        String scoreMsg = "Sizning balingiz: " + score;
        Font font = new Font("Arial", Font.BOLD, 24);
        g.setColor(Color.RED);
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);
        g.drawString(msg, (BOARD_WIDTH - metrics.stringWidth(msg)) / 2, BOARD_HEIGHT / 2 - 20);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(scoreMsg, (BOARD_WIDTH - g.getFontMetrics().stringWidth(scoreMsg)) / 2, BOARD_HEIGHT / 2 + 20);
    }

    private void checkApple() {
        if (snake.get(0).equals(apple)) {
            score++;
            // Quyruqqa yangi bo'g'in qo'shish (oxirgi bo'g'in nusxasi)
            snake.add(new Point(snake.get(snake.size() - 1)));
            locateApple();
        }
    }

    private void move() {
        // Tanani oldinga surish
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).setLocation(snake.get(i - 1));
        }

        // Boshini yo'nalish bo'yicha harakatlantirish
        Point head = snake.get(0);
        switch (direction) {
            case 'U' -> head.y -= DOT_SIZE;
            case 'D' -> head.y += DOT_SIZE;
            case 'L' -> head.x -= DOT_SIZE;
            case 'R' -> head.x += DOT_SIZE;
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);

        // O'z-o'ziga urilishni tekshirish
        for (int i = snake.size() - 1; i > 0; i--) {
            if (head.equals(snake.get(i))) {
                inGame = false;
                break;
            }
        }

        // Devorga urilishni tekshirish
        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (direction != 'R')) {
                direction = 'L';
            }
            if ((key == KeyEvent.VK_RIGHT) && (direction != 'L')) {
                direction = 'R';
            }
            if ((key == KeyEvent.VK_UP) && (direction != 'D')) {
                direction = 'U';
            }
            if ((key == KeyEvent.VK_DOWN) && (direction != 'U')) {
                direction = 'D';
            }
        }
    }

    // O'yinni oynada (Window) ishga tushirish uchun asosiy qism
    public static void main(String[] args) {
        JFrame frame = new JFrame("Java 2D Snake Game");
        SnakeGame game = new SnakeGame();
        
        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}