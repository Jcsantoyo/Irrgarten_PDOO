require_relative 'Dice'

module Irrgarten
  class Weapon
    def initialize(pow,us)
      @power=pow
      @uses=us
    end

    def attack()
      attack=@power
      if @uses > 0
        @uses-=1
      else
        attack=0
      end
      return attack
    end

    def to_s
      "W[#{@power}, #{@uses}]"
    end
    def discard()
      return Dice.discard_element(@uses)
    end
  end
end

