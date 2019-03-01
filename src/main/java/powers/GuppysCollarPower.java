package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GuppysCollarPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "GuppysCollarPower";
    public static final String NAME ;
    public static final String IMG = "images/powers/GuppysCollarPower.png";
    public static final String[] DESCRIPTIONS ;


    public GuppysCollarPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "GuppysCollarPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/GuppysCollarPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount >= AbstractDungeon.player.currentHealth) {
            int rnd = AbstractDungeon.aiRng.random(0, 99);
            if (rnd < 60) {
                AbstractDungeon.player.currentHealth = 0;
                AbstractDungeon.player.heal(1, true);
                AbstractDungeon.player.currentHealth = 1;
                flash();
                return 0;
            }
            return damageAmount;
        }
        return damageAmount;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }

}
