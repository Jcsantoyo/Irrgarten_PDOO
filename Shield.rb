require_relative 'Dice' #Para discard

module Irrgarten

  # La clase Shield representa un escudo utilizado en combate para proteger al jugador.
  #
  # Esta clase administra el nivel de protección que aporta el escudo y la cantidad de usos restantes.
  # Además, provee la funcionalidad para ejecutar la acción de descarte (discard) a través de la clase Dice.
  #
  # @author Juan Caballero Santoyo
  class Shield

    
    # Inicializa una nueva instancia del escudo.
    #
    # @param [Integer] protection El valor de protección que ofrece el escudo.
    # @param [Integer] uses   La cantidad de usos disponibles para el escudo.
    def initialize(protection,uses)
      @protection=protection
      @uses=uses
    end


    # Realiza una acción de protección con el escudo.
    #
    # Si el escudo cuenta con usos disponibles, se decrementa en uno el contador de usos y se retorna el valor
    # de protección asociado; de lo contrario, se retorna 0, indicando que no se puede usar el escudo.
    #
    # @return [Integer] El valor de protección aplicado o 0 si no hay usos restantes.
    def protect
      prot=@protection

      if @uses > 0
        @uses-=1

      else
        prot=0

      end

      return prot
    end


    # Retorna una representación en cadena del escudo.
    #
    # El formato de la cadena es "W[<protección>, <usos>]", donde se muestran el valor actual de protección
    # y la cantidad de usos restantes.
    #
    # @return [String] La representación textual del escudo.
    def to_s
      "S[#{@protection}, #{@uses}]"
    end
    
  
    # Desecha el escudo utilizando la funcionalidad definida en Dice.
    #
    # Este método delega la operación de descarte al método de clase `discard_element` de Dice, pasándole
    # el número de usos actuales del escudo.
    #
    # @return [Object] El resultado retornado por Dice.discard_element.
    def discard
      return Dice.discard_element(@uses)
    end

  end

end

