package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.MinionHelper;

public class BFFS extends CustomRelic {
    public static final String ID = "BFFS";
    public static final String IMG = "images/relics/BFFS.png";
    public static final String DESCRIPTION = "战斗开始时，给所有随从增加5点力量。";


    public BFFS() {
        super("BFFS", new Texture(Gdx.files.internal("images/relics/BFFS.png")), RelicTier.SHOP, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BFFS();
    }

    private boolean firstTurn = false;

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        firstTurn = true;
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (firstTurn) {
            firstTurn = false;
            for (AbstractMonster m : MinionHelper.getMinionMonsters()) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new StrengthPower(m,  5), 5));
            }
            this.flash();
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
    }

    @Override
    public void update() {
        super.update();
    }
}
