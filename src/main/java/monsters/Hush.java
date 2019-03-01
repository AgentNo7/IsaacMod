package monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import helpers.MinionHelper;
import monsters.Intent.Move;
import monsters.minions.BlueGaper;
import monsters.minions.HushFly;
import powers.*;
import utils.Point;
import utils.Utils;

public class Hush extends CustomMonster {

    public static final String NAME;

    private int base = 7;

    private int flyAmount = 15;
    private int gaperAmount = 6;

    public boolean[] flyAlive = new boolean[flyAmount];
    public boolean[] gaperAlive = new boolean[gaperAmount];

    private int spawnCount = 0;

    private float saveX;
    private float saveY;

    public Hush(float x, float y) {
        super(NAME, "Hush", 6666, -8.0F, 10.0F, 660.0F, 240.0F, (String) null, x, y);
        this.type = EnemyType.BOSS;
        this.powers.add(new InvinciblePower(this, 2222));
        this.setHp(6666);
        this.damage.add(new DamageInfo(this, base));
        saveX = x;
        saveY = y;
        this.setMove((byte) Move.DEBUFF.id, Intent.DEBUFF);
        this.img = new Texture(Gdx.files.internal("images/monsters/Hush.png"));
    }

    @Override
    public void takeTurn() {
        Move nextMove = Move.getMove(this.nextMove);
        switch (nextMove) {
            case DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEBUFF"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new PowerDownPower(AbstractDungeon.player, 1), 1));
                break;
            case BUFF:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BUFF"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                int rnd = AbstractDungeon.aiRng.random(0, 99);
                if (rnd < 90) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new DownPower(this, 1), 1));
                    this.img = new Texture(Gdx.files.internal("images/monsters/hush_hide.png"));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ShuaLaiPower(this, 1), 1));
                }
                break;
            case DEFAULT:
            default:
                break;
            case MULATTACK:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                for (int i = 0; i < base; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0), AttackEffect.BLUNT_LIGHT, true));
                }
                base += 3;
                damage.get(0).base = base;
                break;
            case UNKNOWN:
                ++spawnCount;
                Point center = new Point(saveX, saveY + 150);
                this.img = new Texture(Gdx.files.internal("images/monsters/hush_sum.png"));
                if (spawnCount % 2 != 0) {
                    for (int i = 0; i < flyAmount; i++) {
                        if (!flyAlive[i]) {
                            double angle = 2 * Math.PI * i / flyAmount;
                            Point point = Utils.getCirclePoint(center, angle, 350);
                            HushFly fly = new HushFly((float) point.x, (float) point.y);
                            fly.source = this;
                            fly.index = i;
                            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(fly, true));
                            flyAlive[i] = true;
                        }
                    }
                } else {
                    for (int i = 0; i < gaperAmount; i++) {
                        if (!gaperAlive[i]) {
                            double angle = 2 * Math.PI * i / gaperAmount;
                            Point point = Utils.getCirclePoint(center, angle, 200);
                            BlueGaper blueGaper = new BlueGaper((float) point.x, (float) point.y);
                            blueGaper.source = this;
                            blueGaper.index = i;
                            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(blueGaper, true));
                            gaperAlive[i] = true;
                        }
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new LonelyPower(this, 1), 1));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

//    public void usePreBattleAction() {
//        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SlowPower(this, 0)));
//        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new DecreaseDamagePower(this, 0)));
//    }

    private boolean addPower = false;

    @Override
    public void update() {
        super.update();
        if (!addPower) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SlowPower(this, 0)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new DecreaseDamagePower(this, 0)));
//            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MyTimeWarpPower(this, 36)));
//            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, 2222)));
            addPower = true;
            if (intent == Intent.DEBUG) {
                intent = Intent.DEBUFF;
            }
        }
    }

    private int debuffTry = 0;

    @Override
    public void die() {
        super.die();

        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDying) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(m.hb.cX, m.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
            }
        }

        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();
        }
    }

    @Override
    protected void getMove(int i) {
        if ((this.moveHistory.isEmpty() || !AbstractDungeon.player.hasPower(PowerDownPower.POWER_ID)) && debuffTry < 3) {
            boolean minionAdded = false;
            for (AbstractMonster m : MinionHelper.getMinions().monsters) {
                if (m.hasPower(PowerDownPower.POWER_ID)) {
                    minionAdded = true;
                }
            }
            if (!minionAdded) {
                debuffTry++;
                this.setMove((byte) Move.DEBUFF.id, Intent.DEBUFF);
                return;
            }
        }//StunMonsterAction
        if (lastMove((byte) Move.MULATTACK.id)) {
            this.setMove((byte) Move.UNKNOWN.id, Intent.UNKNOWN);
        } else if (lastMove((byte) Move.UNKNOWN.id)) {
            int rnd = AbstractDungeon.aiRng.random(0, 99);
            if (rnd < 75) {
                this.setMove((byte) Move.BUFF.id, Intent.BUFF);
            } else {
                this.setMove((byte) Move.MULATTACK.id, Intent.ATTACK, base, base, true);
            }
            this.img = new Texture(Gdx.files.internal("images/monsters/Hush.png"));
        } else if (lastMove((byte) Move.DEBUFF.id) || lastMove((byte) Move.BUFF.id)) {
            this.setMove((byte) Move.MULATTACK.id, Intent.ATTACK, base, base, true);
        } else {
            this.setMove((byte) Move.MULATTACK.id, Intent.ATTACK, base, base, true);
        }
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "凹凸";
        } else {
            NAME = "Hush";
        }
    }

}
