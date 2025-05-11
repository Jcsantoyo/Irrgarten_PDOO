
package irrgarten;

import java.util.ArrayList;

/**
 * Esta clase representará el tablero indicando los elementos que hay
 * en cada posición de él, es decir, los monstruos, muros y jugadores, así como
 * la casilla de salida.
 * 
 * @author Juan Caballero Santoyo
 */
public class Labyrinth {
    
    private static final char BLOCK_CHAR='X';
    private static final char EMPTY_CHAR='-';
    private static final char MONSTER_CHAR='M';
    private static final char COMBAT_CHAR='C';
    private static final char EXIT_CHAR='E';
    private static final int ROW=0;
    private static final int COL=1;
    private static final int DIM=2;
    
    private static final int NO_POS=-1;
    
    private int nRows;
    private int nCols;
    private int exitRow;
    private int exitCol;
    
    private Monster[][] monsters;
    private Player[][] players;
    private char[][] squareStates;
    
    
    /**
     * Constructor de la clase
     * @param nRows Número de filas del laberinto
     * @param nCols Número de columnas del laberinto
     * @param exitRow Fila de la casilla de salida
     * @param exitCol Columna de la casilla de salida
     */
    public Labyrinth(int nRows, int nCols, int exitRow, int exitCol){
        
        this.nRows=nRows;
        this.nCols=nCols;
        this.exitRow=exitRow;
        this.exitCol=exitCol;
        
        monsters=new Monster[nRows][nCols];
        players= new Player[nRows][nCols];
        squareStates=new char[nRows][nCols];
        
        for(int i=0; i<nRows;++i){
            for(int j=0; j<nCols;++j){
                squareStates[i][j]=EMPTY_CHAR;
            }
        }
        squareStates[exitRow][exitCol]=EXIT_CHAR;
        
    }
    
    
     /**
     * Distribuye una lista de jugadores por el laberinto.
     * @param players ArrayList con los jugadores a repartir por el Laberinto
     */
    public void spreadPlayers(ArrayList<Player> players){
        
        for(int i=0; i<players.size();++i){
            Player p=players.get(i);
            int[] pos=randomEmptyPos();
            putPlayer2D(NO_POS,NO_POS,pos[ROW],pos[COL],p);
        }
    }
    
    
    /**
     * Indica si algún jugador llego a la casilla de salida, y por tanto
     * gano el juego
     * 
     * @return Devuelve true si alguien gano, false en caso contrario
     */
    public boolean haveAWinner(){
        return (players[exitRow][exitCol]!=null);
    }
    
    
    /**
      * Muestra el estado de cada casilla del laberinto, como
      * por ejemplo si hay monstruo, combate, etc
      * 
      * @return Cadena de carácteres que indican el estado de cada casilla del laberinto
      */
    @Override
    public String toString(){
        // Cálculo del número de caracteres que debe ocupar cada parte
        int filSize = Integer.toString(this.nRows-1).length();
        int colSize = Integer.toString(this.nCols-1).length();
        int nPlayersSize = Integer.toString(this.players.length-1).length();
        
        // Cálculo del tamaño máximo
        int maxSize = Math.max(Math.max(filSize, colSize), nPlayersSize);
        final String FORMAT = "%"+maxSize+"s";

        // Cadena a devolver
        String toReturn="";

        // Índices en cada columna
        toReturn+=" " + String.format(FORMAT, " ");
        for (int i=0; i<this.nCols; i++){
            toReturn+=String.format(FORMAT, i)+" ";
        }
        toReturn+="\n";


        for(int r=0; r<this.nRows; r++){
            toReturn+=String.format(FORMAT, r)+" "; // Índices en cada fila
            for(int c=0; c<this.nCols; c++){
                toReturn+=String.format(FORMAT, this.squareStates[r][c])+" ";
            }
            toReturn+="\n";
        }
        return toReturn;
    }
    
    
    /**
     * Añade un monstruo recibido al laberinto, si la posición dada está vacía
     * y es correcta, es decir, es una posición válida en el laberinto
     * 
     * @param row Fila en la que se implementara el monstruo
     * @param col Columna en la que se implementara el monstruo
     * @param monster Monstruo que se implementara en el laberinto
     */
    public void addMonster(int row, int col, Monster monster){
        if(posOK(row,col) && emptyPos(row,col)){
            squareStates[row][col]=MONSTER_CHAR;
            monsters[row][col]=monster;
            monster.setPos(row, col);
        }
    }
    
    
    /**
     * Método que mueve un jugador en el laberinto en una dirección.
     * Informa sobre si se encuentra con un monstruo.
     * 
     * @param direction Dirección en la que ha de moverse el jugador. Si no es válida, no se mueve.
     * @param player Jugador a desplazar
     * @return El monstruo con el que se ha encontrado. Devuelve *null* si no hay monstro
     */
    public Monster putPlayer(Directions direction, Player player){
        
        int oldRow=player.getRow();
        int oldCol=player.getCol();
        
        int[] newPos=dir2Pos(oldRow,oldCol,direction);
        Monster monster=putPlayer2D(oldRow,oldCol,newPos[ROW],newPos[COL],player);
        
        return monster;
    }
    
    
    /**
     * Método que añade un bloque al laberinto.
     *
     * @param orientation Orientación del bloque, vertical u horizontal
     * @param startRow Fila de inicio del bloque
     * @param startCol Columna de inicio del bloque
     * @param length Longitud del bloque
     */
    public void addBlock(Orientation orientation, int startRow, int startCol, 
                         int length){
        int incRow;
        int incCol;
        if(orientation == Orientation.VERTICAL){
            incRow = 1;
            incCol = 0;
        }
        
        else{
            incRow = 0;
            incCol = 1;
        }
        int row = startRow;
        int col = startCol;
        while(this.posOK(row, col)&& this.emptyPos(row, col)&& length > 0){
            squareStates[row][col] = BLOCK_CHAR;
            length--;
            row += incRow;
            col += incCol;
        }
            
    }
    
