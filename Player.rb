require_relative 'Weapon' #Para contenedores de Weapon
require_relative 'Shield' #Para contenedores de Shield
require_relative 'Directions' #Para move


module Irrgarten

  # Clase que representa al jugador del juego.
  #
  #
  # @author Juan Caballero Santoyo
 
  class Player

    @@MAX_WEAPONS=2 # Número máximo de armas que puede tener un jugador
    @@MAX_SHIELDS=3 # Número máximo de escudos que puede tener un jugador
    @@INITIAL_HEALTH=10 # Salud inicial de los jugadores
    @@HITS2LOSE=3 # Número de golpes que puede recibir un jugador antes de morir
    @@NO_POS=-1     # Valor para posición no asignada.



    # Inicializa una nueva instancia de Player.
    #
    # @param [Integer] number Número identificador del jugador.
    # @param [Numeric] intelligence Valor de la inteligencia del jugador, que influye en la defensa.
    # @param [Numeric] strength Valor de la fuerza del jugador, que influye en el ataque.
    def initialize(number,intelligence,strength)
      @name= "Persona #{number}"
      @number=number.to_s
      @intelligence=intelligence
      @strength=strength
      @health=@@INITIAL_HEALTH

      @row=@@NO_POS
      @col=@@NO_POS

      @consecutive_hits=0

      @weapons=Array.new
      @shields=Array.new
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

    # Retorna la fila actual del jugador en el tablero.
    #
    # @return [Integer] La fila actual.
    def row
      @row
    end


    # Retorna la columna actual del jugador en el tablero.
    #
    # @return [Integer] La columna actual.
    def col
      @col
    end


    # Retorna el número identificador del jugador en forma de cadena.
    #
    # @return [String] El número del jugador.
    def number
      @number
    end


    # Establece la posición del jugador en el tablero.
    #
    # @param [Integer] row La fila donde se ubicará el jugador.
    # @param [Integer] col La columna donde se ubicará el jugador.
    # @return [void]
    def set_pos(row,col)
      @row=row
      @col=col
    end


    # Determina si el jugador está muerto.
    #
    # @return [Boolean] true si la salud es menor o igual a 0, false en caso contrario.
    def dead
      return @health<=0
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

      formato = "%.2f"  # Formato para números decimales
      cad_1="#{@name}[i:#{format(formato,@intelligence)}, s:#{format(formato,@strength)}, "+"h:#{format(formato,@health)}, p:(#{@row}, #{@col})]"

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

      return cad_1 + " [ ch:#{@consecutive_hits}, w:"+to_weapons+", sh:"+to_shields + " ]"

     
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
        snew=new_weapon()
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


    # Calcula la suma de los valores de ataque aportados por todas las armas del jugador.
    #
    # @return [Numeric] La suma total de ataques de las armas.
    def sum_weapons
      sum = 0.0
      @weapons.each do |weapon|
        sum += weapon.attack
      end
      return sum
    end
    
    # Calcula la suma de la protección aportada por todos los escudos del jugador.
    #
    # @return [Numeric] La suma total de la protección de los escudos.
    def sum_shields
      sum = 0.0
      @shields.each do |shield|
        sum += shield.protect
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


    # Aplica daño al jugador, reduciendo su salud en 1.
    #
    # @return [void]
    def got_wounded
      @health-=1
    end

    # Incrementa el contador de golpes consecutivos recibidos por el jugador.
    #
    # @return [void]
    def inc_consecutive_hits
      @consecutive_hits+=1
    end
    
  end




  
  if __FILE__ == $0
    
    # 1. Crear una instancia del jugador
    puts "== Creando jugador =="
    player = Player.new(1, 10, 8)
    puts "Estado inicial del jugador:"
    puts player.to_s
    puts
    
    # 2. Establecer posición
    puts "== Estableciendo posición =="
    player.set_pos(2, 3)
    puts "Posición establecida a (2,3)"
    puts player.to_s
    puts

    # 3. Mover al jugador
    puts "== Probando movimiento =="
    valid_moves = [:north, :east, :south]
    puts "Movimientos válidos: #{valid_moves}"
    puts "Intentando mover al jugador a :west (no válido)."
    chosen_direction = player.move(:west, valid_moves)
    puts "Resultado del movimiento: #{chosen_direction}"
    puts

    # 4. Atacar y defender
    puts "== Probando ataque y defensa =="
    puts "Valor de ataque del jugador (fuerza + armas): #{player.attack}"
    
    # Vamos a simular que el jugador recibe un ataque de valor 15
    received_attack = 15
    puts "Jugador defiende un ataque de #{received_attack}."
    player_loses = player.defend(received_attack)
    if player_loses
      puts "El jugador ha sido derrotado o ha llegado al límite de golpes consecutivos."
    else
      puts "El jugador sobrevivió al ataque."
    end
    puts "Estado del jugador tras el ataque:"
    puts player.to_s
    puts

    # 5. Recibir recompensas
    puts "== Probando recepción de recompensas =="
    puts "Invocando player.receive_reward ..."
    player.receive_reward
    puts "Estado del jugador después de recibir recompensas:"
    puts player.to_s
    puts

    # 6. Resucitar al jugador
    puts "== Probando resurrección =="
    player.resurrect
    puts "Estado del jugador tras resucitar:"
    puts player.to_s
    puts

    puts "== Pruebas completadas. =="
  
  end
end
  
  