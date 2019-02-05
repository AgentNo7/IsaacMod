package cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class BloodyBrimstone extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String UPGRADE_DESCRIPTION;
    public static final String ID = "BloodyBrimstone";
    public static final String NAME;// = "鲜血硫磺火";
    public static final String DESCRIPTION;// = "扣除3点血上限，对所有敌人分别造成他们 !M! %血上限的真实伤害(最低30)。 NL 消耗 。";
    public static final String imgUrl = "images/cards/BloodyBrimstone.png";

    public BloodyBrimstone() {
        super("BloodyBrimstone", NAME, imgUrl, 1, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.exhaust = true;
        this.baseMagicNumber = 25;
        this.isMultiDamage = true;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int min = 30;

    public void use(AbstractPlayer p, AbstractMonster m) {
        int hp_before = p.currentHealth;
        //动画开始
        SpriteBatch sb = new SpriteBatch();
        sb.begin();
        p.currentHealth = p.maxHealth * 10;
        p.healthBarRevivedEvent();
        p.showHealthBar();
        p.renderHealth(sb);
        sb.end();
        sleep(200);
        //动画结束
        p.currentHealth = hp_before;
        ArrayList<AbstractMonster> monsters = AbstractDungeon.currMapNode.room.monsters.monsters;
        if (!this.upgraded) {
            for (AbstractMonster monster : monsters) {
                int damage = monster.maxHealth * baseMagicNumber / 100;
                damage = damage < min ? min : damage;
                AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(monster, p));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(p, damage, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE));
            }
        } else {
            int damage = 0;
            for (AbstractMonster monster : monsters) {
                int dmg = monster.maxHealth * baseMagicNumber / 100;
                if (damage < dmg) {
                    damage = dmg;
                }
            }
            this.baseDamage = damage;
            damage = damage < min ? min : damage;
            for (AbstractMonster monster : monsters) {
                AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(monster, p));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(p, damage, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE));
            }
        }
        if (!upgraded) {
            AbstractDungeon.player.decreaseMaxHealth(3);
        } else {
            AbstractDungeon.player.decreaseMaxHealth(3);
        }
    }

    public AbstractCard makeCopy() {
        return new BloodyBrimstone();
    }

    @Override
    public void update() {
        super.update();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.baseMagicNumber = 33;
            min = 40;
            this.rawDescription = UPGRADE_DESCRIPTION;
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
