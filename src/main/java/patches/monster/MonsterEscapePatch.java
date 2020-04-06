package patches.monster;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.DeliriousPower;

public class MonsterEscapePatch {

    @SpirePatch(cls = "com.megacrit.cardcrawl.monsters.AbstractMonster", method = "escape")
    public static class EscapePatch {

        public static SpireReturn Prefix(AbstractMonster monster) {
            if (monster.type == AbstractMonster.EnemyType.BOSS) {
                System.out.println("不会逃的！！！");
                AbstractDungeon.actionManager.addToTop(new TalkAction(monster, (Settings.language == Settings.GameLanguage.ZHS) ? "弱者才会逃跑！" : "I NEVER SHRINK BACK!", 1.0f, 2.0f));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.monsters.AbstractMonster", method = "die", paramtypez = {boolean.class})
    public static class DiePatch {

        public static SpireReturn Prefix(AbstractMonster monster, boolean b) {
            if (monster.hasPower(DeliriousPower.POWER_ID) && AbstractDungeon.player.hasRelic("Hope")) {
                AbstractDungeon.getCurrRoom().cannotLose = false;
                AbstractDungeon.player.loseRelic("Hope");
                monster.currentHealth = (int) (monster.maxHealth * 0.6);
                AbstractDungeon.actionManager.addToTop(new TalkAction(monster, (Settings.language == Settings.GameLanguage.ZHS) ? "败北？不可能！！" : "Die? Not me.", 1.0f, 2.0f));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

//    @SpirePatch(cls = "com.megacrit.cardcrawl.monsters.AbstractMonster", method = "damage", paramtypez = {DamageInfo.class})
//    public static class DamagePatch {
//
//        public static int temp = 0;
//
//        @SpireInsertPatch(rloc = 41, localvars = {"damageAmount"})
//        public static void Insert1(AbstractMonster monster, DamageInfo damageInfo, @ByRef int[] damageAmount) {
//            temp = damageAmount[0];
//        }
//
//        @SpireInsertPatch(rloc = 45, localvars = {"damageAmount"})
//        public static void Insert2(AbstractMonster monster, DamageInfo damageInfo, @ByRef int[] damageAmount) {
//            if (damageAmount[0] > temp) {
//                if (damageAmount[0] > 5) {
//                    damageAmount[0] = 5;
//                } else if (AbstractDungeon.player.hasRelic(Boot.ID)) {
//                    damageAmount[0] = 5;
//                }
//            }
//        }
//    }
}
