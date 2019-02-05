package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MagicMushroom extends CustomRelic {
    public static final String ID = "MagicMushroom";
    public static final String IMG = "images/relics/MagicMushroom.png";
    public static final String DESCRIPTION = "获得7点血上限，在战斗开始时获得1点力量，敏捷，集中，多层护甲，金属化，再生，残影，保留，火焰屏障，愤怒。";

    public MagicMushroom() {
        super("MagicMushroom", new Texture(Gdx.files.internal("images/relics/MagicMushroom.png")), RelicTier.BOSS, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new MagicMushroom();
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractDungeon.player.increaseMaxHp(7, true);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        try {
            Method loadAnimation = AbstractCreature.class.getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            String name = AbstractDungeon.player.chosenClass.toString().replace("_", "").toLowerCase();
            if (name.equals("thesilent")) {
                name = "theSilent";
            }
            loadAnimation.invoke(AbstractDungeon.player, "images/characters/" + name + "/idle/skeleton.atlas", "images/characters/" + name + "/idle/skeleton.json", 0.9f);
            AnimationState.TrackEntry e = AbstractDungeon.player.state.setAnimation(0, "Idle", true);
            e.setTimeScale(0.9F);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MetallicizePower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegenPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConservePower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlameBarrierPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RagePower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void update() {
        super.update();
    }
}
