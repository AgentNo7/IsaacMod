package powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowerDownPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "PowerDownPower";
    public static final String NAME;//= "力竭";
    public static final String IMG = "images/powers/PowerDownPower.png";
    public static final String[] DESCRIPTIONS;//= new String[]{"每三回合，在回合开始时所有增益状态丢失一层。(机器人部分能力免疫)"};

    private static final List<String> whiteList = Arrays.asList(EchoPower.POWER_ID, CreativeAIPower.POWER_ID, ElectroPower.POWER_ID,
            StaticDischargePower.POWER_ID, BufferPower.POWER_ID,
            GuppysCollarPower.POWER_ID,
            NineLifeCatPower.POWER_ID
    );

    private int debuffTurn = 0;

    public PowerDownPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "PowerDownPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/PowerDownPower.png");
        this.updateDescription();
        this.type = PowerType.DEBUFF;
        this.owner = owner;
    }

    public void atStartOfTurn() {
        ArrayList<AbstractPower> powers = AbstractDungeon.player.powers;
        debuffTurn++;
        if (debuffTurn >= 3) {
            debuffTurn = 0;
            for (AbstractPower power : powers) {
                if (power.type == PowerType.BUFF && !whiteList.contains(power.ID)) {
                    if (power.amount == 0) {
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, power.ID));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, power.ID, 1));
                    }
                }
            }
        }
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
