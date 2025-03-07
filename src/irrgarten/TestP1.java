package irrgarten;

import java.util.Arrays;  // para mostrar distribuciones en arrays

public class TestP1 {
    public static void main(String[] args) {
        
        // =========================================================================
        // 1. PRUEBAS DE ENUM (OK)
        // =========================================================================
        Directions dir1 = Directions.LEFT;
        Directions dir2 = Directions.RIGHT;
        Directions dir3 = Directions.UP;
        Directions dir4 = Directions.DOWN;
        
        System.out.println(dir1);
        System.out.println(dir2);
        System.out.println(dir3);
        System.out.println(dir4);
        
        Orientation or1 = Orientation.HORIZONTAL;
        Orientation or2 = Orientation.VERTICAL;
        
        System.out.println(or1);
        System.out.println(or2);
        
        GameCharacter gm1 = GameCharacter.MONSTER;
        GameCharacter gm2 = GameCharacter.PLAYER;
        
        System.out.println(gm1);
        System.out.println(gm2);
        
        System.out.println();
        System.out.println();

        // =========================================================================
        // 2. PRUEBAS DE WEAPON (OK, sin métodos de Dice)
        // =========================================================================
        Weapon arma1 = new Weapon(3,4);
        Weapon arma2 = new Weapon(5,0);

        System.out.println(arma1.attack());
        System.out.println(arma1.toString());
        System.out.println(arma2.attack());
        System.out.println(arma2.toString());
       
        System.out.println();
        System.out.println();
        
        // =========================================================================
        // 3. PRUEBAS DE SHIELD (OK, sin métodos de Dice)
        // =========================================================================
        Shield esc1 = new Shield(6,7);
        Shield esc2 = new Shield(9,0);
        
        System.out.println(esc1.toString());
        System.out.println(esc1.protect());
        System.out.println(esc1.toString());
        System.out.println(esc2.toString());
        System.out.println(esc2.protect());
        System.out.println(esc2.toString());
        
        System.out.println();
        System.out.println();
        
        // =========================================================================
        // 4. PRUEBAS DE GAMESTATE (OK)
        // =========================================================================
        GameState gs1 = new GameState("Hola","Pepe","wech",2,false,"prueba");
        
        System.out.println(gs1.getLabyrinth());
        System.out.println(gs1.getCurrentPlayer());
        System.out.println(gs1.getLog());
        System.out.println(gs1.getMonsters());
        System.out.println(gs1.getPlayers());
        System.out.println(gs1.getWinner());
          
        System.out.println();
        System.out.println();
        
        // =========================================================================
        // 5. PRUEBAS DE DICE (100 iteraciones para cada método) (OK)
        // =========================================================================
        final int NTIMES = 100;
        testRandomPos(NTIMES);
        testWhoStarts(NTIMES);
        testResurrectPlayer(NTIMES);
        testWeaponsReward(NTIMES);
        testShieldsReward(NTIMES);
        testHealthReward(NTIMES);
        testWeaponPower(NTIMES);
        testShieldPower(NTIMES);
        testUsesLeft(NTIMES);
        testIntensity(NTIMES);
        testDiscardElement(NTIMES);
    }

    // -------------------------------------------------------------------------
    // Métodos de prueba para la clase Dice
    // -------------------------------------------------------------------------
    
    private static void testRandomPos(int nTimes) {
        int max = 10;
        int[] distribution = new int[max];
        for (int i = 0; i < nTimes; i++) {
            int r = Dice.randomPos(max);
            // Verificamos rango
            if (r < 0 || r >= max) {
                System.out.println("¡Valor fuera de rango en randomPos!: " + r);
            }
            distribution[r]++;
        }
        System.out.println("== randomPos(" + max + ") (Llamado " + nTimes + " veces) ==");
        System.out.println("Distribución: " + Arrays.toString(distribution));
        System.out.println();
    }

    private static void testWhoStarts(int nTimes) {
        int nplayers = 4;
        int[] distribution = new int[nplayers];
        for (int i = 0; i < nTimes; i++) {
            int w = Dice.whoStarts(nplayers);
            if (w < 0 || w >= nplayers) {
                System.out.println("¡Valor fuera de rango en whoStarts!: " + w);
            }
            distribution[w]++;
        }
        System.out.println("== whoStarts(" + nplayers + ") (Llamado " + nTimes + " veces) ==");
        System.out.println("Distribución: " + Arrays.toString(distribution));
        System.out.println();
    }

    private static void testResurrectPlayer(int nTimes) {
        int countTrue = 0;
        for (int i = 0; i < nTimes; i++) {
            if (Dice.resurrectPlayer()) {
                countTrue++;
            }
        }
        double ratio = countTrue / (double) nTimes;
        System.out.println("== resurrectPlayer() (Llamado " + nTimes + " veces) ==");
        System.out.println("Veces true: " + countTrue + " de " + nTimes + " => Ratio obtenido: " + ratio
                           + " (Esperado ~ 0.3)");
        System.out.println();
    }

