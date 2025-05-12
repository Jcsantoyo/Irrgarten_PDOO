require_relative 'Dice'

module Irrgarten
  class LabyrinthCharacter

    @@NO_POS=-1

    def initialize(name, intelligence, strength, health)
      @name = name.to_s
      @intelligence = intelligence.to_f
      @strength = strength.to_f
      @health = health.to_f

      @row = @@NO_POS
      @col = @@NO_POS
  end

    def copy(other)
      @name=other.name
      @intelligence=other.intelligence
      @strength=other.strength
      @health=other.health
      @row=other.row
      @col=other.col
    end

    def dead
      return (@health<=0)
    end

    attr_reader :row
    attr_reader :col

    protected

    attr_reader :intelligence
    attr_reader :strength
    attr_accessor :health

    public

    def set_pos(row, col)
      @row=row
      @col=col
    end

    def to_s
      # Formato para mostrar los datos flotantes del personaje
      formato='%.6f'

      return "#{@name}[i:#{format(formato,@intelligence)}, s:#{format(formato,@strength)}, "+
      "h:#{format(formato,@health)}, p:(#{@row}, #{@col})]"
    end

    protected

    def got_wounded
      @health-=1
    end

    public

    def attack
      raise NotImplementedError
    end

    def defend
      raise NotImplementedError
    end

    private_class_method :new
  end
end