    /**
     * Calcula las direcciones hacia las que se puede mover un jugador desde una
     * posición dada.
     * @param row Fila desde la que se quiere ver hacia donde se puede mover
     * @param col Columna desde la que se quiere ver hacia donde se puede mover
     * @return Direcciones en las que se puede mover desde (row, col)
     */
    public ArrayList<Directions> validMoves(int row, int col){
        
        ArrayList<Directions> output=new ArrayList<>();
        
        if(canStepOn(row+1,col))
            output.add(Directions.DOWN);
        if(canStepOn(row-1,col))
            output.add(Directions.UP);
        if(canStepOn(row,col+1))
            output.add(Directions.RIGHT);
        if(canStepOn(row,col-1))
            output.add(Directions.LEFT);
        
        return output;

    }
    
    /**
     * Comprueba si una posición es válida dentro del laberinto
     * @param row Fila a comprobar
     * @param col Columna a comprobar
     * @return True si es correcta la posición, false en caso contrario
     */
    private boolean posOK(int row, int col){
        return (row>=0 && row<nRows && col>=0 && col<nCols);
    }
    
    /**
     * Comprueba si una casilla está vacía dentro del laberinto
     * @param row Fila a comprobar
     * @param col Columna a comprobar
     * @return True si está vacía la casilla, false en caso contrario
     */
    private boolean emptyPos(int row, int col){
        return (squareStates[row][col] == EMPTY_CHAR);
    }
    
    /**
     * Comprueba si una casilla tiene un monstruo
     * @param row Fila a comprobar
     * @param col Columna a comprobar
     * @return True si hay un monstruo en la casilla, false en caso contrario
     */
    private boolean monsterPos(int row, int col){
        return (squareStates[row][col] == MONSTER_CHAR);
    }
    
    /**
     * Comprueba si una casilla es la de salida
     * @param row Fila a comprobar
     * @param col Columna a comprobar
     * @return True si la casilla es la de salida, false en caso contrario
     */
    private boolean exitPos(int row, int col){
        return (squareStates[row][col] == EXIT_CHAR);
    }
    
    
    /**
     * Comprueba si una casilla está en estado de combate
     * @param row Fila a comprobar
     * @param col Columna a comprobar
     * @return True si hay un combate en la casilla, false en caso contrario
     */
    private boolean combatPos(int row, int col){
        return (squareStates[row][col] == COMBAT_CHAR);
    }
    
