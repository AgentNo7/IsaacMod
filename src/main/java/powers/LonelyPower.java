package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class LonelyPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "LonelyPower";
    public static final String NAME;// = "孑然一人";
    public static final String IMG = "images/powers/LonelyPower.png";
    public static final String[] DESCRIPTIONS;//= new String[]{"在回合结束房间内只剩自己时，对自己造成666伤害"};

    private boolean justApplied;

    public LonelyPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "LonelyPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/LonelyPower.png");
        this.updateDescription();
        this.type = PowerType.DEBUFF;
        this.priority = 99;
        this.justApplied = true;
    }

//    public void playApplyPowerSfx() {
//        CardCrawlGame.sound.play("POWER_LonelyPower", 0.05F);
//    }


    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            this.flash();
            ArrayList<AbstractMonster> monsters = AbstractDungeon.currMapNode.room.monsters.monsters;
            boolean isAlone = true;
            for (AbstractMonster monster : monsters) {
                if (!monster.isDead && monster != this.owner) {
                    isAlone = false;
                }
            }
            if (isAlone) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(this.owner, new DamageInfo(this.owner, 666, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
            if (this.amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "LonelyPower"));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, "LonelyPower", 1));
            }
        }
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
