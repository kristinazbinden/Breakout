package breakout;

import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/*
 * Description: Breakout Project - Part Two Created for SMCC CSCI_110_D1
 * Principles of Computer Science Professor Imhoff Author: Kristina Zbinden 
 * Due Date: 10/17/21 Pledged: All work seen here is my own and it was created
 * without collaboration.
 *
 */
public class Breakout extends GraphicsProgram {

    /**
     * Width of the game display (all coordinates are in pixels)
     */
    private static final int WIDTH = 400;
    /**
     * Height of the game display
     */
    private static final int HEIGHT = 600;
    /**
     * Width of the paddle
     */
    private static final int PADDLE_WIDTH = 60;
    /**
     * Height of the paddle
     */
    private static final int PADDLE_HEIGHT = 10;
    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 30;
    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 10;
    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 10;
    /**
     * Separation between bricks
     */
    private static final int BRICK_SEP = 4;
    /**
     * Width of a brick
     */
    private static final int BRICK_WIDTH
            = WIDTH / NBRICKS_PER_ROW - BRICK_SEP;
    /**
     * Height of a brick
     */
    private static final int BRICK_HEIGHT = 8;
    /**
     * Radius of the ball in pixels
     */
    private static final int BALL_RADIUS = 10;
    /**
     * Offset of the top brick row from the top
     */
    private static final int BRICK_Y_OFFSET = 70;
    /**
     * Number of turns
     */
    private static final int NTURNS = 3;
    /**
     * Pause between each step in the animation
     */
    private static final int PAUSE_TIME = 10;

    /**
     * The paddle. You are responsible for drawing this in run(). When you
     * create the GRect for the paddle, store it in this variable (you don't
     * have to make a new paddle variable). Because it is created here, in the
     * class and not inside a method, both the mouseMoved() method and the run()
     * method can access it.
     */
    private GRect paddle;

    /**
     * Runs the program as an application. This method differs from the simplest
     * possible boilerplate in that it passes parameters to specify the
     * dimensions of the court. You do not have to add any code to this method.
     */
    public static void main(String[] args) {
        String[] sizeArgs = {"width=" + WIDTH, "height=" + HEIGHT};
        new Breakout().start(sizeArgs);
    }

    /**
     * Method: mouseMoved() is called whenever the mouse moves inside the app
     * window. Don't worry about how this happens for now, just know that this
     * is the place where you can put code that will happen every time the mouse
     * moves. Note: in the run() method you will see an addMouseListeners()
     * line. That line is what makes mouseMoved() work here.
     */
    @Override
    public void mouseMoved(MouseEvent me) {
        // The paddle does not move in the y direction, so you only need to 
        // pay attention to the mouse's x coordinate.
        // Change the variable name if you wish.

        // Get mouse position and center in middle of paddle
        int mouseX = me.getX() - (PADDLE_WIDTH / 2);

        // The paddle's y coordinate 
        double paddleY = paddle.getY();

        // Keep paddle within gameplay boundary
        if (mouseX > WIDTH - PADDLE_WIDTH) {
            mouseX = WIDTH - PADDLE_WIDTH;
        } else if (mouseX < 0 + PADDLE_WIDTH) {
            mouseX = 0;
        }

        // Set paddle's initial location in center of X axis
        paddle.setLocation(mouseX, paddleY);
    }

