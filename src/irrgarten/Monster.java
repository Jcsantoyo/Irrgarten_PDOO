
package irrgarten;

/**
 *
 * @author juan
 */
public class Monster {
    private static final int INITIAL_HEALTH=5;
    private static final int NO_POS=-1;
    
    private String name;
    private float intelligence;
    private float strength;
    private float health;
    private int row;
    private int col;
    
    public Monster(String name, float intelligence, float strength){
        this.name=name;
        this.intelligence=intelligence;
        this.strength=strength;
        health=INITIAL_HEALTH;
        row=NO_POS;
        col=NO_POS;
    }
    
    public boolean dead(){
        return (health<=0);
    }
    
    
    public float attack(){
        return Dice.intensity(strength);
    }
    
    
    public boolean defend(float receivedAttack){
        boolean isDead=dead();
        
        if(!isDead){
            float defensiveEnergy=Dice.intensity(intelligence);
            
            if(defensiveEnergy < receivedAttack){
                gotWounded();
                isDead=dead();
            }
        }
        return isDead;
        
    }
    
    
    public void setPos(int row,int col){
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
    private void gotWounded(){
        --health;
    }
    
}
