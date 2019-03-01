package relics;

import basemod.abstracts.CustomSavable;
import cards.tempCards.PokeBall;
import cards.tempCards.PokeGOGO;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.SummonHelper;
import patches.action.ChangeTargetPatch;
import relics.abstracrt.ClickableRelic;
import utils.InstanceMaker;
import utils.Invoker;
import utils.Point;
import utils.PokeGoSave;

public class PokeGo extends ClickableRelic implements CustomSavable<PokeGoSave> {
    public static final String ID = "PokeGo";
    public static final String IMG = "images/relics/PokeGo.png";
    public static final String DESCRIPTION = "右击获得一张精灵球。精灵球可以抓住1血的非BOSS怪物或者以15%的概率抓住1血的BOSS，并成为你的随从。随从最多一只（可替换）。";

    public static int slotNum = 0;

    public PokeGo() {
        this(slotNum % 6);
    }

    private static Point[] site = {
            new Point(0, -50),
            new Point(350, -50),
            new Point(500, -50),
            new Point(0, 200),
            new Point(175, 200),
            new Point(350, 200),
    };

    public int slot;

    public PokeGo(int slot) {
        super("PokeGo", new Texture(Gdx.files.internal("images/relics/PokeGo.png")), RelicTier.RARE, LandingSound.CLINK);
        this.slot = slot;
        counter = 0;
    }

    public Class monsterClass = null;
    public AbstractMonster pet = null;
    public AbstractMonster target = null;

    private int lastPetHp = -1;

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new PokeGo(slotNum % 6);
    }

    private boolean getCard = true;

    //右键使用
    public void onRightClick() {
        //选一个怪物抓住
        if (getCard) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new PokeBall(this), 1, false));
            getCard = false;
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
        slotNum++;
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        getCard = true;
        if (pet != null) {
            //设置目标
            target = null;
            if (!pet.isDead && !pet.escaped) {
                ChangeTargetPatch.target = pet;
                //选一个怪为目标
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new PokeGOGO(this), 1, false));
            } else {
                pet = null;
            }
        }
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        if (pet != null) {
            //设置目标
            if (!pet.isDead && !pet.escaped) {
                ChangeTargetPatch.target = pet;
                ChangeTargetPatch.source.clear();
                ChangeTargetPatch.source.addAll(AbstractDungeon.getMonsters().monsters);
            } else if (ChangeTargetPatch.target == pet) {
                ChangeTargetPatch.target = null;
                ChangeTargetPatch.source.clear();
            }
        }
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        newPet = false;
        if (counter > 0 && monsterClass != null) {
            AbstractMonster monster = InstanceMaker.getInstanceByClass(monsterClass);
            if (monster == null) {
                monster = new Cultist(0, 0);
                Invoker.setField(monster, "talky", true);
                System.out.println("抓不到的都变成咔咔");
            }
            monster.drawY = AbstractDungeon.player.drawY + (int) site[slot].y;
            monster.drawX = AbstractDungeon.player.drawX - 175 + (int) site[slot].x;
            pet = monster;
            monster.maxHealth = monster.currentHealth = counter;
            monster.flipHorizontal = true;
            SummonHelper.summonMinion(monster);
        } else {
            monsterClass = null;
            pet = null;
        }
    }

    public boolean newPet = false;

    @Override
    public void update() {
        super.update();
        //怪的血是带到下一局的
        if (pet != null && lastPetHp != pet.currentHealth && !newPet) {
            lastPetHp = pet.currentHealth;
            counter = lastPetHp;
        }
    }

    @Override
    public PokeGoSave onSave() {
        return monsterClass == null ? null : new PokeGoSave(monsterClass.toString(), slot);
    }

    @Override
    public void onLoad(PokeGoSave s) {
        if (s != null && s.slot != null && s.className != null) {
            try {
                monsterClass = Class.forName(s.className.substring(6, s.className.length()));
                slot = s.slot;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
