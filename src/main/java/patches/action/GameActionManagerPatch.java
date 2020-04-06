package patches.action;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GameActionManagerPatch {

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class BURNPatch {
        @SpireInsertPatch(rloc = 33)
        public static void Insert(GameActionManager manager) {
            if (manager.cardQueue.get(0).card instanceof Burn) {
                --AbstractDungeon.player.cardsPlayedThisTurn;
            }
        }
    }
}