    private static void testWeaponsReward(int nTimes) {
        // Rango esperado [0..2]
        int[] distribution = new int[3];
        for (int i = 0; i < nTimes; i++) {
            int w = Dice.weaponsReward();
            if (w < 0 || w > 2) {
                System.out.println("¡Valor fuera de rango en weaponsReward!: " + w);
            }
            distribution[w]++;
        }
        System.out.println("== weaponsReward() (Llamado " + nTimes + " veces) ==");
        System.out.println("Distribución: " + Arrays.toString(distribution));
        System.out.println();
    }

    private static void testShieldsReward(int nTimes) {
        // Rango esperado [0..3]
        int[] distribution = new int[4];
        for (int i = 0; i < nTimes; i++) {
            int s = Dice.shieldsReward();
            if (s < 0 || s > 3) {
                System.out.println("¡Valor fuera de rango en shieldsReward!: " + s);
            }
            distribution[s]++;
        }
        System.out.println("== shieldsReward() (Llamado " + nTimes + " veces) ==");
        System.out.println("Distribución: " + Arrays.toString(distribution));
        System.out.println();
    }

    private static void testHealthReward(int nTimes) {
        // Rango esperado [0..5]
        int[] distribution = new int[6];
        for (int i = 0; i < nTimes; i++) {
            int h = Dice.healthReward();
            if (h < 0 || h > 5) {
                System.out.println("¡Valor fuera de rango en healthReward!: " + h);
            }
            distribution[h]++;
        }
        System.out.println("== healthReward() (Llamado " + nTimes + " veces) ==");
        System.out.println("Distribución: " + Arrays.toString(distribution));
        System.out.println();
    }

    private static void testWeaponPower(int nTimes) {
        float maxAttack = 3.0f; // Esperamos valores en [0..3)
        float min = Float.MAX_VALUE, max = Float.MIN_VALUE, sum = 0;
        for (int i = 0; i < nTimes; i++) {
            float wp = Dice.weaponPower();
            if (wp < 0 || wp >= maxAttack) {
                System.out.println("¡Valor fuera de rango en weaponPower!: " + wp);
            }
            sum += wp;
            if (wp < min) min = wp;
            if (wp > max) max = wp;
        }
        System.out.println("== weaponPower() (Llamado " + nTimes + " veces) ==");
        System.out.println("Mínimo observado: " + min + ", Máximo observado: " + max 
                           + ", Media: " + (sum / nTimes));
        System.out.println();
    }

    private static void testShieldPower(int nTimes) {
        float maxShield = 2.0f; // Esperamos valores en [0..2)
        float min = Float.MAX_VALUE, max = Float.MIN_VALUE, sum = 0;
        for (int i = 0; i < nTimes; i++) {
            float sp = Dice.shieldPower();
            if (sp < 0 || sp >= maxShield) {
                System.out.println("¡Valor fuera de rango en shieldPower!: " + sp);
            }
            sum += sp;
            if (sp < min) min = sp;
            if (sp > max) max = sp;
        }
        System.out.println("== shieldPower() (Llamado " + nTimes + " veces) ==");
        System.out.println("Mínimo observado: " + min + ", Máximo observado: " + max 
                           + ", Media: " + (sum / nTimes));
        System.out.println();
    }

    private static void testUsesLeft(int nTimes) {
        // Esperamos valores en [0..5]
        int[] distribution = new int[6];
        for (int i = 0; i < nTimes; i++) {
            int u = Dice.usesLeft();
            if (u < 0 || u > 5) {
                System.out.println("¡Valor fuera de rango en usesLeft!: " + u);
            }
            distribution[u]++;
        }
        System.out.println("== usesLeft() (Llamado " + nTimes + " veces) ==");
        System.out.println("Distribución: " + Arrays.toString(distribution));
        System.out.println();
    }

    private static void testIntensity(int nTimes) {
        float competence = 5.0f; // probamos con un valor fijo
        float min = Float.MAX_VALUE, max = Float.MIN_VALUE, sum = 0;
        for (int i = 0; i < nTimes; i++) {
            float val = Dice.intensity(competence);
            if (val < 0 || val >= competence) {
                System.out.println("¡Valor fuera de rango en intensity!: " + val);
            }
            sum += val;
            if (val < min) min = val;
            if (val > max) max = val;
        }
        System.out.println("== intensity(" + competence + ") (Llamado " + nTimes + " veces) ==");
        System.out.println("Mínimo observado: " + min + ", Máximo observado: " + max 
                           + ", Media: " + (sum / nTimes));
        System.out.println();
    }

    private static void testDiscardElement(int nTimes) {
        // Ejemplo: usesLeftValue=2 y MAX_USES=5 => prob ~ 0.6
        int usesLeftValue = 2;
        int countTrue = 0;
        for (int i = 0; i < nTimes; i++) {
            if (Dice.discardElement(usesLeftValue)) {
                countTrue++;
            }
        }
        double ratio = countTrue / (double) nTimes;
        double expected = (5.0 - usesLeftValue) / 5.0; // 0.6 con usesLeftValue=2
        System.out.println("== discardElement(" + usesLeftValue + ") (Llamado " + nTimes + " veces) ==");
        System.out.println("Veces true: " + countTrue + " de " + nTimes
                + " => Ratio obtenido: " + ratio 
                + " (Esperado aprox: " + expected + ")");
        System.out.println();
    }
}
