
package irrgarten;
import java.util.ArrayList;


/**
 * Clase que representa el juego Irrgarten. Esta clase se encarga de gestionar el estado del juego,
 * las acciones de los jugadores y los monstruos, y de generar un estado del juego que pueda ser
 * consultado por la interfaz de usuario.
 * 
 * @author Juan Caballero Santoyo
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
    
    
    /**
     * Constructor de la clase Game, que inicializará los jugadores a jugar 
     * (definiendo el jugador que inicia la partida) y el estado del laberinto, 
     * que incluye su propia construcción, es decir, definir los bloques,
     * los monstruos y sus posiciones.
     * 
     * @param nPlayers Numero de jugadores
     */
    public Game (int nPlayers){
        
        int exitRow=Dice.randomPos(ROWS);
        int exitCol=Dice.randomPos(COLS);

        this.players=new ArrayList<>();
        this.monsters=new ArrayList<>();
        
        for(int n = 0; n < nPlayers; n++){
            players.add(new Player(Character.forDigit(n, 10), Dice.randomIntelligence(), Dice.randomStrength()));
        }
                
        this.currentPlayerIndex=Dice.whoStarts(nPlayers);
        this.currentPlayer=this.players.get(this.currentPlayerIndex);
        
        this.lab= new Labyrinth(ROWS, COLS, exitRow, exitCol);
        this.configureLabyrinth();

        this.lab.spreadPlayers(this.players);
        
        this.log="- Game just started.\n";
    }
    
    /**
     * Indica si algún jugador ha ganado la partida, es decir, si ha finalizado
     * el juego
     * @return True si se finalizo el juego, false en caso contrario
     */
    public boolean finished(){
        return lab.haveAWinner();
    }
    
    
    /**
     * Método que estudia un turno completo del juego. Gestiona el paso de un turno a otro.
     * @param preferredDirection  Dirección a la que se pretende mover el jugador
     * @return True si se finalizo el juego, false en caso contrario
     */
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
    
    /**
     * Crea una intancia de la clase GameState a partir de los miembros de 
     * la clase Game
     * 
     * @return Una intancia de GameState con los datos del estado actual del juego
     */
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
    
    /**
     * Se crea el laberinto a partir de los monstruos y bloques definidos
     */
    private void configureLabyrinth(){
        
        Monster monster_1=new Monster("Mike Wasowsky", Dice.randomIntelligence(),Dice.randomStrength());
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
    
    
    
    /**
     * Se indica que el turno pasa al siguiente jugador
     */
    private void nextPlayer(){
        if(currentPlayerIndex==players.size()-1)
            currentPlayerIndex=0;
        else
            ++currentPlayerIndex;
        
        currentPlayer=players.get(currentPlayerIndex);
    }
    
    
    /**
     * Método que mueve de hecho al jugador. Se intenta que se mueva a la casilla
     * indicada por la dirección, y si no es posible, se moverá en otra dirección.
     * 
     * @param preferredDirection  Dirección a la que se pretende mover el jugador
     * @return   Dirección a la que se moverá el jugador (si es válida)
     */
    private Directions actualDirection(Directions preferredDirection){
        
        int currentRow=currentPlayer.getRow();
        int currentCol=currentPlayer.getCol();
        
        ArrayList<Directions> validMoves=lab.validMoves(currentRow,currentCol);

        Directions output=currentPlayer.move(preferredDirection,validMoves);
        
        return output;

    }
    
    /**
     * Método encargado de llevar a cabo un combate entre el jugador actual y un monstruo.
     * 
     * @param monster Monstruo con el que se va a combatir.
     * @return GameCharacter: Jugador o Monstruo, tipo de rol del ganador del combate.
     */
    private GameCharacter combat(Monster monster){
        
         int rounds=0;   
        
        GameCharacter winner=GameCharacter.PLAYER;
        boolean lose = monster.defend(currentPlayer.attack());
        
        while (!lose && rounds<MAX_ROUNDS){ 
            rounds++;   
            winner = GameCharacter.MONSTER;
            lose = currentPlayer.defend(monster.attack());
            
            if (!lose){ 
                winner = GameCharacter.PLAYER;
                lose = monster.defend(currentPlayer.attack());
            }
        }
        this.logRounds(rounds, MAX_ROUNDS);
        return winner;
        
    }
    
    /**
     * Método que gestiona las recompensas que se producen al finalizar un combate.
     * Se encarga de actualizar el log con la información de si ha ganado el jugador
     * o el monstruo.
     * 
     * @param winner Tipo de personaje que ha ganado el combate, puede ser un jugador o un monstruo.
     * @see GameCharacter
     */
    private void manageReward(GameCharacter winner){
        if(winner==GameCharacter.PLAYER){
            currentPlayer.receiveReward();
            logPlayerWon();
        }
        else
            logMonsterWon();
            
    }
    /**
     * Método que gestiona la resurrección de un jugador al finalizar un combate.
     */
    private void manageResurrection(){
        boolean resurrect=Dice.resurrectPlayer();
        
        if(resurrect){
            currentPlayer.resurrect();
            logResurrected();
            
            FuzzyPlayer fuzzy=new FuzzyPlayer(currentPlayer);
            this.players.set(currentPlayerIndex, fuzzy);
            lab.switchToFuzzy(fuzzy);
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
        this.log+= "- Player "+this.currentPlayerIndex+" resurrected as Fuzzy.\n";
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