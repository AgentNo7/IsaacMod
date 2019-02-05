package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ClickableRelic;

public class HowToJump extends ClickableRelic {
    public static final String ID = "HowToJump";
    public static final String IMG = "images/relics/HowToJump.png";
    public static final String DESCRIPTION = "在攻击时有15%的概率不受到伤害，右击让角色跳一下。";

    public HowToJump() {
        super("HowToJump", new Texture(Gdx.files.internal("images/relics/HowToJump.png")), RelicTier.COMMON, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new HowToJump();
    }

    //右键使用
    protected void onRightClick() {
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(AbstractDungeon.player));
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(m));
            }
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (AbstractDungeon.aiRng.randomBoolean(0.10F)) {
            return super.onAttacked(info, 0);
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        if (AbstractDungeon.player.getRelic(this.relicId) == this) {
            HushsDoor.bookCount++;
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
