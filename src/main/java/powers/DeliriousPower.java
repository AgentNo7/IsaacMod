package powers;

import actions.CreateIntentAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import monsters.Delirium;
import monsters.Hush;
import monsters.LittleHush;
import utils.Invoker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeliriousPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "DeliriousPower";
    public static final String NAME;
    public static final String IMG = "images/powers/DeliriousPower.png";
    public static final String[] DESCRIPTIONS;//= new String[]{"每次攻击增加减伤(1%)，本次只受到", "%的伤害。（最低50%）"};

    private static final int maxBeat = 2;

    private static final int maxTakeTurn = 5;

    private static final int minAmount = 666;
    private static final int maxAmount = 3333;

    private static final int minDamageAmount = 333;

    private static final int maxTimeTakeTurn = 6;

    private int saveMaxAmount = 0;

    private boolean isWeaken = false;

    public AbstractMonster[] monsters = {
            new TheGuardian(),
            new Hexaghost(),
            new BronzeAutomaton(),
            new Champ(),
            new TheCollector(),
            new AwakenedOne(100.0f, 15.0f),
            new TimeEater(),
            new Donu(),
            new Deca(),
            new CorruptHeart(),
            new LittleHush(-50.0F, 30.0F),
            new Hush(-50.0F, 30.0F),
//            new Cultist(-50.0F, 30.0F),
            new GiantHead(),
            new BookOfStabbing(),
            new ShelledParasite(),
            new Taskmaster(-50.0F, 30.0F),
            new JawWorm(-50.0F, 30.0F),
            new SpireGrowth(),
            new Maw(-50.0F, 30.0F),
            new WrithingMass(),
            new SnakePlant(-50.0F, 30.0F),
            new Reptomancer(),
            new Sentry(-50.0F, 30.0F)
    };

    public ArrayList<AbstractMonster> monsterList = new ArrayList<>(Arrays.asList(monsters));

    public DeliriousPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "DeliriousPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/DeliriousPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
