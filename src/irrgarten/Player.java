
package irrgarten;

import java.util.ArrayList;
/**
 *
 * @author juan
 */
public class Player {
    static final int MAX_WEAPONS=2;
    static final int MAX_SHIELDS=3;
    static final int INITIAL_HEALTH=2;
    static final int HITS2LOSE=8;
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
        name="Player #"+ number;
        this.number=number;
        this.intelligence=intelligence;
        
        this.strength=strength;
        health=INITIAL_HEALTH;
        row=NO_POS;
        col=NO_POS;
        
        weapons=new ArrayList<>();
        shields=new ArrayList<>();
    }
    
    public void resurrect() {
       weapons.clear();
       shields.clear();
       health=INITIAL_HEALTH;
       resetHits();
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
        int size=validMoves.size();
        boolean contained=validMoves.contains(direction);
        
        Directions output;
                
        if((size>0) && (!contained)){
            output= validMoves.get(0);
        }
        else{
            output= direction;
        }
        return output;
    }
    
    public float attack(){
        return (strength+sumWeapons());
    }
   
    public boolean defend(float receivedAttack){
        return manageHit(receivedAttack);
    } 
    
    public void receiveReward(){
        int wReward=Dice.weaponsReward();
        int sReward=Dice.shieldsReward();
        
        for(int i=0;i<wReward;++i){
            Weapon wnew=newWeapon();
            receiveWeapon(wnew);
        }
        
        for(int i=0; i<sReward;++i){
            Shield snew=newShield();
            receiveShield(snew);
        }
        int extraHealth=Dice.healthReward();
        health+=extraHealth;
    }
    
    
    public String toString(){
        final String FORMAT = "%.6f";
        String toReturn=this.name+"["+"i:"+ String.format(FORMAT, this.intelligence) + ", s:"+String.format(FORMAT, this.strength);
        toReturn+=", h:"+String.format(FORMAT, this.health)+", p:("+this.row+", "+this.col+")]";
        
        // Bucles para mostrar con un formato determinado el array de
        // armas y escudos del jugador
        String toWeapons="[";
        int tamWeapons=this.weapons.size();
        for(int i=0; i<tamWeapons-1; i++){
            toWeapons+=this.weapons.get(i).toString()+", ";
        }
        if (tamWeapons>0)
            toWeapons+=this.weapons.get(tamWeapons-1);
        toWeapons+="]";
        
        String toShields="[";
        int tamShields=this.shields.size();
        for(int i=0; i<tamShields-1; i++){
            toShields+=this.shields.get(i).toString()+", ";
        }
        if (tamShields>0)
            toShields+=this.shields.get(tamShields-1);
        toShields+="]";
        
        // Definimos el formato final para el toString
        toReturn+="w:" + toWeapons+", sh:"+toShields+" ]";
        
        return toReturn;
                
    }
    
    private void receiveWeapon(Weapon w){
        
        for(int i=0; i<weapons.size();++i){
            Weapon wi=weapons.get(i);
            if(wi.discard()){
                weapons.remove(wi);
                i--;
            }
            
        }
        
        if(weapons.size()<MAX_WEAPONS)
            weapons.add(w);
        
    }
    
    
    private void receiveShield(Shield s){
        
        for(int i=0; i<shields.size();++i){
            Shield si=shields.get(i);
            if(si.discard()){
                shields.remove(si);
                i--;
            }
        }
        
        if(shields.size()<MAX_SHIELDS)
            shields.add(s);
        
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
        
        // Se contabiliza el ataque recibido
        if (this.defensiveEnergy() < receivedAttack){
            // El jugador no se ha defendido
            this.gotWounded();
            this.incConsecutiveHits();
        }
        else{
            // El jugador se ha defendido
            this.resetHits();
        }

        // Se comprueba si el jugador ha perdido
        boolean lose = (this.consecutiveHits==Player.HITS2LOSE) || this.dead();

        if (lose)   // Si ha perdido se resetea el contador de golpes consecutivos
            resetHits();

        return lose;
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