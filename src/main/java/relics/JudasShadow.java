package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import relics.abstracrt.DevilInterface;
import relics.abstracrt.ResurrectRelic;

import static patches.ui.JudasPatch.haveJudas;
import static patches.ui.SoulHeartPatch.blackHeart;

public class JudasShadow extends ResurrectRelic implements DevilInterface{
    public static final String ID = "JudasShadow";
    public static final String IMG = "images/relics/JudasShadow.png";
    public static final String DESCRIPTION = "死亡后复活成犹大之影，血上限为1，获得40黑心，开局获得15力量。";

    private static final int PRIORITY = 99;

    public JudasShadow() {
        super("JudasShadow", new Texture(Gdx.files.internal("images/relics/JudasShadow.png")), RelicTier.SPECIAL, LandingSound.CLINK, PRIORITY);
        counter = 1;
        price = 25;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new JudasShadow();
    }

    @Override
    public int onResurrect() {
        AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth);
        blackHeart += 40;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 15), 15));
        counter = -1;
        haveJudas = true;
        return 1;
    }

    @Override
    public boolean canResurrect() {
        return counter > 0;
    }

    @Override
    public void onEquip() {
        super.onEquip();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        if (counter != 1) {
            haveJudas = true;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 15), 15));
        }
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void update() {
        super.update();
    }
}
