package com.example;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Piece {
    private final boolean color;
    private BufferedImage img;
    private final String type;

    // precondition: img_file is the correct path to an image file.
    // postcondition: this Piece is created with a color, type, and image.
    public Piece(boolean isWhite, String img_file, String type) {
        this.color = isWhite;
        this.type = type;

        try {
            if (this.img == null) {
                this.img = ImageIO.read(new File(System.getProperty("user.dir") + img_file));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public boolean getColor() {
        return color;
    }

    public Image getImage() {
        return img;
    }

    public String getType() {
        return type;
    }

    // precondition: g and currentSquare must be non-null valid objects.
    // postcondition: the image stored in the img property of this object is drawn to the screen.
    public void draw(Graphics g, Square currentSquare) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();

        g.drawImage(this.img, x, y, currentSquare.getWidth(), currentSquare.getHeight(), null);
    }

    // RULES FOR THE MENCHUKOV:
    // - The Menchukov can only move by capturing an opponent's piece.
    // - It may move to any square on the board that contains an enemy piece.
    // - It may not move to an empty square.
    // - It may not capture a king.

    // precondition: board and start are non-null valid objects.
    // postcondition: returns a list of every square controlled by this piece.
    // a square is controlled if this piece could legally capture into it.
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
        ArrayList<Square> controlledSquares = new ArrayList<Square>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square targetSquare = board[row][col];

                if (targetSquare.isOccupied()) {
                    Piece otherPiece = targetSquare.getOccupyingPiece();

                    if (otherPiece.getColor() != this.color && !otherPiece.getType().equals("king")) {
                        controlledSquares.add(targetSquare);
                    }
                }
            }
        }

        return controlledSquares;
    }

    // precondition: b and start are non-null valid objects.
    // postcondition: returns an ArrayList of every legal move for this piece.
    public ArrayList<Square> getLegalMoves(Board b, Square start) {
        Square[][] board = b.getSquareArray();
        ArrayList<Square> legalMoves = new ArrayList<Square>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square targetSquare = board[row][col];

                if (targetSquare.isOccupied()) {
                    Piece otherPiece = targetSquare.getOccupyingPiece();

                    if (otherPiece.getColor() != this.color && !otherPiece.getType().equals("king")) {
                        legalMoves.add(targetSquare);
                    }
                }
            }
        }

        return legalMoves;
    }
}