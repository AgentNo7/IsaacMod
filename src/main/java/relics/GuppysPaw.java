package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ClickableRelic;

public class GuppysPaw extends ClickableRelic {
    public static final String ID = "GuppysPaw";
    public static final String IMG = "images/relics/GuppysPaw.png";
    public static final String DESCRIPTION = "右击扣除 10 血上限，并在战斗开始获得 5 层多层护甲。每使用一次，减少一层多层护甲获得。";

    private static int shieldBase = 5;

    public GuppysPaw() {
        super("GuppysPaw", new Texture(Gdx.files.internal("images/relics/GuppysPaw.png")), RelicTier.RARE, LandingSound.CLINK);
        counter = 0;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new GuppysPaw();
    }

    //右键使用
    protected void onRightClick() {
        if (shieldBase > 0 && counter < 15) {
            if (shieldBase == 5) {
                int sum = 0;
                for (int i = 5; i > 0; i--) {
                    if (sum == counter) {
                        shieldBase = i;
                        break;
                    }
                    sum += i;
                }
            }
            counter += shieldBase;
            AbstractDungeon.player.decreaseMaxHealth(10);
            if (AbstractDungeon.getMonsters() != null) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, shieldBase), shieldBase));
            }
            shieldBase--;
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
        shieldBase = 5;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        if (counter > 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, counter), counter));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
