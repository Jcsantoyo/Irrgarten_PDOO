
package irrgarten;

import java.util.ArrayList;
/**
 * Clase que almacena información de cada jugador
 * 
 * @author Juan Caballero Santoyo
 */
public class Player extends LabyrinthCharacter {
    private static final int MAX_WEAPONS=2;
    private static final int MAX_SHIELDS=3;
    private static final int INITIAL_HEALTH=10;
    private static final int HITS2LOSE=3;
            
    private char number;
    private float intelligence;
    private int consecutiveHits=0;
    
    private ArrayList<Weapon> weapons;
    private ArrayList<Shield> shields;
    
    
    /**
     * Constructor de la clase Player
     * @param number número identificador del jugador
     * @param intelligence inteligencia del jugador
     * @param strength fuerza del jugador
     */
    public Player(char number, float intelligence, float strength){
        
        super(("Player #" + number), intelligence, strength, INITIAL_HEALTH);
        this.number=number;
        consecutiveHits=0;
        weapons=new ArrayList<>();
        shields=new ArrayList<>();
    }
    
    public Player(Player other){
        super(other);
        this.number=other.number;
        this.consecutiveHits=other.consecutiveHits;
        this.weapons=other.weapons;
        this.shields=other.shields;
    }
    
    /**
     * Resurreción de un jugador, indicando los valores que debe tener
     * al resucitar
     */
    public void resurrect() {
       weapons.clear();
       shields.clear();
       this.setHealth(INITIAL_HEALTH);
       resetHits();
    }
    
    /**
     * Informa sobre el número del jugador
     * @return Número del jugador
     */
    public char getNumber(){
        return number;
    }
    
    
    
    /**
     * Comprueba si la dirección pasada hacia la que se pretende desplazar el personaje
     * es válida, devolviéndola en caso de que lo sea o si no se puede mover hacia ninguna posición, es decir,
     * si el array `validMoves` está vacío. Si la dirección no está en `validMoves` y dicho array no está vacío,
     * se devuelve la primera dirección guardada en el array.
     *
     * @param direction  Dirección en la que se quiere mover el jugador
     * @param validMoves  Lista de movimientos válidos
     * @return  Devuelve la dirección en la que se moverá el jugador (si es válida)
     */
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
    
    
    /**
     * Calcula ataque total del jugador, teniendo en cuenta su fuerza y el poder
     * de sus armas
     * @return Devuelve la suma de su fuerza y el poder de las armas
     */
    @Override
    public float attack(){
        return (this.getStrength()+sumWeapons());
    }
   
    
    /**
     * Método que permite al jugador defenderse de un ataque.
     * Relega en #manageHit(float) la gestión de la defensa.
     * @param receivedAttack Intensidad del ataque recibido
     * @return  Devuelve true si el jugador ha muerto y false en caso contrario.
     * @see #manageHit(float)
     */
    @Override
    public boolean defend(float receivedAttack){
        return manageHit(receivedAttack);
    } 
    
    
     /**
     * Método que actualiza las armas, escudos y salud del jugador tras recibir una recompensa.     
     */
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
        this.setHealth(this.getHealth()+extraHealth);
    }
    
    
    
     /**
     * Método que devuelve una representación en forma de cadena de caracteres
     * del estado interno del jugador.
     * @return Representación en forma de cadena de caracteres del estado interno del jugador.
     */
    public String toString(){
        
        String toReturn=super.toString();
      
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
        toReturn+=" w:" + toWeapons+", sh:"+toShields+" ]";
        
        return toReturn;
                
    }
    
    
    /**
     * Método que actualiza las armas del jugador tras recibir un arma.
     * En primer lugar, se eliminan las armas que han de ser descartadas.
     * Después, se añade el arma al jugador si cabe.
     * @param w Arma nueva a añadir
     */
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
    
     /**
     * Método que actualiza los escudos del jugador tras recibir un escudo.
     * En primer lugar, se eliminan los escudos que han de ser descartados.
     * Después, se añade el escudo al jugador si cabe.
     * 
     * @param s  Escudo que recibe el jugador
     */
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
    
    /**
     * Genera una nueva instancia de la clase Weapon, con los parámetros
     * que indica el dado
     * @return Devuelve el arma creada de forma aleatoria con el dado
     */
    private Weapon newWeapon(){
        return (new Weapon(Dice.weaponPower(),Dice.usesLeft()));
    }
    
    
    /**
     * Genera una nueva instancia de la clase Shield, con los parámetros
     * que indica el dado
     * @return Devuelve el escudo creado de forma aleatoria con el dado
     */
    private Shield newShield(){
        return (new Shield(Dice.shieldPower(),Dice.usesLeft()));
    }
    
    
     /**
     * Indica la suma total de los poderes de todas las armas que tiene
     * el jugador
     * @return Devuelve la suma de poderes de todas las armas
     */
    protected float sumWeapons(){
        float sum=0.0f;
        for(int i=0; i<weapons.size();++i){
            sum+=weapons.get(i).attack();
        }
        return sum;
    }
    
    
    /**
     * Indica la suma total de protección de todos los escudos que tiene
     * el jugador
     * @return Devuelve la suma de protección de todas los escudos
     */
    protected float sumShields(){
        float sum=0.0f;
        for(int i=0; i<shields.size();++i){
            sum+=shields.get(i).protect();
        }
        return sum;
    }
    
    
     /**
     * Calcula defensa total del jugador, teniendo en cuenta su inteligencia y 
     * la protección de sus escudos
     * @return Devuelve la suma de su inteligencia y la protección de los escudos
     */
    protected float defensiveEnergy(){
        return (intelligence+sumShields());
    }
    
    
    /**
     * Método que gestiona el ataque recibido por el jugador.
     * 
     * @param receivedAttack Intensidad del ataque recibido
     * @return Devuelve true si el jugador ha muerto o llega al limite de 
     * ataques consecutivos HITS2LOSE y false en caso contrario.
     */
    private boolean manageHit(float receivedAttack){
        
        float defense=defensiveEnergy();
        if(defense<receivedAttack){
            gotWounded();
            incConsecutiveHits();
        }
        else
            resetHits();
        
        boolean lose;
        
        if((consecutiveHits==HITS2LOSE) || (dead())){
            resetHits();
            lose=true;
        }
        else
            lose=false;
        
        return lose;
    }
    
    /**
     * Reinicia el contador de impactos consecutivos a 0
     */
    private void resetHits(){
        consecutiveHits=0;
    }
    
    
    /**
     * Incrementa en una unidad el contador de impactos consecutivos
     */
    private void incConsecutiveHits(){
        ++consecutiveHits;
    }
 
       
}