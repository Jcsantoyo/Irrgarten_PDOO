/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author juan
 */
public class GameState {
    private String labyrinth;
    private String players;
    private String monsters;
    private int currentPlayer;
    private boolean winner;
    private String log;
    
    public GameState(String lab, String pl, String mons, int cpl, boolean win, 
                     String lg){
        labyrinth=lab;
        players=pl;
        monsters=mons;
        currentPlayer=cpl;
        winner=win;
        log=lg;
        
    }
    public String getLabyrinth(){
        return labyrinth;
    }
    public String getPlayers(){
        return players;
    }
    public String getMonsters(){
        return monsters;
    }
    public int getCurrentPlayer(){
        return currentPlayer;
    }
    public boolean getWinner(){
        return winner;
    }
    public String getLog(){
        return log;
    }
}
