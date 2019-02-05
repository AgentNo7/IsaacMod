package patches.relic;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;

public class CircletPatch {

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.relics.Circlet",
            method = "onEquip"
    )
    public static class onEquipPatch {
        public onEquipPatch() {
        }

        public static void Prefix(Circlet circlet) {
            AbstractDungeon.player.increaseMaxHp(5, true);
        }
    }

    @SpirePatch(
            clz = AbstractRelic.class,
            method = "instantObtain",
            paramtypez = {}
    )
    public static class instantObtainPatch {
        public instantObtainPatch() {
        }

        @SpireInsertPatch(rloc = 3)
        public static void Insert(AbstractRelic relic) {
            if (relic.counter < 24) {
                if (AbstractDungeon.player != null) {
                    AbstractDungeon.player.increaseMaxHp(5, true);
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractRelic.class,
            method = "instantObtain",
            paramtypez = {AbstractPlayer.class, int.class, boolean.class}
    )
    public static class instantObtainPatch2 {
        public instantObtainPatch2() {
        }

        @SpireInsertPatch(rloc = 3)
        public static void Insert(AbstractRelic relic, AbstractPlayer player, int slot, boolean callonEquip) {
            if (relic.relicId.equals(Circlet.ID) && relic.counter < 24) {
                if (player != AbstractDungeon.player && player != null) {
                    try {
                        player.increaseMaxHp(5, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @SpirePatch(
            clz = AbstractRelic.class,
            method = "obtain",
            paramtypez = {}
    )
    public static class obtainPatch {
        public obtainPatch() {
        }

        @SpireInsertPatch(rloc = 3)
        public static void Insert(AbstractRelic relic) {
            if (relic.counter < 24) {
                if (AbstractDungeon.player != null) {
                    AbstractDungeon.player.increaseMaxHp(5, true);
                }
            }
        }
    }
}
