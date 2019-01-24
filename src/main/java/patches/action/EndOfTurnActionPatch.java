package patches.action;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import patches.player.PlayerAddFieldsPatch;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.actions.GameActionManager",
        method = "callEndOfTurnActions"
)
public class EndOfTurnActionPatch {
    public EndOfTurnActionPatch() {
    }

    public static void Postfix(GameActionManager callEndOfTurnActions) {
        BaseMod.logger.info("----------- Minion Before Attacking --------------");
        ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
            monster.takeTurn();
        });
        ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
            monster.applyEndOfTurnTriggers();
        });
        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            action.update();
        }
        ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
            monster.powers.forEach((power) -> {
                power.atEndOfRound();
            });
        });
    }

}
