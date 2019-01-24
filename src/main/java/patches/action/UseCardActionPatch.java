package patches.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helpers.BasePlayerMinionHelper;

@SpirePatch(cls = "com.megacrit.cardcrawl.actions.utility.UseCardAction", method = "<ctor>", paramtypez = {AbstractCard.class, AbstractCreature.class})
public class UseCardActionPatch {
    @SpirePostfixPatch
    public static void Postfix(final UseCardAction action, final AbstractCard card, final AbstractCreature target) {
        for (final AbstractMonster m : BasePlayerMinionHelper.getMinions(AbstractDungeon.player).monsters) {
            for (final AbstractPower p : m.powers) {
                if (!card.dontTriggerOnUseCard) {
                    p.onUseCard(card, action);
                }
            }
        }
    }
}
