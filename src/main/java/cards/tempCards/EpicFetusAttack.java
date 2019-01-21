package cards.tempCards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

import java.util.ArrayList;

public class EpicFetusAttack extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "EpicFetusAttack";
    public static final String NAME;// = "史诗攻击";
    public static final String DESCRIPTION;//= "对敌人造成 !D! 点伤害，对其余敌人造成一半的溅射伤害。可以多次强化。 消耗。 虚无。";

    public EpicFetusAttack() {
        super("EpicFetusAttack", NAME, "images/cards/EpicFetusAttack.png", 0, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.exhaust = true;
        this.isEthereal = true;
        this.baseDamage = 14;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8F));
        }
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        ArrayList<AbstractMonster> monsters = AbstractDungeon.currMapNode.room.monsters.monsters;
        for (AbstractMonster monster : monsters) {
            if (!(monster == m)) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(p, damage / 2, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }

    public AbstractCard makeCopy() {
        return new EpicFetusAttack();
    }

    public void upgrade() {
        this.upgradeDamage(8 + this.timesUpgraded);
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
