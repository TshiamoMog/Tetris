/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Tshiamo
 */
public class GameBoard extends JPanel implements KeyListener {
    public static int STATE_GAME_PLAY = 0;
    public static int STATE_GAME_PAUSE = 1;
    public static int STATE_GAME_OVER = 2;
    
    private int state = STATE_GAME_PLAY;
    
    private static int FPS = 60;
    private static int delay = FPS / 1000;
    
    public static final int GAME_BOARD_WIDTH = 10;
    public static final int GAME_BOARD_HEIGHT = 20;
    public static final int BLOCK_SIZE = 30;
    private Timer looper;
    private Color[][] board = new Color[GAME_BOARD_HEIGHT][GAME_BOARD_WIDTH];
    
    private Random random;
    
    private Color[] colors = {Color.decode("#ed1c24"),Color.decode("#ff7f27"),Color.decode("#fff200"),
            Color.decode("#22b14c"),Color.decode("#00a2e8"),Color.decode("#a349a4"),Color.decode("#3f488cc"),};
    
    private Shape[] shapes = new Shape[7];
    
    private Shape currentShape;
    
    public GameBoard(){
        random = new Random();
        
        //create shapes
        shapes[0] = new Shape(new int[][]{
            {1, 1, 1, 1} // I shape
        }, this, colors[0]);
        shapes[1] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 1, 0},  // T shape
        }, this, colors[1]);
        shapes[2] = new Shape(new int[][]{
            {1, 1, 1},
            {1, 0, 0}, // L right shape
        }, this, colors[2]);
        shapes[3] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 0, 1}, // L left shape
        }, this, colors[3]);
        shapes[4] = new Shape(new int[][]{
            {0, 1, 1},
            {1, 1, 0}, // S right shape
        }, this, colors[4]);
        shapes[5] = new Shape(new int[][]{
            {1, 1, 0},
            {0, 1, 1}, // S left shape
        }, this, colors[5]);
        shapes[6] = new Shape(new int[][]{
            {1, 1},
            {1, 1}, // O shape
        }, this, colors[6]);
        
        currentShape = shapes[0];
        
        looper = new Timer(delay, new ActionListener(){
            int n = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                Update();
                repaint();
            }
            
        });
        looper.start();
    }
    
    private void Update(){
        if(state == STATE_GAME_PLAY){
            currentShape.Update();
        }
    }
    
    public void SetCurrentShape(){
        currentShape = shapes[random.nextInt(shapes.length)];
        currentShape.ResetBoard();
        CheckGameOver();
    }
    
    private void CheckGameOver(){
        int[][] coords = currentShape.GetCoords();
        for(int row = 0; row < coords.length; row++){
            for(int col = 0; col < coords[0].length; col++){
                if (coords[row][col] != 0){
                    if(board[row + currentShape.GetY()][col + currentShape.GetX()] != null){
                        state = STATE_GAME_OVER;
                    }
                }
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        currentShape.Render(g);
        
        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[row].length; col++){
                if (board[row][col] != null){
                    g.setColor(board[row][col]);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE,BLOCK_SIZE);
                }
            }
        }
        // Draw the board
        g.setColor(Color.white);
        for (int row = 0; row < GAME_BOARD_HEIGHT + 1; row++){
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * GAME_BOARD_WIDTH, BLOCK_SIZE * row);
        }
        
        for (int col = 0; col < GAME_BOARD_WIDTH + 1; col++){
            g.drawLine(BLOCK_SIZE * col, 0, BLOCK_SIZE * col, BLOCK_SIZE * GAME_BOARD_HEIGHT);
        }
        
        if(state == STATE_GAME_OVER){
            g.setColor(Color.white);
            g.drawString("GAME OVER", 50, 200);
        }
        
        if(state == STATE_GAME_PAUSE){
            g.setColor(Color.white);
            g.drawString("GAME PAUSED", 50, 200);
        }
    }
    
    public Color[][] getGameBoard(){
        return board;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            currentShape.SpeedUp();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            currentShape.MoveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            currentShape.MoveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
            currentShape.RotateShape();
        }
        
        
        // Clear the board for new game
        if(state == STATE_GAME_OVER){
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                for (int row = 0; row < board.length; row++){
                    for (int col = 0; col < board[row].length; col++){
                        board[row][col] = null;
                    }
                }
                SetCurrentShape();
                state = STATE_GAME_PLAY;
            }
        }

        // Pause the game
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            if(state == STATE_GAME_PLAY){
                state = STATE_GAME_PAUSE;
            } else if(state == STATE_GAME_PAUSE){
                state = STATE_GAME_PLAY;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            currentShape.SlowDown();
        }}
}
