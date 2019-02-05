package patches.monster;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterEscapePatch {

    @SpirePatch(cls = "com.megacrit.cardcrawl.monsters.AbstractMonster", method = "escape")
    public static class EscapePatch {

        @SpireInsertPatch(rloc = 0)
        public static SpireReturn Insert(AbstractMonster monster) {
            if (monster.type == AbstractMonster.EnemyType.BOSS) {
                System.out.println("不会逃的！！！");
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

    }
}
