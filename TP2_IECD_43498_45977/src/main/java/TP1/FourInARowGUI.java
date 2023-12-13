package TP1;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.xml.sax.SAXException;

/**
 * Classe da janela do 4 em linha.
 */
public class FourInARowGUI extends JFrame {

   private static final int ROWS = 8;
   private static final int COLUMNS = 8;

   private JPanel contentPane;
   private JPanel boardPanel;
   private JPanel buttonPanel;
   private JLabel currentPlayerLabel;
   private JButton[] columnButtons = new JButton[COLUMNS];
   private Circle[][] circles = new Circle[ROWS][COLUMNS];
   private int[][] board = new int[ROWS][COLUMNS];
   private int currentPlayer = 1;
   private static final int CIRCLE_RADIUS = 25;
   ColumnButtonListener buttonGroup;
   private int playerNumber;

   public FourInARowGUI(int playerNumber) {
	  this.playerNumber = playerNumber;
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 600, 600);
      contentPane = new JPanel();
      contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      setContentPane(contentPane);
      contentPane.setLayout(new BorderLayout(0, 0));

      // Create the board panel
      boardPanel = new JPanel();
      boardPanel.setLayout(new GridLayout(ROWS, COLUMNS, 0, 0));
      boardPanel.setBackground(Color.BLUE);
      contentPane.add(boardPanel, BorderLayout.CENTER);

      // Create the circles
      for (int i = 0; i < ROWS; i++) {
         for (int j = 0; j < COLUMNS; j++) {
            circles[i][j] = new Circle(CIRCLE_RADIUS, Color.WHITE);
            circles[i][j].setPosition((int) (j + CIRCLE_RADIUS*1.3) ,(int) (i + CIRCLE_RADIUS * 1.2));
            boardPanel.add(circles[i][j]);
         }
      }

      // Create the button panel
      buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(1, COLUMNS, 5, 0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);
      
      // Create the current player label
      currentPlayerLabel = new JLabel("Turn: Player" + currentPlayer + " You are the player number");
      contentPane.add(currentPlayerLabel, BorderLayout.NORTH);

      
      // Initialize the board
      for (int i = 0; i < ROWS; i++) {
         for (int j = 0; j < COLUMNS; j++) {
            board[i][j] = 0;
         }
      }
      
      buttonGroup = new ColumnButtonListener(board, circles, currentPlayer, currentPlayerLabel, playerNumber);

      // Create the column buttons
      for (int i = 0; i < COLUMNS; i++) {
         columnButtons[i] = new JButton("" + i);
         columnButtons[i].addActionListener(buttonGroup);
         buttonPanel.add(columnButtons[i]);
      }
   }
   
   public Boolean[] getButtonClicked() {
	   if(this.buttonGroup != null) {
	   return this.buttonGroup.getColumn();
	   }
	return null;
   }
   
   public void setButtonClicked() {
	   this.buttonGroup.setFalse();
	   
   }
   

}



   