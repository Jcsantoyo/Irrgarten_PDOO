package irrgarten;

import irrgarten.controller.Controller;

import irrgarten.UI.TextUI;
import java.util.Arrays;  // para mostrar distribuciones en arrays

public class TestP1 {
    public static void main(String[] args) {
        
         final int N_PLAYERS = 3;
        
         
        TextUI vista = new TextUI();
        Game juego = new Game(N_PLAYERS);
        Controller controlador = new Controller(juego, vista);

        controlador.play();
    }
}
