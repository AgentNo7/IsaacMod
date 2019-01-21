package powers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.reflect.Field;

public class UpPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "UpPower";
    public static final String NAME;// = "起";
    public static final String IMG = "images/powers/UpPower.png";
    public static final String[] DESCRIPTIONS;//= new String[]{"收到的伤害加成x0%"};

    private boolean justApplied;

    public UpPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "UpPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/UpPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.priority = 99;
        justApplied = true;
    }

    public float atDamageReceive(float damage, DamageType type) {
//        damage = damage * (10 + this.amount) / 10;
        return damage;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount * 5 + DESCRIPTIONS[1];
    }

    private int attackTimes = 0;

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (++attackTimes % 2 == 1) {
            try {
                Field img = AbstractMonster.class.getDeclaredField("img");
                img.setAccessible(true);
                img.set(this.owner, new Texture(Gdx.files.internal("images/monsters/hush_hide.png")));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new DownPower(this.owner, 1), 1));
        } else {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, "DownPower"));
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new UpPower(this.owner, 1), 1));
            damageAmount = 0;
            try {
                Field img = AbstractMonster.class.getDeclaredField("img");
                img.setAccessible(true);
                img.set(this.owner, new Texture(Gdx.files.internal("images/monsters/Hush.png")));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return super.onAttacked(info, damageAmount * (5 * this.amount) / 100);
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "UpPower"));
        }
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "UpPower"));
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
