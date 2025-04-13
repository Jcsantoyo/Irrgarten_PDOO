
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
        
        log=" ";
        
        configureLabyrinth();
    }
    
    public boolean finished(){
        return lab.haveAWinner();
    }
    
    public boolean nextStep(Directions preferredDirection){
        log="";
        boolean dead=currentPlayer.dead();
        
        if(!dead){
            Directions direction=actualDirection(preferredDirection);
            
            if(direction != preferredDirection){
                logPlayerNoOrders();
            }
            
            Monster monster=lab.putPlayer(direction, currentPlayer);
            
            if(monster==null){
                logNoMonster();
            }
            else{
                GameCharacter winner=combat(monster);
                manageReward(winner);
            }
            
        }
        else{
            manageResurrection();
        }
        boolean endGame=finished();
        
        if(!endGame)
            nextPlayer();
        return endGame;
    }
    
    public GameState getGameState(){
        
        String player_str="";
        
        for (int i=0; i<players.size();++i){
            player_str+=players.get(i).toString();
        }
        
        String monster_str="";
        
        for (int i=0; i<monsters.size();++i){
            monster_str=monsters.get(i).toString();
        }
        
        return new GameState(
               lab.toString(),
               player_str,
               monster_str,
               currentPlayerIndex,
               lab.haveAWinner(),
               ""
                );    
    }
    
    private void configureLabyrinth(){
        lab=new Labyrinth(5,5,Dice.randomPos(5),Dice.randomPos(5)); 
        
        //Configuración del laberinto como yo quiera
        lab.spreadPlayers(players);
    }
    
    private void nextPlayer(){
        if(currentPlayerIndex==players.size()-1)
            currentPlayerIndex=0;
        else
            ++currentPlayerIndex;
        
        currentPlayer=players.get(currentPlayerIndex);
    }
    
    private Directions actualDirection(Directions preferredDirection){
        
        int currentRow=currentPlayer.getRow();
        int currentCol=currentPlayer.getCol();
        
        ArrayList<Directions> validMoves=lab.validMoves(currentRow,currentCol);

        Directions output=currentPlayer.move(preferredDirection,validMoves);
        
        return output;

    }
    
    private GameCharacter combat(Monster monster){
        
        int rounds=0;
        GameCharacter winner=GameCharacter.PLAYER;
        
        float playerAttack=currentPlayer.attack();
        boolean lose=monster.defend(playerAttack);
        while(!lose && rounds<MAX_ROUNDS){
            winner=GameCharacter.MONSTER;
            ++rounds;
            float monsterAttack=monster.attack();
            lose=currentPlayer.defend(monsterAttack);
            
            if(!lose){
                playerAttack=currentPlayer.attack();
            }
            else{
                winner=GameCharacter.PLAYER;
                lose=monster.defend(playerAttack);
            }
        }
        logRounds(rounds,MAX_ROUNDS);
        
        return winner;
        
    }
    
    private void manageReward(GameCharacter winner){
        if(winner==GameCharacter.PLAYER){
            currentPlayer.receiveReward();
            logPlayerWon();
        }
        else
            logMonsterWon();
            
    }
    
    private void manageResurrection(){
        boolean resurrect=Dice.resurrectPlayer();
        
        if(resurrect){
            currentPlayer.resurrect();
            logResurrected();
        }
        else{
            logPlayerSkipTurn();
        }
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
