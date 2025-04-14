
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
    
    //Dimensiones del laberinto
    private static final int ROWS=7;
    private static final int COLS=7;
    
    
    public Game (int nPlayers){
        
        // Definimos casilla de salida
        int exitRow=Dice.randomPos(ROWS);
        int exitCol=Dice.randomPos(COLS);

        // Inicializamos los vectores de jugadores y monstruos
        this.players=new ArrayList<>();
        this.monsters=new ArrayList<>();
        
        for(int n = 0; n < nPlayers; n++){
            players.add(new Player(Character.forDigit(n, 10), Dice.randomIntelligence(), Dice.randomStrength()));
        }
                
        // Definimos el jugador que empezará, es decir, el currentPlayer
        this.currentPlayerIndex=Dice.whoStarts(nPlayers);
        this.currentPlayer=this.players.get(this.currentPlayerIndex);
        
        // Inicializamos la instancia de laberinto
        this.lab= new Labyrinth(ROWS, COLS, exitRow, exitCol);
        // Se configura con bloques y mosntruos el laberinto
        this.configureLabyrinth();

        // Se distribuyen los jugadores por el laberinto
        this.lab.spreadPlayers(this.players);
        
        // Inicializamos log
        this.log="- Game just started.\n";
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
       
        String infoPlayers="";
        String infoMonsters="";

        for (int i=0; i<this.players.size(); i++){
            infoPlayers+="- " + this.players.get(i).toString()+"\n";
        }
        
        
        for (int i=0; i<this.monsters.size(); i++){
            infoMonsters+="- " + this.monsters.get(i).toString()+"\n";
        }
        
        GameState estadoGeneral = new GameState (this.lab.toString(), infoPlayers,
                                infoMonsters, this.currentPlayerIndex, this.finished(), this.log);
        
        return estadoGeneral;
    }  
    
    
    private void configureLabyrinth(){
        
        Monster monster_1=new Monster("Mike Wasowsky", 10,10);
        Monster monster_2=new Monster("Alucard", Dice.randomIntelligence(),Dice.randomStrength());
        Monster monster_3=new Monster("Frankenstein", Dice.randomIntelligence(),Dice.randomStrength());
        lab.addMonster(3, 3, monster_1);
        lab.addMonster(1, 5, monster_2);
        lab.addMonster(5, 5, monster_3);
        monsters.add(monster_1);
        monsters.add(monster_2);
        monsters.add(monster_3);
        lab.addBlock(Orientation.HORIZONTAL, 6, 3, 1);
        lab.addBlock(Orientation.HORIZONTAL, 2, 1, 2);
        lab.addBlock(Orientation.VERTICAL, 4, 5, 2);

        
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
        
         int rounds=0;   // Inicializamos el número de rounds a 0
        
        // Suponemos que el jugador ganará, y empieza este atacando.
        GameCharacter winner=GameCharacter.PLAYER;
        boolean lose = monster.defend(currentPlayer.attack());
        
        
        // Bucle que simula el combate entre el jugador y el monstruo, de forma alternada.

        while (!lose && rounds<MAX_ROUNDS){ // Si el monstruo no ha muerto y no se han superado los rounds

            rounds++;   // Incrementamos el número de rounds
            
            // Suponemos que el monstruo ganará, y continúa este atacando.
            winner = GameCharacter.MONSTER;
            lose = currentPlayer.defend(monster.attack());
            
            if (!lose){ // Si el jugador no ha muerto

                // Suponemos que el jugador ganará, y continúa este atacando.
                winner = GameCharacter.PLAYER;
                lose = monster.defend(currentPlayer.attack());
            }
        } // while (!lose && rounds<MAX_ROUNDS)
        
        this.logRounds(rounds, MAX_ROUNDS);
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
    
    /**
     * Se actualiza el log con la información de que el jugador actual
     * ha ganado el combate
     */
    private void logPlayerWon(){
        this.log+= "- Player "+this.currentPlayerIndex+" won the fight.\n";
    }
    
    /**
     * Se actualiza el log con la información de que el mosntruo
     * ha ganado el combate al jugador actual
     */
    private void logMonsterWon(){
        this.log+= "- Monster won the fight.\n";
    }
    
    /**
     * Se actualiza el log con la información de que el jugador actual
     * ha ganado resucitado
     */
    private void logResurrected(){
        this.log+= "- Player "+this.currentPlayerIndex+" resurrected.\n";
    }
    
    /**
     * Se actualiza el log con la información de que el jugador actual
     * ha perdido su turno por estar muerto
     */
    private void logPlayerSkipTurn(){
        this.log+= "- Player "+this.currentPlayerIndex+" skipped turn (is dead).\n";
    }
    
    /**
     * Se actualiza el log con la información de que el jugador actual
     * ha intentado ejecutar alguna acción no permitida y no se ha producido ningún
     * cambio
     */
    private void logPlayerNoOrders(){
        this.log+= "- Player "+this.currentPlayerIndex+" didn't follow orders, it was not possible.\n";
    }
    
    /**
     * Se actualiza el log con la información de que el jugador actual
     * se ha desplazado a una casilla vacía o no ha sido posible el desplazamiento
     */
    private void logNoMonster(){
        this.log+= "- Player "+this.currentPlayerIndex+" moved to an empty square or it was not possible to move.\n";
    }
    
    /**
     * Se actualiza el log con la información de que se han producido rounds
     * de max rounds en el combate actual
     * 
     * @param rounds Número de rounds que se han producido
     * @param max Número máximo de rounds que puede durar un combate
     */
    private void logRounds(int rounds, int max){
        this.log+= "- Rounds: "+rounds+"/"+max+".\n";
    }
    
}