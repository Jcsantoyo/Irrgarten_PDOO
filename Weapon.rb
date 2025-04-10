require_relative 'Dice'

module Irrgarten

  # Esta clase representa las armas que utiliza el jugador en los ataques
  # durante los combates.
  #
  # @author Juan Caballero Santoyo


  class Weapon

    # Crea una nueva instancia de Weapon.
    #
    # @param [Integer] power La potencia o valor de ataque que proporciona el arma.
    # @param [Integer] uses  El número de usos disponibles para el arma.
    
    def initialize(power ,uses)
      @power=power
      @uses=uses
    end

    # Ejecuta un ataque utilizando el arma.
    #
    # Este método evalúa si el arma dispone de usos disponibles. Si es así, se decrementa
    # el contador de usos en 1 y se retorna la potencia asignada al arma. Si no quedan usos,
    # el ataque retorna 0, indicando que el arma no puede ser utilizada.
    #
    # @return [Integer] El valor del ataque, que corresponde a la potencia del arma o 0 si no hay usos.

    def attack
      attack=@power

      if @uses > 0
        @uses-=1

      else
        attack=0
      end

      return attack
    end

    # Retorna una representación en cadena del arma.
    #
    # El formato de la representación es "W[<potencia>, <usos>]", donde <potencia> y <usos>
    # son los atributos actuales del arma.
    #
    # @return [String] La representación textual del arma.

    def to_s
      "W[#{@power}, #{@uses}]"
    end


    # Desecha el arma utilizando la funcionalidad de la clase Dice.
    #
    # Este método invoca el método de clase Dice.discard_element pasándole el número
    # de usos actuales del arma, lo que permite realizar una acción asociada al descarte
    # del arma en el contexto del juego.
    #
    # @return [Object] El resultado de la operación de descarte proporcionada por Dice.

    def discard
      return Dice.discard_element(@uses)
    end


  end

end

