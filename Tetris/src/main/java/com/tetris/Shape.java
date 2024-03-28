/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tetris;

import static com.tetris.GameBoard.BLOCK_SIZE;
import static com.tetris.GameBoard.GAME_BOARD_HEIGHT;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Tshiamo
 */
public class Shape {
    private int x =4, y = 0;
    private int normal = 600;
    private int fast = 50;
    private int delayTimeForMovement = normal;
    private long beginTime;
    
    private int deltaX = 0;
    private boolean collision = false;
    
    private int[][] coords;
    private GameBoard board;
    private Color color;
    
    public Shape(int[][] coords, GameBoard board, Color color){
        this.coords = coords;
        this.board = board;
        this.color = color;
    }
    
    public void ResetBoard(){
        this.x = 4;
        this.y = 0;
        this.collision = false;
    }
    
    public void Update(){
        if (collision) {
            // Saving the previous shapes and colors
            for (int row = 0; row < coords.length; row++){
                for (int col = 0; col < coords[0].length; col++){
                    if (coords[row][col] != 0){
                        board.getGameBoard()[y + row][x + col] = color;
                    }
                }
            }
            checkLine();
            // Set current shape
            board.SetCurrentShape();
            return;
        }
        
        // Check moving horizontal
        boolean moveX = true;
        if (!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0)){
            for (int row = 0; row < coords.length; row++){
                for (int col = 0; col < coords[row].length; col++){
                    if (coords[row][col] != 0){
                        if (board.getGameBoard()[y + row][x + deltaX + col] != null){
                            moveX = false;
                        }
                    }
                }
            }
            if (moveX){
                x += deltaX;
            }
        }
        deltaX = 0;
                
        if (System.currentTimeMillis() - beginTime > delayTimeForMovement){
            // Vertical movement
            if (!(y + 1 + coords.length > GAME_BOARD_HEIGHT)){
                for (int row = 0; row < coords.length; row++){
                    for (int col = 0; col < coords[row].length; col++){
                        if (coords[row][col] != 0){
                            if (board.getGameBoard()[y + 1 + row][x + deltaX + col] != null){
                                collision = true;
                            }
                        }
                    }
                }
                if (!collision){
                    y++;
                }
            } else {
                collision = true;
            }
            beginTime = System.currentTimeMillis();
        }
    }
    
    private void checkLine(){
        int bottomLine = board.getGameBoard().length - 1;
        for (int topLine = board.getGameBoard().length - 1; topLine > 0; topLine--){
            int count = 0;
            for (int col = 0; col < board.getGameBoard()[0].length; col++){
                if(board.getGameBoard()[topLine][col] != null){
                    count++;
                }
                board.getGameBoard()[bottomLine][col] = board.getGameBoard()[topLine][col];
            }
            if (count < board.getGameBoard()[0].length){
                bottomLine--;
            }
        }
    }
    
    public void RotateShape(){
        int[][] rotatedShape = TransposeMatrix(coords);
        ReverseRows(rotatedShape);
        
        // Check that the shape would still be with in the board after rotation
        if ((x + rotatedShape[0].length > GameBoard.GAME_BOARD_WIDTH) || (y + rotatedShape.length > GameBoard.GAME_BOARD_HEIGHT)){
            return;
        }
        
        // Check that no shape is in a collision with other shapes after rotation is implemeneted
        for (int row = 0; row < rotatedShape.length; row++){
            for (int col = 0; col < rotatedShape[row].length; col++){
                if (rotatedShape[row][col] != 0){
                    if(board.getGameBoard()[y + row][x + col] != null){
                        return;
                    }
                }
            }
        }
        
        coords = rotatedShape;
    }
    
    private int[][] TransposeMatrix(int[][] matrix){
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int row = 0; row < matrix.length; row++){
            for (int col = 0; col < matrix[0].length; col++){
                temp[col][row] = matrix[row][col];
            }
        }
        return temp;
    }
    
    private void ReverseRows(int[][] matrix){
        int middle = matrix.length / 2;
        for (int row = 0; row < middle; row++){
            int[] temp = matrix[row];
            matrix[row] = matrix[matrix.length - row -1];
            matrix[matrix.length - row - 1] = temp;
        }
    }
    
    public void Render(Graphics g){
        // Draw the shape
        for (int row = 0; row < coords.length; row++){
            for (int col = 0; col < coords[0].length; col++){
                if (coords[row][col] != 0){
                    g.setColor(color);
                    g.fillRect(col * BLOCK_SIZE + x * BLOCK_SIZE, row * BLOCK_SIZE + y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);   
                }
            }
        }
    }
    
    public int[][] GetCoords(){
        return coords;
    }
    
    public void SpeedUp() {
        delayTimeForMovement = fast;
    }
    
    public void SlowDown() {
        delayTimeForMovement = normal;
    }
    
    public void MoveLeft() {
        deltaX = -1;
    }
    
    public void MoveRight(){
        deltaX = 1;
    }
    
    public int GetY(){
        return y;
    }
    
    public int GetX(){
        return x;
    }  
}
