module Irrgarten
  
  class GameState

    def initializer(lab,pla,mons,cpl,win,lg)
      @labyrinth=lab
      @players=pla
      @monsters=mons
      @currentPlayer=cpl
      @winner=win
      @log=lg
    end

    def get_labyrinth
      return @labyrinth
    end

    def get_players
      return @players
    end

    def get_monsters
      return @monsters
    end

    def get_currentPlayers
      return @currentPlayer
    end

    def get_winner
      return @winner
    end

    def get_log
      return @log
    end
  end
end
