
package irrgarten;

import java.util.ArrayList;

/**
 *
 * @author juan
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
    
    public void spreadPlayers(ArrayList<Player> players){
        
        for(int i=0; i<players.size();++i){
            Player p=players.get(i);
            int[] pos=randomEmptyPos();
            putPlayer2D(NO_POS,NO_POS,pos[ROW],pos[COL],p);
        }

    }
    
    public boolean haveAWinner(){
        return (players[exitRow][exitCol]!=null);
    }
    
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
    
    public void addMonster(int row, int col, Monster monster){
        if(posOK(row,col) && emptyPos(row,col)){
            squareStates[row][col]=MONSTER_CHAR;
            monsters[row][col]=monster;
            monster.setPos(row, col);
        }
    }
    
    public Monster putPlayer(Directions direction, Player player){
        
        int oldRow=player.getRow();
        int oldCol=player.getCol();
        
        int[] newPos=dir2Pos(oldRow,oldCol,direction);
        Monster monster=putPlayer2D(oldRow,oldCol,newPos[ROW],newPos[COL],player);
        
        return monster;

    }
    
    public void addBlock(Orientation orientation, int startRow, int startCol, 
                         int length){
        int incRow=0;
        int incCol=0;
        if(orientation==Orientation.VERTICAL){
            incRow=1;
        }
        else{
            incCol=1;
        }
        int row=startRow;
        int col=startCol;
        
        while((posOK(row,col)) && (length>0) && emptyPos(row,col)){
            squareStates[row][col]=BLOCK_CHAR;
            length--;
            row+=incRow;
            col+=incCol;
        }
            
    }
    
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
    
    private boolean posOK(int row, int col){
        return (row>=ROW && row<nRows && col>=COL && col<nCols);
    }
    
    private boolean emptyPos(int row, int col){
        return (squareStates[row][col] == EMPTY_CHAR);
    }
    private boolean monsterPos(int row, int col){
        return (squareStates[row][col] == MONSTER_CHAR);
    }
    
    private boolean exitPos(int row, int col){
        return (squareStates[row][col] == EXIT_CHAR);
    }
    private boolean combatPos(int row, int col){
        return (squareStates[row][col] == COMBAT_CHAR);
    }
    
    private boolean canStepOn(int row,int col){
        boolean comprobacion=this.posOK(row, col);
        comprobacion = comprobacion && (this.monsterPos(row, col) || this.exitPos(row, col) ||
                this.emptyPos(row, col));

        return comprobacion;
    }
    
    private void updateOldPos(int row, int col){
        if(posOK(row,col)){
            if(combatPos(row,col))
                squareStates[row][col]=MONSTER_CHAR;
            else 
                squareStates[row][col]=EMPTY_CHAR;
        }
    }
    
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
    
    private int[] randomEmptyPos(){
        int row, col;
        do{
            row=Dice.randomPos(this.nRows);
            col=Dice.randomPos(this.nCols);
        }while (!this.emptyPos(row, col));
        
        int[] output= new int[2]; // = {row, col};
        output[ROW]=row;
        output[COL]=col;
        
        return output;
    }
    
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
    
}
