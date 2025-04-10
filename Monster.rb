require_relative 'Dice' #Para attack

module Irrgarten

  # La clase Monster representa a un monstruo en el juego que puede atacar, defenderse y recibir
  # daño durante los combates. Maneja atributos como nombre, inteligencia, fuerza, posición (fila y columna)
  # y salud. Utiliza métodos de la clase Dice para calcular la intensidad de los ataques y defensas.
  #
  # Se definen dos variables de clase:
  # - @@INITIAL_HEALTH: Salud inicial asignada a cada monstruo.
  # - @@NO_POS: Valor por defecto que indica que la posición aún no ha sido asignada.
  #
  # @author Juan Caballero Santoyo

  class Monster

    # Salud inicial asignada a cada monstruo.
    @@INITIAL_HEALTH=5

    # Valor para posición no asignada.
    @@NO_POS=-1


    # Inicializa una nueva instancia de Monster.
    #
    # Convierte el nombre recibido a cadena, y los valores de inteligencia y fuerza a flotantes.
    # Establece la posición inicial utilizando @@NO_POS y asigna la salud inicial.
    #
    # @param [String, #to_s] name El nombre del monstruo.
    # @param [Numeric] intelligence La inteligencia del monstruo, que se utiliza para calcular la defensa.
    # @param [Numeric] strength La fuerza del monstruo, que se utiliza para calcular el ataque.
    def initialize(name,intelligence,strength)
      @name=name.to_s
      @intelligence=intelligence.to_f
      @strength=strength.to_f
      set_pos(@@NO_POS,@@NO_POS)
      @health=@@INITIAL_HEALTH
    end

    # Verifica si el monstruo está muerto.
    #
    # @return [Boolean] Retorna true si la salud del monstruo es 0, false en caso contrario.
    def dead
      return @health==0
    end


    # Realiza un ataque calculando la intensidad basado en la fuerza del monstruo.
    #
    # Utiliza el método Dice.intensity para determinar el valor del ataque.
    #
    # @return [Numeric] El valor del ataque.
    def attack
      return Dice.intensity(@strength)
    end
    

    # Permite que el monstruo se defienda de un ataque recibido.
    #
    # Calcula la energía defensiva utilizando Dice.intensity basado en la inteligencia.
    # Si la energía defensiva es menor que el ataque recibido, el monstruo recibe daño (se reduce la salud).
    # Finalmente, retorna true si el monstruo ha muerto tras el ataque, o false en caso contrario.
    #
    # @param [Numeric] received_attack El valor del ataque recibido.
    # @return [Boolean] true si el monstruo resulta muerto tras defenderse, false de lo contrario.
    def defend(received_attack)
      is_dead=dead

      if(!is_dead)
        defensive_energy=Dice.intensity(@intelligence)

        if(defensive_energy<received_attack)
          got_wounded
          is_dead=dead

        end

      end

      return is_dead
    end


    # Establece la posición del monstruo en el tablero de juego.
    #
    # @param [Integer] row La fila en la que se ubicará el monstruo.
    # @param [Integer] col La columna en la que se ubicará el monstruo.
    def set_pos(row, col)
      @row=row
      @col=col
    end



    # Retorna una representación en cadena de los atributos del monstruo.
    #
    # El formato incluye el nombre, la inteligencia, la fuerza, la vida y la posición actual.
    #
    # @return [String] Una cadena que resume los atributos del monstruo.
    def to_s
      formato = "%.2f"  # Formato para números decimales
      cad_1="#{@name}[i:#{format(formato,@intelligence)}, s:#{format(formato,@strength)}, "+"h:#{format(formato,@health)}, p:(#{@row}, #{@col})]"
    end



    private

    
    # Aplica daño al monstruo reduciendo su salud en 1.
    #
    # Este método es invocado internamente cuando la defensa del monstruo falla en evitar un ataque.
    #
    # @return [void]
    def got_wounded
      @health -= 1
    end

  end



  
  # Bloque de prueba que se ejecuta solo si este archivo es el programa principal
    if __FILE__ == $0
      # 1. Crear una instancia del monstruo
    monster = Monster.new("Pepe", 89, 8)
    
    # 2. Mostrar información inicial del monstruo
    puts "== Datos iniciales del monstruo =="
    puts monster.to_s

    # 3. Probar el ataque
    puts "\n== Probando ataque =="
    attack_value = monster.attack
    puts "El monstruo ataca con intensidad: #{attack_value}"

    # 4. Cambiar la posición del monstruo
    puts "\n== Cambiando posición =="
    monster.set_pos(3, 4)
    puts "Posición actual:"
    puts monster.to_s

    # 5. Probar el método defend
    #    Supongamos que recibe un ataque de valor 10
    puts "\n== Probando defensa ante un ataque de valor 10 =="
    monster_defeated = monster.defend(10)
    puts "¿El monstruo ha muerto tras defenderse?: #{monster_defeated ? 'Sí' : 'No'}"
    puts "Estado del monstruo después de defender:"
    puts monster.to_s

    # 6. Comprobar si el monstruo está muerto
    puts "\n== Verificando si está muerto =="
    puts "¿El monstruo está muerto? #{monster.dead ? 'Sí' : 'No'}"
    end
end


 