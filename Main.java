import java.io.IOException;

public class Main {
    public static void main(String... args) throws IOException {
        TicTacToe ticTacToe = new TicTacToe();
        
        System.out.println("Tic-Tac-Toe multijoueur!");


        if (args.length < 2) {
            System.out.println("Usage: java Main <host|client> [port] [host]");
            return;
        }
        
        if(args[0].equals("host"))
            ticTacToe.serve(Integer.parseInt(args[1]));
        
        else if(args[0].equals("client"))
            ticTacToe.connect(args[1], Integer.parseInt(args[2])); 
            
        System.out.println(ticTacToe);

        new Fenetre(ticTacToe, ticTacToe.getIsServer() ? "Host" : "Client");
        
        /* while(true){
            System.out.println(ticTacToe.isMyTurn() ? "Your turn" : (ticTacToe.getIsServer()) ? "Client turn" : "Host turn");

            ticTacToe.play();

            if(ticTacToe.win()){
                System.out.printf("Victoire du joueur %d\n",ticTacToe.getTurn());
                break;
            }

            if(ticTacToe.isDraw() && !ticTacToe.win()){
                System.out.printf("Egalité!\n");
                break;
            }

            ticTacToe.nextTurn();
        } */
        
        //ticTacToe.stop();
        //System.out.println("Fin de la partie");
    }
}
