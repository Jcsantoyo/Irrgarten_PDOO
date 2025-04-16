
package irrgarten;


/**
 * Clase que almacena información de cada monstruo
 * 
 * @author Juan Caballero Santoyo
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
    
    
    /**
     * Constructor de la clase
     * 
     * @param name Valor inicial para el nombre
     * @param intelligence Valor inicial para la inteligencia
     * @param strength Valor inicial para la fuerza
     */
    public Monster(String name, float intelligence, float strength){
        this.name=name;
        this.intelligence=intelligence;
        this.strength=strength;
        health=INITIAL_HEALTH;
        row=NO_POS;
        col=NO_POS;
    }
    
    
    /**
     * Comprueba si está muerto el monstruo
     * @return Devuelve true si está muerto y false en caso contrario
     */
    public boolean dead(){
        return (health<=0);
    }
    
    
    /**
     * Indica la fuerza de ataque del monstruo
     * @return Devuelve el valor de fuerza del ataque
     */
    public float attack(){
        return Dice.intensity(strength);
    }
    
    /**
     * Método que permite al monstruo defenderse de un ataque.
     * @param receivedAttack Intensidad del ataque recibido.
     * @return Devuelve true si el monstruo ha muerto y false en caso contrario.
     */
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
    
    /**
     * Definimos la posición del monstruo en el tablero
     * @param row Fila en el tablero
     * @param col Columna en el tablero
     */
    public void setPos(int row,int col){
        this.row=row;
        this.col=col;
    }
    
    
     /**
     * Muestra las características del monstruo
     * @return devuelve una cadena con la información del monstruo y su posición
     */
    @Override
    public String toString(){
       final String FORMAT = "%.6f";
        String toReturn=this.name+"["+"i:"+ String.format(FORMAT, this.intelligence) + ", s:"+String.format(FORMAT, this.strength);
        toReturn+=", h:"+String.format(FORMAT, this.health)+", p:("+this.row+", "+this.col+")]";
        
        return toReturn;
    }
    
    /**
     * Decrementa en uno la vida del monstruo
     */
    private void gotWounded(){
        --health;
    }
    
}
