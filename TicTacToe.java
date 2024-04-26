import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;

public class TicTacToe{
    private String[][] grid;
    private String[] symboles;
    private ServerSocket server;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner playerInput;
    private boolean isServer;
    
    static int turn;

    public TicTacToe(){
        grid = new String[3][3];   

        for(int i=0; i< grid.length; i++){
            for(int j=0; j< grid[i].length; j++)
                grid[i][j] = ".";
        }

        symboles = new String[]{"X","O"};
        playerInput = new Scanner(System.in);
        turn = 0;
    }

    public void serve(int port){
        try{
            server = new ServerSocket(port);
            
            System.out.println("En attente d'un autre joueur...");
            socket = server.accept();
            System.out.println("Joueur connecté, début de la partie!");

            isServer = true;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void connect(String host,int port){
        try{
            System.out.printf("Connexion au serveur %s:%d\n",host,port);
            socket = new Socket(host, port);

            System.out.println("Connexion au serveur établie, début de la partie!");

            isServer = false;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void stop() throws IOException{
        if(server != null)  server.close();
        if(socket != null)  socket.close();
        
        playerInput.close();
    }


    public boolean win(){
        return checkVertical() || checkHorizontal() || checkDiagonal() || checkAntiDiagonal();
    }

    public boolean checkHorizontal(){
    row:for(int i=0; i<grid.length; i++){
            for(int j=0; j< grid[i].length; j++){
                if(grid[i][j] != symboles[turn])
                    continue row;
                
                if(grid[i][j] == symboles[turn] && j==grid[i].length - 1)
                    return true;
            }
        }

        return false;
    }

    public boolean checkVertical(){
        row:for(int i=0; i<grid.length; i++){
                for(int j=0; j< grid.length; j++){
                    if(!grid[j][i].equals(symboles[turn]))
                        continue row;
                    
                    if(grid[j][i].equals(symboles[turn]) && j==grid.length - 1)
                        return true;
                }
        }

        return false;
    }

    public boolean checkDiagonal(){
        for(int i=0; i<grid.length; i++){
            if(!grid[i][i].equals(symboles[turn]))
                return false;
        }

        return true;
    }

    public boolean checkAntiDiagonal(){
        for(int i=0; i<grid.length; i++){
                if(!grid[i][grid.length-i-1].equals(symboles[turn]))
                    return false;
        }

        return true;
    }

    public void play(){
        int x=-1,y=-1;

        if(isMyTurn()){
            do{
                x = -1;
                y = -1;
                while(x < 0 || x >= grid.length){
                    System.out.print("Entrez la coordonnée x où vous souhaitez placer votre pion: ");
                    x = playerInput.nextInt();
                }
        
                while(y < 0 || y >= grid[x].length){
                    System.out.print("Entrez la coordonnée y où vous souhaitez placer votre pion: ");
                    y = playerInput.nextInt();
                }
    
                if(!grid[x][y].equals("."))
                    System.out.println("La position que vous avez choisi est déjà pris, choisissez une autre!");
    
            }while(!grid[x][y].equals("."));

            System.out.printf("[!]Sending (%d,%d) to the oppenent...\n",x,y);
            out.printf("%d,%d\n",x,y);
           
            System.out.println("[+]Move sent!!");
        }

        else{
            try{
                System.out.println("En attente du déplacement de l'adversaire...");    
                String[] move = in.readLine().split(",");
                
                System.out.printf("Oppenent move: (%s,%s)\n",move[0],move[1]);
                x = Integer.parseInt(move[0]);
                y = Integer.parseInt(move[1]);
            }catch(IOException e){
                e.printStackTrace();        
            }
        }

        grid[x][y] = turn == 0 ? "X" : "O";

        System.out.println(this);
    }

    public boolean isDraw(){
        for(int i = 0; i<grid.length; i++){
            for(int j=0; j<grid[i].length; j++){
                if(grid[i][j].equals("."))   
                    return false;    
            }
        }

        return true;
    }

    public int getTurn(){
        return turn;
    }

    public boolean isMyTurn(){
        if(isServer)
            return turn == 0;
        else
            return turn == 1;
    }

    public boolean getIsServer(){
        return isServer;
    }

    public String get(int i, int j){
        return grid[i][j];
    }

    public void set(int i, int j){
        grid[i][j] = symboles[turn];
    }

    public void nextTurn(){
        turn = (turn+1)%2;
    }

    public void sendMoveToOppenent(int x, int y){
        System.out.printf("[!]Sending (%d,%d) to the oppenent...\n",x,y);
        out.printf("%d,%d\n",x,y);
       
        System.out.println("[+]Move sent!!");

        grid[x][y] = turn == 0 ? "X" : "O";

        nextTurn();
    }

    public int[] receiveMoveFromOppenent(){
        try{
            System.out.println("En attente du déplacement de l'adversaire...");    
            String[] move = in.readLine().split(",");
                    
            System.out.printf("Oppenent move: (%s,%s)\n",move[0],move[1]);
            
            int x = Integer.parseInt(move[0]);
            int y = Integer.parseInt(move[1]);

            return new int[]{x,y};
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString(){
        String str = "";
        
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid[i].length; j++)
                str += grid[i][j] + ((j < grid[i].length - 1) ? "\t" : "");
            
            str += "\n";
        }

        return str;
    }
}