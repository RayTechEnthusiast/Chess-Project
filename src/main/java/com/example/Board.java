package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;

// You will be implementing a part of a function and a whole function in this document.
// Please follow the directions for the suggested order of completion that should make testing easier.
@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {
    // Resource location constants for piece images
    public static final String PICTURE_PATH = "/src/main/java/com/example/Pictures/";
    private static final String RESOURCES_WBISHOP_PNG = PICTURE_PATH + "wbishop.png";
    private static final String RESOURCES_BBISHOP_PNG = PICTURE_PATH + "bbishop.png";
    private static final String RESOURCES_WKNIGHT_PNG = PICTURE_PATH + "wknight.png";
    private static final String RESOURCES_BKNIGHT_PNG = PICTURE_PATH + "bknight.png";
    private static final String RESOURCES_WROOK_PNG = PICTURE_PATH + "wrook.png";
    private static final String RESOURCES_BROOK_PNG = PICTURE_PATH + "brook.png";
    private static final String RESOURCES_WKING_PNG = PICTURE_PATH + "wking.png";
    private static final String RESOURCES_BKING_PNG = PICTURE_PATH + "bking.png";
    private static final String RESOURCES_BQUEEN_PNG = PICTURE_PATH + "bqueen.png";
    private static final String RESOURCES_WQUEEN_PNG = PICTURE_PATH + "wqueen.png";
    private static final String RESOURCES_WPAWN_PNG = PICTURE_PATH + "wpawn.png";
    private static final String RESOURCES_BPAWN_PNG = PICTURE_PATH + "bpawn.png";
    private static final String RESOURCES_WMENCHUKOV_PNG = PICTURE_PATH + "wmenchukov.png";
    private static final String RESOURCES_BMENCHUKOV_PNG = PICTURE_PATH + "bmenchukov.png";

    // constant used to keep track of where the piece should be drawn when the user is dragging it
    private static final int PIECE_OFFSET = 24;

    // Logical and graphical representations of board
    private final Square[][] board;
    private final GameWindow g;

    // contains true if it's white's turn.
    private boolean whiteTurn;

    // if the player is currently dragging a piece this variable contains it.
    Piece currPiece;
    // the square your piece came from when the user tries to move it.
    private Square fromMoveSquare;
    // the square your piece tries to go to when the user tries to move it.
    private Square endSquare;

    // used to keep track of the x/y coordinates of the mouse.
    private int currX;
    private int currY;

    // precondition: g is a non-null valid GameWindow object.
    // postcondition: a full 8 by 8 board of alternating colored squares is created,
    // pieces are initialized, and the board is ready to be displayed.
    public Board(GameWindow g) {
        this.g = g;
        board = new Square[8][8];
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // populate the board with squares in row major order.
        // each outer level is a row, each inner level is a column.
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boolean isWhiteSquare = ((row + col) % 2 == 0);
                board[row][col] = new Square(this, isWhiteSquare, row, col);
                this.add(board[row][col]);
            }
        }

        initializePieces();

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;
    }

    // precondition: board has already been filled with valid Square objects.
    // postcondition: the starting pieces are placed in purposeful positions for both sides.
    void initializePieces() {
        // White back row
        board[0][0].put(new Piece(true, RESOURCES_WROOK_PNG, "rook"));
        board[0][1].put(new Piece(true, RESOURCES_WKNIGHT_PNG, "knight"));
        board[0][2].put(new Piece(true, RESOURCES_WBISHOP_PNG, "bishop"));
        board[0][3].put(new Piece(true, RESOURCES_WKING_PNG, "king"));
        board[0][4].put(new Piece(true, RESOURCES_WQUEEN_PNG, "queen"));
        board[0][5].put(new Piece(true, RESOURCES_WBISHOP_PNG, "bishop"));
        board[0][6].put(new Piece(true, RESOURCES_WKNIGHT_PNG, "knight"));
        board[0][7].put(new Piece(true, RESOURCES_WROOK_PNG, "rook"));

        // Black back row
        board[7][0].put(new Piece(false, RESOURCES_BROOK_PNG, "rook"));
        board[7][1].put(new Piece(false, RESOURCES_BKNIGHT_PNG, "knight"));
        board[7][2].put(new Piece(false, RESOURCES_BBISHOP_PNG, "bishop"));
        board[7][3].put(new Piece(false, RESOURCES_BKING_PNG, "king"));
        board[7][4].put(new Piece(false, RESOURCES_BQUEEN_PNG, "queen"));
        board[7][5].put(new Piece(false, RESOURCES_BBISHOP_PNG, "bishop"));
        board[7][6].put(new Piece(false, RESOURCES_BKNIGHT_PNG, "knight"));
        board[7][7].put(new Piece(false, RESOURCES_BROOK_PNG, "rook"));

        // Pawns
        for (int col = 0; col < 8; col++) {
            board[1][col].put(new Piece(true, RESOURCES_WPAWN_PNG, "pawn"));
            board[6][col].put(new Piece(false, RESOURCES_BPAWN_PNG, "pawn"));
        }

        // Menchukov pieces
        // a3 = row 2, col 0
        // a6 = row 5, col 0
        board[2][0].put(new Piece(true, RESOURCES_WMENCHUKOV_PNG, "menchukov"));
        board[5][0].put(new Piece(false, RESOURCES_BMENCHUKOV_PNG, "menchukov"));
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col].draw(g);
            }
        }

        if (currPiece != null) {
            if (currPiece.getColor() == whiteTurn) {
                final Image img = currPiece.getImage();
                g.drawImage(img, currX, currY, 50, 50, null);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();

        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied() && sq.getOccupyingPiece().getColor() == whiteTurn) {
            currPiece = sq.getOccupyingPiece();

            for (Square s : currPiece.getLegalMoves(this, sq)) {
                s.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.red));
            }

            fromMoveSquare = sq;
            sq.setDisplay(false);
        }
        repaint();
    }

    // precondition: the user has released the mouse after selecting a piece.
    // postcondition: if the destination square is legal, the piece is moved there.
    // otherwise the piece returns to its original square.
    @Override
    public void mouseReleased(MouseEvent e) {
        if (currPiece == null) {
            return;
        }

        for (Square[] row : board) {
            for (Square s : row) {
                s.setBorder(null);
            }
        }

        endSquare = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));
        ArrayList<Square> legalMoves = currPiece.getLegalMoves(this, fromMoveSquare);

        if (legalMoves.contains(endSquare)) {
            endSquare.put(currPiece);
            fromMoveSquare.removePiece();
            whiteTurn = !whiteTurn;
        }

        if (fromMoveSquare != null) {
            fromMoveSquare.setDisplay(true);
        }

        currPiece = null;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currX = e.getX() - PIECE_OFFSET;
        currY = e.getY() - PIECE_OFFSET;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}