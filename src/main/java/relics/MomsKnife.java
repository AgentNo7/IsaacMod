package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.SummonHelper;
import monsters.pet.MomsKnifePet;
import relics.abstracrt.DevilRelic;
import utils.Point;
import utils.Utils;

public class MomsKnife extends DevilRelic {
    public static final String ID = "MomsKnife";
    public static final String IMG = "images/relics/MomsKnife.png";
    public static final String DESCRIPTION = "在战斗中获得一个妈刀，妈刀可以每回合左击蓄力打出一次，并对刺中的怪物造成伤害。";

    public MomsKnife() {
        super("MomsKnife", new Texture(Gdx.files.internal("images/relics/MomsKnife.png")), RelicTier.SPECIAL, LandingSound.CLINK);
        this.price = 32;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    private MomsKnifePet momsKnife;

    public AbstractRelic makeCopy() {
        return new MomsKnife();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        Point center = new Point(AbstractDungeon.player.hb.x - 1200, AbstractDungeon.player.hb_y + 70);
        double angle = 0;
        Point point = Utils.getCirclePoint(center, angle, 100);
        MomsKnifePet momsKnife = new MomsKnifePet((float) point.x, (float) point.y, this);
        this.momsKnife = momsKnife;
        SummonHelper.summonMinion(momsKnife);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        lastTime = -1;
        timePass = 0;
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        momsKnife.clickFinish = false;
        lastTime = -1;
        timePass = 0;
        momsKnife.drawX = AbstractDungeon.player.drawX + 70;
        momsKnife.drawY = AbstractDungeon.player.drawY + 30;
    }

    @Override
    public void onVictory() {
        super.onVictory();
        momsKnife = null;
    }

    private int lastTime = -1;

    private int timePass = 0;

    @Override
    public void update() {
        super.update();
        if (momsKnife != null) {
            momsKnife.hideHealthBar();
            if (momsKnife.clickFinish) {
                //单位时间
                int time = (int) (CardCrawlGame.playtime * 100);
                if (lastTime != time) {
                    lastTime = time;
                    int speed = (momsKnife.miliTimeTake - timePass) / 4;
                    if (timePass > momsKnife.miliTimeTake * 2) {
                        momsKnife.clickFinish = false;
                        return;
                    }
                    timePass += 1;
                    momsKnife.drawX += speed;
                    for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                        if (Math.abs(monster.drawX - momsKnife.drawX) < 30) {
                            int damage;
                            if (monster.maxHealth / 100 < 3) {
                                damage = 3;
                            } else if (monster.maxHealth > 5000){
                                damage = 50;
                            } else {
                                damage = monster.maxHealth / 100;
                            }
                            DamageInfo damageInfo = new DamageInfo(monster, damage);
                            damageInfo.applyPowers(momsKnife, monster);
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, damageInfo, AbstractGameAction.AttackEffect.NONE));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
    }


}
