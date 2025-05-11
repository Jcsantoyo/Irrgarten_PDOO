/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author juan
 */
abstract  class LabyrinthCharacter {
    private String name;
    private float intelligence;
    private float strength;
    private float health;
    private int row;
    private int col;
    
    private static final int NO_POS=-1;

        
    public LabyrinthCharacter(String name, float intelligence, float strength, float health){
        this.name=name;
        this.intelligence=intelligence;
        this.strength=strength;
        this.health=health;
        this.row=NO_POS;
        this.col=NO_POS;
    }
    
    public LabyrinthCharacter(LabyrinthCharacter other){
        this.name=other.name;
        this.intelligence=other.intelligence;
        this.strength=other.strength;
        this.health=other.health;
        
        this.row=other.row;
        this.col=other.col;
    }
    public boolean dead(){
        return(health<=0);
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    protected float getIntelligence(){
        return intelligence;
    }
    protected float getStrength(){
        return strength;
    }
    protected float getHealth(){
        return health;
    }
    protected void setHealth(float health){
        this.health=health;
    }
    public void setPos(int row, int col){
        this.row=row;
        this.col=col;
    }
    
    @Override
    public String toString(){
        final String FORMAT = "%.6f";
        String toReturn=this.name+"["+"i:"+ String.format(FORMAT, this.intelligence) + ", s:"+String.format(FORMAT, this.strength);
        toReturn+=", h:"+String.format(FORMAT, this.health)+", p:("+this.row+", "+this.col+")]";
        
        return toReturn;
    }
    
    protected void gotWounded(){
        --health;
    }
    
    public abstract float attack();
    
    public abstract boolean defend(float attack);
    
    
}
