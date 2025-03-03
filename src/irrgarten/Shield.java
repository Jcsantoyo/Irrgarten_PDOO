/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author juan
 */
public class Shield {
    private float protection;
    private int uses;
    
    public Shield(float proc, int us){
        protection=proc;
        uses=us;
    }
    public float protect(){
        float prot=protection;
        if(uses>0)
           --uses;
        else 
            prot=0;
        return prot;
    }
    
    public String toString(){
        return "W[" + String.valueOf(protection)+", "+String.valueOf(uses)+"]";
    }
    
    public boolean discard(){
        return Dice.discardElement(uses);
    }
    
}
