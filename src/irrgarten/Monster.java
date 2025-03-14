
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
        setPos(NO_POS,NO_POS);
    }
    public boolean dead(){
        return (health==0);
    }
    public float attack(){
        return Dice.intensity(strength);
    }
    public boolean defend(float receivedAttack){
        throw new UnsupportedOperationException();
    }
    public void setPos(int row,int col){
        this.row=row;
        this.col=col;
    }
    @Override
    public String toString(){
        String nombre= "Nombre: "+name;
        String inteligencia= "Inteligencia: "+ String.valueOf(intelligence);
        String fuerza= "Fuerza: " + String.valueOf(strength);
        String vida= "Vida: " + String.valueOf(health);
        String posicion = "Casilla: ("+String.valueOf(row)+", "
                        +String.valueOf(col)+")";
        
        return (nombre+'\n'+inteligencia+'\n'+fuerza+'\n'+vida+'\n'+posicion);
    }
    private void gotWounded(){
        --health;
    }
    
}
