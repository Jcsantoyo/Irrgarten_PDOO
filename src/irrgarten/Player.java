/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

import java.util.ArrayList;
/**
 *
 * @author juan
 */
public class Player {
    static final int MAX_WEAPONS=2;
    static final int MAX_SHIELDS=3;
    static final int INITIAL_HEALTH=10;
    static final int HITS2LOSE=3;
    static final int NO_POS=-1;
            
    private String name;
    private char number;
    private float intelligence;
    private float strength;
    private float health;
    private int row;
    private int col;
    private int consecutiveHits=0;
    private ArrayList<Weapon> weapons;
    private ArrayList<Shield> shields;
    
    
    public Player(char number, float intelligence, float strength){
        name="Player #"+number;
        this.number=number;
        this.intelligence=intelligence;
        this.strength=strength;
        health=INITIAL_HEALTH;
        setPos(NO_POS,NO_POS);
        weapons=new ArrayList<>();
        shields=new ArrayList<>();
    }
    
    public void resurrect() {
       weapons.clear();
       shields.clear();
       health=INITIAL_HEALTH;
       consecutiveHits=0;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public char getNumber(){
        return number;
    }
    
    public void setPos(int row, int col){
        this.row=row;
        this.col=col;
    }
    
    public boolean dead(){
        return(health==0);
    }
    
    public Directions move(Directions direction, ArrayList<Directions> validMoves){
        throw new UnsupportedOperationException();
    }
    
    public float attack(){
        return (strength+sumWeapons());
    }
   
    public boolean defend(float receivedAttack){
        return manageHit(receivedAttack);
    } 
    
    public void receiveReward(){
        throw new UnsupportedOperationException();
    }
    
    //revisar si es necesario mostrar consecutivehits, armas y escudos
    public String toString(){
        String nombre= "Nombre: "+name;
        String inteligencia= "Inteligencia: "+ String.valueOf(intelligence);
        String fuerza= "Fuerza: " + String.valueOf(strength);
        String vida= "Vida: " + String.valueOf(health);
        String posicion = "Casilla: ("+String.valueOf(row)+", "
                        +String.valueOf(col)+")";
        
        return (nombre+'\n'+inteligencia+'\n'+fuerza+'\n'+vida+'\n'+posicion);
    }
    
    private void receiveWeapon(Weapon w){
        throw new UnsupportedOperationException();
    }
    private void receiveShield(Shield s){
        throw new UnsupportedOperationException();
    }
    private Weapon newWeapon(){
        return (new Weapon(Dice.weaponPower(),Dice.usesLeft()));
    }
    private Shield newShield(){
        return (new Shield(Dice.shieldPower(),Dice.usesLeft()));
    }
    private float sumWeapons(){
        float sum=0.0f;
        for(int i=0; i<weapons.size();++i){
            sum+=weapons.get(i).attack();
        }
        return sum;
    }
    
    private float sumShields(){
        float sum=0.0f;
        for(int i=0; i<shields.size();++i){
            sum+=shields.get(i).protect();
        }
        return sum;
    }
    
    private float defensiveEnergy(){
        return (intelligence+sumShields());
    }
    private boolean manageHit(float receivedAttack){
        throw new UnsupportedOperationException();
    }
    private void resetHits(){
        consecutiveHits=0;
    }
    private void gotWounded(){
        --health;
    }
    private void incConsecutiveHits(){
        ++consecutiveHits;
    }
 
       
}