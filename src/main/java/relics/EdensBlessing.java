package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class EdensBlessing extends CustomRelic {
    public static final String ID = "EdensBlessing";
    public static final String IMG = "images/relics/EdensBlessing.png";
    public static final String DESCRIPTION = "拾取后下一局游戏开始获得一个随机稀有遗物，本局游戏在战斗开始随机获得 #b1 点力量，敏捷或者集中。";

    public static boolean obtained = false;

    static {
        File obtained = new File(".data");
        if (obtained.exists()) {
            obtained.delete();
            EdensBlessing.obtained = true;
        }
    }

    public static void show() {
        AbstractDungeon.player.getRelic("EdensBlessing").flash();
    }

    public EdensBlessing() {
        super("EdensBlessing", new Texture(Gdx.files.internal("images/relics/EdensBlessing.png")), RelicTier.RARE, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new EdensBlessing();
    }

    @Override
    public void onEquip() {
        obtained = true;
        File obtained = new File(".data");
        if (!obtained.exists()) {
            try {
                obtained.createNewFile();
            } catch (IOException e) {
                System.out.println("创建文件失败");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void atBattleStart() {
        this.flash();
        int roll = new Random().nextInt(3);
        switch (roll) {
            case 0:{
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                break;
            }
            case 1:{
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                break;
            }
            case 2:{
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1), 1));
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                break;
            }
        }
    }

}
