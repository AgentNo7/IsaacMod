package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.SummonHelper;
import monsters.pet.LeechPet;
import relics.abstracrt.DevilRelic;
import utils.Point;
import utils.Utils;

public class Leech extends DevilRelic {
    public static final String ID = "Leech";
    public static final String IMG = "images/relics/Leech.png";
    public static final String DESCRIPTION = "战斗开始召唤一个水蛭，水蛭可以造成伤害并吸取生命。";

    public Leech() {
        super("Leech", new Texture(Gdx.files.internal("images/relics/Leech.png")), RelicTier.RARE, LandingSound.CLINK);
        price = 9;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Leech();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        Point center = new Point(AbstractDungeon.player.hb.x - 1200, AbstractDungeon.player.hb_y + 170);
        double angle = 0;
        Point point = Utils.getCirclePoint(center, angle, 100);
        LeechPet leechPet = new LeechPet((float) point.x, (float) point.y);
        SummonHelper.summonMinion(leechPet);
    }

    @Override
    public void onEquip() {
        super.onEquip();
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
