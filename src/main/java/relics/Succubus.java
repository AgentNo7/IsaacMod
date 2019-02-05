package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.SummonHelper;
import monsters.pet.SuccubusPet;
import relics.abstracrt.DevilRelic;
import utils.Point;
import utils.Utils;

public class Succubus extends DevilRelic {
    public static final String ID = "Succubus";
    public static final String IMG = "images/relics/Succubus.png";
    public static final String DESCRIPTION = "战斗开始召唤一个魅魔，魅魔会移动对怪造成伤害或者强化你。";

    public Succubus() {
        super("Succubus", new Texture(Gdx.files.internal("images/relics/Succubus.png")), RelicTier.SPECIAL, LandingSound.CLINK);
        this.price = 15;
    }

    private SuccubusPet Succubus;

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Succubus();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        Point center = new Point(AbstractDungeon.player.hb.x - 1200, AbstractDungeon.player.hb_y + 170);
        double angle = 5 * Math.PI / 6;
        Point point = Utils.getCirclePoint(center, angle, 250);
        SuccubusPet Succubus = new SuccubusPet((float) point.x, (float) point.y, this);
        this.Succubus = Succubus;
        SummonHelper.summonMinion(Succubus);
        damageInfo = new DamageInfo(Succubus, 1);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        turnGo = 0;
        addPower = false;
    }

    @Override
    public void onVictory() {
        super.onVictory();
        Succubus = null;
    }

    private int lastTime = -1;

    private boolean moveL = false;
    private int turnGo = 0;

    private DamageInfo damageInfo = new DamageInfo(null, 1);


    @Override
    public void update() {
        super.update();
        if (Succubus != null) {
            Succubus.hideHealthBar();
            int time = (int) (CardCrawlGame.playtime * 40);
            if (lastTime != time) {
                lastTime = time;
                if (moveL) {
                    Succubus.drawX += -3;
                    if (Succubus.drawX < 50) {
                        moveL = false;
                        turnGo++;
                    }
                } else {
                    Succubus.drawX += 3;
                    if (Succubus.drawX >= Settings.WIDTH - 95) {
                        moveL = true;
                        turnGo++;
                    }
                }
                if (time % 5 == 0) {
                    if (turnGo < 2) {
                        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                            if (!(monster instanceof SuccubusPet) && Math.abs(monster.drawX - Succubus.drawX) < 50) {
                                damageInfo.applyPowers(Succubus, monster);
                                AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, damageInfo, AbstractGameAction.AttackEffect.NONE));
                            }
                        }

                    }
                    if (!addPower && Math.abs(AbstractDungeon.player.drawX - Succubus.drawX) < 50) {
                        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 4), 4));
                        addPower = true;
                    }
                    if (addPower && Math.abs(AbstractDungeon.player.drawX - Succubus.drawX) >= 50) {
                        AbstractDungeon.actionManager.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, StrengthPower.POWER_ID, 4));
                        addPower = false;
                    }
                }
            }
        }
    }

    private boolean addPower = false;

    @Override
    public void onEquip() {
        super.onEquip();
    }
}
