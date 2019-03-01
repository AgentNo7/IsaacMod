package monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import helpers.MinionHelper;
import monsters.Intent.Move;
import powers.ConceitedPower;
import powers.MonstroPower;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class Monstro extends CustomMonster {

    public static final String NAME;

    private int base = 7;

    private TrackEntry e;

    private int slot;

    public Monstro(float x, float y) {
        super(NAME, "Monstro", 250, -8.0F, 0.0F, 360.0F, 240.0F, (String) null, x, y);
        this.type = EnemyType.BOSS;
        this.setHp(250);
        this.damage.add(new DamageInfo(this, base));
        this.setMove((byte) Move.DEBUFF.id, Intent.DEBUFF);
        this.loadAnimation("images/monsters/monstro/air.atlas", "images/monsters/monstro/air.json", 0.25F);
        e = this.state.setAnimation(0, "idle", true);
    }

    public Monstro(float x, float y, int slot) {
        super(NAME, "Monstro", 250, -8.0F, 0.0F, 360.0F, 240.0F, (String) null, x, y);
        this.type = EnemyType.BOSS;
        this.setHp(250);
        this.damage.add(new DamageInfo(this, base));
        this.setMove((byte) Move.DEBUFF.id, Intent.DEBUFF);
        this.loadAnimation("images/monsters/monstro/air.atlas", "images/monsters/monstro/air.json", 0.25F);
        e = this.state.setAnimation(0, "idle", true);
        this.slot = slot;
    }

    public Monstro(float x, float y, float scale) {
        super(NAME, "Monstro", 250, -8.0F, 0.0F, 360.0F * scale, 240.0F, (String) null, x, y);
        this.type = EnemyType.BOSS;
        this.setHp(250);
        this.damage.add(new DamageInfo(this, base));
        this.setMove((byte) Move.DEBUFF.id, Intent.DEBUFF);
        this.loadAnimation("images/monsters/monstro/air.atlas", "images/monsters/monstro/air.json", scale);
        e = this.state.setAnimation(0, "idle", true);
    }

    public Monstro(float x, float y, float scale, int slot) {
        super(NAME, "Monstro", 250, -8.0F, 0.0F, 360.0F * scale, 240.0F, (String) null, x, y);
        this.type = EnemyType.BOSS;
        this.setHp(250);
        this.damage.add(new DamageInfo(this, base));
        this.setMove((byte) Move.DEBUFF.id, Intent.DEBUFF);
        this.loadAnimation("images/monsters/monstro/air.atlas", "images/monsters/monstro/air.json", scale);
        e = this.state.setAnimation(0, "idle", true);
        this.slot = slot;
    }

    @Override
    public void takeTurn() {
        Move nextMove = Move.getMove(this.nextMove);
        switch (nextMove) {
            case DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
//                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ConceitedPower(AbstractDungeon.player, 1), 1));
                AbstractDungeon.player.powers.add(new ConceitedPower(AbstractDungeon.player, 1));
                break;
            case BUFF:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
                break;
            case DEFAULT:
            default:
                break;
            case MULATTACK:
                int times = AbstractDungeon.aiRng.random(3, 6);
                damage.clear();
                for (int i = 0; i < times; i++) {
                    int tempDamage = AbstractDungeon.aiRng.random(2, 6);
                    damage.add(new DamageInfo(this, tempDamage));
                    damage.get(i).applyPowers(this, AbstractDungeon.player);
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(i), AbstractGameAction.AttackEffect.BLUNT_LIGHT, false));
                }
                break;
            case UNKNOWN:
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MonstroPower(this, 1), 1));
    }

    private static SpriteBatch sb = new SpriteBatch();

    @Override
    public void update() {
        super.update();
        if (!ConceitedPower.canSee) {
            this.intent = Intent.NONE;
        } else if (myMove == Move.MULATTACK) {
            sb.begin();
            try {
                Field intentDmg = AbstractMonster.class.getDeclaredField("intentDmg");
                Field intentMultiAmt = AbstractMonster.class.getDeclaredField("intentMultiAmt");
                intentDmg.setAccessible(true);
                intentMultiAmt.setAccessible(true);
                intentDmg.setInt(this, 4 + random.nextInt(5));
                intentMultiAmt.setInt(this, 4 + random.nextInt(5));
                Method updateIntentTip = AbstractMonster.class.getDeclaredMethod("updateIntentTip");
                updateIntentTip.setAccessible(true);
                updateIntentTip.invoke(this);
            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1) {
                e1.printStackTrace();
            }
            super.renderDamageRange(sb);
            sb.end();
        }
    }

    @Override
    public void die() {
        super.die();
        ConceitedPower.canSee = true;
    }

    private int debuffTry = 0;

    @Override
    protected void getMove(int i) {
        if ((this.moveHistory.isEmpty() || !AbstractDungeon.player.hasPower(ConceitedPower.POWER_ID)) && debuffTry < 3) {
            boolean minionAdded = false;
            for (AbstractMonster m : MinionHelper.getMinions().monsters) {
                if (m.hasPower(ConceitedPower.POWER_ID)) {
                    minionAdded = true;
                }
            }
            if (!minionAdded) {
                debuffTry++;
                this.setMove((byte) Move.DEBUFF.id, Intent.DEBUFF);
                myMove = Move.DEBUFF;
                return;
            }
        }
        if (lastMove((byte) Move.DEBUFF.id)) {
            if (slot % 2 == 1) {
                this.setMove((byte) Move.MULATTACK.id, Intent.ATTACK, base, base, true);
                myMove = Move.MULATTACK;
            } else {
                this.setMove((byte) Move.UNKNOWN.id, Intent.UNKNOWN);
                myMove = Move.UNKNOWN;
            }
        } else if (lastMove((byte) Move.MULATTACK.id)) {
            this.setMove((byte) Move.UNKNOWN.id, Intent.UNKNOWN);
            myMove = Move.UNKNOWN;
        } else if (lastMove((byte) Move.UNKNOWN.id)) {
            int rnd = AbstractDungeon.aiRng.random(0, 99);
            if (rnd < 20) {
                this.setMove((byte) Move.BUFF.id, Intent.BUFF);
                myMove = Move.BUFF;
            } else {
                this.setMove((byte) Move.MULATTACK.id, Intent.ATTACK, base, base, true);
                myMove = Move.MULATTACK;
            }
        } else if (lastMove((byte) Move.BUFF.id)) {
            this.setMove((byte) Move.MULATTACK.id, Intent.ATTACK, base, base, true);
            myMove = Move.MULATTACK;
        }
    }

    private Move myMove = null;

    private Random random = new Random();

    @Override
    public void createIntent() {
        super.createIntent();
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "萌死戳";
        } else {
            NAME = "Monstro";
        }
    }

}
