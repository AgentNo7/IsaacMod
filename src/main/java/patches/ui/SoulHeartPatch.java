package patches.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import utils.Invoker;

public class SoulHeartPatch {

    public static Texture soulHP = new Texture("images/ui/panel/panelSoulHeart.png");

    public static Texture blackHP = new Texture("images/ui/panel/panelBlackHeart.png");

    public static int soulHeart = 0;

    public static int blackHeart = 0;

    @SpirePatch(
            clz = TopPanel.class,
            method = "renderHP"
    )
    public static class RenderHP {
        public RenderHP() {
        }

        public static SpireReturn Prefix(TopPanel topPanel, final SpriteBatch sb) {
            if (blackHeart != 0 || soulHeart != 0) {
                sb.setColor(Color.WHITE);

                float hpIconX = (float) Invoker.getField(topPanel, "hpIconX");
                float ICON_W = (float) Invoker.getField(topPanel, "ICON_W");
                float HP_NUM_OFFSET_X = (float) Invoker.getField(topPanel, "HP_NUM_OFFSET_X");
                float INFO_TEXT_Y = (float) Invoker.getField(topPanel, "INFO_TEXT_Y");
                //血量
                sb.draw(ImageMaster.TP_HP, hpIconX, (float) Invoker.getField(topPanel, "ICON_Y") + 7, ICON_W, ICON_W);
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, AbstractDungeon.player.currentHealth + "/" + AbstractDungeon.player.maxHealth, hpIconX + HP_NUM_OFFSET_X, INFO_TEXT_Y + 7, Color.SALMON);
                if (soulHeart != 0) {
                    //魂心
                    sb.draw(soulHP, hpIconX, (float) Invoker.getField(topPanel, "ICON_Y") - 13, ICON_W, ICON_W);
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, soulHeart + "", hpIconX + HP_NUM_OFFSET_X, INFO_TEXT_Y - 13, Color.GRAY);
                }
                if (blackHeart != 0) {
                    float width = FontHelper.getSmartWidth(FontHelper.topPanelInfoFont, soulHeart + "", 99999.0F, 0.0F);
                    sb.draw(blackHP, hpIconX + width + ICON_W, (float) Invoker.getField(topPanel, "ICON_Y") - 13, ICON_W, ICON_W);
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, blackHeart + "", hpIconX + width + HP_NUM_OFFSET_X + ICON_W, INFO_TEXT_Y - 13, Color.DARK_GRAY);
                }


                topPanel.hpHb.render(sb);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
