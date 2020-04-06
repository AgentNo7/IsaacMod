package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.ui.RenderCreaturePatch;
import relics.JudasShadow;

import static patches.ui.SoulHeartPatch.blackHeart;

public class JudasShadowPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "JudasShadowPower";
    public static final String NAME ;
    public static final String IMG = "images/powers/JudasShadowPower.png";
    public static final String[] DESCRIPTIONS ;


    public JudasShadowPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "JudasShadowPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/JudasShadowPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
    }

    private JudasShadow judasShadow;
    public JudasShadowPower(AbstractCreature owner, int bladeAmt, JudasShadow judasShadow) {
        this.name = NAME;
        this.ID = "JudasShadowPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/JudasShadowPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.judasShadow = judasShadow;
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount >= AbstractDungeon.player.currentHealth) {
            AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth);
            AbstractDungeon.player.currentHealth = 0;
            AbstractDungeon.player.heal(1, true);
            blackHeart += 40;
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 15), 15));
            judasShadow.counter = -1;
            RenderCreaturePatch.haveJudas = true;
            return 0;
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
