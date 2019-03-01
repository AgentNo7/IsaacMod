package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.SummonHelper;
import monsters.pet.Charger;
import relics.abstracrt.DevilRelic;
import utils.Point;
import utils.Utils;

public class MyShadow extends DevilRelic {
    public static final String ID = "MyShadow";
    public static final String IMG = "images/relics/MyShadow.png";
    public static final String DESCRIPTION = "每当你失去生命时，生成一只冲锋怪。";

    public MyShadow() {
        super("MyShadow", new Texture(Gdx.files.internal("images/relics/MyShadow.png")), RelicTier.SPECIAL, LandingSound.CLINK);
        price = 12;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new MyShadow();
    }

    @Override
    public void onLoseHp(int damageAmount) {
        super.onLoseHp(damageAmount);
        for (int i = 0; i < Charger.ChargerAmount; i++) {
            if (!Charger.ChargerAlive[i]) {
                Charger.ChargerAlive[i] = true;
                Charger charger = new Charger(0, 0);
                Point center = new Point(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY);
                double angle = i * 2 * Math.PI / Charger.ChargerAmount;
                Point point = Utils.getCirclePoint(center, angle, 125);
                charger.drawX = (float) point.x;
                charger.drawY = (float) point.y;
                charger.index = i;
                SummonHelper.summonMinion(charger);
                break;
            }
        }
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        for (int i = 0; i < Charger.ChargerAmount; i++) {
            Charger.ChargerAlive[i] = false;
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
    }
}
