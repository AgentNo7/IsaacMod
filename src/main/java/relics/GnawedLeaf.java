package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class GnawedLeaf extends CustomRelic {
    public static final String ID = "GnawedLeaf";
    public static final String IMG = "images/relics/GnawedLeaf.png";
    public static final String DESCRIPTION = "连续三回合不出牌后，免疫一切伤害,出牌后解除。";

    private final int maxIdle = 3;

    public GnawedLeaf() {
        super("GnawedLeaf", new Texture(Gdx.files.internal("images/relics/GnawedLeaf.png")), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        counter = 0;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new GnawedLeaf();
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if (counter == maxIdle && AbstractDungeon.player.cardsPlayedThisTurn == 0) {
            super.onLoseHp(0);
        } else {
            super.onLoseHp(damageAmount);
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (counter == maxIdle && AbstractDungeon.player.cardsPlayedThisTurn == 0 && info.type != DamageInfo.DamageType.HP_LOSS) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            return super.onAttacked(info, 0);
        }
        return damageAmount;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        counter = 0;
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        if (AbstractDungeon.player.cardsPlayedThisTurn == 0){
            if (counter < maxIdle) {
                counter++;
            }
        } else {
            counter = 0;
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
