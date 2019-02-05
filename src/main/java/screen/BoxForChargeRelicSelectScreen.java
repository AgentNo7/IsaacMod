package screen;

import basemod.abstracts.CustomRelic;
import cards.Battery;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mymod.IsaacMod;
import relics.Diplopia;
import relics.KeepersGift;
import relics.SharpPlug;
import relics.abstracrt.ChargeableRelic;
import utils.Utils;

import java.util.Collection;

public class BoxForChargeRelicSelectScreen extends RelicSelectScreen {

    public Battery battery = null;
    public Diplopia diplopia = null;
    public KeepersGift selectARelic = null;
    public SharpPlug sharpPlug = null;

    public BoxForChargeRelicSelectScreen() {
        super();
    }

    public BoxForChargeRelicSelectScreen(boolean canSkip) {
        super(canSkip);
    }

    public BoxForChargeRelicSelectScreen(Collection<? extends AbstractRelic> c) {
        super(c);
    }

    public BoxForChargeRelicSelectScreen(String bDesc, String title, String desc) {
        super(bDesc, title, desc);
    }

    public BoxForChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc, Battery battery) {
        super(canSkip, bDesc, title, desc);
        this.battery = battery;
    }

    public BoxForChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc, SharpPlug sharpPlug) {
        super(canSkip, bDesc, title, desc);
        this.sharpPlug = sharpPlug;
    }

    public BoxForChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc) {
        super(canSkip, bDesc, title, desc);
    }

    public BoxForChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc, Diplopia diplopia) {
        super(canSkip, bDesc, title, desc);
        this.diplopia = diplopia;
    }

    public BoxForChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc, KeepersGift keepersGift) {
        super(canSkip, bDesc, title, desc);
        this.selectARelic = keepersGift;
    }

    public BoxForChargeRelicSelectScreen(Collection<? extends AbstractRelic> c, boolean canSkip) {
        super(c, canSkip);
    }

    public BoxForChargeRelicSelectScreen(Collection<? extends AbstractRelic> c, boolean canSkip, String bDesc, String title, String desc) {
        super(c, canSkip, bDesc, title, desc);
    }

    @Override
    protected void addRelics() {
        if (battery != null || sharpPlug != null) {
            for (AbstractRelic r : AbstractDungeon.player.relics)
                if (r instanceof ChargeableRelic)
                    this.relics.add(r.makeCopy());
        }
        if (diplopia != null) {
            for (AbstractRelic r : AbstractDungeon.player.relics)
                this.relics.add(r.makeCopy());
        }
        if (selectARelic != null) {
            obtainRandomRelics(3);
        }
    }

    @Override
    protected void afterSelected() {
        if (battery != null) {
            ChargeableRelic relic = (ChargeableRelic) AbstractDungeon.player.getRelic(this.selectedRelic.relicId);
            if (relic.maxCharge <= 6) {
                relic.counter = relic.maxCharge;
            } else {
                relic.counter = (relic.counter < 6) ? 6 : relic.maxCharge;
            }
        }
        if (diplopia != null) {
            diplopia.addRelic = this.selectedRelic;
        }
        if (selectARelic != null) {
            selectARelic.addRelic = this.selectedRelic;
        }
    }

    private void obtainRandomRelics(int num) {
        try {
            String[] rnd = Utils.getRandomRelics(IsaacMod.relics.size(), num);
            for (String relic : rnd) {
                Class c = Class.forName("relics." + relic);
                CustomRelic rndRelic = (CustomRelic) c.newInstance();
                this.relics.add(rndRelic.makeCopy());
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("你没找到类");
            e.printStackTrace();
        }
    }

    @Override
    protected void afterCanceled() {
    }
}