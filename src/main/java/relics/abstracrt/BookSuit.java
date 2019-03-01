package relics.abstracrt;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import relics.HushsDoor;

public abstract class BookSuit extends ChargeableRelic {

    public BookSuit(String id, Texture texture, RelicTier tier, LandingSound sfx, int maxCharge) {
        super(id, texture, tier, sfx, maxCharge);
    }

    public BookSuit(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);
    }

    public BookSuit(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
    }

    public BookSuit(String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(id, imgName, tier, sfx);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        if (AbstractDungeon.player.getRelic(this.relicId) == this) {
            HushsDoor.bookCount++;
        }
    }

    @Override
    public void onUnequip() {
        super.onUnequip();
//        if (AbstractDungeon.player.getRelic(this.relicId) == this) {
//            HushsDoor.bookCount--;
//        }
    }
}
