package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.CursedTome;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Enchiridion;
import com.megacrit.cardcrawl.relics.Necronomicon;
import com.megacrit.cardcrawl.relics.NilrysCodex;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.shop.Merchant;
import relics.abstracrt.ChargeableRelic;
import screen.BloodShopScreen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class D6 extends ChargeableRelic {
    public static final String ID = "D6";
    public static final String IMG = "images/relics/D6.png";
    public static final String DESCRIPTION = "六充能，满充能时右击可以roll商店，遗物，宝箱和boss宝箱。";

    public D6() {
        super("D6", new Texture(Gdx.files.internal("images/relics/D6.png")), RelicTier.RARE, LandingSound.CLINK, 6);
    }

    private static List<String> books = Arrays.asList(Necronomicon.ID, Enchiridion.ID, NilrysCodex.ID);

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new D6();
    }

    //右键开roll
    public void onRightClick() {
        if (counter >= maxCharge) {
            AbstractRoom room = AbstractDungeon.currMapNode.room;
            if (room instanceof ShopRoom) {
                ShopRoom shopRoom = (ShopRoom) room;
                shopRoom.merchant = new Merchant();
                show();
                if (AbstractDungeon.player.hasRelic(SteamSale.ID) && !(AbstractDungeon.shopScreen instanceof BloodShopScreen)) {
                    AbstractDungeon.shopScreen.applyDiscount(0.5F, true);
                }
                counter = 0;
            }
            else if (room instanceof TreasureRoomBoss) {
                BossChest bossChest = (BossChest) ((TreasureRoomBoss) room).chest;
                if (bossChest != null && bossChest.isOpen && AbstractDungeon.bossRelicScreen.relics.size() != 0) {
                    bossChest.relics.clear();
                    for(int i = 0; i < 3; ++i) {
                        bossChest.relics.add(AbstractDungeon.returnRandomRelic(RelicTier.BOSS));
                    }
                    bossChest.open(true);
                    show();
                    counter = 0;
                }
            }
            else {
                if (AbstractDungeon.combatRewardScreen.rewards != null && AbstractDungeon.combatRewardScreen.rewards.size() > 0) {
                    for (RewardItem item : AbstractDungeon.combatRewardScreen.rewards) {
                        if (!item.isDone && item.relic != null) {
                            if (books.contains(item.relic.relicId)) {
                                try {
                                    Method randomBook = CursedTome.class.getDeclaredMethod("randomBook");
                                    randomBook.setAccessible(true);
                                    randomBook.invoke(new CursedTome());
                                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                AbstractRelic relic = AbstractDungeon.returnRandomRelic(item.relic.tier);
                                item.relic = relic;
                                item.text = relic.name;
                                AbstractDungeon.combatRewardScreen.open();
                            }
                        }
                    }
                    show();
                    counter = 0;
                }
            }
            this.pulse = false;
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
