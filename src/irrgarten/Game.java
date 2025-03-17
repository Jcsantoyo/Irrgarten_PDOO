
package irrgarten;
import java.util.ArrayList;

/**
 *
 * @author juan
 */
public class Game {
    private static final int MAX_ROUNDS=10;
    
    private int currentPlayerIndex;
    private String log;
    
    private Labyrinth lab;
    private ArrayList<Monster> monsters;
    private ArrayList<Player> players;
    private Player currentPlayer;
    
    public Game (int nplayers){
        players= new ArrayList<>();
        for(int i=0; i<nplayers;++i){
            players.add(new Player((char)i, Dice.randomIntelligence(),Dice.randomStrength()));
        }
        currentPlayerIndex=Dice.whoStarts(nplayers);
        currentPlayer=players.get(currentPlayerIndex);
        monsters=new ArrayList<>();
        //lab=new Labyrinth(); Atrinutos?
        log=" ";
        //Configuración del laberinto?
        lab.spreadPlayers(players);
    }
    
    public boolean finished(){
        return lab.haveAWinner();
    }
    
    public boolean nextStep(Directions preferredDirection){
        throw new UnsupportedOperationException();
    }
    
    public GameState getGameState(){
        //return GameState(Atributos?)
    }
    
    private void configureLabyrinth(){
        
    }
    
    private void nextPlayer(){
        
    }
    
    private Directions actualDirection(Directions preferredDirection){
        throw new UnsupportedOperationException();
    }
    
    private GameCharacter combat(Monster monster){
        throw new UnsupportedOperationException();
    }
    
    private void manageReward(GameCharacter winner){
        throw new UnsupportedOperationException();
    }
    
    private void manageResurrection(){
        throw new UnsupportedOperationException();
    }
    
    private void logPlayerWon(){
        log+="El jugador ha ganado el combate '\n'";
    }
    
    private void logMonsterWon(){
        log+="El monstruo ha ganado el combate '\n'";
    }
    
    private void logResurrected(){
        log+="El jugador ha resucitado '\n'";
    }
    
    private void logPlayerSkipTurn(){
        log+="El jugador ha perdido el turno por estar muerto '\n'";
    }
    
    private void logPlayerNoOrders(){
        log+="El jugador no ha seguido las instrucciones del jugador humano "
                + "(no fue posible) '\n'";
    }
    
    private void logNoMonster(){
        log+="El jugador se ha movido a una celda vacía o no le ha sido posible"
                + "moverse '\n'";
    }
    
    private void logRounds(int rounds, int max){
        log+="Se han producido " + String.valueOf(rounds) + " de " + 
              String.valueOf(max) + " rondas de combate '\n'";
    }
    
}
