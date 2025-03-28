module Irrgarten
  
  class GameState

    def initialize(labyrinth,players,monster,current_player,winner,log)
      @labyrinth=labyrinth
      @players=players
      @monsters=monster
      @current_player=current_player
      @winner=winner
      @log=log
    end

    attr_reader :labyrinth
    attr_reader :players
    attr_reader :monsters
    attr_reader :current_player
    attr_reader :winner
    attr_reader :log


    def labyrinth
      return @labyrinth
    end

    def players
      return @players
    end

    def monsters
      return @monsters
    end

    def current_player
      return @currentPlayer
    end

    def winner
      return @winner
    end

    def log
      return @log
    end
  end
end
