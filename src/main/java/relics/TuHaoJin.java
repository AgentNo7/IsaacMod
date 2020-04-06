package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import relics.abstracrt.ClickableRelic;

public class TuHaoJin extends ClickableRelic {
    public static final String ID = "TuHaoJin";
    public static final String IMG = "images/relics/TuHaoJin.png";
    public static final String DESCRIPTION = "右击扣除 #b1 块钱，对所有敌人造成 #b1 点伤害，每次使用增加 #b1 块金钱消耗和 #b1 点伤害。";

    private int addon = 0;

    public TuHaoJin() {
        super("TuHaoJin", new Texture(Gdx.files.internal("images/relics/TuHaoJin.png")), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new TuHaoJin();
    }

    //右键使用
    public void onRightClick() {
        if (AbstractDungeon.player.gold < 1 + addon) {
            return;
        }
        if (AbstractDungeon.getMonsters() != null) {
            AbstractDungeon.player.loseGold(1 + addon);
            show();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(1 + addon, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            addon++;
        }
    }

    private boolean inBattle = false;

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
    }

    @Override
    public void update() {
        super.update();
    }
}
