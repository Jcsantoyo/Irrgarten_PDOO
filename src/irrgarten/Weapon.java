
package irrgarten;

/**
 * Esta clase representa las armas que utiliza el jugador en los ataques
 * durante los combates.
 * 
 * @author Juan Caballero Santoyo
 */
public class Weapon {
    private float power;
    private int uses;
    
    /**
    * Constructor de la clase Weapon.
    * @param power El poder del arma.
    * @param uses El número de usos disponibles del arma.
    */
    public Weapon(float power, int uses){
        this.power=power;
        this.uses=uses;
    }
    
    /**
     * Método para realizar una ataque con el arma.
     * @return La intensidad del ataque del arma.
    */
    public float attack(){
        float attack=power;
        if(uses>0)
           --uses;
        else 
            attack=0;
        return attack;
    }
    
    /**
    * Método que devuelve una representación en forma de cadena de caracteres
    * del estado interno del objeto.
    * @return Representación en forma de cadena de caracteres del estado interno del objeto.
    */
    @Override
    public String toString(){
        return "W[" + String.valueOf(power)+", "+String.valueOf(uses)+"]";
    }
    
    /**
    * Método que indica si se descartará el escudo en función de sus usos
    * 
    * @return devuelve true o false si se descarta o no
    */
    public boolean discard(){
        return Dice.discardElement(uses);
    }
}
