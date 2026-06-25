require_relative 'Dice'

module Irrgarten
  # Clase que representa un personaje del laberinto, ya sea monstruo ({Monster}) o jugador ({Player}).
  #
  # @abstract Implementar los métodos {#attack} y {#defend} en las clases hijas.
  #
  # @author Juan Caballero Santoyo
  
  class LabyrinthCharacter

    @@NO_POS=-1


    # Constructor de la clase {LabyrinthCharacter}. Inicializa los atributos de la clase.
    # La posición inicial del personaje es inválida
    #
    # @param name [String] Nombre del personaje
    # @param intelligence [float] Inteligencia del personaje
    # @param strength [float] Fuerza del personaje
    # @param health [float] Salud del personaje
    def initialize(name, intelligence, strength, health)
      @name = name.to_s
      @intelligence = intelligence.to_f
      @strength = strength.to_f
      @health = health.to_f

      @row = @@NO_POS
      @col = @@NO_POS
    end


    public

    # Método que busca asemejarse a un constructor de copia.
    # Copia los atributos de un personaje a otro.
    #
    # @param other [LabyrinthCharacter] personaje al que se le copiarán los atributos
    # @note Al terminar se copian los atributos del personaje **other** al personaje que llama al método
    def copy(other)
      @name = other.name
      @intelligence = other.intelligence
      @strength = other.strength
      @health = other.health
      @row=other.row
      @col=other.col
    end

    # Método que informa sobre si un personaje ha muerto o no.
    # Un personaje ha muerto si su salud es menor o igual que 0
    #
    # @return [boolean] **true** si el personaje ha muerto, **false** en caso contrario
    def dead
      return (@health<=0)
    end

    attr_reader :row
    attr_reader :col

    protected
    attr_reader :name
    attr_reader :intelligence
    attr_reader :strength
    attr_accessor :health

    public

    # Modificador de la posición del personaje
    #
    # @param row [int] fila de la posición del personaje
    # @param col [int] columna de la posición del personaje
    def set_pos(row, col)
      @row=row
      @col=col
    end

    # Método que genera una cadena de caracteres con la información del personaje
    #
    # @return [String] cadena de caracteres con la información del personaje
    def to_s
      # Formato para mostrar los datos flotantes del personaje
      formato='%.6f'

      return "#{@name}[i:#{format(formato,@intelligence)}, s:#{format(formato,@strength)}, "+
      "h:#{format(formato,@health)}, p:(#{@row}, #{@col})]"
    end

    protected
    # Este método decrementa en una unidad el atributo que representa la salud del personaje.
    def got_wounded
      @health-=1
    end

    public
    # Método que realiza un ataque.
    #
    # @abstract Método abstracto. Implementar en las clases hijas.
    # @raise [NotImplementedError] Si se llama en esta clase.
    def attack
      raise NotImplementedError
    end


    # Método que defiende al personaje.
    #
    # @abstract Método abstracto. Implementar en las clases hijas.
    # @raise [NotImplementedError] Si se llama en esta clase.
    def defend
      raise NotImplementedError
    end

    private_class_method :new
  end
end