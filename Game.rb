require_relative 'Labyrinth'
require_relative 'GameCharacter'


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


    def next_step(preferred_direction)
      log=""
      dead=@current_player.dead

      if(!dead)

        direction=actual_direction(preferred_direction)

        if(direction!= preferred_direction)
          log_player_no_orders
        end

        monster=@lab.put_player(direction,@current_player)

        if(monster==nil)
          log_no_monster
        else
          winner=combat(monster)
          manage_reward(winner)
        end
      else
        manage_resurrection
      end
      
      end_game=finished

      if(!end_game)
        next_player
      end

      return end_game
    
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
      current_row=@current_player.row
      current_col=@current_player.col
      valid_moves=@lab.valid_moves(current_row,current_col)
      output=@current_player.move(preferred_direction,valid_moves)
      return output
    end

    def combat(monster)
      rounds=0
      winner=GameCharacter::PLAYER

      player_attack=@current_player.attack

      lose=monster.defend(player_attack)

      while( (!lose) && (round < @@MAX_ROUNDS))
        winner=GameCharacter::Monster
        rounds+=1

        monster_attack=monster.attack
        lose=player.defend(monster_attack)

        if(!lose)
          player_attack=player.attack
          winner = GameCharacter::PLAYER
          lose=monster.defend(player_attack)
        end
      end

      log_rounds(rounds, @@MAX_ROUNDS)

      return winner
    end

    def manage_reward(winner)
      if(winner==GameCharacter::PLAYER)
        @current_player.receive_reward
        log_player_won
      else
        log_monster_won
      end
    end

    def manage_resurrection
      resurrect=Dice.resurrect_player
      if(resurrect)
        @current_player.resurrect
        log_resurrected
      else
        log_player_skip_turn
      end

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
