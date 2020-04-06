package patches.mod;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class TestModPatch {

    public static int hpChange = 0;
    public static int preHP = 0;

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "decreaseMaxHealth"
    )
    public static class decreaseMaxHealth {
        public decreaseMaxHealth() {
        }

        public static void Prefix(AbstractCreature _instance, int amount) {
            preHP = _instance.maxHealth;
            hpChange = amount;
        }

        public static void Postfix(AbstractCreature _instance, int amount) {
            if (preHP <= _instance.maxHealth) {
                _instance.maxHealth -= 1;
                if (_instance.maxHealth < 1) {
                    _instance.maxHealth = 1;
                }
                if (_instance.currentHealth > _instance.maxHealth) {
                    _instance.currentHealth = _instance.maxHealth;
                }
            }
        }
    }
}
