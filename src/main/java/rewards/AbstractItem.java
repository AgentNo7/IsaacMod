package rewards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractItem extends RewardItem{
    public AbstractItem(RewardItem setRelicLink, RewardType type) {
        super(setRelicLink, type);
    }

    public AbstractItem(int gold) {
        super(gold);
    }

    public AbstractItem(int gold, boolean theft) {
        super(gold, theft);
    }

    public AbstractItem(AbstractRelic relic) {
        super(relic);
    }

    public AbstractItem(AbstractPotion potion) {
        super(potion);
    }

    public AbstractItem() {
        super();
    }

    public AbstractItem(AbstractCard.CardColor colorType) {
        super(colorType);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.hb.hovered) {
            sb.setColor(new Color(0.4F, 0.6F, 0.6F, 1.0F));
        } else {
            sb.setColor(new Color(0.5F, 0.6F, 0.6F, 0.8F));
        }

        if (this.hb.clickStarted) {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, (float)Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.scale * 0.98F, Settings.scale * 0.98F, 0.0F, 0, 0, 464, 98, false, false);
        } else {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, (float)Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 464, 98, false, false);
        }

        if (this.flashTimer != 0.0F) {
            sb.setColor(0.6F, 1.0F, 1.0F, this.flashTimer * 1.5F);
            sb.setBlendFunction(770, 1);
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, (float)Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.scale * 1.03F, Settings.scale * 1.15F, 0.0F, 0, 0, 464, 98, false, false);
            sb.setBlendFunction(770, 771);
        }


        sb.setColor(Color.WHITE);

        try {
            Method renderKey = RewardItem.class.getDeclaredMethod("renderKey", SpriteBatch.class);
            renderKey.setAccessible(true);
            renderKey.invoke(this, sb);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Color color;
        if (this.hb.hovered) {
            color = Settings.GOLD_COLOR;
        } else {
            color = Settings.CREAM_COLOR;
        }

        if (this.redText) {
            color = Settings.RED_TEXT_COLOR;
        }
        Field REWARD_TEXT_X;
        float x = 0;
        try {
            REWARD_TEXT_X = RewardItem.class.getDeclaredField("REWARD_TEXT_X");
            REWARD_TEXT_X.setAccessible(true);
            x = REWARD_TEXT_X.getFloat(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, this.text, x, this.y + 5.0F * Settings.scale, 1000.0F * Settings.scale, 0.0F, color);

        this.hb.render(sb);
    }
}
