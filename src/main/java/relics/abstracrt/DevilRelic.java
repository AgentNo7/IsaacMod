package relics.abstracrt;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import mymod.IsaacMod;

public abstract class DevilRelic extends CustomRelic implements DevilInterface{
    public DevilRelic(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);
    }

    public DevilRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
    }

    public DevilRelic(String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(id, imgName, tier, sfx);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        IsaacMod.devilRelics.remove(this.relicId);
        IsaacMod.devilOnlyRelics.remove(this.relicId);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        IsaacMod.relics.remove(this.relicId);
        IsaacMod.devilRelics.remove(this.relicId);
        IsaacMod.devilOnlyRelics.remove(this.relicId);
    }

    public int price;
}
