package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class Bomb extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "Bomb";
    public static final String NAME;// = "炸弹";
    public static final String DESCRIPTION;// = "对所有敌人造成 40 固定伤害。使用一次后从主牌库移除。不能升级。消耗。";
    public static final String imgUrl = "images/cards/Bomb.png";

    public Bomb() {
        super("Bomb", NAME, imgUrl, 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.exhaust = true;
        this.baseDamage = 40;
        this.isMultiDamage = true;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    private boolean used = false;

    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractMonster> monsters = AbstractDungeon.currMapNode.room.monsters.monsters;
        for (AbstractMonster monster : monsters) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(p, this.baseDamage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        }
        used = true;
    }

    @Override
    public void update() {
        super.update();
        if (used) {
            AbstractDungeon.player.masterDeck.removeCard(this.cardID);
            used = false;
        }
    }

    public AbstractCard makeCopy() {
        return new Bomb();
    }

    public void upgrade() {
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
