import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;

public class Grille extends JPanel {
    private JButton[][] grid;
    private TicTacToe ticTacToe;
    private int numPlayer;

    public Grille(TicTacToe ticTacToe){
        JPanel center = new JPanel(new GridLayout(3,3));
        this.ticTacToe = ticTacToe;
        grid = new JButton[3][3];

        numPlayer = (ticTacToe.getIsServer()) ? 0 : 1;

        center.setPreferredSize(new Dimension(300,300));
        
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                grid[i][j] = new JButton(" ");
                grid[i][j].setBackground(Color.WHITE);
                
                grid[i][j].setSize(100,100);
                
                grid[i][j].setBorder(BorderFactory.createLineBorder(new Color(43, 43, 43)));
                
                grid[i][j].addActionListener(e -> {
                    int[] pos = new int[2];
                    
                    for(int k=0; k<3 ; k++){
                        for(int l=0; l<3; l++){
                            if(grid[k][l].equals((JButton) e.getSource())){
                                pos[0] = k;
                                pos[1] = l;
                            }
                        }
                    }
                    
                    if(ticTacToe.getTurn() == numPlayer){
                        if(grid[pos[0]][pos[1]].getText() == " "){
                            if(ticTacToe.isMyTurn()){
                                ticTacToe.set(pos[0],pos[1]);

                                grid[pos[0]][pos[1]].setText(numPlayer == 0 ? "X" : "O");

                                ticTacToe.sendMoveToOppenent(pos[0], pos[1]);
                                
                                if(ticTacToe.win())
                                    JOptionPane.showMessageDialog(this, String.format("Fin de la partie, victoire du joueur %d", ticTacToe.getTurn()));
                                
                                System.out.println(ticTacToe);
                            }else
                                System.out.println("Not Empty case");
                        }
                    }
                     
                    else
                        System.out.println("Not your turn");    
                    
                });

                center.setBorder(BorderFactory.createEmptyBorder(60, 50, 0, 50));
                center.add(grid[i][j]);
            }
        }

        this.add(center);

        listenForIncomingMoves();
    }

    public void listenForIncomingMoves() {
        new Thread(() -> {
            while (true) {
                //ystem.out.println();
                if(ticTacToe.getTurn() != numPlayer){
                    int[] move = ticTacToe.receiveMoveFromOppenent();
                    if (move == null) break; 
    
                    System.out.printf("Received: (%d,%d)\n",move[0],move[1]);
                    updateBoard(move, numPlayer == 0 ? "O" : "X");
                }
                else{
                    try{
                        Thread.sleep(10);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private void updateBoard(int[] move, String symbole) {
        SwingUtilities.invokeLater(() -> {
            grid[move[0]][move[1]].setText(symbole);
            ticTacToe.set(move[0], move[1]);

            if(ticTacToe.win())
                JOptionPane.showMessageDialog(this, String.format("Fin de la partie, victoire du joueur %d", ticTacToe.getTurn()));
            
            ticTacToe.nextTurn();
        });
    }
}
