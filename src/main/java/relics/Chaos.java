package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mymod.IsaacMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Chaos extends CustomRelic {
    public static final String ID = "Chaos";
    public static final String IMG = "images/relics/Chaos.png";
    public static final String DESCRIPTION = "打乱所有道具池。";


    public Chaos() {
        super("Chaos", new Texture(Gdx.files.internal("images/relics/Chaos.png")), RelicTier.SHOP, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Chaos();
    }


    @Override
    public void onEquip() {
        super.onEquip();
        List<String> all = new ArrayList<>();
        int otherRelics = RelicLibrary.redList.size() + RelicLibrary.blueList.size() + RelicLibrary.greenList.size();
        if (AbstractDungeon.player instanceof Ironclad) {
            otherRelics -= RelicLibrary.redList.size();
            for (AbstractRelic relic : RelicLibrary.greenList) {
                all.add(relic.relicId);
            }
            for (AbstractRelic relic : RelicLibrary.blueList) {
                all.add(relic.relicId);
            }
        }
        if (AbstractDungeon.player instanceof TheSilent) {
            otherRelics -= RelicLibrary.greenList.size();
            for (AbstractRelic relic : RelicLibrary.redList) {
                all.add(relic.relicId);
            }
            for (AbstractRelic relic : RelicLibrary.blueList) {
                all.add(relic.relicId);
            }
        }
        if (AbstractDungeon.player instanceof Defect) {
            otherRelics -= RelicLibrary.blueList.size();
            for (AbstractRelic relic : RelicLibrary.greenList) {
                all.add(relic.relicId);
            }
            for (AbstractRelic relic : RelicLibrary.redList) {
                all.add(relic.relicId);
            }
        }
        int n = otherRelics / 6;
        int m = otherRelics % 6;
        int devilOnly = IsaacMod.devilOnlyRelics.size() + n;
        int rare = AbstractDungeon.rareRelicPool.size() + n;
        int uncommon = AbstractDungeon.uncommonRelicPool.size() + n;
        int common = AbstractDungeon.commonRelicPool.size() + n + m;
        int boss = AbstractDungeon.bossRelicPool.size() + n;
        int shop = AbstractDungeon.shopRelicPool.size() + n;
        addAndClear(all, IsaacMod.devilOnlyRelics);
        addAndClear(all, AbstractDungeon.rareRelicPool);
        addAndClear(all, AbstractDungeon.uncommonRelicPool);
        addAndClear(all, AbstractDungeon.commonRelicPool);
        addAndClear(all, AbstractDungeon.bossRelicPool);
        addAndClear(all, AbstractDungeon.shopRelicPool);
        Collections.shuffle(all, new Random(AbstractDungeon.miscRng.randomLong()));
        for (int i = 0; i < devilOnly; i++) {
            addAndRemove(all, IsaacMod.devilOnlyRelics);
        }
        for (int i = 0; i < rare; i++) {
            addAndRemove(all, AbstractDungeon.rareRelicPool);
        }
        for (int i = 0; i < uncommon; i++) {
            addAndRemove(all, AbstractDungeon.uncommonRelicPool);
        }
        for (int i = 0; i < boss; i++) {
            addAndRemove(all, AbstractDungeon.bossRelicPool);
        }
        for (int i = 0; i < common; i++) {
            addAndRemove(all, AbstractDungeon.commonRelicPool);
        }
        for (int i = 0; i < shop; i++) {
            addAndRemove(all, AbstractDungeon.shopRelicPool);
        }
    }

    private void addAndClear(List<String> list, List<String> toAdd) {
        list.addAll(toAdd);
        toAdd.clear();
    }

    private void addAndRemove(List<String> all, List<String> toAdd) {
        if (all.size() > 0) {
            toAdd.add(all.get(0));
            all.remove(0);
        }
    }


    @Override
    public void onVictory() {
        super.onVictory();
    }

    @Override
    public void update() {
        super.update();
    }
}
