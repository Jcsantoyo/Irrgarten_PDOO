
package irrgarten;

/**
 *
 * Clase que representa la baraka de cartas de tipo Escudo
 * 
 * @author Juan Caballero Santoyo
 */
public class ShieldCardDeck extends CardDeck<Shield> {
    
    /**
     * Método que añade todas las cartas de tipo Escudo a la baraja
     * con un tamaño máximo de TAMANIO_MAX cartas
     */
    @Override
    protected void addCards(){
        for(int i=0; i<ShieldCardDeck.MAX_TAM; i++){
            this.addCard(new Shield(Dice.shieldPower(), Dice.usesLeft()));
        }
    }
    
}
