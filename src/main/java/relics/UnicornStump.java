package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relics.abstracrt.ChargeableRelic;

public class UnicornStump extends ChargeableRelic {
    public static final String ID = "UnicornStump";
    public static final String IMG = "images/relics/UnicornStump.png";
    public static final String DESCRIPTION = "一充能，满充能时右击使怪物本回合不能对你造成伤害，只能在回合开始时使用，使用后本回合无法出牌。";

    public static final Logger logger = LogManager.getLogger(UnicornStump.class);

    private int turn = 0;

    private int usedTurn = -1;

    public UnicornStump() {
        super("UnicornStump", new Texture(Gdx.files.internal("images/relics/UnicornStump.png")), RelicTier.UNCOMMON, LandingSound.CLINK, 1);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new UnicornStump();
    }

    //右键开大
    public void onRightClick() {
        if (isUsable()) {
            if (AbstractDungeon.player.cardsPlayedThisTurn == 0) {
                usedTurn = turn;
                this.flash();
                show();
                resetCharge();
            }
            this.stopPulse();
        }
    }

    public void atBattleStart() {
        this.turn = 0;
        this.usedTurn = -1;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (this.turn == this.usedTurn) {
            show();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            return super.onAttacked(info, 0);
        }
        return damageAmount;
    }

    @Override
    public void atTurnStart() {
        this.turn++;
        if (isUsable()) {
            beginLongPulse();
        } else {
            stopPulse();
        }
    }

    public boolean canPlay(AbstractCard card) {
        if (this.turn == this.usedTurn) {
            card.cantUseMessage = "使用断角的回合无法出牌。";
            return false;
        } else {
            return true;
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
