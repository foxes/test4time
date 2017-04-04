package gvsu457;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by fletcher on 11/17/16.
 */
public class TicTacToeGUI extends JFrame implements ActionListener {

    private JButton[][] gameBoard;
    private JPanel panel;
    private TicTacToeGame ticTacToe;

    public TicTacToeGUI(Player p1, Player p2){

        ticTacToe = new TicTacToeGame(p1, p2);
        panel = new JPanel(new GridLayout(3, 3));
        gameBoard = new JButton[3][3];
        for(int i = 0; i < 3; i++){
            for(int k = 0; k < 3; k++){
                gameBoard[i][k] = new JButton();
                gameBoard[i][k].addActionListener(this);
                panel.add(gameBoard[i][k]);
            }
        }

        add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400,400);
        setVisible(true);
        setTitle("Tic Tac Toe");

    }

    @Override
    public void actionPerformed(ActionEvent event){

        JComponent e = (JComponent) event.getSource();

        for(int i = 0; i < 3; i++){
            for(int k = 0; k < 3; k++){
                if(e == gameBoard[i][k]) {
                    System.out.println(i + ":" + k);
                }
            }
        }
    }


}