    /**
     * Comprueba si una casilla es válida en el laberinto y hay un monstruo, está vacía
     * o es casilla de salida
     * @param row Fila a comprobar
     * @param col Columna a comprobar
     * @return True si cumple las características pedida, false en caso contrario
     */
    private boolean canStepOn(int row,int col){
        boolean step = false;
        if(posOK(row, col)){
            if(emptyPos(row,col)||monsterPos(row,col)||exitPos(row,col)){
                step = true;
            }
        }
        
        return step;
    }
    
    /**
     * Comprueba si una posición es válida, en caso afirmativo se establece la casilla
     * con un monstruo si estaba en estado de combate o en caso contrario se define
     * la casilla como vacía
     * @param row Fila a comprobar
     * @param col Columna a comprobar
     */
    private void updateOldPos(int row, int col){
        if(posOK(row,col)){
            if(combatPos(row,col))
                squareStates[row][col]=MONSTER_CHAR;
            else 
                squareStates[row][col]=EMPTY_CHAR;
        }
    }
    
    /**
     * Calcula la nueva posición tras hacer el movimiento en una unidad hacia la
     * dirección dada
     * @param row Fila inicial
     * @param col Columna inicial
     * @param direction Dirección en la que nos desplazamos
     * @return Nueva posición tras el desplazamiento en una unidad
     */
    private int[] dir2Pos(int row, int col, Directions direction){
        int pos[] = new int[DIM];
        switch(direction){
            case UP:
                --row;
                break;
            case DOWN:
                ++row;
                break;
            case LEFT:
                --col;
                break;
            case RIGHT:
                ++col;
                break;
        }
        pos[ROW]=row;
        pos[COL]=col;
        
        return pos;
    }
    
    /**
     * Define una posición random en el laberinto, la cual debe estar vacía
     * @return Posición random dentro del laberinto
     */
    private int[] randomEmptyPos(){
        int row, col;
        do{
            row=Dice.randomPos(this.nRows);
            col=Dice.randomPos(this.nCols);
        }while (!this.emptyPos(row, col));
        
        int[] output= new int[DIM]; 
        output[ROW]=row;
        output[COL]=col;
        
        return output;
    }
    
    /**
     * Método que actualiza la posición del jugador dado a la nueva posición, actualizando
     * el estado de la casilla antigua y nueva.
     *
     * Se comprueba si la nueva posición es válida y, además,
     * si el número del jugador pasado no coincide con el que hay en `players`,
     * no se cambia el estado de la casilla antigua del jugador.
     * 
     * Devuelve el monstruo de la nueva casilla si hay un combate en la posición actualizada.
     *
     * @param oldRow  Posición antigua del jugador (fila)
     * @param oldCol  Posición antigua del jugador (columna)
     * @param row  Nueva posición del jugador (fila)
     * @param col  Nueva posición del jugador (columna)
     * @param player Jugador a mover
     * @return  Monstruo que hay en la casilla a la que se llega.
     */
    private Monster putPlayer2D(int oldRow, int oldCol, int row, int col, 
                                Player player){
        Monster output=null;
        if(canStepOn(row,col)){
            
            if(posOK(oldRow,oldCol)){
                
                Player p=players[oldRow][oldCol];
                
                if(p==player){
                    
                    updateOldPos(oldRow,oldCol);
                    players[oldRow][oldCol]=null;
                    
                }
            }
            boolean monsterPos=monsterPos(row,col);
            
            if(monsterPos){
                squareStates[row][col]=COMBAT_CHAR;
                output=monsters[row][col];
            }
            else{
                
                squareStates[row][col]=player.getNumber();
            }
            players[row][col]=player;
            player.setPos(row, col);
        }
        return output;
    }
    
    
    public void switchToFuzzy(FuzzyPlayer fuzzy){
        int row=fuzzy.getRow();
        int col=fuzzy.getCol();
        if(this.players[row][col].getNumber()==fuzzy.getNumber())
            players[row][col]=fuzzy;
    }
    
}