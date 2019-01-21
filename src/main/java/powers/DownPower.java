package powers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.reflect.Field;

public class DownPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "DownPower";
    public static final String NAME;// = "蹲";
    public static final String IMG = "images/powers/DownPower.png";
    public static final String[] DESCRIPTIONS;//= new String[]{"不会受到伤害"};

    private boolean justApplied;

    public DownPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "DownPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/DownPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.priority = 99;
        justApplied = true;
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return damage;
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        fisttime = true;
    }

    private static boolean fisttime = true;

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        try {
            Field img = AbstractMonster.class.getDeclaredField("img");
            img.setAccessible(true);
            img.set(this.owner, new Texture(Gdx.files.internal("images/monsters/Hush.png")));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new UpPower(this.owner, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "DownPower"));
        fisttime = false;
        damageAmount = 0;
        return super.onAttacked(info, damageAmount);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (this.owner.hasPower(UpPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "DownPower"));
        }
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
