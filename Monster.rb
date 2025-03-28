require_relative 'Dice' #Para attack

module Irrgarten

  class Monster

    @@INITIAL_HEALTH=5
    @@NO_POS=-1

    def initialize(name,intelligence,strength)
      @name=name
      @intelligence=intelligence
      @strength=strength
      set_pos(@@NO_POS,@@NO_POS)
      @health=@@INITIAL_HEALTH
    end

    def dead
      return @health==0
    end

    def attack
      return Dice.intensity(@strength)
    end
    
    def defend
    end

    def set_pos(row, col)
      @row=row
      @col=col
    end

    def to_s
      nombre= "Nombre: "+@name;
      inteligencia= "Inteligencia: "+ "#{@intelligence}";
      fuerza= "Fuerza: " + "#{@strength}";
      vida= "Vida: " + "#{@health}";
      posicion = "Casilla: (#{@row}, #{@col})"
      
      return (nombre+"\n"+inteligencia+"\n"+fuerza+"\n"+vida+"\n"+posicion);
    end
     
    def got_wounded
      @health-=1
    end

  end



  
  # Bloque de prueba que se ejecuta solo si este archivo es el programa principal
    if __FILE__ == $0
      # Crear una instancia del monstruo
      monster = Monster.new("Pepe", 89, 8)
      
      # Mostrar información inicial del monstruo
      puts "Datos iniciales del monstruo:"
      puts monster.to_s

      # Probar el ataque
      puts "\nEl monstruo ataca y produce un ataque de intensidad: #{monster.attack}"

      # Simular que el monstruo recibe daño
      monster.got_wounded
      puts "\nDespués de recibir daño:"
      puts monster.to_s

      # Comprobar si el monstruo está muerto
      puts "\n¿El monstruo está muerto? #{monster.dead ? "Sí" : "No"}"
    end
end


 