    /**
     * Method: run() Runs the Breakout program.
     */
    public void run() {
        // This boolean becomes true when game is over
        boolean isDone = false;
        // Initialize counter of bricks to later check if all bricks are eliminated and user has won the game
        int bricksCounter = NBRICK_ROWS * NBRICKS_PER_ROW;
        // Initialize number of turns to later check how many have been used
        int ballLives = NTURNS;
        // Introduce random method to later generate numbers
        Random randGen = new Random();
        // Center bricks in center of gameboard X axis
        int brickXOffset = (WIDTH - (((BRICK_WIDTH + BRICK_SEP) * NBRICKS_PER_ROW) - BRICK_SEP)) / 2;
        // Calculate center of X and Y axis
        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;
        // Create a random double for vx between one and three
        double vx = randGen.nextDouble() * 3.0 + 1.0;
        double vy = 3.0;

        // Create rows of bricks using constant variables
        for (int i = 0; i < NBRICK_ROWS; i++) {
            for (int j = 0; j < NBRICKS_PER_ROW; j++) {
                int x = j * (BRICK_SEP + BRICK_WIDTH);
                int y = i * (BRICK_SEP + BRICK_HEIGHT);

                //GRect arguements: x, y, width, height
                GRect brickOne = new GRect(x + brickXOffset, y + BRICK_Y_OFFSET, BRICK_WIDTH, BRICK_HEIGHT);

                //Style bricks
                brickOne.setFillColor(Color.RED);
                brickOne.setColor(Color.RED);
                brickOne.setFilled(true);

                //Add bricks to screen
                add(brickOne);
            }
        }

        // Create ball object
        GOval ball = new GOval(10, 10);
        ball.setFillColor(Color.gray);
        ball.setColor(Color.gray);
        ball.setFilled(true);

        // Half the time, vx will be negative
        if (randGen.nextBoolean()) {
            vx = -vx;
        }

        // Position ball in center of game board
        ball.setLocation(centerX - BALL_RADIUS, centerY - BALL_RADIUS);

        // Create paddle
        paddle = new GRect(centerX - PADDLE_WIDTH / 2, HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFillColor(Color.BLACK);
        paddle.setFilled(true);

        // Add paddle to screen
        add(paddle);

        // This sets up the program so that mouseMoved() is called when the
        // mouse moves.
        addMouseListeners();

        // Runs until isDone becomes true.
        while (!isDone) {
            // Add ball to screen at beginning of each turn
            add(ball);

            // Begin ball tragectory downward, with a random vx
            ball.move(vx, vy);

            // Keep ball within boundary of game board
            if (ball.getX() >= WIDTH - BALL_RADIUS || ball.getX() <= 0) {
                vx = -vx;
            } else if (ball.getY() <= 0) {
                vy = -vy;
                // If ball hits bottom wall, remove ball, decrement number of turns, and reset ball    
            } else if (ball.getY() > HEIGHT - BALL_RADIUS) {
                --ballLives;
                remove(ball);
                add(ball);
                // Start ball on new random trajectory from center of game board
                vx = randGen.nextDouble() * 3.0 + 1.0;
                ball.setLocation(centerX - BALL_RADIUS, centerY - BALL_RADIUS);
                if (randGen.nextBoolean()) {
                    vx = -vx;
                }
            }

            // Check to see if any part of ball collides with any object
            GObject colliderTopLeft = getElementAt(ball.getX(), ball.getY());
            GObject colliderTopRight = getElementAt(ball.getX() + (BALL_RADIUS * 2), ball.getY());
            GObject colliderBottomLeft = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS);
            GObject colliderBottomRight = getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS);

            if (colliderTopLeft != null) {
                // If the ball collides with the paddle, reverse the vy
                if (colliderTopLeft == paddle) {
                    vy = -vy;
                    // If the ball collides with a brick, eliminate the brick and decrement the brick counter    
                } else {
                    vy = -vy;
                    remove(colliderTopLeft);
                    --bricksCounter;
                }
            } else if (colliderTopRight != null) {
                if (colliderTopRight == paddle) {
                    vy = -vy;
                } else {
                    vy = -vy;
                    remove(colliderTopRight);
                    --bricksCounter;
                }
            } else if (colliderBottomLeft != null) {
                if (colliderBottomLeft == paddle) {
                    vy = -vy;
                } else {
                    vy = -vy;
                    remove(colliderBottomLeft);
                    --bricksCounter;
                }
            } else if (colliderBottomRight != null) {
                if (colliderBottomRight == paddle) {
                    vy = -vy;
                } else {
                    vy = -vy;
                    remove(colliderBottomRight);
                    --bricksCounter;
                }
            }

            // Check if all ball lives have been used and quit game loop with fail message
            if (ballLives == 0) {
                double messageX = this.getWidth() / 2;
                double messageY = this.getHeight() / 2;
                remove(ball);
                remove(paddle);
                GLabel winMessage = new GLabel("Game Over");
                winMessage.setFont("Times-48");
                winMessage.setLocation((messageX - winMessage.getWidth() / 2), messageY);
                add(winMessage);
                isDone = true;
            }

            // Check if all bricks have been eliminated and display win message
            if (bricksCounter == 0) {
                double messageX = this.getWidth() / 2;
                double messageY = this.getHeight() / 2;
                remove(ball);
                remove(paddle);
                GLabel winMessage = new GLabel("You win!!!");
                winMessage.setFont("Times-48");
                winMessage.setLocation((messageX - winMessage.getWidth() / 2), messageY);
                add(winMessage);
                isDone = true;
            }

            // Adjust the value of PAUSE_TIME to change the animation speed.
            pause(PAUSE_TIME);
        }

    }

}
