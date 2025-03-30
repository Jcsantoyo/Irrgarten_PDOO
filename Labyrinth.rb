require_relative 'Player'
require_relative 'Monster'
require_relative 'Orientation'

module Irrgarten

  class Labyrinth
    @@BLOCK_CHAR='X'
    @@EMPTY_CHAR='-'
    @@MONSTER_CHAR='M'
    @@COMBAT_CHAR='C'
    @@EXIT_CHAR='E'

    @@ROW=0
    @@COL=1
    @@DIM=2

    def initialize(nRows, nCols, exitRow, exitCol)
      @n_rows = n_rows.to_i
      @n_cols = n_cols.to_i
      @exit_row = exit_row.to_i
      @exit_col = exit_col.to_i

      @monsters  = Array.new(@n_rows) {Array.new(@n_cols)} 
      @players   = Array.new(@n_rows) {Array.new(@n_cols)} 
      @squareStates = Array.new(@n_rows) {Array.new(@n_cols, @@EMPTY_CHAR)} 

      @square_states[@exit_row][@exit_col] = @@EXIT_CHAR
    end

    def spread_players(players)
    end

    def have_a_winner
      return (@players[exit_row][exit_col]!=nil)
    end

    def to_s
    end

    def add_monster(row, col, monster)
      if (pos_ok(row,col) && empty_pos(row,col))
        @square_states[row][col]=@@MONSTER_CHAR
        monster.set_pos(row,col)
        @monsters[row][col]=monster
      end
    end

    def put_player(direction, player)
    end

    def add_block(orientation, start_row,start_col,length)
    end

    def valid_moves(row, col)
    end

    def pos_ok(row, col)
      return (row>=@@ROW && row<@n_rows && col>=@@COL && col<@n_cols);
    end

    def empty_pos(row, col)
      return (@square_states[row][col]== @@EMPTY_CHAR)
    end

    def monster_pos(row, col)
      return (@square_states[row][col]==@@MONSTER_CHAR)
    end

    def exit_pos(row,col)
      return (@square_states[row][col]==@@EXIT_CHAR)
    end

    def combat_pos(row, col)
      return (@square_states[row][col]==@@COMBAT_CHAR)
    end

    def can_step_on(row, col)
      return (pos_ok(row, col) && empty_pos(row, col) && monster_pos(row,col) && exit_pos(row, col))
    end

    def update_old_pos(row, col)
      if(pos_ok(row,col))
        if(combat_pos(row,col))
          @square_states[row][col]=@@MONSTER_CHAR
        else
          @square_states[row][col]=@@EMPTY_CHAR
        end
      end
    end

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

    def put_player_2d
    end
  
end

#Revisar visibilidad
