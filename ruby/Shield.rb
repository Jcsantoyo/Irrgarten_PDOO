require_relative 'CombatElement'
module Irrgarten

  # La clase Shield representa un escudo utilizado en combate para proteger al jugador.
  #
  # Esta clase administra el nivel de protección que aporta el escudo y la cantidad de usos restantes.
  # Además, provee la funcionalidad para ejecutar la acción de descarte (discard) a través de la clase Dice.
  #
  # @author Juan Caballero Santoyo
  class Shield < CombatElement


    # Realiza una acción de protección con el escudo.
    #
    # Si el escudo cuenta con usos disponibles, se decrementa en uno el contador de usos y se retorna el valor
    # de protección asociado; de lo contrario, se retorna 0, indicando que no se puede usar el escudo.
    #
    # @return [Integer] El valor de protección aplicado o 0 si no hay usos restantes.
    def protect
      self.produce_effect
    end


    # Retorna una representación en cadena del escudo.
    #
    # El formato de la cadena es "W[<protección>, <usos>]", donde se muestran el valor actual de protección
    # y la cantidad de usos restantes.
    #
    # @return [String] La representación textual del escudo.
    def to_s
      return "S"+super
    end
    
    public_class_method :new
  end

end

