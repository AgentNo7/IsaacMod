package monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import monsters.Intent.Move;

import java.util.Iterator;

import static com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction.TextType;

public class LittleHush extends CustomMonster {
    public static final String NAME;
    public static final String MOVE_NAME;
    private boolean isAwake;
    private static String DEBUFF_NAME;

    private int debuffTurnCount;

    private int attackDmg = 40;
    private int multiDmg = 22;
    private int multiple = 3;

    private int idleCount;

    private final int maxIdle = 3;

    private int debuff = -3;

    private TrackEntry e;

    private boolean onceBuff = false;
    private float saveX;
    private float saveY;


    public static String[] DIALOG = {"凹凸...强", "ZZZZZ...", "啊，起床"};

    public LittleHush(float x, float y) {
        super(NAME, "LittleHush", 1000, -8.0F, 10.0F, 660.0F, 240.0F, (String) null, x, y);
        this.setHp(1000);
        saveX = x;
        saveY = y;
        isAwake = false;
        this.type = EnemyType.BOSS;
        debuffTurnCount = 0;
        this.damage.add(new DamageInfo(this, multiDmg));
        this.damage.add(new DamageInfo(this, attackDmg));
        this.damage.add(new DamageInfo(this, attackDmg + 20));
        this.damage.add(new DamageInfo(this, attackDmg + 40));
        this.damage.add(new DamageInfo(this, attackDmg + 60));
        this.setMove((byte) Move.SLEEP.id, Intent.SLEEP);
        this.loadAnimation("images/monsters/LittleHush/skeleton.atlas", "images/monsters/LittleHush/skeleton.json", 1.0F);
        e = this.state.setAnimation(0, "sleep", true);
    }

    private int count = 0;

    @Override
    public void takeTurn() {
        Move nextMove = Move.getMove(this.nextMove);
        switch (nextMove) {
            case DEBUFF:
                this.debuffTurnCount = 0;
                debuff -= 2;
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEBUFF"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                int range = 2;
                if (onceBuff) {
                    range = 1;
                }
                int rnd = AbstractDungeon.aiRng.random(0, range);
                if (rnd == 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, this.debuff), this.debuff));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, Math.abs(this.debuff)), Math.abs(this.debuff)));
                } else if (rnd == 1) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, this.debuff), this.debuff));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, -this.debuff), -this.debuff));

                } else if (rnd == 2) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FocusPower(AbstractDungeon.player, this.debuff), this.debuff));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MalleablePower(this)));
                    onceBuff = true;
                }
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            case DEFAULT:
            default:
                break;
            case ATTACK:
                ++this.debuffTurnCount;
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo) this.damage.get(1 + count), AttackEffect.BLUNT_HEAVY));
                if (count <= 4) {
                    count++;
                }
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            case MULATTACK:
                ++this.debuffTurnCount;
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                for (int i = 0; i < multiple; i++) {
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo) this.damage.get(0), AttackEffect.BLUNT_LIGHT, true));
                }
                ++multiple;
                ++multiple;
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            case ABORT:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, TextType.STUNNED));
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte) Move.DEBUFF.id, Intent.STRONG_DEBUFF, ((DamageInfo) this.damage.get(0)).base));
                break;
            case SLEEP:
                ++this.idleCount;
                if (this.idleCount >= maxIdle) {
                    this.isAwake = true;
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "OPEN"));
                    AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                } else {
                    this.setMove((byte) Move.SLEEP.id, Intent.SLEEP);
                }

                switch (this.idleCount) {
                    case 1:
                        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 0.5F, 2.0F));
                        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                        return;
                    case 2:
                        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 0.5F, 2.0F));
                        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                        return;
                    default:
                        return;
                }
            case AWAKE:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "OPEN"));
                this.setMove(DEBUFF_NAME, (byte) Move.DEBUFF.id, Intent.STRONG_DEBUFF);
                this.createIntent();
                this.isAwake = true;
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
        if (!this.isAwake) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 30));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, 30), 30));
            showHealthBar();
        } else {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");
            showHealthBar();
            e.setTime(e.getEndTime() * MathUtils.random());
            this.setMove(DEBUFF_NAME, (byte) 1, Intent.STRONG_DEBUFF);
        }
    }

    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
//                this.state.setAnimation(0, "Attack", false);
//                this.state.addAnimation(0, "Idle_2", true, 0.0F);
                break;
            case "DEBUFF":
