/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tetris;

import javax.swing.JFrame;

/**
 *
 * @author Tshiamo
 */
public class GameWindow {
    public static final int WIDTH = 445, HEIGHT = 638;
    
    private GameBoard board;
    private JFrame window;
    
    public GameWindow(){
        window = new JFrame("Tetris");
        window.setSize(WIDTH,HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
                
        board = new GameBoard();
        window.add(board);
        window.addKeyListener(board);
        window.setVisible(true);        
    }
}
