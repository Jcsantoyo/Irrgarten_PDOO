require_relative 'Player'
require_relative 'Monster'
require_relative 'Orientation'

module Irrgarten

  # Clase que representa un laberinto en el juego Irrgarten.
  #
  # Esta clase se encarga de gestionar el estado del laberinto, incluyendo la
  # distribución de jugadores, monstruos, bloques y la celda de salida.
  #
  # @author Juan Caballero Santoyo

  class Labyrinth
    @@BLOCK_CHAR='X'
    @@EMPTY_CHAR='-'
    @@MONSTER_CHAR='M'
    @@COMBAT_CHAR='C'
    @@EXIT_CHAR='E'

    @@ROW=0
    @@COL=1
    @@DIM=2

    @@NO_POS=-1


    # Inicializa una nueva instancia del laberinto.
    #
    # @param [Integer] n_rows Número de filas del laberinto.
    # @param [Integer] n_cols Número de columnas del laberinto.
    # @param [Integer] exit_row Fila en la que se encuentra la salida.
    # @param [Integer] exit_col Columna en la que se encuentra la salida.
    # @return [Labyrinth] La nueva instancia del laberinto.
    def initialize(n_rows, n_cols, exit_row, exit_col)
      @n_rows = n_rows.to_i
      @n_cols = n_cols.to_i
      @exit_row = exit_row.to_i
      @exit_col = exit_col.to_i

      @monsters  = Array.new(@n_rows) {Array.new(@n_cols)} 
      @players   = Array.new(@n_rows) {Array.new(@n_cols)} 
      @square_states = Array.new(@n_rows) {Array.new(@n_cols, @@EMPTY_CHAR)} 

      @square_states[@exit_row][@exit_col] = @@EXIT_CHAR
    end

    # Distribuye a los jugadores en posiciones vacías del laberinto.
    #
    # Itera sobre el array de jugadores y, para cada uno, obtiene una posición
    # aleatoria vacía donde colocarlo.
    #
    # @param [Array<Player>] players Array de jugadores a esparcir.
    # @return [void]
    def spread_players(players)
      players.each do |p|
        pos=self.random_empty_pos
        self.put_player_2d(@@NO_POS,@@NO_POS,pos[@@ROW],pos[@@COL],p)
      end
    end

    # Indica si hay un ganador en el laberinto.
    #
    # Se considera ganador aquel jugador que se encuentre en la celda de salida.
    #
    # @return [Boolean] true si existe un jugador en la salida, false en caso contrario.
    def have_a_winner
      return (@players[@exit_row][@exit_col]!=nil)
    end



    # Genera una representación en cadena del estado actual del laberinto.
    #
    # Se muestran los índices de las filas y columnas junto con el contenido de cada celda.
    #
    # @return [String] Cadena que representa el estado del laberinto.
    def to_s
      # Cálculo del número de caracteres que debe ocupar cada parte
      fil_size = (@n_rows - 1).to_s.size
      col_size = (@n_cols - 1).to_s.size
      n_players_size = (@players.size - 1).to_s.size
    
      # Cálculo del tamaño máximo
      max_size = [fil_size, col_size, n_players_size].max
      format_str = "%#{max_size}s"
    
      # Construimos la cadena de salida
      to_return = ""
    
      # Índices en cada columna (encabezado)
      to_return << " " + sprintf(format_str, " ")
      @n_cols.times do |i|
        to_return << sprintf(format_str, i) + " "
      end
      to_return << "\n"
    
      # Recorremos las filas y columnas
      @n_rows.times do |r|
        to_return << sprintf(format_str, r) + " "
        @n_cols.times do |c|
          to_return << sprintf(format_str, @square_states[r][c]) + " "
        end
        to_return << "\n"
      end
    
      to_return
    end


    # Añade un monstruo al laberinto en la posición indicada.
    #
    # Si la posición es válida y se encuentra vacía, se coloca el monstruo y se
    # actualiza la celda con el carácter correspondiente.
    #
    # @param [Integer] row Fila en la que se añade el monstruo.
    # @param [Integer] col Columna en la que se añade el monstruo.
    # @param [Monster] monster Instancia del monstruo a colocar.
    # @return [void]
    def add_monster(row, col, monster)
      if (pos_ok(row,col) && empty_pos(row,col))
        @square_states[row][col]=@@MONSTER_CHAR
        monster.set_pos(row,col)
        @monsters[row][col]=monster
      end
    end


    # Mueve a un jugador en la dirección especificada.
    #
    # Calcula la nueva posición según la dirección y actualiza el estado del tablero.
    # Devuelve cualquier monstruo que se encuentre en la nueva celda.
    #
    # @param [Symbol] direction Dirección del movimiento (ej. Directions::LEFT).
    # @param [Player] player El jugador que se mueve.
    # @return [Monster, nil] El monstruo en la celda destino, si existe; de lo contrario, nil.
    def put_player(direction, player)
      old_row=player.row
      old_col=player.col

      new_pos=dir_2_pos(old_row,old_col,direction)

      monster=put_player_2d(old_row,old_col,new_pos[@@ROW],new_pos[@@COL], player)

      return monster
    end


    # Añade un bloque al laberinto en la orientación y posición inicial indicada.
    #
    # Se actualizan las celdas, cambiando su contenido al carácter de bloque, según la
    # longitud especificada.
    #
    # @param [Symbol] orientation Orientación del bloque (ej. Orientation::VERTICAL o HORIZONTAL).
    # @param [Integer] start_row Fila inicial para el bloque.
    # @param [Integer] start_col Columna inicial para el bloque.
    # @param [Integer] length Longitud del bloque.
    # @return [void]
    def add_block(orientation, start_row,start_col,length)
      if(orientation==Orientation::VERTICAL)
        inc_row = 1
        inc_col = 0
      else
          inc_row = 0
          inc_col = 1
      end

      row = start_row
      col = start_col

      while ( pos_ok(row,col) && empty_pos(row,col) && (length>0) )
          @square_states[row][col] = @@BLOCK_CHAR
          length-=1
          row+=inc_row
          col+=inc_col
      end
    end

    # Retorna los movimientos válidos a partir de una posición dada.
    #
    # Un movimiento es válido si la celda destino es accesible (vacía, con monstruo o la salida).
    #
    # @param [Integer] row Fila de la posición de referencia.
    # @param [Integer] col Columna de la posición de referencia.
    # @return [Array<Symbol>] Lista de direcciones válidas.
    def valid_moves(row, col)
      output=Array.new
      if(can_step_on(row+1,col))
        output.push(Directions::DOWN)
      end

      if(can_step_on(row-1,col))
        output.push(Directions::UP)
      end

      if(can_step_on(row,col+1))
        output.push(Directions::RIGHT)
      end

      if(can_step_on(row, col-1))
        output.push(Directions::LEFT)
      end
      
      return output

    end

    private


    # Verifica si una posición (row, col) se encuentra dentro de los límites del laberinto.
    #
    # @param [Integer] row Fila a verificar.
    # @param [Integer] col Columna a verificar.
    # @return [Boolean] true si la posición es válida, false en caso contrario.
    def pos_ok(row, col)
      return (row>=0 && row<@n_rows && col>=0 && col<@n_cols);
    end

    # Verifica si la celda en (row, col) está vacía.
    #
    # @param [Integer] row Fila de la celda.
    # @param [Integer] col Columna de la celda.
    # @return [Boolean] true si la celda contiene el carácter de vacío, false en caso contrario.
    def empty_pos(row, col)
      return (@square_states[row][col]== @@EMPTY_CHAR)
    end

    # Verifica si la celda en (row, col) contiene un monstruo.
    #
    # @param [Integer] row Fila de la celda.
    # @param [Integer] col Columna de la celda.
    # @return [Boolean] true si la celda contiene el carácter del monstruo, false en caso contrario.
    def monster_pos(row, col)
      return (@square_states[row][col]==@@MONSTER_CHAR)
    end


    # Verifica si la celda en (row, col) es la celda de salida.
    #
    # @param [Integer] row Fila de la celda.
    # @param [Integer] col Columna de la celda.
    # @return [Boolean] true si la celda contiene el carácter de la salida, false en caso contrario.
    def exit_pos(row,col)
      return (@square_states[row][col]==@@EXIT_CHAR)
    end


    # Verifica si la celda en (row, col) representa un estado de combate.
    #
    # @param [Integer] row Fila de la celda.
    # @param [Integer] col Columna de la celda.
    # @return [Boolean] true si la celda contiene el carácter de combate, false en caso contrario.
    def combat_pos(row, col)
      return (@square_states[row][col]==@@COMBAT_CHAR)
    end

    # Determina si se puede pisar la celda (row, col).
    #
    # La celda es "pisable" si está dentro de los límites y contiene una celda vacía,
    # con un monstruo o la salida.
    #
    # @param [Integer] row Fila destino.
    # @param [Integer] col Columna destino.
    # @return [Boolean] true si se puede pisar la celda, false en caso contrario.
    def can_step_on(row, col)
      return (pos_ok(row, col) && (empty_pos(row, col) || monster_pos(row,col) || exit_pos(row, col)))
    end


    # Actualiza el estado de la celda anterior del jugador.
    #
    # Si la celda estaba en estado de combate, se restablece al carácter del monstruo;
    # en otro caso, se marca como vacía.
    #
    # @param [Integer] row Fila de la celda.
    # @param [Integer] col Columna de la celda.
    # @return [void]
    def update_old_pos(row, col)
      if(pos_ok(row,col))
        if(combat_pos(row,col))
          @square_states[row][col]=@@MONSTER_CHAR
        else
          @square_states[row][col]=@@EMPTY_CHAR
        end
      end
    end

    # Calcula la nueva posición a partir de una dirección.
    #
    # @param [Integer] row Fila origen.
    # @param [Integer] col Columna origen.
    # @param [Symbol] direction Dirección del movimiento (ej. Directions::LEFT).
    # @return [Array<Integer>] Nuevo par [fila, columna].
    def dir_2_pos(row, col, direction)
      pos = Array.new
      pos[@@ROW] = row
      pos[@@COL] = col
      case direction  
          when Directions::LEFT
              pos[@@COL] -= 1
          when Directions::RIGHT
              pos[@@COL] += 1
          when Directions::UP
              pos[@@ROW] -= 1
          when Directions::DOWN
              pos[@@ROW] += 1
         
      end
      return pos
    end
    
    # Retorna una posición aleatoria que se encuentre vacía en el laberinto.
    #
    # @return [Array<Integer>] Arreglo que contiene la fila y columna de la posición vacía.
    def random_empty_pos
      begin
          row = Dice.random_pos(@n_rows)
          col = Dice.random_pos(@n_cols)
      end while !empty_pos(row, col)

      pos = Array.new
      pos[@@ROW] = row
      pos[@@COL] = col

      return pos
    end

    # Coloca al jugador en la posición (row, col) y actualiza el tablero.
    #
    # Si la posición destino es válida, actualiza la celda de destino con el valor del
    # jugador (o el carácter de combate si hay un monstruo) y elimina al jugador de la posición antigua.
    #
    # @param [Integer] old_row Fila antigua del jugador.
    # @param [Integer] old_col Columna antigua del jugador.
    # @param [Integer] row Nueva fila para el jugador.
    # @param [Integer] col Nueva columna para el jugador.
    # @param [Player] player Jugador a mover.
    # @return [Monster, nil] El monstruo en la celda destino, si existe; de lo contrario, nil.
    def put_player_2d(old_row,old_col,row,col,player)
      output=nil

      if(can_step_on(row,col))
        if(pos_ok(old_row,old_col))
          p = @players[old_row][old_col]
          if (p==player)
            update_old_pos(old_row,old_col)
            @players[old_row][old_col]=nil
          end
        end
      
        monster_pos=self.monster_pos(row,col)

        if(monster_pos)
          @square_states[row][col]=@@COMBAT_CHAR
          output=@monsters[row][col]
        else
          number = player.number
          @square_states[row][col]= number
        end

        @players[row][col]=player
        player.set_pos(row,col)
        return output
      end
    end
  end






  if __FILE__ == $0
    
  
    puts "== Creando Laberinto =="
    # Crear un laberinto de 5x5 con la salida en (2,2)
    labyrinth = Irrgarten::Labyrinth.new(5, 5, 2, 2)
    puts "Estado inicial del laberinto:"
    puts labyrinth.to_s
    puts
  
    puts "== Prueba de add_block =="
    # Agregar un bloque vertical desde (0,0) de longitud 3
    labyrinth.add_block(Orientation::VERTICAL, 0, 0, 3)
    puts "Después de add_block (vertical, 0,0,3):"
    puts labyrinth.to_s
    puts
  
    # Agregar un bloque horizontal desde (4,0) de longitud 4
    labyrinth.add_block(Orientation::HORIZONTAL, 4, 0, 4)
    puts "Después de add_block (horizontal, 4,0,4):"
    puts labyrinth.to_s
    puts
  
    puts "== Prueba de add_monster =="
    # Crear un monstruo y agregarlo en (1,1)
    monster = Monster.new("pepe",1,1)
    labyrinth.add_monster(1, 1, monster)
    puts "Después de add_monster en (1,1):"
    puts labyrinth.to_s
    puts
  
    puts "== Prueba de valid_moves =="
    # Obtener movimientos válidos desde (2,2)
    moves = labyrinth.valid_moves(2, 2)
    puts "Movimientos válidos desde (2,2): #{moves}"
    puts
  
    puts "== Prueba de spread_players =="
    player1 = Irrgarten::Player.new(1, 10, 8)
    player2 = Irrgarten::Player.new(2, 9, 7)
    players = [player1, player2]
    labyrinth.spread_players(players)
    puts "Después de spread_players:"
    puts labyrinth.to_s
    puts
  
    puts "== Prueba de put_player =="
    moved_monster = labyrinth.put_player(Directions::RIGHT, player1)
    puts "Después de mover al jugador 1 a la derecha:"
    puts labyrinth.to_s
    if moved_monster
      puts "El jugador se encontró con un monstruo: #{moved_monster}"
    end
    puts
  
    puts "== Prueba de have_a_winner =="
    if labyrinth.have_a_winner
      puts "¡Hay un ganador! Un jugador está en la salida."
    else
      puts "Aún no hay ganador (ningún jugador en la salida)."
    end
    puts
  
    
    
    puts "== Pruebas completadas =="
  end
end



