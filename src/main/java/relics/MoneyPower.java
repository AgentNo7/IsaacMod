package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.io.File;

public class MoneyPower extends CustomRelic {
    public static final String ID = "MoneyPower";
    public static final String IMG = "images/relics/MoneyPower.png";
    public static final String DESCRIPTION = "在战斗开始获得 当前金币÷100 点力量。";

    public static boolean obtained = false;

    static {
        File obtained = new File(".data");
        if (obtained.exists()) {
            obtained.delete();
            MoneyPower.obtained = true;
        }
    }

    public MoneyPower() {
        super("MoneyPower", new Texture(Gdx.files.internal("images/relics/MoneyPower.png")), RelicTier.RARE, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new MoneyPower();
    }

    @Override
    public void atBattleStart() {
        this.flash();
        int str = AbstractDungeon.player.gold / 150;
        if (str > 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player,  str), str));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

}
