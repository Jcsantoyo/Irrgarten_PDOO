require_relative 'Weapon'
require_relative 'Shield'
require_relative 'Directions'
require_relative 'Orientation'
require_relative 'GameCharacter'
require_relative 'GameState'

module Irrgarten
  class Test_P1
    
   
    def self.main

      #Prueba enumerados(OK)

      dir1=Directions::LEFT
      dir2=Directions::RIGHT
      dir3=Directions::UP
      dir4=Directions::DOWN

      puts dir1
      puts dir2
      puts dir3
      puts dir4
    
      or1=Orientation::VERTICAL
      or2=Orientation::HORIZONTAL

      puts or1
      puts or2

      gm1=GameCharacter::PLAYER
      gm2=GameCharacter::MONSTER

      puts gm1
      puts gm2

      puts
      puts
      
      #Prueba clase Weapon sin métodos dice (OK)

      arma1=Weapon.new(4.9, 9)
      arma2=Weapon.new(6,9)
      puts arma1.to_s
      puts arma1.attack
      puts arma1.to_s
      puts arma2.to_s

      puts 
      puts

      #Prueba clase Shield (OK)
      
      esc1=Shield.new(7, 0)
      puts esc1.to_s
      puts esc1.protect
      puts esc1.to_s

    end
  end
end

# Solo invoca Test_P1.main si este archivo es el ejecutado directamente en la consola
if __FILE__ == $0
  Irrgarten::Test_P1.main
end
