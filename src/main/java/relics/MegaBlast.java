package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import relics.abstracrt.ChargeableRelic;
import relics.abstracrt.DevilInterface;

import java.util.Iterator;

public class MegaBlast extends ChargeableRelic implements DevilInterface {
    public static final String ID = "MegaBlast";
    public static final String IMG = "images/relics/MegaBlast.png";
    public static final String DESCRIPTION = "十二充能，满充能时右击获得20秒涅奥的悲恸效果。";

    public MegaBlast() {
        super("MegaBlast", new Texture(Gdx.files.internal("images/relics/MegaBlast.png")), RelicTier.SPECIAL, LandingSound.CLINK, 12);
        this.price = 40;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new MegaBlast();
    }

    private int timeEnd = -1;

    private boolean open = false;

    private int remember = 0;

    //右键开roll
    protected void onRightClick() {
        if (!open && counter >= maxCharge) {
            remember = 0;
            counter = 0;
            timeEnd = (int) CardCrawlGame.playtime + 20;
            this.pulse = false;
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
                neowsLament();
            }
        }
    }

    @Override
    public void onVictory() {
        if (!open) {
            if (counter < maxCharge) {
                counter++;
            }
            if (counter == maxCharge) {
                this.pulse = true;
                this.beginPulse();
            }
        } else {
            remember++;
        }
    }

    @Override
    public void update() {
        int time = (int) CardCrawlGame.playtime;
        if (time < timeEnd) {
            open = true;
            counter = timeEnd - time;
        } else if (time == timeEnd) {
            open = true;
            counter = remember;
        } else {
            open = false;
        }
        super.update();
    }

    private void neowsLament() {
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while (var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            m.currentHealth = 1;
            m.healthBarUpdatedEvent();
        }

        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (open) {
            neowsLament();
        }
    }
}
