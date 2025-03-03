/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author juan
 */
public class Weapon {
    private float power;
    private int uses;
    
    public Weapon(float pow, int us){
        power=pow;
        uses=us;
    }
    public float attack(){
        float attack=power;
        if(uses>0)
           --uses;
        else 
            attack=0;
        return attack;
    }
    
    public String toString(){
        return "W[" + String.valueOf(power)+", "+String.valueOf(uses)+"]";
    }
    
    public boolean discard(){
        return Dice.discardElement(uses);
    }
}
