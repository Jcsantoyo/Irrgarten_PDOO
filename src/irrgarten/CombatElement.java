/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author juan
 */
public abstract class CombatElement {
    
    private float effect;
    private int uses;
    
    public CombatElement(float effect,int uses){
        this.effect=effect;
        this.uses=uses;
    }
    
    protected float produceEffect(){
        float efecto=effect;
        if(uses>0)
           --uses;
        else 
            efecto=0;
        return efecto;
    }
    
    public boolean discard(){
        return Dice.discardElement(uses);
    }
    
    @Override
    public String toString(){
        return "[" + String.valueOf(effect)+", "+String.valueOf(uses)+"]"; 
    }
}
