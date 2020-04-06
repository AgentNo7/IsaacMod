package screen;

import basemod.abstracts.CustomRelic;
import cards.Battery;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import mymod.IsaacMod;
import relics.*;
import relics.Void;
import relics.abstracrt.ChargeableRelic;
import relics.abstracrt.ClickableRelic;
import utils.Utils;

import java.util.Collection;

public class ChargeRelicSelectScreen extends RelicSelectScreen {

    public Battery battery = null;
    public Diplopia diplopia = null;
    public KeepersGift selectARelic = null;
    public SharpPlug sharpPlug = null;
    public Void Void = null;

    public ChargeRelicSelectScreen() {
        super();
    }

    public ChargeRelicSelectScreen(boolean canSkip) {
        super(canSkip);
    }

    public ChargeRelicSelectScreen(Collection<? extends AbstractRelic> c) {
        super(c);
    }

    public ChargeRelicSelectScreen(String bDesc, String title, String desc) {
        super(bDesc, title, desc);
    }

    public ChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc, Battery battery) {
        super(canSkip, bDesc, title, desc);
        this.battery = battery;
    }

    public ChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc, SharpPlug sharpPlug) {
        super(canSkip, bDesc, title, desc);
        this.sharpPlug = sharpPlug;
    }

    public ChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc) {
        super(canSkip, bDesc, title, desc);
    }

    public ChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc, Diplopia diplopia) {
        super(canSkip, bDesc, title, desc);
        this.diplopia = diplopia;
    }

    public ChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc, KeepersGift keepersGift) {
        super(canSkip, bDesc, title, desc);
        this.selectARelic = keepersGift;
    }

    public ChargeRelicSelectScreen(Collection<? extends AbstractRelic> c, boolean canSkip) {
        super(c, canSkip);
    }

    public ChargeRelicSelectScreen(Collection<? extends AbstractRelic> c, boolean canSkip, String bDesc, String title, String desc) {
        super(c, canSkip, bDesc, title, desc);
    }

    public ChargeRelicSelectScreen(boolean canSkip, String bDesc, String title, String desc, Void Void, int count) {
        super(canSkip, bDesc, title, desc);
        this.Void = Void;
        this.count = count;
    }

    private int count;

    @Override
    protected void addRelics() {
        if (battery != null) {
            for (AbstractRelic r : AbstractDungeon.player.relics)
                if (r instanceof ChargeableRelic)
                    this.relics.add(r);
        } else if(sharpPlug != null){
            for (AbstractRelic r : AbstractDungeon.player.relics)
                if (r instanceof ChargeableRelic && r.counter % ((ChargeableRelic) r).maxCharge == 0) {
                    this.relics.add(r);
                }
        } else if (diplopia != null) {
            this.relics.addAll(AbstractDungeon.player.relics);
            this.relics.remove(diplopia);
        } else if (selectARelic != null) {
            obtainRandomRelics(3);
        } else if (Void != null) {
            for (AbstractRelic r : AbstractDungeon.player.relics)
                if (!(r instanceof MarkOfTheBloom) && !(r instanceof Void) && !(r instanceof HushsDoor))
                    this.relics.add(r);
        }
    }

    @Override
    protected void afterSelected() {
        if (battery != null) {
            ChargeableRelic relic = (ChargeableRelic) (this.selectedRelic);
            relic.counter += relic.maxCharge;
            if (AbstractDungeon.player.hasRelic(TheBattery.ID) && relic.counter >= relic.maxCharge) {
                if (relic.maxCharge <= 6) {
                    relic.counter = 2 * relic.maxCharge;
                } else {
                    relic.counter += relic.counter + 6 < 2 * relic.maxCharge ? relic.counter + 6 : 2 * relic.maxCharge;
                }
            } else {
                if (relic.maxCharge <= 6) {
                    relic.counter = relic.maxCharge;
                } else {
                    relic.counter += relic.counter < 6 ? 6 : relic.maxCharge;
                }
            }
            AbstractDungeon.player.reorganizeRelics();
        } else if (sharpPlug != null) {
            ChargeableRelic relic = (ChargeableRelic) (this.selectedRelic);
            if (AbstractDungeon.player.hasRelic(TheBattery.ID) && relic.counter == relic.maxCharge) {
                relic.counter += relic.maxCharge;
            }else {
                relic.counter = relic.maxCharge;
            }
            AbstractDungeon.player.reorganizeRelics();
        } else if (diplopia != null) {
            diplopia.addRelic = this.selectedRelic;
            IsaacMod.obtainRelics.add(selectedRelic.makeCopy());
            IsaacMod.removeRelics.add(diplopia.relicId);
        } else if (selectARelic != null) {
            selectARelic.addRelic = this.selectedRelic;
            IsaacMod.obtainRelics.add(selectedRelic);
            IsaacMod.removeRelics.add(selectARelic.relicId);
            Utils.removeFromPool(selectARelic.addRelic);
        } else if (Void != null) {
            if (this.selectedRelic instanceof ClickableRelic) {
                ClickableRelic clickableRelic = (ClickableRelic) this.selectedRelic;
                if (clickableRelic instanceof ChargeableRelic) {
                    ChargeableRelic chargeableRelic = (ChargeableRelic) clickableRelic;
                    chargeableRelic.counter = chargeableRelic.maxCharge;
                }
                if (!(clickableRelic instanceof Diplopia)) {
                    Void.relicList.add(clickableRelic);
                }
                if (count == Void.maxCharge) {
                    clickableRelic.onRightClick();
                }
            } else {
                Void.addPower();
                Void.tips.clear();
                Void.tips.add(new PowerTip(Void.name, Void.getUpdatedDescription()));
            }
            Void.counter++;
            IsaacMod.removeRelics.add(this.selectedRelic.relicId);
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