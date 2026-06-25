require_relative 'CombatElement'

module Irrgarten

  # Esta clase representa las armas que utiliza el jugador en los ataques
  # durante los combates.
  #
  # @author Juan Caballero Santoyo


  class Weapon < CombatElement

   

    # Ejecuta un ataque utilizando el arma.
    #
    # Este método evalúa si el arma dispone de usos disponibles. Si es así, se decrementa
    # el contador de usos en 1 y se retorna la potencia asignada al arma. Si no quedan usos,
    # el ataque retorna 0, indicando que el arma no puede ser utilizada.
    #
    # @return [Integer] El valor del ataque, que corresponde a la potencia del arma o 0 si no hay usos.

    def attack
      self.produce_effect
    end

    # Retorna una representación en cadena del arma.
    #
    # El formato de la representación es "W[<potencia>, <usos>]", donde <potencia> y <usos>
    # son los atributos actuales del arma.
    #
    # @return [String] La representación textual del arma.

    def to_s
      return "W" + super
    end

    public_class_method :new
  end

end

