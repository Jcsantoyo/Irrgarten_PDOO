require_relative 'Weapon' #Para contenedores de Weapon
require_relative 'Shield' #Para contenedores de Shield
require_relative 'Directions' #Para move
require_relative 'LabyrinthCharacter'


module Irrgarten

  # Clase que representa al jugador del juego.
  #
  #
  # @author Juan Caballero Santoyo
 
  class Player < LabyrinthCharacter

    @@MAX_WEAPONS=2 # Número máximo de armas que puede tener un jugador
    @@MAX_SHIELDS=3 # Número máximo de escudos que puede tener un jugador
    @@INITIAL_HEALTH=10 # Salud inicial de los jugadores
    @@HITS2LOSE=3 # Número de golpes que puede recibir un jugador antes de morir



    # Inicializa una nueva instancia de Player.
    #
    # @param [Integer] number Número identificador del jugador.
    # @param [Numeric] intelligence Valor de la inteligencia del jugador, que influye en la defensa.
    # @param [Numeric] strength Valor de la fuerza del jugador, que influye en el ataque.
    def initialize(number,intelligence,strength)
      
      super("Player #{number}", intelligence, strength, @@INITIAL_HEALTH)
      @number=number.to_s

      @consecutive_hits=0
      
      @weapons=Array.new
      @shields=Array.new
    end

    attr_reader :number

    protected

    attr_reader :weapons

    attr_reader :shields

    attr_reader :consecutive_hits



    public 


    # Método que busca asemejarse a un constructor de copia.
    # Copia los atributos de un jugador a otro.
    #
    # @param other [Player] jugador al que se quiere copiar
    # @note Al terminar se copian los atributos del jugador **other** al jugador que llama al método
    def copy(other)
      super
      @number=other.number
      @weapons=other.weapons
      @shields=other.shields
      @consecutive_hits=other.consecutive_hits
    end



    # Resucita al jugador, reiniciando armas, escudos, salud y contador de golpes consecutivos.
    #
    # @return [void]
    def resurrect
      @weapons.clear
      @shields.clear
      @health=@@INITIAL_HEALTH
      reset_hits()
    end


    # Calcula el movimiento que realizará el jugador.
    #
    # Verifica si la dirección solicitada se encuentra en el array de movimientos válidos.
    # Para ello se utiliza el método `.size` para determinar el número de elementos en el arreglo
    # y el método `.include?` para comprobar si la dirección está contenida en él.
    # Si hay movimientos válidos pero la dirección solicitada no está incluida,
    # se retorna el primer movimiento válido; de lo contrario, se retorna la dirección original.
    #
    # @param [Object] direction La dirección solicitada.
    # @param [Array] valid_moves Array con las direcciones válidas.
    # @return [Object] La dirección de movimiento elegida.
    def move(direction,valid_moves)

      size=valid_moves.size

      # El método del array es include?(<
      contained=valid_moves.include?(direction)

      if((size>0) && (!contained))
        return valid_moves[0]
      else
        return direction
      end
    end

    # Calcula el valor de ataque del jugador.
    #
    # Suma la fuerza base del jugador y la potencia acumulada de todas sus armas.
    #
    # @return [Numeric] El valor total del ataque.
    def attack
      return (@strength+sum_weapons())
    end

    # Permite que el jugador se defienda ante un ataque recibido.
    #
    # Se calcula la energía defensiva combinando la inteligencia y la protección de los escudos,
    # y se administra el golpe recibido.
    #
    # @param [Numeric] received_attack El valor del ataque recibido.
    # @return [Boolean] true si el jugador pierde como consecuencia del ataque, false de lo contrario.
    def defend(received_attack)
      return manage_hit(received_attack)
    end


    # Retorna una representación en cadena de los atributos del jugador.
    #
    # La cadena incluye:
    # - Nombre, inteligencia, fuerza, salud y posición.
    # - Información de las armas y escudos del jugador.
    #
    # @return [String] La representación textual del jugador.
    def to_s

      # Guardamos la info de todas las armas en un string
      to_weapons="["
      tam_weapons=@weapons.size
      # Se incluye 0 y tam_weapons-1
      for i in 0..(tam_weapons-1) do
          to_weapons+=@weapons[i].to_s

          # NO hace la parte de delante si i == (tam_weapons-1)
          to_weapons += ", " unless i == (tam_weapons - 1)
      end
      to_weapons+="]"

      # Guardamos la info de todas las armas en un string
      to_shields="["
      tam_shields=@shields.size
      # Se incluye 0 y tam_shields-1
      for i in 0..(tam_shields-1) do
          to_shields+=@shields[i].to_s

          # NO hace la parte de delante si i == (tam_weapons-1)
          to_shields += ", " unless i == (tam_shields - 1)
      end
      to_shields+="]"

      return super + " [ ch:#{@consecutive_hits}, w:"+to_weapons+", sh:"+to_shields + " ]"
    end


    # Asigna recompensas al jugador.
    #
    # Se otorgan recompensas en forma de armas, escudos y salud adicional,
    # con valores obtenidos a través de métodos de Dice.
    #
    # @return [void]
    def receive_reward

      w_reward=Dice.weapons_reward
      s_reward=Dice.shields_reward

      w_reward.times do |i|
        wnew=new_weapon()
        receive_weapon(wnew)
      end

      s_reward.times do |i|
        snew=new_shield()
        receive_shield(snew)
      end

      extra_health=Dice.health_reward

      @health+=extra_health
    end



    private


    # Recibe y almacena una nueva arma en el inventario del jugador.
    #
    # Se recorre el array de armas y, si alguna debe descartarse (según Dice),
    # se elimina del array. Luego, si no se ha alcanzado el número máximo,
    # se añade la nueva arma.
    #
    # @param [Weapon] w La nueva arma a recibir.
    # @return [void]
    def receive_weapon(w)

      @weapons.each do |wi|
        if(wi.discard)
          @weapons.delete(wi)
        end
      end

      if(@weapons.size<@@MAX_WEAPONS)
        @weapons.push(w)
      end

    end


    # Recibe y almacena un nuevo escudo en el inventario del jugador.
    #
    # Se recorre el array de escudos y, si alguno debe descartarse (según Dice),
    # se elimina del array. Luego, si no se ha alcanzado el número máximo,
    # se añade el nuevo escudo.
    #
    # @param [Shield] s El nuevo escudo a recibir.
    # @return [void]
    def receive_shield(s)

      @shields.each do |si|
        if(si.discard)
          @shields.delete(si)
        end
      end

      if(@shields.size<@@MAX_SHIELDS)
        @shields.push(s)
      end
    end


    # Crea una nueva instancia de Weapon utilizando valores obtenidos de Dice.
    #
    # @return [Weapon] Una nueva arma.
    def new_weapon
      return Weapon.new(Dice.weapon_power,Dice.uses_left)
    end


    # Crea una nueva instancia de Shield utilizando valores obtenidos de Dice.
    #
    # @return [Shield] Un nuevo escudo.
    def new_shield
      return Shield.new(Dice.shield_power,Dice.uses_left)
    end 

    protected

    # Calcula la suma de los valores de ataque aportados por todas las armas del jugador.
    #
    # @return [Numeric] La suma total de ataques de las armas.
    def sum_weapons
      sum = 0.0
      @weapons.each do |weapon|
        sum += weapon.attack()
      end
      return sum
    end
    
    # Calcula la suma de la protección aportada por todos los escudos del jugador.
    #
    # @return [Numeric] La suma total de la protección de los escudos.
    def sum_shields
      sum = 0.0
      @shields.each do |shield|
        sum += shield.protect()
      end
      return sum
    end


    # Calcula la energía defensiva total del jugador.
    #
    # Suma la inteligencia base y la protección total de los escudos.
    #
    # @return [Numeric] El valor de la energía defensiva.
    def defensive_energy
      return (@intelligence+sum_shields())
    end

    private

    # Administra un golpe recibido por el jugador, determinando si éste debe perder.
    #
    # Se compara la energía defensiva con el ataque recibido; si la defensa es insuficiente,
    # el jugador recibe daño y se incrementa el contador de golpes consecutivos.
    # Si el número de golpes consecutivos alcanza un límite o la salud llega a 0, se considera
    # que el jugador pierde.
    #
    # @param [Numeric] received_attack El valor del ataque recibido.
    # @return [Boolean] true si el jugador pierde (muere), false en caso contrario.
    def manage_hit(received_attack)

      defense=defensive_energy()
      lose=false

      if(defense<received_attack)
        got_wounded()
        inc_consecutive_hits()
      else
        reset_hits()
      end

      if(@consecutive_hits == @@HITS2LOSE || dead())
        reset_hits()
        lose=true
      end
      return lose
    end


    # Resetea el contador de golpes consecutivos del jugador.
    #
    # @return [void]
    def reset_hits
      @consecutive_hits=0
    end

    # Incrementa el contador de golpes consecutivos recibidos por el jugador.
    #
    # @return [void]
    def inc_consecutive_hits
      @consecutive_hits+=1
    end
    
    public_class_method :new

  end



  
  

  if __FILE__ == $0

    puts "== Creando jugador para pruebas =="
    player = Player.new(1, 10, 8)
    puts "Estado inicial del jugador:"
    puts player.to_s
    puts

    puts "== Prueba del movimiento =="
    valid_moves = [:north, :east, :south]
    puts "Movimientos válidos: #{valid_moves}"
    puts "Intentando mover al jugador a :west (no válido)."
    chosen_direction = player.move(:west, valid_moves)
    puts "Resultado del movimiento: #{chosen_direction}"
    puts

    puts "== Prueba de ataque y defensa =="
    puts "Valor de ataque del jugador (fuerza + armas): #{player.attack}"
    received_attack = 15
    puts "Jugador defiende un ataque de #{received_attack}."
    player_loses = player.defend(received_attack)
    if player_loses
      puts "El jugador ha sido derrotado o ha alcanzado el límite de golpes consecutivos."
    else
      puts "El jugador sobrevivió al ataque."
    end
    puts "Estado del jugador tras el ataque:"
    puts player.to_s
    puts

    puts "== Prueba de recepción de recompensas =="
    puts "Invocando receive_reward ..."
    player.receive_reward
    puts "Estado del jugador después de recibir recompensas:"
    puts player.to_s
    puts

    puts "== Prueba de resurrección =="
    player.resurrect
    puts "Estado del jugador tras resucitar:"
    puts player.to_s
    puts

    # ------------------------------------------------------------------------
 
    #puts "== Pruebas de métodos privados adicionales =="
    # Nota: Estos métodos estaban en la sección privada; asegúrate de comentarla o haberla removido temporalmente.
    #puts "\n>> Probando new_weapon:"
    #weapon = player.new_weapon
    #puts "Resultado de new_weapon: #{weapon}"  # Se asume que Weapon tiene un to_s adecuado.

    #puts "\n>> Probando new_shield:"
    #shield = player.new_shield
    #puts "Resultado de new_shield: #{shield}"    # Se asume que Shield tiene un to_s adecuado.

    #puts "\n>> Probando sum_weapons y sum_shields:"
    # Agregamos manualmente los objetos creados para testear las sumas.
    #player.receive_weapon(weapon)
    #player.receive_shield(shield)
    #puts "Suma total de ataque de armas (sum_weapons): #{player.sum_weapons}"
    #puts "Suma total de protección de escudos (sum_shields): #{player.sum_shields}"

    #puts "\n>> Probando defensive_energy:"
    #puts "Energía defensiva: #{player.defensive_energy}"

    #puts "\n>> Probando got_wounded, inc_consecutive_hits, y reset_hits:"
    #initial_health = player.instance_variable_get(:@health)
    #puts "Salud inicial: #{initial_health}"
    #player.got_wounded
    #puts "Salud tras got_wounded: #{player.instance_variable_get(:@health)}"
    #player.inc_consecutive_hits
    #player.inc_consecutive_hits
    #puts "Golpes consecutivos tras dos inc_consecutive_hits: #{player.instance_variable_get(:@consecutive_hits)}"
    #player.reset_hits
    #puts "Golpes consecutivos tras reset_hits: #{player.instance_variable_get(:@consecutive_hits)}"

    #puts "\n>> Probando manage_hit:"
    # Se simula un ataque, por ejemplo, de 15 puntos.
    #result = player.manage_hit(15)
    #puts "Resultado de manage_hit(15): #{result} (true indica que se perdió el combate)."
    
    #puts "\n== Pruebas completadas. =="
   

  end
end
  
  