package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.BaseSummonHelper;
import monsters.pet.IncubusPet;
import powers.BlankCardPower;
import relics.abstracrt.DevilRelic;
import utils.Point;
import utils.Utils;

public class Incubus extends DevilRelic {
    public static final String ID = "Incubus";
    public static final String IMG = "images/relics/Incubus.png";
    public static final String DESCRIPTION = "战斗开始召唤一个莉莉丝宝宝，莉莉丝宝宝会在回合结束时打出你这回合的最后一张攻击牌。";

    public Incubus() {
        super("Incubus", new Texture(Gdx.files.internal("images/relics/Incubus.png")), RelicTier.SPECIAL, LandingSound.CLINK);
        this.price = 20;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Incubus();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        usedCard = null;
        Point center = new Point(AbstractDungeon.player.hb.x - 1200, AbstractDungeon.player.hb_y + 170);
        double angle = Math.PI / 6;
        Point point = Utils.getCirclePoint(center, angle, 150);
        IncubusPet incubus = new IncubusPet((float) point.x, (float) point.y, this);
//        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(incubus, false));
        BaseSummonHelper.summonMinion(incubus);
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        if (usedCard != null) {
            BlankCardPower.playAgain(usedCard, targetMonster);
        }
    }

    public AbstractCard usedCard;
    public AbstractMonster targetMonster;

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        usedCard = null;
        targetMonster = null;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (c.type == AbstractCard.CardType.ATTACK) {
            usedCard = c;
            targetMonster = m;
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
    }


    @Override
    public void update() {
        super.update();
    }

    @Override
    public void onEquip() {
        super.onEquip();
    }
}
