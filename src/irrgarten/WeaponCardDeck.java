

package irrgarten;

/**
 *
 * Clase que representa la baraja de cartas de tipo Weapon
 * 
 * @author Juan Caballero Santoyo
 */
public class WeaponCardDeck extends CardDeck<Weapon> {
    
    @Override
    protected void addCards(){
        for(int i=0; i<WeaponCardDeck.MAX_TAM; i++){
            this.addCard(new Weapon(Dice.weaponPower(), Dice.usesLeft()));
        }
    }
    
}
