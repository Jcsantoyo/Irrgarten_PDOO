module Irrgarten
  class Dice
    @@max_uses = 5
    @@max_intelligence = 10.0
    @@max_strength = 10.0
    @@resurrect_prob = 0.3
    @@weapons_reward = 2
    @@shields_reward = 3
    @@health_reward = 5
    @@max_attack = 3
    @@max_shield = 2
    @@generator = Random.new
  
    def self.random_pos(max)
      @@generator.rand(max)
    end
  
    def self.who_starts(nplayers)
      @@generator.rand(nplayers)
    end
  
    def self.random_intelligence
      @@generator.rand(@@max_intelligence)
    end
  
    def self.random_strength
      @@generator.rand(@@max_strength)
    end
  
    def self.resurrect_player
      @@generator.rand < @@resurrect_prob
    end
  
    def self.weapons_reward
      @@generator.rand(@@weapons_reward + 1)
    end
  
    def self.shields_reward
      @@generator.rand(@@shields_reward + 1)
    end
  
    def self.health_reward
      @@generator.rand(@@health_reward + 1)
    end
  
    def self.weapon_power
      @@generator.rand(@@max_attack)
    end
  
    def self.shield_power
      @@generator.rand(@@max_shield)
    end
  
    def self.uses_left
      @@generator.rand(@@max_uses + 1)
    end
  
    def self.intensity(competence)
      @@generator.rand(competence)
    end
  
    def self.discard_element(uses_left)
      prob = (@@max_uses - uses_left) / @@max_uses.to_f
      @@generator.rand < prob
    end
  end
end
  