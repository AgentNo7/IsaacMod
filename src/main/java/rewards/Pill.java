package rewards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rewards.RewardItem;
import relics.PillsDrop;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Pill extends RewardItem {

    private Color color;
    public Effect effect;
    public static List<Color> forgotColor = getRandomColor();

    private PillsDrop pillsDrop;

    public Pill(Color color, Effect effect, PillsDrop pillsDrop) {
        this.hb = new Hitbox(460.0F * Settings.scale, 90.0F * Settings.scale);
        this.flashTimer = 0.0F;
        this.isDone = false;
        this.ignoreReward = false;
        this.redText = false;
        this.pillsDrop = pillsDrop;
        if (pillsDrop.pillFound.get(color) == null || !pillsDrop.pillFound.get(color)) {
            this.text = "吃下这个随机药丸";
        } else {
            if (forgotColor.contains(color)) {
                this.text = "你吃过这个???药丸";
            } else {
                if (effect != null) {
                    String prefix = effect.toString().substring(0, 3);
                    if ("Add".equals(prefix)) {
                        this.text = "吃下这个正收益药丸";
                    } else if ("Dec".equals(prefix)) {
                        this.text = "吃下这个负收益药丸";
                    }
                }
            }
        }
        this.type = RewardType.EMERALD_KEY;
        setColor(color);
        this.effect = effect;
    }

    private void setColor(Color color) {
        this.color = color;
        this.img = new Texture(Gdx.files.internal("images/rewards/pills/" + color + ".png"));
        this.outlineImg = this.img;
    }

    public static List<Color> getRandomColor() {
        int size = 3;
        int rnd[] = Utils.randomCombine(Color.values().length, size);
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            colors.add(Color.values()[rnd[i]]);
        }
        return colors;
    }

    @Override
    public boolean claimReward() {
        if (effect == null) {
            if (color != null) {
                effect = pillsDrop.pillEffect.get(color);
            }
        }
        if (effect != null) {
            switch (effect) {
                case AddFocus:
                    pillsDrop.setFocus(pillsDrop.getFocus() + 1);
                    break;
                case AddStrength:
                    pillsDrop.setStrength(pillsDrop.getStrength() + 1);
                    break;
                case DecStrength:
                    pillsDrop.setStrength(pillsDrop.getStrength() - 1);
                    break;
                case AddDexterity:
                    pillsDrop.setDexterity(pillsDrop.getDexterity() + 1);
                    break;
                case DecDexterity:
                    pillsDrop.setDexterity(pillsDrop.getDexterity() - 1);
                    break;
                case AddMaxHp:
                    pillsDrop.setMaxHp(pillsDrop.getMaxHp() + 3);
                    AbstractDungeon.player.increaseMaxHp(3, true);
                    break;
                case DecMaxHp:
                    pillsDrop.setMaxHp(pillsDrop.getMaxHp() - 5);
                    AbstractDungeon.player.decreaseMaxHealth(5);
                    break;
            }
        }
        pillsDrop.getUpdatedDescription();
        pillsDrop.tips.clear();
        pillsDrop.tips.add(new PowerTip(pillsDrop.name, pillsDrop.getUpdatedDescription()));
        pillsDrop.pillFound.put(this.color, true);
        return true;
    }

    public static enum Effect {
        AddStrength,
        AddDexterity,
        DecStrength,
        DecDexterity,
        AddFocus,
        AddMaxHp,
        DecMaxHp;
    }


    public static enum Color {
        BlueBlue,
        WhiteWhite,
        WhiteRed,
        WhiteBlue,
        YellowOrange,
        BlackWhite,
        WhiteYellow;
    }
}
