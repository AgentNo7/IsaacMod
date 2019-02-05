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
import utils.Point;
import utils.PokeGoSave;
import utils.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class PokeGo extends ClickableRelic implements CustomSavable<PokeGoSave> {
    public static final String ID = "PokeGo";
    public static final String IMG = "images/relics/PokeGo.png";
    public static final String DESCRIPTION = "右击获得一张精灵球。精灵球可以抓住1血的非BOSS怪物或者以15%的概率抓住1血的BOSS，并成为你的随从。随从最多一只（可替换）。";

    public PokeGo() {
        this(0);
//        super("PokeGo", new Texture(Gdx.files.internal("images/relics/PokeGo.png")), RelicTier.RARE, LandingSound.CLINK);
//        counter = 0;
    }

    private static Point[] site = {
            new Point(0, 0),
            new Point(0, 250),
            new Point(175, 250),
            new Point(350, 250),
            new Point(350, 0),
            new Point(500, 0),
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
        return new PokeGo(this.slot);
    }

    private boolean getCard = true;

    //右键使用
    protected void onRightClick() {
        //选一个怪物抓住
        if (getCard) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new PokeBall(this), 1, false));
            getCard = false;
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
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
            }
        }
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        if (pet != null) {
            //设置目标
            ChangeTargetPatch.target = null;
            ChangeTargetPatch.source.clear();
            if (!pet.isDead && !pet.escaped) {
                ChangeTargetPatch.source.addAll(AbstractDungeon.getMonsters().monsters);
                ChangeTargetPatch.target = pet;
            }
        }
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        newPet = false;
        if (counter > 0 && monsterClass != null) {
            Point center = new Point(AbstractDungeon.player.hb.x - 1200, AbstractDungeon.player.hb_y);
            Point point = Utils.getCirclePoint(center, -Math.PI, 250);
            AbstractMonster monster = null;
            while (monster == null) {
                Constructor[] cons = monsterClass.getConstructors();
                int min = 999;
                for (Constructor constructor : cons) {
                    if (constructor.getParameterCount() == 0) {
                        try {
                            monster = (AbstractMonster) monsterClass.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    } else if (constructor.getParameterCount() < min) {
                        min = constructor.getParameterCount();
                    }
                }

                for (Constructor constructor : cons) {
                    if (min == constructor.getParameterCount()) {
                        Class[] types = constructor.getParameterTypes();
                        Object[] params = new Object[types.length];
                        for (int i = 0; i < types.length; i++) {
                            Class c = types[i];
                            if (c.getConstructors().length == 0) {
                                if (c == boolean.class) {
                                    params[i] = false;
                                } else {
                                    params[i] = (byte) 0;
                                }
                            } else {
                                if (c == Integer.class || c == Float.class || c == Double.class || c == Long.class || c == Byte.class) {
                                    params[i] = (byte) 0;
                                } else if (c == String.class) {
                                    params[i] = "";
                                } else {
                                    params[i] = null;
                                }
                            }
                        }
                        try {
                            monster = (AbstractMonster) constructor.newInstance(params);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (monster == null) {
                    monster = new Cultist(0, 0);
                    try {
                        Field talky = Cultist.class.getDeclaredField("talky");
                        talky.setAccessible(true);
                        talky.setBoolean(monster, true);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    System.out.println("抓不到的都变成咔咔");
                }
            }
//                if (monsterClass == Lagavulin.class) {
//                    monster = new Lagavulin(false);
//                    monster.drawX = AbstractDungeon.player.drawX - 250;
//                    monster.drawY = AbstractDungeon.player.drawY;
//                    break;
//                }
//                if (monsterClass == BronzeOrb.class) {
//                    monster = new BronzeOrb((float) point.x, (float) point.y, 0);
//                    break;
//                }
//                if (monsterClass == AcidSlime_S.class) {
//                    monster = new AcidSlime_S((float) point.x, (float) point.y, 0);
//                    break;
//                }
//                if (monsterClass == SpikeSlime_S.class) {
//                    monster = new SpikeSlime_S((float) point.x, (float) point.y, 0);
//                    break;
//                }
//                try {
//                    Constructor constructor = monsterClass.getConstructor(float.class, float.class);
//                    try {
//                        monster = (AbstractMonster) constructor.newInstance((float) point.x, (float) point.y);
//                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
//                        e.printStackTrace();
//                        System.out.println("!!!!!!!怎么回事啊");
//                    }
//                } catch (NoSuchMethodException e) {
//                    try {
//                        monster = (AbstractMonster) monsterClass.newInstance();
//                    } catch (InstantiationException | IllegalAccessException e1) {
//                        e1.printStackTrace();
//                        System.out.println("!!!!!!!怎么回事");
//                    }
//                    e.printStackTrace();
//                }
//            }
//            if (retry >= 10) {
//                monster = new Cultist(0, 0);
//            }
            monster.drawY = AbstractDungeon.player.drawY + (int) site[slot].y;
            monster.drawX = AbstractDungeon.player.drawX - 175 + (int) site[slot].x;
            pet = monster;
            monster.maxHealth = monster.currentHealth = counter;
            monster.flipHorizontal = true;
//            monster.skeleton.setFlip();
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
