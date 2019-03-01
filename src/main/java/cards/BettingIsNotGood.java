package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

import java.util.Random;

public class BettingIsNotGood extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String UPGRADE_DESCRIPTION;
    public static final String ID = "BettingIsNotGood";
    public static final String NAME;// = "赌博是个恶习";
    public static final String DESCRIPTION;//= "打出时随机获得下列一个效果： NL ①掉血 3 滴 ②回血 5 滴 ③对所有敌人造成 !D! 伤害 ④获得 15 金币。 NL 消耗 。";
    public static final String imgUrl = "images/cards/BettingIsNotGood.png";
    private int healNum = 5;
    private int damageNum = 3;
    private int goldNum = 15;

    public BettingIsNotGood() {
        super(ID, NAME, imgUrl, 1, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.exhaust = true;
        this.baseDamage = 20;
        this.isMultiDamage = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        int rnd = new Random().nextInt(4);
        switch (rnd) {
            case 0: {
                AbstractDungeon.player.heal(healNum, true);
                break;
            }
            case 1: {
                p.damage(new DamageInfo(p, damageNum, DamageInfo.DamageType.HP_LOSS));
                break;
            }
            case 2: {
                p.gainGold(goldNum);
                for (int i = 0; i < goldNum; ++i) {
                    AbstractDungeon.effectList.add(new GainPennyEffect(p, this.hb.cX, this.hb.cY, p.hb.cX, p.hb.cY, true));
                }
                break;
            }
            case 3: {
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
                break;
            }
        }
    }

    public AbstractCard makeCopy() {
        return new BettingIsNotGood();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.upgradeDamage(10);
            this.exhaust = false;
            goldNum = 25;
            healNum = 10;
            damageNum = 6;
            this.initializeDescription();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
