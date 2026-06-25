
package irrgarten;


/**
 * Clase que almacena información de cada monstruo
 * 
 * @author Juan Caballero Santoyo
 */
public class Monster extends LabyrinthCharacter {
    private static final int INITIAL_HEALTH=5;
  
    
    
    /**
     * Constructor de la clase
     * 
     * @param name Valor inicial para el nombre
     * @param intelligence Valor inicial para la inteligencia
     * @param strength Valor inicial para la fuerza
     */
    public Monster(String name, float intelligence, float strength){
        super(name,intelligence,strength,INITIAL_HEALTH);
    }
    
    
    /**
     * Indica la fuerza de ataque del monstruo
     * @return Devuelve el valor de fuerza del ataque
     */
    @Override
    public float attack(){
        return Dice.intensity(this.getStrength());
    }
    
    /**
     * Método que permite al monstruo defenderse de un ataque.
     * @param receivedAttack Intensidad del ataque recibido.
     * @return Devuelve true si el monstruo ha muerto y false en caso contrario.
     */
    @Override
    public boolean defend(float receivedAttack){
        boolean isDead=dead();
        
        if(!isDead){
            float defensiveEnergy=Dice.intensity(this.getIntelligence());
            
            if(defensiveEnergy < receivedAttack){
                gotWounded();
                isDead=dead();
            }
        }
        return isDead;
        
    }
    
 
}
