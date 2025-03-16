
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
        
    }
    
    public void spreadPlayers(ArrayList<Player> players){
        throw new UnsupportedOperationException();
    }
    
    public boolean haveAWinner(){
        return (players[exitRow][exitCol]!=null);
    }
    
    //Por implementar
    public String toString(){
        return "";
    }
    
    public void addMonster(int row, int col, Monster monster){
        if(posOK(row,col) && emptyPos(row,col)){
            squareStates[row][col]=MONSTER_CHAR;
            monsters[row][col]=monster;
            monster.setPos(row, col);
        }
    }
    
    public Monster putPlayer(Directions direction, Player player){
        throw new UnsupportedOperationException();
    }
    
    public void addBlock(Orientation orientation, int startRow, int starCol, 
                         int length){
        throw new UnsupportedOperationException();
    }
    
    public ArrayList<Directions> validMoves(int row, int col){
        throw new UnsupportedOperationException();
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
        return (posOK(row,col) && emptyPos(row,col) && monsterPos(row,col) && 
                exitPos(row,col));
    }
    
    private void updateOldPos(int row, int col){
        if(posOK(row,col)){
            if(combatPos(row,col))
                squareStates[row][col]=MONSTER_CHAR;
            else 
                squareStates[row][col]=EMPTY_CHAR;
        }
    }
    
    //Redundante DIM? para evitar no magico
    private ArrayList<Integer> dir2Pos(int row, int col, Directions direction){
        int DIM=2;
        ArrayList<Integer> pos = new ArrayList<>(DIM);
        switch(direction){
            case UP:
                pos.add(row+1);
                pos.add(col);
                break;
            case DOWN:
                pos.add(row-1);
                pos.add(col);
                break;
            case LEFT:
                pos.add(row);
                pos.add(col-1);
                break;
            case RIGHT:
                pos.add(row);
                pos.add(col+1);
                break;
        }
        return pos;
    }
    
    private ArrayList<Integer> randomEmptyPos(){
        int DIM=2;
        ArrayList<Integer> pos = new ArrayList<>(DIM);
        pos.add(Dice.randomPos(nRows));
        pos.add(Dice.randomPos(nCols));
        
        while(!emptyPos(pos.get(0),pos.get(1))){
            pos.add(Dice.randomPos(nRows));
            pos.add(Dice.randomPos(nCols));
        }
        return pos;
    }
    
    private Monster putPlayer2D(int oldRow, int oldCol, int row, int col, 
                                Player player){
        throw new UnsupportedOperationException();
    }
    
}
