package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ChargeableRelic;

public class Habit extends CustomRelic {
    public static final String ID = "Habit";
    public static final String IMG = "images/relics/Habit.png";
    public static final String DESCRIPTION = "当你受伤时，为你的第一个未充满能的遗物增加一充能。";

    public Habit() {
        super("Habit", new Texture(Gdx.files.internal("images/relics/Habit.png")), RelicTier.SHOP, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Habit();
    }

    @Override
    public void onLoseHp(int amount) {
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof ChargeableRelic) {
                ChargeableRelic chargeableRelic = (ChargeableRelic) relic;
                if (chargeableRelic.counter < chargeableRelic.maxCharge) {
                    chargeableRelic.counter += 1;
                    return;
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
