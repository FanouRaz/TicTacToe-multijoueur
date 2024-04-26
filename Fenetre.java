import javax.swing.JFrame;

import java.awt.Container;

import javax.swing.BoxLayout;

public class Fenetre extends JFrame {
    public Fenetre(TicTacToe ticTacToe, String status){
        Container pane = getContentPane();
        BoxLayout layout = new BoxLayout (pane, BoxLayout.Y_AXIS);

        pane.setLayout(layout);

        Grille grille = new Grille(ticTacToe);
        
        pane.add(grille); 
        
        setSize(400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic-Tac-Toe Multijoueur "+status);
        setResizable(false);
        setVisible(true);
    }
    
}