//        AbstractDungeon.getCurrRoom().monsters.monsters.get(0).addPower(new DeliriousPower(AbstractDungeon.getCurrRoom().monsters.monsters.get(0), 1));AbstractDungeon.getCurrRoom().monsters.monsters.get(0).currentHealth = 9999;
//        AbstractDungeon.getCurrRoom().monsters.monsters.get(0).addPower(new DeliriousPower(AbstractDungeon.getCurrRoom().monsters.monsters.get(0), 100));AbstractDungeon.player.addPower(new StrengthPower(AbstractDungeon.player, 50));
//        AbstractDungeon.player.addPower(new StrengthPower(AbstractDungeon.player, 20));AbstractDungeon.getCurrRoom().monsters.monsters.get(0).damage(new DamageInfo(AbstractDungeon.getCurrRoom().monsters.monsters.get(0), 1000))

        if (owner instanceof Delirium) {
            isWeaken = ((Delirium) owner).isWeaken();
        }
    }

    //限伤后打几下
    private int count = 0;

    private int takeTurnCount = 0;

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!(owner instanceof AbstractMonster)) {
            return super.onAttacked(info, damageAmount);
        }
        AbstractMonster thisMonster = (AbstractMonster) owner;
        if (thisMonster.hasPower(InvinciblePower.POWER_ID)) {
            AbstractPower power = thisMonster.getPower(InvinciblePower.POWER_ID);
            if (power.amount < 1000) {
                power.amount = 666;
            }
        }
        if (damageAmount >= owner.currentHealth + owner.currentBlock && AbstractDungeon.getCurrRoom().cannotLose) {
            AbstractDungeon.getCurrRoom().cannotLose = false;
        }
        if (damageAmount > owner.currentBlock) {
            if (count < maxBeat - 1) {
                if (count == 0) {
                    if (damageAmount > this.amount) {
                        damageAmount = this.amount;
                    }
                    this.amount -= damageAmount;
                    //限伤后
                    if (this.amount <= 0) {
                        this.amount = 0;
                        count++;
                        this.amount++;
                    }
                } else {
                    count++;
                    this.amount++;
                    damageAmount = 0;
                }
                if (owner instanceof AbstractMonster) {
                    //有来源的反伤类：反伤，势不可挡，电击等等 要大于5伤害才行动 ，但如果是最后一滴血就会行动
                    if (amount <= 1 || (info.owner == AbstractDungeon.player && !(info.type == DamageInfo.DamageType.THORNS && damageAmount < 12))) {
                        takeTurnCount++;
                        if (takeTurnCount >= maxTakeTurn) { // && saveMaxAmount - amount > minDamageAmount
                            takeTurnCount = 0;
                        }
                        if (takeTurnCount >= maxTakeTurn - 1) {
                            AbstractDungeon.actionManager.addToBottom(new TalkAction(owner, Settings.language == Settings.GameLanguage.ZHS ? "要来咯~~" : "I'm coming~~", 1.0f, 2.0f));
                        }
                        if (takeTurnCount == 0) {
                            takeTurn();
                        }
                    }
                }
            } else if (count == maxBeat - 1) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
                int maxHealth = owner.maxHealth;
                int health = owner.currentHealth;
                List<AbstractPower> powers = new ArrayList<>();
                for (AbstractPower power : owner.powers) {
                    if (isWeaken) {
                        powers.add(power);
                    } else if (power.type == PowerType.BUFF || power instanceof PoisonPower) {
                        powers.add(power);
                    }
                }
                AbstractMonster monster = getMonster();
                saveMaxAmount = this.amount = monster.maxHealth;
                if (amount < minAmount) {
                    amount = (amount * 2 < minAmount) ? minAmount : amount * 2;
                    int hpMin = maxHealth / 20;
                    if (amount < hpMin) {
                        amount = hpMin;
                    }
                } else if (amount > maxAmount) {
                    amount = maxAmount;
                }
                count = 0;
                monster.type = AbstractMonster.EnemyType.BOSS;
                this.owner.isDead = true;
                AbstractMonster m = ((AbstractMonster) this.owner);
                Invoker.setField(m, "img", null);
                this.owner = monster;
                AbstractDungeon.getCurrRoom().monsters.monsters.clear();
                AbstractDungeon.getCurrRoom().monsters.add(monster);
                ArrayList<TempMusic> tempMusics = Invoker.getField(CardCrawlGame.music, "tempTrack");
                CardCrawlGame.music.fadeAll();
                assert tempMusics != null;
                int size = tempMusics.size();
                try {
                    monster.usePreBattleAction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (size == tempMusics.size()) {
                    tempMusics.add(Delirium.getTempMusic());
                    CardCrawlGame.music.silenceBGMInstantly();
                } else if (size == tempMusics.size() - 1) {
                    for (int i = 0; i < size; i++) {
                        tempMusics.get(i).silenceInstantly();
                    }
                }
                owner.maxHealth = maxHealth;
                owner.currentHealth = health;
                monster.showHealthBar();
                monster.healthBarUpdatedEvent();
                for (AbstractPower power : powers) {
                    power.owner = owner;
                    if (power instanceof InvinciblePower && power.amount < 2000) {
                        power.amount = 2000;
                        InvinciblePower invinciblePower = ((InvinciblePower) power);
                        Invoker.setField(invinciblePower, "maxAmt", 2000);
                    }
                    if (power instanceof ImpatiencePower) {
                        power = new ImpatiencePower(owner);
                    }
                    owner.addPower(power);
                }
                monster.rollMove();
                AbstractDungeon.actionManager.addToBottom(new CreateIntentAction(monster));
            }
            this.updateDescription();
        }
        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
    }

    private void takeTurn() {
        if (owner instanceof Delirium) {
            return;
        }//            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(200, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        //AbstractDungeon.player.addPower(new BufferPower(AbstractDungeon.player, 999));
        AbstractMonster monster = (AbstractMonster) owner;
        monster.takeTurn();
        AbstractDungeon.actionManager.addToBottom(new CreateIntentAction(monster));
        if (owner.hasPower(ImpatiencePower.POWER_ID)) {
            owner.getPower(ImpatiencePower.POWER_ID).amount = 0;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new DamageSelfPower(owner, 1), 1));
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    private int hushCount = 0;

    private AbstractMonster getMonster() {
        if (monsterList.size() > 0) {
            int rnd = AbstractDungeon.monsterRng.random(0, monsterList.size() - 1);
            if (monsterList.get(rnd) instanceof Hush && hushCount < 4) {
                hushCount++;
                return getMonster();
            }
            return monsterList.remove(rnd);
        }
        monsterList = new ArrayList<>();
        for (AbstractMonster m : monsters) {
            Class c = m.getClass();
            AbstractMonster monster = null;
            for (Constructor con : c.getConstructors()) {
                if (con.getParameterCount() == 0) {
                    try {
                        monster = (AbstractMonster) c.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (con.getParameterCount() == 2) {
                    try {
                        monster = (AbstractMonster) con.newInstance(-50.0F, 30.0F);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                if (monster != null) {
                    monsterList.add(monster);
                }
            }
        }
        return getMonster();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (maxTakeTurn - takeTurnCount) + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + (maxBeat - count) + DESCRIPTIONS[3];
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
