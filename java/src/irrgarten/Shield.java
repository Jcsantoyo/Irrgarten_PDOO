
package irrgarten;

/**
 * Clase que representa los escudos que utiliza el jugador cuando se defiende
 * de un ataque de un monstruo.
 * 
 * @author Juan Caballero Santoyo
 */
public class Shield extends CombatElement {
    
    /**
     * Constructor de la clase Shield
     * @param protection El número que indica la protección que aporta el escudo
     * @param uses El número de usos disponibles del elemento de combate
     */
    public Shield(float protection, int uses){
        super(protection,uses);
    }
    
    /**
     * Método para realizar una protección con el escudo.
     * @return La intensidad de la protección del escudo.
     */
    public float protect(){
        return this.produceEffect();
    }
     
    /**
     * Método que devuelve una representación en forma de cadena de caracteres
     * del estado interno del objeto.
     * @return Representación en forma de cadena de caracteres del estado interno del objeto.
     */
    @Override
    public String toString(){
        return "S" + super.toString();
    }
   
}
