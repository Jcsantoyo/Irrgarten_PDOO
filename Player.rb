require_relative 'Weapon' #Para contenedores de Weapon
require_relative 'Shield' #Para contenedores de Shield
require_relative 'Directions' #Para move

module Irrgarten

  class Player

    @@MAX_WEAPONS=2
    @@MAX_SHIELDS=3
    @@INITIAL_HEALTH=10
    @@HITS2LOSE=3
    @@NO_POS=-1

    def initialize(number,intelligence,strength)
      @name= "Persona #{number}"
      @number=number
      @intelligence=intelligence
      @strength=strength
      @health=@@INITIAL_HEALTH
      @row=@@NO_POS
      @col=@@NO_POS
      @consecutive_hits=0

      @weapons=[]
      @shields=[]
    end

    attr_reader :row
    attr_reader :col
    attr_reader :number

    def resurrect
      @weapons.clear
      @shields.clear
      @health=@@INITIAL_HEALTH
      @consecutive_hits=0
    end

    def row
      @row
    end

    def col
      @col
    end

    def number
      @number
    end

    def set_pos(row,col)
      @row=row
      @col=col
    end

    def dead
      return @health==0
    end

    def move(direction,valid_moves)

    end

    def attack
      return (@strength+sum_weapons)
    end

    def defend(received_attack)
      return manage_hit(received_attack)
    end

    def to_s
      nombre = "Nombre: #{@name}"
      inteligencia = "Int: #{@intelligence}"
      fuerza = "Fuerza: #{@strength}"
      vida = "Vida: #{@health}"
      posicion = "Casilla: (#{@row}, #{@col})"
      hits = "Hits: #{@consecutive_hits}"
      
      armas = "Armas: " + @weapons.map(&:to_s).join(", ")
      escudos = "Escudos: " + @shields.map(&:to_s).join(", ")

      "#{nombre}  #{inteligencia}  #{fuerza}  #{vida}  #{posicion}  #{hits}  #{armas}  #{escudos}"
    end

    private

    def receive_weapon
    end

    def receive_shield
    end

    def new_weapon
      return Weapon.new(Dice.weapon_power,Dice.uses_left)
    end

    def new_shield
      return Shield.new(Dice.shield_power,Dice.uses_left)
    end 

    def sum_weapons
      sum = 0.0
      @weapons.each do |weapon|
        sum += weapon.attack
      end
      sum
    end
    
    def sum_shields
      sum = 0.0
      @shields.each do |shield|
        sum += shield.protect
      end
      sum
    end

    def defensive_energy
      return (@intelligence+sum_shields)
    end

    def manage_hit(received_attack)
    end

    def reset_hits
      @consecutive_hits=0
    end

    def got_wounded
      @health-=1
    end
    
    def inc_consecutive_hits
      @consecutive_hits+=1
    end
    
  end
  # Bloque de prueba que se ejecuta solo si este archivo es el programa principal
  if __FILE__ == $0
    # Crear una instancia del monstruo
    player = Player.new(1, 10, 8)
    puts player.to_s
  
  end
end
  
  