package relics.abstracrt;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class ChargeableRelic extends ClickableRelic {

    public int maxCharge;
    public int price;

    public ChargeableRelic(String id, Texture texture, RelicTier tier, LandingSound sfx, int maxCharge) {
        super(id, texture, tier, sfx);
        this.Rclick = false;
        this.RclickStart = false;
        this.counter = 0;
        this.maxCharge = maxCharge;
    }

    public ChargeableRelic(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);
        this.Rclick = false;
        this.RclickStart = false;
        this.counter = 0;
    }

    public ChargeableRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
        this.Rclick = false;
        this.RclickStart = false;
        this.counter = 0;
    }

    public ChargeableRelic(String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(id, imgName, tier, sfx);
        this.Rclick = false;
        this.RclickStart = false;
        this.counter = 0;
    }

    public void show() {
        AbstractDungeon.player.getRelic(this.relicId).flash();
    }

    protected abstract void onRightClick();

    /**
     * 打完怪物充能+1
     */
    @Override
    public void onVictory() {
        if (counter < maxCharge) {
            counter++;
        }
        if (counter == maxCharge) {
            this.pulse = true;
            this.beginPulse();
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
        this.counter = maxCharge;
    }

    @Override
    public void update() {
        super.update();
    }
}
