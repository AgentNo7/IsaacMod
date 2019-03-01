package patches.relic;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.HushsDoor;

public class ObtainLoseRelicPatch {
    @SpirePatch(
            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
            method = "loseRelic"
    )
    public static class loseRelicPatch {
        public loseRelicPatch() {
        }

        public static SpireReturn<Boolean> Prefix(AbstractPlayer player, String targetID) {
            if (HushsDoor.ID.equals(targetID)) {
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }



    @SpirePatch(
            cls = "com.megacrit.cardcrawl.relics.AbstractRelic",
            method = "instantObtain",
            paramtypez = {AbstractPlayer.class, int.class, boolean.class}
    )
    public static class instantObtainPatch {
        public instantObtainPatch() {
        }

        public static SpireReturn Prefix(AbstractRelic relic, AbstractPlayer p, int slot, boolean callOnEquip) {
            if (relic != null && HushsDoor.ID.equals(relic.relicId) && p.hasRelic(HushsDoor.ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.relics.AbstractRelic",
            method = "obtain"
    )
    public static class ObtainPatch {
        public ObtainPatch() {
        }

        public static SpireReturn Prefix(AbstractRelic relic) {
            if (relic != null && HushsDoor.ID.equals(relic.relicId) && AbstractDungeon.player.hasRelic(HushsDoor.ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.relics.AbstractRelic",
            method = "instantObtain",
            paramtypez = {}
    )
    public static class instantObtainPatch2 {
        public instantObtainPatch2() {
        }

        public static SpireReturn Prefix(AbstractRelic relic) {
            if (relic != null && HushsDoor.ID.equals(relic.relicId) && AbstractDungeon.player.hasRelic(HushsDoor.ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.relics.AbstractRelic",
            method = "reorganizeObtain",
            paramtypez = {AbstractPlayer.class, int.class, boolean.class, int.class}
    )
    public static class reorganizeObtainPatch {
        public reorganizeObtainPatch() {
        }

        public static SpireReturn Prefix(AbstractRelic relic, AbstractPlayer p, int slot, boolean callOnEquip, int relicAmount) {
            if (relic != null && HushsDoor.ID.equals(relic.relicId) && p.hasRelic(HushsDoor.ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