//                this.state.setAnimation(0, "Debuff", false);
//                this.state.addAnimation(0, "Idle_2", true, 0.0F);
                break;
            case "OPEN":
                this.isAwake = true;
                this.updateHitbox(0.0F, -25.0F, 320.0F, 360.0F);
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2], 0.5F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, "Metallicize", 15));
//                e.setTime(e.getEndTime() * MathUtils.random());
                CardCrawlGame.music.unsilenceBGM();
                AbstractDungeon.scene.fadeOutAmbiance();
                AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");
                this.state.setAnimation(0, "awake", false);
                this.state.addAnimation(0, "fly", true, 0.0F);
                break;
        }
    }

    private boolean isFly = false;

    private boolean canNotLose = true;

    @Override
    public void update() {
        super.update();
        if (this.currentHealth <= 0 && canNotLose) {
            canNotLose = false;
            if (this.hasPower(CorpseExplosionPower.POWER_ID)) {
                AbstractPower power = this.getPower(CorpseExplosionPower.POWER_ID);
                power.amount = 1;
                super.update();
            }
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new Hush(this.saveX, this.saveY), false));
            this.currentBlock = 0;
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
        }
        if (!isFly) {
            if ("fly".equals(this.state.getCurrent(0).getAnimation().getName())) {
                isFly = true;
                this.loadAnimation("images/monsters/LittleHush/skeleton.atlas", "images/monsters/LittleHush/skeleton.json", 1.0F);
                this.state.setAnimation(0, "fly", true);
            }
        }
    }

    @Override
    public void damage(DamageInfo info) {
        int previousHealth = this.currentHealth;
        super.damage(info);
//        AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new Hush(this.saveX, this.saveY), false));
        if (this.currentHealth != previousHealth && !this.isAwake) {
            this.setMove((byte) Move.ABORT.id, Intent.STUN);
            this.createIntent();
            this.isAwake = true;
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "OPEN"));
        }
        if (this.currentHealth <= 0) {
            AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(this, 1.0F, 0.1F));
            this.canDie = true;
            AbstractDungeon.actionManager.addToTop(new ClearCardQueueAction());
            Iterator s = this.powers.iterator();
            AbstractPower p;
            while (s.hasNext()) {
                p = (AbstractPower) s.next();
                p.onDeath();
            }
            s = AbstractDungeon.player.relics.iterator();
            while (s.hasNext()) {
                AbstractRelic r = (AbstractRelic) s.next();
                r.onMonsterDeath(this);
            }
        }
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            update();
            if (this.hasPower(CorpseExplosionPower.POWER_ID)) {
                AbstractPower power = this.getPower(CorpseExplosionPower.POWER_ID);
                power.amount = 1;
                this.update();
            }
            super.die();
            Iterator it = AbstractDungeon.actionManager.actions.iterator();

            AbstractGameAction a;
            do {
                if (!it.hasNext()) {
                    if (this.currentHealth <= 0) {
                        this.useFastShakeAnimation(5.0F);
                        CardCrawlGame.screenShake.rumble(4.0F);
                        this.onBossVictoryLogic();
                    }
                    return;
                }

                a = (AbstractGameAction) it.next();
            } while (!(a instanceof SpawnMonsterAction));
        }
    }

    private boolean canDie = false;

    @Override
    protected void getMove(int i) {
        if (this.isAwake) {
            if (lastMove((byte) Move.MULATTACK.id)) {
                this.setMove(DEBUFF_NAME, (byte) Move.DEBUFF.id, Intent.STRONG_DEBUFF);
            } else if (lastMove((byte) Move.ATTACK.id)) {
                this.setMove(MOVE_NAME, (byte) Move.MULATTACK.id, Intent.ATTACK, ((DamageInfo) this.damage.get(1)).base, multiple, true);
            } else if (lastMove((byte) Move.DEBUFF.id)) {
                this.setMove((byte) Move.ATTACK.id, Intent.ATTACK_BUFF, ((DamageInfo) this.damage.get(0)).base);
            } else if (lastMove((byte) Move.SLEEP.id)) {
                this.setMove(DEBUFF_NAME, (byte) Move.DEBUFF.id, Intent.STRONG_DEBUFF);
            }
        } else {
            this.setMove((byte) Move.SLEEP.id, Intent.SLEEP);
        }
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "？？？（变种）";
            MOVE_NAME = "多段连斩";
            DEBUFF_NAME = "混乱虹吸";
        } else {
            NAME = "LittleHush";
            MOVE_NAME = "multi-attack";
            DEBUFF_NAME = "Chaos Absorb";
        }
    }
}

