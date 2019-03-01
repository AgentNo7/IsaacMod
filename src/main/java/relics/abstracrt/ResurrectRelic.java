package relics.abstracrt;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import mymod.IsaacMod;
import patches.player.PlayerDamagePatch;


public abstract class ResurrectRelic extends CustomRelic{

    public int price;

    public int priority;

    public ResurrectRelic(String id, Texture texture, RelicTier tier, LandingSound sfx, int priority) {
        super(id, texture, tier, sfx);
        this.priority = priority;
    }

    public ResurrectRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
    }

    public ResurrectRelic(String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(id, imgName, tier, sfx);
    }

    public abstract int onResurrect();

    public abstract boolean canResurrect();

    @Override
    public void onEquip() {
        super.onEquip();
        PlayerDamagePatch.resurrectRelics.add(this);
        IsaacMod.relics.remove(this.relicId);
        IsaacMod.devilRelics.remove(this.relicId);
        IsaacMod.devilOnlyRelics.remove(this.relicId);
    }

    @Override
    public void update() {
        super.update();
    }
}
