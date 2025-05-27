require_relative 'Dice'

module Irrgarten
    # Esta clase representa un elemento de combate, que puede ser un arma ({Weapon}) o un escudo ({Shield}).
    #
    # @abstract No se debe instanciar directamente.
    #
    # @author Juan Caballero Santoyo
 
  class CombatElement

    # Constructor de la clase
    #
    # @param effect [float] efecto del elemento de combate
    # @param uses [int] usos que tiene el elemento de combate
    def initialize(effect,uses)
      @effect=effect.to_f
      @uses=uses.to_i
    end

    protected
    # Método que produce el efecto del elemento de combate y decrementa el número de usos en uno.
    #
    # @return [float] Si tiene algún uso disponible devuelve el uso del elemento. En caso contrario (**uses**==**0**) devuelve 0.
    def produce_effect
      efecto=@effect
      if(@uses>0)
        @uses-=1
      else
        efecto=0.0
      end
      return efecto
    end

    public

    # Método que indica si se descartará el elemento de combate.
    #
    # @see Dice#dicard_element
    # @return [boolean] devuelve **true** o **false** si se descarta o no
    def discard
      return Dice.discard_element(@uses)
    end

    # Método que muestra en una cadena el estado del elemento, en cuanto a
    # uso y efecto.
    #
    # @return [String] devuelve una cadena que muestra usos y efecto del elemento
    def to_s
      "[#{@effect}, #{@uses}]"
    end 

    private_class_method :new
  end
end
