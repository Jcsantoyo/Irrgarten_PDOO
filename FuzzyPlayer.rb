require_relative 'Player'


module Irrgarten

  class FuzzyPlayer < Player
    # Constructor de la clase {FuzzyPlayer}.
    def initialize(other_player)
        copy(other_player)
    end

    def move(direction, valid_moves)
      dir=super(direction,valid_moves)
      return Dice.next_step(dir, valid_moves,self.intelligence)
    end

    def attack
      return Dice.intensity(self.strength+self.sum_weapons)
    end

    protected

    def defensive_energy
      return Dice.intensity(self.intelligence+self.sum_shields)
    end

    public
    def to_s
      return "(Fuzzy) " + super
    end
  end
end


