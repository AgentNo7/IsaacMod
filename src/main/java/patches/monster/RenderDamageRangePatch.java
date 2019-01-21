package patches.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.monsters.AbstractMonster",
        method = "renderDamageRange",
        paramtypez = {SpriteBatch.class}
)
public class RenderDamageRangePatch {
    public RenderDamageRangePatch() {
    }

    @SpirePostfixPatch
    public static void Postfix(AbstractMonster monster, SpriteBatch sb) {
//


    }
}
