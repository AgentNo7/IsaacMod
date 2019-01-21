package patches.monster;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.monsters.AbstractMonster",
        method = "update",
        paramtypez = {}
)
public class AbstractMonsterUpdatePatch {
    public AbstractMonsterUpdatePatch() {
    }

    @SpirePostfixPatch
    public static void Postfix(AbstractMonster monster) {
        if (AbstractMonsterClickField.RclickStart.get(monster) && InputHelper.justReleasedClickRight) {
            if (monster.hb.hovered) {
                AbstractMonsterClickField.Rclick.set(monster, true);
            }
            AbstractMonsterClickField.RclickStart.set(monster, false);
        }
        if ((monster.hb != null) && ((monster.hb.hovered) && (InputHelper.justClickedRight))) {
            AbstractMonsterClickField.RclickStart.set(monster, true);
        }
        if (AbstractMonsterClickField.Rclick.get(monster)) {
            AbstractMonsterClickField.Rclick.set(monster, false);
            //右击动作
            AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(monster));
        }
    }

}
