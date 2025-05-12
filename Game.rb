require_relative 'Labyrinth'
require_relative 'GameCharacter'
require_relative 'GameState'


module Irrgarten
  # Clase que representa el juego Irrgarten.
  #
  # Coordina la creación y configuración del laberinto, los jugadores, los monstruos,
  # el desarrollo de los turnos (movimientos, combate, recompensas o resurrección) y la
  # generación de un log con los eventos del juego.
  #
  #
  # @author Juan Caballero Santoyo
  class Game
    @@MAX_ROUNDS=10

    #Dimensiones del laberinto
    @@ROWS=7
    @@COLS=7


    @@EXIT_ROW
    @@EXIT_COL 

    # Inicializa una nueva instancia del juego.
    #
    # Se crea el laberinto con una salida en una posición aleatoria, se instancian los jugadores,
    # se selecciona el jugador que iniciará el juego y se configuran monstruos y bloques en el laberinto.
    #
    # @param [Integer] nplayers Número de jugadores de la partida.
    # @return [Game] La instancia del juego inicializada.
    def initialize(nplayers)
      @@EXIT_ROW=Dice.random_pos(@@ROWS)
      @@EXIT_COL=Dice.random_pos(@@COLS)

      @players = Array.new
      @monsters = Array.new

      nplayers.times do |i|
        @players.push(Player.new(i.to_s, Dice.random_intelligence, Dice.random_strength))
      end
  
      @current_player_index = Dice.who_starts(nplayers)
      @current_player = @players[@current_player_index]
      
      @log = "-Game just started. \n "
      
      @lab=Labyrinth.new(@@ROWS,@@COLS,@@EXIT_ROW,@@EXIT_COL)
      configure_labyrinth()
      @lab.spread_players(@players)

    end

    # Indica si la partida ha finalizado.
    #
    # Se considera finalizada cuando algún jugador alcanza la celda de salida.
    #
    # @return [Boolean] true si hay un ganador, false en caso contrario.
    def finished
      return @lab.have_a_winner
    end


    # Procesa el siguiente turno del juego.
    #
    # Realiza el movimiento del jugador actual en función de la dirección preferida,
    # gestiona el combate (si se encuentra un monstruo), aplica recompensas o la
    # resurrección en caso de que el jugador esté muerto, y finalmente avanza al siguiente jugador.
    #
    # @param [Direction] preferred_direction Dirección preferida para mover al jugador.
    # @return [Boolean] true si el juego ha finalizado (hay ganador), false en caso contrario.
    def next_step(preferred_direction)
      @log=""

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

    # Retorna el estado actual del juego.
    #
    # Se recopila la representación en cadena del laberinto, la información de cada jugador,
    # la información de los monstruos, el índice del jugador actual, si el juego ha finalizado y el log de eventos.
    #
    # @return [GameState] Objeto que encapsula el estado actual del juego.
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

    # Configura el laberinto: añade monstruos y bloques en posiciones predefinidas.
    #
    # Se crean tres monstruos, se colocan en el laberinto y se guardan en el array @monsters.
    # También se añaden bloques en determinadas posiciones y orientaciones.
    #
    # @return [void]
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

    # Avanza al siguiente jugador en la lista.
    #
    # Actualiza el índice y el jugador actual de forma circular.
    #
    # @return [void]
    def next_player
      if(@current_player_index==@players.size-1)
        @current_player_index=0
      else
        @current_player_index+=1
      end

      @current_player=@players[@current_player_index]
    end

    # Calcula la dirección efectiva a partir de la preferida.
    #
    # Consulta al laberinto por los movimientos válidos en la posición actual del jugador
    # y utiliza el método move del jugador para determinar la dirección final.
    #
    # @param [Direction] preferred_direction Dirección preferida por el jugador.
    # @return [Symbol] La dirección efectivamente elegida.
    def actual_direction(preferred_direction)
      current_row=@current_player.row
      current_col=@current_player.col

      valid_moves=@lab.valid_moves(current_row,current_col)

      output=@current_player.move(preferred_direction,valid_moves)

      return output
    end


    # Simula un combate entre el jugador actual y un monstruo.
    #
    # Alterna ataques entre el jugador y el monstruo hasta que uno pierda o se alcance
    # el número máximo de rondas. Se retorna quién resultó vencedor.
    #
    # @param [Monster] monster Monstruo contra el que se combate.
    # @return [GameCharacter] Constante que indica el vencedor (p.ej., GameCharacter::PLAYER o GameCharacter::MONSTER).
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

    # Gestiona la recompensa tras el combate.
    #
    # Si el jugador vence, recibe una recompensa llamando a receive_reward;
    # en caso contrario, se registra la victoria del monstruo.
    #
    # @param [GameCharacter] winner Constante que indica al vencedor del combate.
    # @return [void]
    def manage_reward(winner)
      if(winner==GameCharacter::PLAYER)
        @current_player.receive_reward
        log_player_won()
      else
        log_monster_won()
      end
    end

    # Gestiona la resurrección del jugador actual.
    #
    # Consulta a Dice si el jugador debe resucitar. Si es así, invoca el método de resurrección;
    # si no, registra que el jugador omite su turno.
    #
    # @return [void]
    def manage_resurrection
      resurrect=Dice.resurrect_player
      if(resurrect)
        @current_player.resurrect
        log_resurrected()
      else
        log_player_skip_turn()
      end

    end


    # Registra en el log que el jugador ganó el combate.
    #
    # @return [void]
    def log_player_won
        @log += "- Player #{@current_player_index} won the fight.\n"
    end

    # Registra en el log que el monstruo ganó el combate.
    #
    # @return [void]
    def log_monster_won
        @log += "- Monster won the fight.\n"
    end

    # Registra en el log que el jugador fue resucitado.
    #
    # @return [void]
    def log_resurrected
        @log += "- Player #{@current_player_index} resurrected.\n"
    end

    # Registra en el log que el jugador perdió su turno (estaba muerto).
    #
    # @return [void]
    def log_player_skip_turn
        @log += "- Player #{@current_player_index} skipped turn (is dead).\n"
    end

    # Registra en el log que el jugador no siguió las órdenes (la dirección preferida no era válida).
    #
    # @return [void]
    def log_player_no_orders
        @log += "- Player #{@current_player_index} didn't follow orders, it was not possible.\n"
    end

    # Registra en el log que el jugador se movió a una celda vacía o que el movimiento no fue posible.
    #
    # @return [void]
    def log_no_monster
        @log += "- Player #{@current_player_index} moved to an empty cell or it was not possible to move.\n"
    end

    # Registra en el log la cantidad de rondas transcurridas durante el combate.
    #
    # @param [Integer] rounds Número de rondas efectivamente ejecutadas.
    # @param [Integer] max Número máximo de rondas permitidas.
    # @return [void]
    def log_rounds(rounds, max)
        @log += "- Rounds: #{rounds}/#{max}.\n"
    end
  end
  
end
