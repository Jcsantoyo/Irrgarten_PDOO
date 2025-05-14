require_relative 'Dice'

module Irrgarten
  class CombatElement
    def initialize(effect,uses)
      @effect=effect
      @uses=uses
    end

    protected

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

    def discard
      return Dice.discard_element(@uses)
    end

    def to_s
      "[#{@effect}, #{@uses}]"
    end 

    private_class_method :new
  end
end
