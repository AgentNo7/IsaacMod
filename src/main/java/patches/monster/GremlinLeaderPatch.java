package patches.monster;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import helpers.MinionHelper;


public class GremlinLeaderPatch {

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.monsters.city.GremlinLeader",
            method = "usePreBattleAction"
    )
    public static class usePreBattleActionPatch{

        public static SpireReturn Prefix(GremlinLeader gremlinLeader) {
            if (MinionHelper.getMinions(AbstractDungeon.player).monsters.contains(gremlinLeader)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }


}
