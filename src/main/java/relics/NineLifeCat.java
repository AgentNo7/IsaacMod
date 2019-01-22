package relics;

import actions.LoseRelicAction;
import actions.ObtainRelicAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FossilizedHelix;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import powers.NineLifeCatPower;
import relics.abstracrt.DevilRelic;

public class NineLifeCat extends DevilRelic {
    public static final String ID = "NineLifeCat";
    public static final String IMG = "images/relics/NineLifeCat.png";
    public static final String DESCRIPTION = "拾取时减少 #b75% 最大生命值，获得 #b9 条命，复活时回复全部生命。";

    public static void show() {
        AbstractDungeon.player.getRelic("NineLifeCat").flash();
    }

    public static int lives = 9;

    private NineLifeCatPower power;

    public NineLifeCat() {
        super("NineLifeCat", new Texture(Gdx.files.internal("images/relics/NineLifeCat.png")), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new NineLifeCat();
    }

    @Override
    public void onEquip() {
        this.counter = 9;
        lives = 9;
        AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth * 75 / 100);
        if (AbstractDungeon.player.getRelic(this.relicId) == this) {
            HushsDoor.guppyCount++;
        }
    }

    private boolean check = true;

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (!(room instanceof MonsterRoom)) {
            if (this.counter > 0) {
                AbstractDungeon.player.powers.add(new NineLifeCatPower(AbstractDungeon.player, this.counter, this));
            }
        }
    }

    @Override
    public void atPreBattle() {
        if (check && AbstractDungeon.player.hasRelic(FossilizedHelix.ID)) {
            int buffer = 0;
            int cat = 0;
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(FossilizedHelix.ID)) {
                    buffer = i;
                } else if (AbstractDungeon.player.relics.get(i).relicId.equals(NineLifeCat.ID)) {
                    cat = i;
                }
            }
            if (cat > buffer) {
                check = false;
            } else {
                AbstractDungeon.actionManager.addToBottom(new LoseRelicAction(NineLifeCat.ID));
                AbstractDungeon.actionManager.addToBottom(new LoseRelicAction(FossilizedHelix.ID));
                AbstractDungeon.actionManager.addToBottom(new ObtainRelicAction(new FossilizedHelix(), true));
                AbstractDungeon.actionManager.addToBottom(new ObtainRelicAction(new NineLifeCat(), true));
            }
        }
        super.atPreBattle();
    }

    @Override
    public void atBattleStart() {
        if (counter > 0) {
            lives = this.counter;
            this.flash();
            power = new NineLifeCatPower(AbstractDungeon.player, counter, this);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power, counter));
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
