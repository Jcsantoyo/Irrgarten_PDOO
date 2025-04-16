
package irrgarten;

/**
 * Clase que representa los escudos que utiliza el jugador cuando se defiende
 * de un ataque de un monstruo.
 * 
 * @author Juan Caballero Santoyo
 */
public class Shield {
    private float protection;
    private int uses;
    
    /**
     * Constructor de la clase Shield
     * @param protection El número que indica la protección que aporta el escudo
     * @param uses El número de usos disponibles del elemento de combate
     */
    public Shield(float protection, int uses){
        this.protection=protection;
        this.uses=uses;
    }
    
    /**
     * Método para realizar una protección con el escudo.
     * @return La intensidad de la protección del escudo.
     */
    public float protect(){
        float prot=protection;
        if(uses>0)
           --uses;
        else 
            prot=0;
        return prot;
    }
     
    /**
     * Método que devuelve una representación en forma de cadena de caracteres
     * del estado interno del objeto.
     * @return Representación en forma de cadena de caracteres del estado interno del objeto.
     */
    @Override
    public String toString(){
        return "S[" + String.valueOf(protection)+", "+String.valueOf(uses)+"]";
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
