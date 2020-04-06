package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.TheBombPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import monsters.abstracrt.AbstractPet;
import powers.TheMonsterBombPower;
import relics.abstracrt.BookSuit;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AnarchistCookbook extends BookSuit {
    public static final String ID = "AnarchistCookbook";
    public static final String IMG = "images/relics/AnarchistCookbook.png";
    public static final String DESCRIPTION = "三充能，随机给角色快爆炸弹buff或给怪物炸弹buff六次，攻击怪物可清除他们的炸弹buff。";

    public AnarchistCookbook() {
        super("AnarchistCookbook", new Texture(Gdx.files.internal("images/relics/AnarchistCookbook.png")), RelicTier.UNCOMMON, LandingSound.CLINK, 3);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new AnarchistCookbook();
    }

    //右键开大
    public void onRightClick() {
        if (isUsable()) {
            if (AbstractDungeon.getMonsters() != null) {
                this.flash();
                List<AbstractCreature> list = new ArrayList<>();
                list.add(AbstractDungeon.player);
                list.addAll(AbstractDungeon.getMonsters().monsters);
                for (int i = 0; i < 6; i++) {
                    int index = Utils.randomCombineRng(list.size(), 1)[0];
                    if (list.get(index) instanceof AbstractMonster && !(list.get(index) instanceof AbstractPet)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(list.get(index), list.get(index), new TheMonsterBombPower(list.get(index), 3, 40)));
                        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(list.get(index), this));
                    } else if (list.get(index) instanceof AbstractPet){
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(list.get(index), list.get(index), new TheBombPower(list.get(index), 3, 40)));
                        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(list.get(index), this));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(list.get(index), list.get(index), new TheBombPower(list.get(index), 3, 40)));
                        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    }
                }
                this.stopPulse();
                show();
                resetCharge();
            }
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        for (AbstractPower power : target.powers) {
            if (power.ID.contains(TheMonsterBombPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, target, power.ID));
                break;
            }
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
