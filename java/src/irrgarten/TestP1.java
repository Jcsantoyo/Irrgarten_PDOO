package irrgarten;

import irrgarten.controller.Controller;

import irrgarten.UI.GraphicUI;

public class TestP1 {
    public static void main(String[] args) {
        
         final int N_PLAYERS = 1;
        
         
        GraphicUI vista = new GraphicUI();
        Game juego = new Game(N_PLAYERS);
        Controller controlador = new Controller(juego, vista);

        controlador.play();
    }
}
