package patches.card;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import relics.Incubus;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.cards.AbstractCard",
        method = "hasEnoughEnergy",
        paramtypez = {}
)
public class IncubusCardPatch {
    @SpireInsertPatch(
            loc = 940
    )
    public static SpireReturn<Boolean> Insert(Object __obj_instance) {
        //反射私有方法
        if (AbstractDungeon.actionManager.turnHasEnded && AbstractDungeon.player.hasRelic(Incubus.ID)) {
            return SpireReturn.Return(true);
        }
        return SpireReturn.Continue();
    }

}
