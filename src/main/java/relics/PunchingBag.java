package relics;

import cards.tempCards.ScapeGoat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.SummonHelper;
import monsters.pet.ScapeGoatPet;
import patches.action.ChangeTargetPatch;
import relics.abstracrt.ClickableRelic;
import utils.Point;
import utils.Utils;

public class PunchingBag extends ClickableRelic {
    public static final String ID = "PunchingBag";
    public static final String IMG = "images/relics/PunchingBag.png";
    public static final String DESCRIPTION = "战斗开始召唤一只棉花怪，右击遗物获得一张可以使怪物转移攻击到棉花怪的卡牌。";

    public PunchingBag() {
        super("PunchingBag", new Texture(Gdx.files.internal("images/relics/PunchingBag.png")), RelicTier.RARE, LandingSound.CLINK);
        counter = 0;
    }

    private ScapeGoatPet scapeGoatPet;

    public static AbstractMonster source = null;

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new PunchingBag();
    }

    private int cardCount = 0;

    //右键使用
    public void onRightClick() {
        if (counter > 0) {
            int cnt = 0;
            if (AbstractDungeon.getMonsters() != null) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDead || !m.isEscaping) {
                        cnt++;
                    }
                }
            }
            if (cardCount < cnt) {
                cardCount++;
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new ScapeGoat(scapeGoatPet), 1, false));
            }
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
        counter = 150;
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (AbstractDungeon.player.hasRelic(PokeGo.ID)) {
            AbstractMonster m = ((PokeGo) AbstractDungeon.player.getRelic(PokeGo.ID)).pet;
            if (m != null && m.currentHealth > 0) {
                return;
            }
        }
        cardCount = 0;
        ChangeTargetPatch.target = null;
        ChangeTargetPatch.source.clear();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        if (counter > 0) {
            Point center = new Point(AbstractDungeon.player.hb.x - 1200, AbstractDungeon.player.hb_y + 170);
            Point point = Utils.getCirclePoint(center, -Math.PI / 12, 400);
            ScapeGoatPet scapeGoatPet = new ScapeGoatPet((float) point.x, (float) point.y, this);
            this.scapeGoatPet = scapeGoatPet;
            SummonHelper.summonMinion(scapeGoatPet);
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
