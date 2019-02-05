package patches.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

import java.lang.reflect.Field;

public class SoulHeartPatch {

    public static Texture soulHP = new Texture("images/ui/panel/SoulHeart.png");

    public static Texture blackHP = new Texture("images/ui/panel/BlackHeart.png");

    public static int soulHeart = 10;

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
                try {
                    Field hpIconX = TopPanel.class.getDeclaredField("hpIconX");
                    Field ICON_Y = TopPanel.class.getDeclaredField("ICON_Y");
                    Field ICON_W = TopPanel.class.getDeclaredField("ICON_W");
                    Field HP_NUM_OFFSET_X = TopPanel.class.getDeclaredField("HP_NUM_OFFSET_X");
                    Field INFO_TEXT_Y = TopPanel.class.getDeclaredField("INFO_TEXT_Y");
                    hpIconX.setAccessible(true);
                    ICON_Y.setAccessible(true);
                    ICON_W.setAccessible(true);
                    HP_NUM_OFFSET_X.setAccessible(true);
                    INFO_TEXT_Y.setAccessible(true);
                    sb.setColor(Color.WHITE);

                    sb.draw(ImageMaster.TP_HP, hpIconX.getFloat(topPanel), ICON_Y.getFloat(topPanel) + 7, ICON_W.getFloat(topPanel), ICON_W.getFloat(topPanel));
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, AbstractDungeon.player.currentHealth + "/" + AbstractDungeon.player.maxHealth, hpIconX.getFloat(topPanel) + HP_NUM_OFFSET_X.getFloat(topPanel), INFO_TEXT_Y.getFloat(topPanel) + 7, Color.SALMON);

                    sb.draw(soulHP, hpIconX.getFloat(topPanel), ICON_Y.getFloat(topPanel) - 13, ICON_W.getFloat(topPanel), ICON_W.getFloat(topPanel));//ICON_Y.getFloat(topPanel)
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, soulHeart + "-" + blackHeart, hpIconX.getFloat(topPanel) + HP_NUM_OFFSET_X.getFloat(topPanel), INFO_TEXT_Y.getFloat(topPanel) - 13, Color.GRAY);

                    topPanel.hpHb.render(sb);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class damagePatch {
        public damagePatch() {
        }

        @SpireInsertPatch(
                loc=1665,
                localvars={"damageAmount"}
        )
        public static void Insert(AbstractPlayer player, final DamageInfo info, @ByRef int[] damageAmount) { //damageAmount 必定>0
            if (blackHeart > 0) {
                if (damageAmount[0] > blackHeart % 10) {
                    blackHeart = blackHeart <= damageAmount[0] ? 0 : blackHeart - damageAmount[0];
                    if (AbstractDungeon.getMonsters() != null) {
                        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(player, DamageInfo.createDamageMatrix(40, false), DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
                    }
                }  else {
                    blackHeart -= damageAmount[0];
                }
                damageAmount[0] = 0;
            } else if (soulHeart > 0) {
                soulHeart = soulHeart <= damageAmount[0] ? 0 : soulHeart - damageAmount[0];
                damageAmount[0] = 0;
            }
        }
    }
}
