package patches.monster;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import helpers.MinionHelper;

public class CanNotLosePatch {

    public static boolean skip = false;

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.monsters.beyond.Darkling",
            method = "usePreBattleAction"
    )
    public static class usePreBattleActionPatch {

        public static void Postfix(Darkling darkling) {
            if (!skip && MinionHelper.getMinions().monsters.contains(darkling)) {
                AbstractDungeon.getCurrRoom().cannotLose = false;
            }
        }
        public static void Prefix(Darkling darkling) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                skip = true;
            } else {
                skip = false;
            }
        }
    }
}
