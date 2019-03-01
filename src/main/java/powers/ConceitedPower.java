package powers;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class ConceitedPower extends AbstractPower {

    private static final PowerStrings powerString;
    public static final String POWER_ID = "ConceitedPower";
    public static final String NAME;
    public static final String IMG = "images/powers/ConceitedPower.png";
    public static final String[] DESCRIPTIONS;
    public static final String[] DIALOGS;


    public ConceitedPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "ConceitedPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage(IMG);
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    private int chance = 0;

    private boolean done = false;

    public static boolean canSee = true;

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        chance = AbstractDungeon.aiRng.random(0, 99);
        if (chance < 15) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DIALOGS[0], 1.0f, 2.0f));
        }
        else if (chance >= 85 && chance < 100 && !done) {
            AbstractDungeon.actionManager.addToTop(new TalkAction(true, DIALOGS[1], 1.0f, 2.0f));
            done = true;
            canSee = false;
        } else if ((chance >= 60 && chance < 85) || done) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DIALOGS[2], 1.0f, 2.0f));
            for (int i = 0; i < this.amount; i++) {
                if (AbstractDungeon.aiRng.randomBoolean(0.5F)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new WeakPower(this.owner, 1, false), 1));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new VulnerablePower(this.owner, 1, false), 1));
                }
            }
        }
    }


    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        for (int i = 0; i < this.amount; i++) {
            if (chance < 20 && AbstractDungeon.aiRng.randomBoolean(0.5F)) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DIALOGS[3], 1.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new HealAction(target, AbstractDungeon.player, info.output));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        if (chance >= 30 && chance < 60 && owner.currentBlock > 0) {
            double finaldamage = 1;
            for (int i = 0; i < this.amount; i++) {
                finaldamage = finaldamage * 0.67;
            }
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DIALOGS[4] + (100 - (int) (finaldamage * 100)) + DIALOGS[5], 1.0f, 2.0f));
            owner.currentBlock = (int) (owner.currentBlock * finaldamage);
        } else if (chance >= 15 && chance < 30) {
            for (int i = 0; i < this.amount; i++) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DIALOGS[6], 1.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new MoreDamagePower(this.owner, 1), 1));
            }
        }
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            DIALOGS = new String[]{
                    "是时候表演一下吊锤萌死戳了。",
                    "打萌死戳我闭着眼睛都打过了。",
                    "不给自己上个debuff显示不出我实力。",
                    "给你回一口，让我多打打。",
                    "让你",
                    "%格挡也是吊打。",
                    "听说被萌死戳打到基本也就告别这个游戏了。"
            };
        }else {
            DIALOGS = new String[]{
                    "it's time to show how to beat monstro.",
                    "I don't need to know what monstro what to do.",
                    "I can't show my strength without giving me a debuff.",
                    "Give you a heal and let me play more.",
                    "Block is useless, i will abandon ",
                    "% of my block。",
                    "I heard that if someone was beaten by the monstro,it's time said goodbye to this game on the whole."
            };
        }
    }
}
