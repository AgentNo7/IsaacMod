package monsters.abstracrt;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import helpers.BasePlayerMinionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPet extends CustomMonster {

    public float moveTimer = 0.0F;
    public float speedX = 0.0F;
    public float speedY = 0.0F;
    public boolean isSuicide = false;
    public AbstractCreature target = null;
    public int suicideDamage = 20;

    public AbstractPet(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    public AbstractPet(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY, ignoreBlights);
    }

    public AbstractPet(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl);
    }

    public AbstractMonster getTarget() {
        if (AbstractDungeon.getMonsters() == null) {
            return null;
        }
        List<AbstractMonster> monsters = new ArrayList<>();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.escaped && !m.isDying && !m.isDead && m.currentHealth > 0 && !(m instanceof AbstractPet)) {
                monsters.add(m);
            }
        }
        if (monsters.size() == 0) {
            return null;
        }
        int rnd = AbstractDungeon.monsterRng.random(0, monsters.size() - 1);
        return monsters.get(rnd);
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.owner != null && info.owner != AbstractDungeon.player) {
            super.damage(info);
        }
    }


    @Override
    public void applyPowers() {
        try {
            Field move = AbstractMonster.class.getDeclaredField("move");
            move.setAccessible(true);
            EnemyMoveInfo theMove = (EnemyMoveInfo) move.get(this);
            if (theMove.baseDamage > -1) {
                Method calculateDamage = AbstractMonster.class.getDeclaredMethod("calculateDamage", int.class);
                calculateDamage.setAccessible(true);
                calculateDamage.invoke(this, theMove.baseDamage);
//                this.calculateDamage(this.move.baseDamage);
            }
            Field intentImg = AbstractMonster.class.getDeclaredField("intentImg");
            intentImg.setAccessible(true);
            Method getIntentImg = AbstractMonster.class.getDeclaredMethod("getIntentImg");
            getIntentImg.setAccessible(true);
            intentImg.set(this, getIntentImg.invoke(this));
            Method updateIntentTip = AbstractMonster.class.getDeclaredMethod("updateIntentTip");
            updateIntentTip.setAccessible(true);
            updateIntentTip.invoke(this);
//            this.intentImg = this.getIntentImg();
//            this.updateIntentTip();
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        super.update();
        if (isSuicide) {
            updateSuicideAnimation();
        } else {
            updateMoveAnimation();
        }
    }

    @Override
    public void die() {
        super.die();
        BasePlayerMinionHelper.removeMinion(AbstractDungeon.player, this);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        BasePlayerMinionHelper.removeMinion(AbstractDungeon.player, this);
    }

    private void updateMoveAnimation() {
        if (this.moveTimer != 0.0F) {
            this.moveTimer -= Gdx.graphics.getDeltaTime();
            this.drawX += Gdx.graphics.getDeltaTime() * speedX;
            this.drawY += Gdx.graphics.getDeltaTime() * speedY;
        }
        if (this.moveTimer < 0.0F) {
            this.moveTimer = 0.0F;
            this.speedX = 0.0F;
            this.speedY = 0.0F;
        }
    }

    private void updateSuicideAnimation() {
        if (this.moveTimer != 0.0F) {
            this.moveTimer -= Gdx.graphics.getDeltaTime();
            this.drawX += Gdx.graphics.getDeltaTime() * speedX;
            this.drawY += Gdx.graphics.getDeltaTime() * speedY;
        }
        if (this.moveTimer < 0.0F) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(this, suicideDamage), AbstractGameAction.AttackEffect.FIRE));
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
            this.moveTimer = 0.0F;
            this.speedX = 0.0F;
            this.speedY = 0.0F;
        }
    }
}
