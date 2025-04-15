require_relative 'Labyrinth'
require_relative 'GameCharacter'
require_relative 'GameState'


module Irrgarten
  class Game
    @@MAX_ROUNDS=10

    #Dimensiones del laberinto
    @@ROWS=7
    @@COLS=7

    def initialize(nplayers)
      exit_row=Dice.random_pos(@@ROWS)
      exit_col=Dice.random_pos(@@COLS)

      @players = Array.new
      @monsters = Array.new

      nplayers.times do |i|
        @players.push(Player.new(i.to_s, Dice.random_intelligence, Dice.random_strength))
      end
  
      @current_player_index = Dice.who_starts(nplayers)
      @current_player = @players[@current_player_index]
      
      @log = "-Game just started. \n "
      
      @lab=Labyrinth.new(@@ROWS,@@COLS,exit_row,exit_col)
      configure_labyrinth()
      @lab.spread_players(@players)


    end

    def finished
      return @lab.have_a_winner
    end


    def next_step(preferred_direction)
      log=""

      dead=@current_player.dead

      if(!dead)

        direction=actual_direction(preferred_direction)

        if(direction!= preferred_direction)
          log_player_no_orders()
        end

        monster=@lab.put_player(direction,@current_player)

        if(monster==nil)
          log_no_monster()
        else
          winner=combat(monster)
          manage_reward(winner)
        end
      else
        manage_resurrection()
      end
      
      end_game=finished()

      if(!end_game)
        next_player()
      end

      return end_game
    
    end


    def game_state
      info_players=""
      info_monsters=""

      @players.each do |player|
        info_players+=player.to_s+"\n"
      end

      @monsters.each do |monster|
        info_monsters+=monster.to_s+"\n"
      end

      return GameState.new(@lab.to_s,info_players,info_monsters,@current_player_index, finished(),@log)
    end

    private

    def configure_labyrinth

      monster_1=Monster.new("Mike Wasowsky",Dice.random_intelligence,Dice.random_strength)
      monster_2=Monster.new("Alucard",Dice.random_intelligence,Dice.random_strength)
      monster_3=Monster.new("Frankenstein",Dice.random_intelligence,Dice.random_strength)

      @lab.add_monster(3,3,monster_1)
      @lab.add_monster(1,5,monster_2)
      @lab.add_monster(5,5,monster_3)
      
      @monsters.push(monster_1)
      @monsters.push(monster_2)
      @monsters.push(monster_3)

      @lab.add_block(Orientation::HORIZONTAL,6,3,1)
      @lab.add_block(Orientation::HORIZONTAL,2,1,2)
      @lab.add_block(Orientation::VERTICAL,4,5,2)


    end

    def next_player
      if(@current_player_index==@players.size-1)
        @current_player_index=0
      else
        @current_player_index+=1
      end

      @current_player=@players[@current_player_index]
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

      while( (!lose) && (rounds < @@MAX_ROUNDS))
        winner=GameCharacter::MONSTER
        rounds+=1

        monster_attack=monster.attack
        lose=@current_player.defend(monster_attack)

        if(!lose)
          player_attack=@current_player.attack
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
        log_player_won()
      else
        log_monster_won()
      end
    end

    def manage_resurrection
      resurrect=Dice.resurrect_player
      if(resurrect)
        @current_player.resurrect
        log_resurrected()
      else
        log_player_skip_turn()
      end

    end

    def log_player_won
        @log += "- Player #{@current_player_index} won the fight.\n"
    end

    def log_monster_won
        @log += "- Monster won the fight.\n"
    end

    def log_resurrected
        @log += "- Player #{@current_player_index} resurrected.\n"
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
