package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import patches.ui.MagicMushroomPatch;
import utils.Invoker;

public class MagicMushroom extends CustomRelic {
    public static final String ID = "MagicMushroom";
    public static final String IMG = "images/relics/MagicMushroom.png";
    public static final String DESCRIPTION = "获得7点血上限，在战斗开始时获得1点力量，敏捷，集中，多层护甲，金属化，火焰吐息，势不可挡，荆棘，无惧疼痛，再生，残影，保留，火焰屏障，愤怒。";

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
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MetallicizePower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FireBreathingPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new JuggernautPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FeelNoPainPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegenPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConservePower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlameBarrierPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RagePower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    private boolean large = false;


    @Override
    public void update() {
        super.update();
        if (!large && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
            large = true;
            if (MagicMushroomPatch.atlasUrl != null && MagicMushroomPatch.skeletonUrl != null) {
                if (Invoker.getField(AbstractDungeon.player, "skeleton") != null) {
                    try {
                        Invoker.invoke(AbstractDungeon.player, "loadAnimation", MagicMushroomPatch.atlasUrl, MagicMushroomPatch.skeletonUrl, 0.55f);
                        AnimationState.TrackEntry e = AbstractDungeon.player.state.setAnimation(0, "Idle", true);
                        e.setTimeScale(0.9F);
                    } catch (Exception ignored) {
                    }
//                String name = AbstractDungeon.player.chosenClass.toString().replace("_", "").toLowerCase();
//                if (name.equals("thesilent")) {
//                    name = "theSilent";
//                }
//                AbstractDungeon.player.loadAnimation()
//                AbstractDungeon.player.skeleton
//                Invoker.invoke(AbstractDungeon.player, "loadAnimation", "images/characters/" + name + "/idle/skeleton.atlas", "images/characters/" + name + "/idle/skeleton.json", 0.55f);
                }
            }
        }
    }
}
