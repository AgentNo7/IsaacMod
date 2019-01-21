package powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;

import java.util.Iterator;

public class MyTimeWarpPower extends AbstractPower {
    public static final String POWER_ID = "Time Warp";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;
    private static final int STR_AMT = 2;
    private static final int COUNTDOWN_AMT = 12;
    private int countDownAmt;

    public MyTimeWarpPower(AbstractCreature owner, int countDownAmt) {
        this.name = NAME;
        this.ID = "Time Warp";
        this.owner = owner;
        this.amount = 0;
        this.updateDescription();
        this.loadRegion("time");
        this.type = PowerType.BUFF;
        this.countDownAmt = countDownAmt;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
    }

    public void updateDescription() {
        this.description = DESC[0] + countDownAmt + DESC[1] + 5 + DESC[2];
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        ++this.amount;
        if (this.amount == 36) {
            this.amount = 0;
            this.playApplyPowerSfx();
            AbstractDungeon.actionManager.cardQueue.clear();
            Iterator var3 = AbstractDungeon.player.limbo.group.iterator();

            while (var3.hasNext()) {
                AbstractCard c = (AbstractCard) var3.next();
                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
            }

            AbstractDungeon.player.limbo.group.clear();
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.endTurnButton.disable(true);
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
            AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
            var3 = AbstractDungeon.getMonsters().monsters.iterator();

            while (var3.hasNext()) {
                AbstractMonster m = (AbstractMonster) var3.next();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, 5), 5));
            }
        }

        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.amount = 0;
        this.updateDescription();
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("Time Warp");
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            DESC[0] = "每回合最多打出";
            DESC[1] = "张牌（回合开始重新计数），你的回合会强制结束，并且凹凸获得 #b";
        } else {
            DESC[0] = "Play at most";
            DESC[1] = "cards（count to 0 at the start of turn），force to end your turn，and Hush gain #b";
        }
    }
}
