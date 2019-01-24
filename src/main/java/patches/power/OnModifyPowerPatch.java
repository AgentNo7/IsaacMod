package patches.power;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helpers.BasePlayerMinionHelper;

@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "onModifyPower")
public class OnModifyPowerPatch {
    @SpirePostfixPatch
    public static void Postfix() {
        for (final AbstractMonster pet : BasePlayerMinionHelper.getMinions(AbstractDungeon.player).monsters) {
            if (pet != null) {
                pet.applyPowers();
            }
        }
    }
}
