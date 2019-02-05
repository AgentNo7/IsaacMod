package patches.relic;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FossilizedHelix;

public class FossilizedHelixPatch {

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.relics.FossilizedHelix",
            method = "atBattleStart"
    )
    public static class atBattleStartPatch {
        public atBattleStartPatch() {
        }

        public static void Replace(FossilizedHelix buffer) {
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.relics.AbstractRelic",
            method = "atPreBattle"
    )
    public static class atPreBattlePatch {
        public atPreBattlePatch() {
        }

        public static void Postfix(AbstractRelic relic) {
            if (relic instanceof FossilizedHelix) {
                relic.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BufferPower(AbstractDungeon.player, 1), 1));
            }
        }
    }

}
