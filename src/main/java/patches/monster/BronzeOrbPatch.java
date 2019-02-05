package patches.monster;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BronzeOrb;
import helpers.MinionHelper;

public class BronzeOrbPatch {

//    private static final Logger logger = LogManager.getLogger(BronzeOrbPatch.class.getName());

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.monsters.city.BronzeOrb",
            method = "takeTurn"
    )
    public static class TakeTurnPatch {
        @SpireInsertPatch(
                loc = 74
        )
        public static SpireReturn Insert(BronzeOrb bronzeOrb) {
            if (AbstractDungeon.getMonsters().getMonster("BronzeAutomaton") == null) {
                for (AbstractMonster m : MinionHelper.getMinions().monsters) {
                    if (m.id.equals("BronzeAutomaton")) {
//                        logger.info("But Find BronzeAutomaton in PlayerMinion!");
                        AbstractDungeon.actionManager.addToBottom(/*EL:74*/new GainBlockAction(m, bronzeOrb, 12));
                        AbstractDungeon.actionManager.addToBottom(/*EL:80*/new RollMoveAction(bronzeOrb));
                        return SpireReturn.Return(null);
                    }
                }
//                logger.info("Error!Not Find BronzeAutomaton in PlayerMinion!");
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
