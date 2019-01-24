package relics;

import cards.tempCards.PokeBall;
import cards.tempCards.PokeGOGO;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.BaseSummonHelper;
import patches.action.ChangeTargetPatch;
import relics.abstracrt.ClickableRelic;
import utils.Point;
import utils.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PokeGo extends ClickableRelic {
    public static final String ID = "PokeGo";
    public static final String IMG = "images/relics/PokeGo.png";
    public static final String DESCRIPTION = "右击获得一张精灵球。精灵球可以抓住1血的非boss怪物，并成为你的随从。随从最多一只（可替换）。";

    public PokeGo() {
        super("PokeGo", new Texture(Gdx.files.internal("images/relics/PokeGo.png")), RelicTier.RARE, LandingSound.CLINK);
        counter = 0;
    }

    public static Class monsterClass = null;
    public AbstractMonster pet = null;
    public AbstractMonster target = null;

    private int lastPetHp = -1;

    public static AbstractMonster source = null;

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new PokeGo();
    }

    //右键使用
    protected void onRightClick() {
        //选一个怪物抓住
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new PokeBall(this), 1, false));
    }

    @Override
    public void onEquip() {
        super.onEquip();
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (pet != null) {
            //设置目标
            target = null;
            ChangeTargetPatch.source.clear();
            if (!pet.isDead) {
                ChangeTargetPatch.target = pet;
                ChangeTargetPatch.source.addAll(AbstractDungeon.getMonsters().monsters);
                //选一个怪为目标
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new PokeGOGO(this), 1, false));
            }
        }
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        if (counter > 0 && monsterClass != null) {
            Point center = new Point(AbstractDungeon.player.hb.x - 1200, AbstractDungeon.player.hb_y);
            Point point = Utils.getCirclePoint(center, -Math.PI, 250);
            AbstractMonster monster = null;
            while (monster == null) {
                try {
                    Constructor constructor = monsterClass.getConstructor(float.class, float.class);
                    try {
                        monster = (AbstractMonster) constructor.newInstance((float) point.x, (float) point.y);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        System.out.println("!!!!!!!怎么回事啊");
                    }
                } catch (NoSuchMethodException e) {
                    try {
                        monster = (AbstractMonster) monsterClass.newInstance();
                        monster.drawX = AbstractDungeon.player.drawX - 250;
                        monster.drawY = AbstractDungeon.player.drawY;
                    } catch (InstantiationException | IllegalAccessException e1) {
                        e1.printStackTrace();
                        System.out.println("!!!!!!!怎么回事");
                    }
                    e.printStackTrace();
                }
            }
            pet = monster;
            monster.maxHealth = monster.currentHealth = counter;
            monster.flipHorizontal = true;
            BaseSummonHelper.summonMinion(monster);
        } else {
            monsterClass = null;
            pet = null;
        }
    }

    @Override
    public void update() {
        super.update();
        //怪的血是带到下一局的
        if (pet != null && lastPetHp != pet.currentHealth) {
            lastPetHp = pet.currentHealth;
            counter = lastPetHp;
        }
    }
}
