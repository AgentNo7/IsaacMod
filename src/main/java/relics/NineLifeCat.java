package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.DevilInterface;
import relics.abstracrt.ResurrectRelic;

public class NineLifeCat extends ResurrectRelic implements DevilInterface{
    public static final String ID = "NineLifeCat";
    public static final String IMG = "images/relics/NineLifeCat.png";
    public static final String DESCRIPTION = "拾取时减少 #b75% 最大生命值，获得 #b9 条命，复活时回复全部生命。";

    private static final int PRIORITY = 9;

    public void show() {
        this.flash();
    }

    public static int lives = 9;

    public NineLifeCat() {
        super("NineLifeCat", new Texture(Gdx.files.internal("images/relics/NineLifeCat.png")), RelicTier.SPECIAL, LandingSound.CLINK, PRIORITY);
        this.counter = 9;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new NineLifeCat();
    }

    @Override
    public int onResurrect() {
        counter--;
        return AbstractDungeon.player.maxHealth;
    }

    @Override
    public boolean canResurrect() {
        return counter > 0;
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth * 75 / 100);
        if (AbstractDungeon.player.getRelic(this.relicId) == this) {
            HushsDoor.guppyCount++;
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
