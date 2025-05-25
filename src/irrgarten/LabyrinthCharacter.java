/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * 
 * 
 * 
 * @author Juan Caballero Santoyo
 */
abstract  class LabyrinthCharacter {
    private String name;
    private float intelligence;
    private float strength;
    private float health;
    private int row;
    private int col;
    
    private static final int NO_POS=-1;

    /**
     * Constructor de la clase abstracta LabytinthCharacter
     * @param name Valor inicial para el nombre
     * @param intelligence Valor inicial para la inteligencia
     * @param strength Valor inicial para la fuerza
     * @param health Valor inicial para la salud
     */    
    public LabyrinthCharacter(String name, float intelligence, float strength, float health){
        this.name=name;
        this.intelligence=intelligence;
        this.strength=strength;
        this.health=health;
        this.row=NO_POS;
        this.col=NO_POS;
    }
    
    /**
     * Consctructor de copia
     * @param other Objeto que copiar
     */
    public LabyrinthCharacter(LabyrinthCharacter other){
        this.name=other.name;
        this.intelligence=other.intelligence;
        this.strength=other.strength;
        this.health=other.health;
        
        this.row=other.row;
        this.col=other.col;
    }
    
    /**
     * Comprueba si está muerto el personaje
     * @return Devuelve true si está muerto y false en caso contrario
     */
    public boolean dead(){
        return(health<=0);
    }
    
    /**
     * Informa sobre la fila del personaje en el tablero
     * @return Fila de la pos del personaje en el tablero
     */
    public int getRow(){
        return row;
    }
    
    /**
     * Informa sobre la columna del personaje en el tablero
     * @return Columna de la pos del personaje en el tablero
     */
    public int getCol(){
        return col;
    }
    
    /**
     * Método para consultar el valor de la inteligencia del personaje
     * @return Valor de la inteligencia
     */
    protected float getIntelligence(){
        return intelligence;
    }
    
    /**
     * Método para consultar el valor de la fuerza del personaje
     * @return Valor de la fuerza
     */
    protected float getStrength(){
        return strength;
    }
    
    /**
     * Método para consultar el valor de la salud del personaje
     * @return Valor de la salud
     */
    protected float getHealth(){
        return health;
    }
    
    /**
     * Método para modificar el valor de la salud del personaje
     * @param health Salud nueva a actualizar
     */
    protected void setHealth(float health){
        this.health=health;
    }
    
    /**
     * Definimos la posición del personaje en el tablero
     * @param row Fila en el tablero
     * @param col Columna en el tablero
     */
    public void setPos(int row, int col){
        this.row=row;
        this.col=col;
    }
    
    /**
     * Muestra las características del personaje
     * @return devuelve una cadena con la información del personaje y su posición
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
    protected void gotWounded(){
        --health;
    }
    
    /**
     * Método que indica la fuerza del ataque del personaje
     * @return Devuelve el poder del ataque
     */
    public abstract float attack();
    
    
    /**
     * Método que se encarga de gestionar la defensa del personaje frente
     * a un ataque.
     * @param attack Intensidad del ataque recibido.
     * @return Devuelve true si el personaje ha muerto y false en caso contrario.
     */
    public abstract boolean defend(float attack);
    
    
}
