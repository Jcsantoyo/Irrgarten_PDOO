require_relative 'Dice'

module Irrgarten
  class Shield
    def initialize(prote,us)
      @protection=prote
      @uses=us
    end

    def protect
      prot=@protection
      if @uses > 0
        @uses-=1
      else
        prot=0
      end
      return prot
    end

    def to_s
      "W[#{@protection}, #{@uses}]"
    end
  end
  def discard
    return Dice.discard_element(@uses)
  end
end

