package powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

public class FlightPower extends AbstractPower {
    public static final String POWER_ID = "Flight";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private int storedAmount;

    private List<AbstractMonster> pets = new ArrayList<>();

    public FlightPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = "Flight";
        this.owner = owner;
        this.amount = amount;
        this.storedAmount = amount;
        this.updateDescription();
        this.loadRegion("flight");
        this.priority = 50;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_FLIGHT", 0.05F);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void atStartOfTurn() {
        this.amount = this.storedAmount;
        this.updateDescription();
    }

    public float atDamageFinalReceive(float damage, DamageType type) {
        return this.calculateDamageTakenAmount(damage, type);
    }

    private float calculateDamageTakenAmount(float damage, DamageType type) {
        return type != DamageType.HP_LOSS && type != DamageType.THORNS ? damage / 2.0F : damage;
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        Boolean willLive = this.calculateDamageTakenAmount((float) damageAmount, info.type) < (float) this.owner.currentHealth;
        if (info.owner != null && info.type != DamageType.HP_LOSS && info.type != DamageType.THORNS && damageAmount > 0 && willLive) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, "Flight", 1));
        }

        return damageAmount;
    }

//    @Override
//    public void update(int slot) {
//        ArrayList<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;
//        for (AbstractMonster monster : monsters) {
//            if (monster instanceof AbstractPet && !pets.contains(monster)) {
//                pets.add(monster);
//            }
//        }
//    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("Flight");
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
