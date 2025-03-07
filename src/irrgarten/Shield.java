
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
     
    @Override
    public String toString(){
        return "W[" + String.valueOf(protection)+", "+String.valueOf(uses)+"]";
    }
    
    public boolean discard(){
        return Dice.discardElement(uses);
    }
    
}
