package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.DevilInterface;
import relics.abstracrt.DevilRelic;

public class RottenBaby extends DevilRelic implements DevilInterface {
    public static final String ID = "RottenBaby";
    public static final String IMG = "images/relics/RottenBaby.png";
    public static final String DESCRIPTION = "每回合开始生成一只苍蝇。";


    public RottenBaby() {
        super("RottenBaby", new Texture(Gdx.files.internal("images/relics/RottenBaby.png")), RelicTier.SPECIAL, LandingSound.CLINK);
        this.price = 16;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new RottenBaby();
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        HushsDoor.spawnFly();
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
