package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.DevilInterface;
import relics.abstracrt.ResurrectRelic;

public class GuppysCollar extends ResurrectRelic implements DevilInterface{
    public static final String ID = "GuppysCollar";
    public static final String IMG = "images/relics/GuppysCollar.png";
    public static final String DESCRIPTION = "死亡时有60%的概率一血复活。";

    private static final int PRIORITY = 1;

    public GuppysCollar() {
        super("GuppysCollar", new Texture(Gdx.files.internal("images/relics/GuppysCollar.png")), RelicTier.SPECIAL, LandingSound.CLINK, PRIORITY);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new GuppysCollar();
    }

    @Override
    public int onResurrect() {
        return 1;
    }

    @Override
    public boolean canResurrect() {
        return AbstractDungeon.aiRng.randomBoolean(0.6F);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        if (AbstractDungeon.player.getRelic(this.relicId) == this) {
            HushsDoor.guppyCount++;
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
