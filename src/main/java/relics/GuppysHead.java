package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ChargeableRelic;

import static relics.HushsDoor.spawnFly;

public class GuppysHead extends ChargeableRelic {
    public static final String ID = "GuppysHead";
    public static final String IMG = "images/relics/GuppysHead.png";
    public static final String DESCRIPTION = "一充能，满充能时右击，召唤一只苍蝇。";

    public GuppysHead() {
        super("GuppysHead", new Texture(Gdx.files.internal("images/relics/GuppysHead.png")), RelicTier.UNCOMMON, LandingSound.CLINK, 1);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new GuppysHead();
    }

    //右键开大
    protected void onRightClick() {
        if (counter >= maxCharge) {
            spawnFly();
            counter = 0;
            this.stopPulse();
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

    @Override
    public void onEquip() {
        super.onEquip();
        if (AbstractDungeon.player.getRelic(this.relicId) == this) {
            HushsDoor.guppyCount++;
        }
    }
}
