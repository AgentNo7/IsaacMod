package powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import relics.NineLifeCat;

public class NineLifeCatPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "NineLifeCatPower";
    public static final String NAME ;//= "九命猫buff";
    public static final String IMG = "images/powers/NineLifeCatPower.png";
    public static final String[] DESCRIPTIONS ;//= new String[]{"九命猫：“喵~你还有", "条命。”"};

    NineLifeCat nineLifeCat;

    public NineLifeCatPower(AbstractCreature owner, int bladeAmt, NineLifeCat nineLifeCat) {
        this.name = NAME;
        this.ID = "NineLifeCatPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/NineLifeCatPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.nineLifeCat = nineLifeCat;
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount >= AbstractDungeon.player.currentHealth) {
            AbstractDungeon.player.currentHealth = 0;
            AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth, true);
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            nineLifeCat.counter--;
            NineLifeCat.lives--;
            flash();
            updateDescription();
            return 0;
        }
        return damageAmount;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + NineLifeCat.lives + DESCRIPTIONS[1];
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }

}
