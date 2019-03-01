package relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import rewards.Pill;
import rewards.Pill.Color;
import rewards.Pill.Effect;
import utils.PillsDropSave;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PillsDrop extends CustomRelic implements CustomSavable<PillsDropSave> {
    public static final String ID = "PillsDrop";
    public static final String IMG = "images/relics/PillsDrop.png";
    private int strength = 0;
    private int dexterity = 0;
    private int focus = 0;
    private int maxHp = 0;
    public static final String DESCRIPTION = "奖励界面可能掉落随机药丸，同色药丸效果相同。(药丸掉落界面SL本局游戏将死档)。";

    public void show() {
        this.flash();
    }

    public static Map<Color, Effect> pillEffect = new HashMap<>();
    public static Map<Color, Boolean> pillFound = new HashMap<>();

    public PillsDrop() {
        super("PillsDrop", new Texture(Gdx.files.internal("images/relics/PillsDrop.png")), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    private Color getRandomColor() {
        int rnd = Utils.randomCombine(Color.values().length, 1)[0];
        return Color.values()[rnd];
    }

    @Override
    public void onEquip() {
        strength = 0;
        dexterity = 0;
        focus = 0;
        maxHp = 0;
        Color[] colors = Color.values();
        Effect[] effects = Effect.values();
        int size = colors.length;
        //roll效果
        int[] key = Utils.randomCombine(size, size);
        int[] value = Utils.randomCombine(size, size);
        pillFound.clear();
        for (int i = 0; i < size; i++) {
            pillEffect.put(colors[key[i]], effects[value[i]]);
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.getUpdatedDescription()));
    }

    public String getUpdatedDescription() {
        if (strength == 0 && dexterity == 0 && focus == 0) {
            return DESCRIPTIONS[0];
        }
        String result = DESCRIPTIONS[0];
        if (strength != 0) {
            result = result + "力量：" + strength + "。";
        }
        if (dexterity != 0) {
            result = result + "敏捷：" + dexterity + "。";
        }
        if (focus != 0) {
            result = result + "集中：" + focus + "。";
        }
        if (maxHp != 0) {
            result = result + "血上限：" + maxHp + "。";
        }
        return this.description = result;
    }

    public AbstractRelic makeCopy() {
        return new PillsDrop();
    }

    @Override
    public void atBattleStart() {
        if (pillEffect == null || pillEffect.size() == 0) {
            rollEffect();
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.getUpdatedDescription()));
        if (strength != 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, strength), strength));
        }
        if (dexterity != 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, dexterity), dexterity));
        }
        if (focus != 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, focus), focus));
        }
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void onVictory() {
        int rnd = AbstractDungeon.aiRng.random(0, 99);
        if (rnd < 75) {
            Color color = getRandomColor();
            if (pillEffect.get(color) == null) {
                rollEffect();
            }
            RewardItem pillReward = new Pill(color, pillEffect.get(color), this);
            AbstractDungeon.getCurrRoom().rewards.add(pillReward);
        }
    }

    private void rollEffect() {
        Color[] colors = Color.values();
        Effect[] effects = Effect.values();
        int size = colors.length;
        //roll效果
        int[] key = Utils.randomCombine(size, size);
        int[] value = Utils.randomCombine(size, size);
        pillEffect.clear();
        for (int i = 0; i < size; i++) {
            pillEffect.put(colors[key[i]], effects[value[i]]);
        }
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getFocus() {
        return focus;
    }

    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public PillsDropSave onSave() {
        List<Pill> remove = new ArrayList<>();
        for (RewardItem item : AbstractDungeon.getCurrRoom().rewards) {
            if (item instanceof Pill) {
                remove.add((Pill) item);
            }
        }
        Effect effect = null;
        for (Pill pill : remove) {
            effect = pill.effect;
            AbstractDungeon.getCurrRoom().rewards.remove(pill);
        }
        return new PillsDropSave(strength, dexterity, focus, maxHp, effect);
    }

    @Override
    public void onLoad(PillsDropSave pillsDropSave) {
        strength = pillsDropSave.strength;
        dexterity = pillsDropSave.dexterity;
        focus = pillsDropSave.focus;
        maxHp = pillsDropSave.maxHp;
        if (pillsDropSave.effect != null) {
            switch (pillsDropSave.effect) {
                case AddFocus:
                    this.setFocus(this.getFocus() + 1);
                    break;
                case AddStrength:
                    this.setStrength(this.getStrength() + 1);
                    break;
                case DecStrength:
                    this.setStrength(this.getStrength() - 1);
                    break;
                case AddDexterity:
                    this.setDexterity(this.getDexterity() + 1);
                    break;
                case DecDexterity:
                    this.setDexterity(this.getDexterity() - 1);
                    break;
                case AddMaxHp:
                    this.setMaxHp(this.getMaxHp() + 3);
                    AbstractDungeon.player.increaseMaxHp(3, true);
                    break;
                case DecMaxHp:
                    this.setMaxHp(this.getMaxHp() - 5);
                    AbstractDungeon.player.decreaseMaxHealth(5);
                    break;
            }
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.getUpdatedDescription()));
    }
}
