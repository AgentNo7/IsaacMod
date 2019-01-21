package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MembershipCard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import screen.BloodShopScreen;

import java.util.ArrayList;

public class SteamSale extends CustomRelic {
    public static final String ID = "SteamSale";
    public static final String IMG = "images/relics/SteamSale.png";
    public static final String DESCRIPTION = "商店全部半价，如果同时持有会员卡，商店免费。";

    public SteamSale() {
        super("SteamSale", new Texture(Gdx.files.internal("images/relics/SteamSale.png")), RelicTier.SHOP, LandingSound.CLINK);
    }

    private boolean used = false;

    private boolean isDouble() {
        ArrayList<AbstractRelic> relics = AbstractDungeon.player.relics;
        int cnt = 0;
        for (AbstractRelic relic : relics) {
            if (relic.relicId.equals(SteamSale.ID)) {
                cnt++;
            }
        }
        return cnt >= 2;
    }

    private void addDiscount() {
        if (AbstractDungeon.player.hasRelic(MembershipCard.ID) || isDouble()) {
            AbstractDungeon.shopScreen.applyDiscount(0.0F, true);
            used = false;
            ShopScreen.actualPurgeCost = 0;
        } else {
            AbstractDungeon.shopScreen.applyDiscount(0.5F, true);
        }
    }

    private boolean getInShop = false;

    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.player != null && (AbstractDungeon.player.hasRelic(MembershipCard.ID) || isDouble())) {
            getInShop = true;
        }
        if (AbstractDungeon.currMapNode != null) {
            if (AbstractDungeon.getCurrRoom() instanceof ShopRoom && !(AbstractDungeon.shopScreen instanceof BloodShopScreen)) {
                if (getInShop || !used) {
                    this.flash();
                    this.beginLongPulse();
                    addDiscount();
                    used = true;
                    getInShop = false;
                }
            }
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        used = false;
        if (room instanceof ShopRoom) {
            this.flash();
            this.pulse = true;
        } else {
            this.pulse = false;
        }

    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new SteamSale();
    }

}
