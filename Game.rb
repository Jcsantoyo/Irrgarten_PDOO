require_relative 'Labyrinth'


module Irrgarten
  class Game
    @@MAX_ROUNDS=10

    def initialize(nplayers)
      @players = []
      nplayers.times do |i|
        @players << Player.new(i.chr, Dice.random_intelligence, Dice.random_strength)
      end
  
      @current_player_index = Dice.who_starts(nplayers)
      @current_player = @players[@current_player_index]
      @monsters = []
      @log = " "
  
      configure_labyrinth()
    end

    def finished
      return lab.have_a_winner
    end

    def get_game_state
    end

    private
    
    def configure_labyrinth
      @lab=Labyrinth.new(5,5,Dice.random_pos(5),Dice.random_pos(5))
      lab.spread_players(@players)
    end

    def next_player
      if(@current_player_index==@players.size-1)
        @current_player_index=0
      else
        @current_player_index+=1
      end

      current_player=players[current_player_index]
    end

    def actual_direction(preferred_direction)
    end

    def combat(monster)
    end

    def manage_reward(winner)
    end

    def manage_resurrection
    end

    def log_player_won
      @log += "- Player #{@current_player_index} won the fight.\n"
    end

    def log_monster_won
        @log += "- Monster won the fight.\n"
    end

    def log_resurrected
        @log += "- Player #{@current_player_index} resurrected as fuzzy.\n"
    end

    def log_player_skip_turn
        @log += "- Player #{@current_player_index} skipped turn (is dead).\n"
    end

    def log_player_no_orders
        @log += "- Player #{@current_player_index} didn't follow orders, it was not possible.\n"
    end

    def log_no_monster
        @log += "- Player #{@current_player_index} moved to an empty cell or it was not possible to move.\n"
    end

    
    def log_rounds(rounds, max)
        @log += "- Rounds: #{rounds}/#{max}.\n"
    end
  end
end
