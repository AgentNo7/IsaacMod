package relics;

import cards.tempCards.EpicFetusAttack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ChargeableRelic;

public class DoctorsRemote extends ChargeableRelic {
    public static final String ID = "DoctorsRemote";
    public static final String IMG = "images/relics/DoctorsRemote.png";
    public static final String DESCRIPTION = "二充能，每打一个怪物房间加一充能，满充能时右击在手牌中加入一张[史诗攻击+2]。";

    public DoctorsRemote() {
        super("DoctorsRemote", new Texture(Gdx.files.internal("images/relics/DoctorsRemote.png")), RelicTier.UNCOMMON, LandingSound.CLINK, 2);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new DoctorsRemote();
    }

    //右键开大
    public void onRightClick() {
        if (isUsable()) {
            this.flash();
            EpicFetusAttack attack = new EpicFetusAttack();
            attack.upgrade();
            attack.upgrade();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(attack, 1, false));
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.stopPulse();
            show();
            resetCharge();
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
