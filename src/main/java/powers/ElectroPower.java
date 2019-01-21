package powers;

import com.megacrit.cardcrawl.cards.blue.Loop;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.AbstractPower;
import monsters.abstracrt.AbstractPet;

import java.util.ArrayList;

public class ElectroPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "Electro";
    public static final String NAME ;//= "电力洞穴";
    public static final String[] DESCRIPTIONS ;//= new String[]{"闪电球将攻击所有敌人，如果只有一个敌人，闪电球将攻击", "次。"};

    public ElectroPower(AbstractCreature owner, int bladeAmt) {
        this.amount = bladeAmt;
        this.name = NAME;
        this.ID = "Electro";
        this.owner = owner;
        this.updateDescription();
        this.loadRegion("mastery");
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isSingle()) {
            for (AbstractOrb orb : AbstractDungeon.player.orbs)
                if (orb instanceof Lightning) {
                    for (int i = 1; i < this.amount; i++) {
                        orb.onStartOfTurn();
                        orb.onEndOfTurn();
                    }
                }
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        if (isSingle()) {
            int cnt = 0;
            if (AbstractDungeon.player.hasPower(Loop.ID)) {
                cnt += AbstractDungeon.player.getPower(Loop.ID).amount;
            }
            for (int i = 0; i < cnt; i++) {
                for (AbstractOrb orb : AbstractDungeon.player.orbs) {
                    if (orb instanceof Lightning) {
                        for (int j = 1; j < this.amount; j++) {
                            orb.onStartOfTurn();
                            orb.onEndOfTurn();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {
        if (isSingle()) {
            if (orb instanceof Lightning) {
                for (int i = 1; i < this.amount; i++) {
                    orb.onEvoke();
                }
            }
        }
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public static boolean isSingle() {
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;
        if (monsters.size() == 1) {
            return true;
        }
        int cnt = 0;
        for (AbstractMonster monster : monsters) {
            if (!(monster.isDying || monster.isDead) && !(monster instanceof AbstractPet)) {
                cnt++;
            }
        }
        return cnt <= 1;
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }

}
