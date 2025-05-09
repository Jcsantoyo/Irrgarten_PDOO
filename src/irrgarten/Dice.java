
package irrgarten;

/**
 * @author juan
 */

import java.util.Random;
import java.util.ArrayList;

/**
 * Clase que tiene la responsabilidad de tomar todas las decisiones
 * que dependen del azar en el juego.
 * 
 * @author Juan Caballero Santoyo
 */
public class Dice {
    private static final int MAX_USES=5;
    private static final float MAX_INTELLIGENCE = 10.0f;
    private static final float MAX_STRENGTH = 10.0f;
    private static final float RESURRECT_PROB = 0.3f; 
    private static final int WEAPONS_REWARD = 2;
    private static final int SHIELDS_REWARD = 3;
    private static final int HEALTH_REWARD = 5;
    private static final int MAX_ATTACK = 3;
    private static final int MAX_SHIELD = 2;
    private static final Random generator=new Random();
    
    
    /**
     * Genera una fila o columna aleatoria en el tablero
     * @param max Dimension maxima en el tablero (filas o columnas)
     * @return Una fila (o columna) aleatoria entre 0 y max
     */
    public static int randomPos(int max){
        return generator.nextInt(max);
    }
    
     /**
     * Determina qué jugador empieza la partida
     * @param nplayers Numero de jugadores
     * @return Numero que representa al jugador que empieza.
     *          Se numeran desde el 0.
     */
    public static int whoStarts(int nplayers){
        return generator.nextInt(nplayers);
    }
    
    /**
     * Método para generar un valor aleatorio de inteligencia en el intervalo
     * [0, MAX_INTELLIGENCE[.
     * @return Un valor aleatorio de inteligencia.
     */
    public static float randomIntelligence(){
        return generator.nextFloat()*MAX_INTELLIGENCE;
    }
    
    /**
     * Método para generar un valor aleatorio de fuerza en el intervalo
     * [0, MAX_STRENGTH[.
     * @return Un valor aleatorio de fuerza.
     */
    public static float randomStrength() {
        return generator.nextFloat() * MAX_STRENGTH;
    }
    
    /**
     * Método que determina si un jugador resucita o no
     * @return true en el caso de que resucite, false en caso contrario.
     */
    public static boolean resurrectPlayer(){
        
        return generator.nextFloat() <= RESURRECT_PROB;
    }
    
    
    /**
     * Método que indica la cantidad de armas que recibirá el jugador por ganar el combate.
     * @return La cantidad de armas recibidas.
     */
    public static int weaponsReward() {
        return generator.nextInt(WEAPONS_REWARD + 1); // +1 pq debe estar en el cerrado
    }

    /**
     * Método que indica la cantidad de escudos que recibirá el jugador por ganar el combate.
     * @return La cantidad de escudos recibidos.
     */
    public static int shieldsReward() {
        return generator.nextInt(SHIELDS_REWARD + 1); // +1 pq debe estar en el cerrado
    }

    /**
     * Método que indica la cantidad de unidades de salud que recibirá el jugador por ganar el combate.
     * @return La cantidad de unidades de salud recibidas.
     */
    public static int healthReward() {
        return generator.nextInt(HEALTH_REWARD + 1);  // +1 pq debe estar en el cerrado
    }

    /**
     * Método que devuelve un valor aleatorio en el intervalo [0, MAX_ATTACK[.
     * @return Un valor aleatorio de potencia de arma.
     */
    public static float weaponPower() {
        return generator.nextFloat() * MAX_ATTACK;
    }

    /**
     * Método que devuelve un valor aleatorio en el intervalo [0, MAX_SHIELD[.
     * @return Un valor aleatorio de potencia de escudo.
     */
    public static float shieldPower() {
        return generator.nextFloat() * MAX_SHIELD;
    }

    /**
     * Método que devuelve el número de usos que se asignará a un arma o escudo.
     * @return El número de usos asignados.
     */
    public static int usesLeft() {
        return generator.nextInt(MAX_USES + 1);  // +1 pq debe estar en el cerrado
    }

    /**
     * Método que devuelve la cantidad de competencia aplicada.
     * @param competence La competencia aplicada.
     * @return Un valor aleatorio del intervalo [0, competence[.
     */
    public static float intensity(float competence) {
        return generator.nextFloat() * competence;
    }
    
    /**
     * Método que descarta un elemento (arma o escudo) con una probabilidad
     * inversamente proporcional a lo cercano que esté el parámetro del número
     * máximo de usos que puede tener un arma o escudo.
     * @param uses_left El número de usos restantes del elemento.
     * @return True si el elemento debe ser descartado, False en caso contrario.
     */
    public static boolean discardElement(int uses_left){
        float prob= (MAX_USES-uses_left)/(float)MAX_USES;
        return generator.nextFloat()<prob;
    }
    
   
    
    public static Directions nextStep(Directions preference, 
                         ArrayList<Directions> validMoves, float intelligence){
        
        Directions output=preference;
        
        if(Dice.randomIntelligence()>intelligence){
            int randomIndex=generator.nextInt(validMoves.size());
            output=validMoves.get(randomIndex);
        }
        
        return output;
    }
    
     /*  Con nextFloat multplicar por el extremo superior, no hacer
        no hacer nextFloat(extremo_sup) 
    */
}