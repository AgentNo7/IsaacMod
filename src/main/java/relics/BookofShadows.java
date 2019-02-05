package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.BookSuit;

public class BookofShadows extends BookSuit {
    public static final String ID = "BookofShadows";
    public static final String IMG = "images/relics/BookofShadows.png";
    public static final String DESCRIPTION = "三充能，满充能时右击使怪物在两个回合内不能对你造成伤害。";

    private final int avaTurn = 2;

    private int turn = 0;

    private int usedTurn = -1;

    public BookofShadows() {
        super("BookofShadows", new Texture(Gdx.files.internal("images/relics/BookofShadows.png")), RelicTier.UNCOMMON, LandingSound.CLINK, 3);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BookofShadows();
    }

    //右键开大
    protected void onRightClick() {
        if (counter >= maxCharge) {
            usedTurn = turn;
            this.flash();
            counter = 0;
            show();
            this.stopPulse();
        }
    }

    @Override
    public void atBattleStart() {
        this.turn = 0;
        this.usedTurn = -1;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (this.turn < this.usedTurn + avaTurn && info.type != DamageInfo.DamageType.HP_LOSS) {
            show();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            return super.onAttacked(info, 0);
        }
        return damageAmount;
    }

    @Override
    public void atTurnStart() {
        this.turn++;
        if (counter >= maxCharge) {
            beginLongPulse();
        } else {
            stopPulse();
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
    }

    @Override
    public void onVictory() {
        super.onVictory();
    }

    @Override
    public void update() {
        super.update();
    }
}
