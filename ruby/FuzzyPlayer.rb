require_relative 'Player'


module Irrgarten

    # Clase que representa un jugador Fuzzy (mareado). No es ta determinista en sus acciones.
    #
    # @author Juan Caballero Santoyo

  class FuzzyPlayer < Player
    # Constructor de la clase {FuzzyPlayer}.
    def initialize(other_player)
        copy(other_player)
    end


    # Mueve al jugador en la dirección indicada en función de si es válida o no, y
    # de la inteligencia del jugador. A mayor inteligencia, mayor probabilidad de moverse en la dirección deseada.
    #
    # @see Dice#nextStep
    # @see Player#move
    #
    # @param direction [Directions] Dirección a la que se pretende desplazar el personaje
    # @param valid_moves [Array<Directions>] Lista de direcciones válidas a las que se puede mover el jugador
    #
    # @return [Directions] Dirección a la que se quiere desplazar el {FuzzyPlayer} (tendremos que ver si es válida)
    def move(direction, valid_moves)
      dir=super(direction,valid_moves)
      return Dice.next_step(dir, valid_moves,self.intelligence)
    end

    # Calcula la la suma de lo aportado por sus armas ({#sum_weapons}) y
    # de un valor aleatorio menor que su fuerza (por ser Fuzzy)
    #
    # @return [float] la intensidad del ataque
    def attack
      return Dice.intensity(self.strength)+self.sum_weapons
    end

    protected

    # Calcula la suma del aporte de los escudos ({#sum_shields}) y
    # de un valor aleatorio menor que su inteligencia (por ser Fuzzy)
    #
    # @return [float] La energía defensiva
    def defensive_energy
      return Dice.intensity(self.intelligence)+self.sum_shields
    end

    # Método que genera una cadena de caracteres con la información del jugador
    #
    # @return [String] cadena de caracteres con la información del jugador
    public
    def to_s
      return "(Fuzzy) " + super
    end
  end
end


