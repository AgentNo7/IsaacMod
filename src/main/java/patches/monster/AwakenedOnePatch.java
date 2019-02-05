package patches.monster;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import helpers.MinionHelper;

public class AwakenedOnePatch {

    public static boolean skip = false;

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.monsters.beyond.AwakenedOne",
            method = "usePreBattleAction"
    )
    public static class usePreBattleActionPatch {

        public static void Postfix(AwakenedOne awakenedOne) {
            if (!skip && MinionHelper.getMinions().monsters.contains(awakenedOne)) {
                AbstractDungeon.getCurrRoom().cannotLose = false;
            }
        }
        public static void Prefix(AwakenedOne awakenedOne) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                skip = true;
            } else {
                skip = false;
            }
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.monsters.beyond.AwakenedOne",
            method = "damage"
    )
    public static class damagePatch {

        @SpireInsertPatch(
                loc = 327
        )
        public static void Insert(AwakenedOne awakenedOne, DamageInfo info) {
            if (MinionHelper.getMinions().monsters.contains(awakenedOne)) {
                awakenedOne.halfDead = true;
            }
        }
    }
}
