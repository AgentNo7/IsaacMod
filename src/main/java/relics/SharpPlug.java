package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import relics.abstracrt.ChargeableRelic;
import relics.abstracrt.ClickableRelic;
import screen.BoxForChargeRelicSelectScreen;

public class SharpPlug extends ClickableRelic {
    public static final String ID = "SharpPlug";
    public static final String IMG = "images/relics/SharpPlug.png";
    public static final String DESCRIPTION = "右击扣除一半血量（最高60最低15），选择一个充能遗物并充满能。";

    private int addon = 0;

    private BoxForChargeRelicSelectScreen chargeRelicSelectScreen;

    public SharpPlug() {
        super("SharpPlug", new Texture(Gdx.files.internal("images/relics/SharpPlug.png")), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new SharpPlug();
    }

    //右键使用
    protected void onRightClick() {
        if (AbstractDungeon.getMonsters() != null) {
            int cost = AbstractDungeon.player.currentHealth / 2;
            cost = cost < 15 ? 15 : cost;
            cost = cost > 60 ? 60 : cost;
            AbstractDungeon.player.currentHealth -= cost;
            AbstractDungeon.player.healthBarUpdatedEvent();
            AbstractDungeon.effectList.add(new StrikeEffect(AbstractDungeon.player, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, cost));
            if (AbstractDungeon.player.currentHealth <= 0) {
                AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.HP_LOSS));
            }
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof ChargeableRelic) {
                    chargeRelicSelectScreen = new BoxForChargeRelicSelectScreen(false, "选择一件遗物充电", "充电页面", "极速快充2.0，瞬间满电，还在等什么？", this);
                    chargeRelicSelectScreen.open();
                    return;
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (chargeRelicSelectScreen != null && chargeRelicSelectScreen.selectedRelic != null) {
            ChargeableRelic relic = (ChargeableRelic) AbstractDungeon.player.getRelic(chargeRelicSelectScreen.selectedRelic.relicId);
            if (relic.maxCharge <= 6) {
                relic.counter = relic.maxCharge;
            } else {
                relic.counter = (relic.counter < 6) ? 6 : relic.maxCharge;
            }
            chargeRelicSelectScreen.selectedRelic = null;
        }
    }
}
