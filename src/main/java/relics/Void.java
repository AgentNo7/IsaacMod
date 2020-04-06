package relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import relics.abstracrt.ChargeableRelic;
import relics.abstracrt.ClickableRelic;
import screen.ChargeRelicSelectScreen;
import utils.VoidSave;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Void extends ChargeableRelic implements CustomSavable<VoidSave> {
    public static final String ID = "Void";
    public static final String IMG = "images/relics/Void.png";
    public static final String DESCRIPTION = "6充能，偶数充能时右击可以从遗物列表吸入遗物一次，吸入遗物加一充能（不能吸入绽放印记，被动遗物获得随机初始属性，主动遗物获得主动效果，满充能时该效果立刻生效一次），满充能时右击触发所有吸入的主动遗物效果。";

    public List<ClickableRelic> relicList = new ArrayList<>();

    private int strength = 0;
    private int dexterity = 0;
    private int focus = 0;

    public Void() {
        super("Void", new Texture(Gdx.files.internal("images/relics/Void.png")), RelicTier.RARE, LandingSound.CLINK, 6);
    }

//    public String getUpdatedDescription() {
//        return this.DESCRIPTIONS[0];
//    }

    public AbstractRelic makeCopy() {
        return new Void();
    }

    private int count = 0;

    public void onRightClick() {
        if ((counter % 2 == 0)) {
            new ChargeRelicSelectScreen(true, "选择一件遗物吸入虚空", "虚空", "没有人知道虚空的内部到底是怎么样的", this, this.counter).open();
        }
        if (isUsable()) {
            for (ClickableRelic relic : relicList) {
                if (relic instanceof ChargeableRelic) {
                    ((ChargeableRelic) relic).counter = ((ChargeableRelic) relic).maxCharge;
                }
                relic.onRightClick();
            }
            resetCharge();
        }
        AbstractRelic.relicPage = 0;
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
        return this.description = result;
    }

    private void pre() {
        if (AbstractDungeon.combatRewardScreen != null && AbstractDungeon.combatRewardScreen.rewards != null) {
            Iterator<RewardItem> iterator = AbstractDungeon.combatRewardScreen.rewards.iterator();
            while (iterator.hasNext()) {
                RewardItem rewardItem = iterator.next();
                if (rewardItem.relic != null) {
                    if (rewardItem.relic instanceof ClickableRelic) {
                        relicList.add((ClickableRelic) rewardItem.relic);
                        iterator.remove();
                    } else {
                        addPower();
                        if (AbstractDungeon.aiRng.randomBoolean(0.15F)) {
                            addPower();
                        }
                    }
                }
            }
        }
        if (AbstractDungeon.getCurrRoom() instanceof TreasureRoomBoss) {
            BossChest bossChest = (BossChest) ((TreasureRoomBoss) AbstractDungeon.getCurrRoom()).chest;
            if (bossChest != null && bossChest.isOpen && AbstractDungeon.bossRelicScreen.relics.size() > 0) {
                bossChest.relics.clear();
                bossChest.relics.add(new HushsDoor());
                bossChest.relics.add(new HushsDoor());
                bossChest.relics.add(new HushsDoor());
                bossChest.close();
            }
        }
        if (AbstractDungeon.bossRelicScreen != null && AbstractDungeon.bossRelicScreen.relics != null) {
            for (AbstractRelic relic : AbstractDungeon.bossRelicScreen.relics) {
                if (relic != null) {
                    if (relic instanceof ClickableRelic) {
                        relicList.add((ClickableRelic) relic);
                    } else {
                        addPower();
                        if (AbstractDungeon.aiRng.randomBoolean(0.15F)) {
                            addPower();
                        }
                    }
                }
            }
            AbstractDungeon.bossRelicScreen.relics.clear();
        }
    }

    public void addPower() {
        switch (AbstractDungeon.aiRng.random(0, 2)) {
            case 0:
                strength += 1;
                AbstractDungeon.player.addPower(new StrengthPower(AbstractDungeon.player, 1));
                break;
            case 1:
                dexterity += 1;
                AbstractDungeon.player.addPower(new DexterityPower(AbstractDungeon.player, 1));
                break;
            case 2:
                focus += 1;
                AbstractDungeon.player.addPower(new FocusPower(AbstractDungeon.player, 1));
                break;
        }
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (strength != 0) {
            AbstractDungeon.player.addPower(new StrengthPower(AbstractDungeon.player, strength));
        }
        if (dexterity != 0) {
            AbstractDungeon.player.addPower(new DexterityPower(AbstractDungeon.player, dexterity));
        }
        if (focus != 0) {
            AbstractDungeon.player.addPower(new FocusPower(AbstractDungeon.player, focus));
        }
        for (ClickableRelic r : relicList) {
            r.atBattleStart();
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
        count = 0;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        int damage = damageAmount;
        for (ClickableRelic r : relicList) {
            damage = r.onAttacked(info, damageAmount);
        }
        return damage;
    }

    @Override
    public void atTurnStart() {
        for (ClickableRelic r : relicList) {
            r.atTurnStart();
        }
    }

    @Override
    public void update() {
        super.update();
        for (ClickableRelic r : relicList) {
            r.update();
        }
    }

    @Override
    public VoidSave onSave() {
        List<String> list = new ArrayList<>();
        for (AbstractRelic r : relicList) {
            list.add(r.relicId);
        }
        return new VoidSave(strength, dexterity, focus, list);
    }

    @Override
    public void onLoad(VoidSave voidSave) {
        if (voidSave != null) {
            this.strength = voidSave.strength;
            this.dexterity = voidSave.dexterity;
            this.focus = voidSave.focus;
            List<ClickableRelic> list = new ArrayList<>();
            for (String s : voidSave.relicList) {
                list.add((ClickableRelic) RelicLibrary.getRelic(s));
            }
            this.relicList = list;
        }
    }

}
