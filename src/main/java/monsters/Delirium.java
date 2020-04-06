package monsters;

import actions.DeliriumAction;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import monsters.Intent.Move;
import powers.ConceitedPower;
import powers.DeliriousPower;
import powers.ImpatiencePower;
import utils.Invoker;

import java.util.ArrayList;

public class Delirium extends CustomMonster {

    public static final String NAME;

    private int base = 1;

    private boolean isWeaken = false;

    public boolean isWeaken() {
        return isWeaken;
    }

    public void setWeaken(boolean weaken) {
        isWeaken = weaken;
    }

    public static TempMusic tempMusic = new TempMusic("SHRINE", true, true);

    public GenericEventDialog imageEventText = new GenericEventDialog();

    public static TempMusic getTempMusic() {
        tempMusic = new TempMusic("SHRINE", true, true);
        String s = "sounds/musics/nativitate-intro" + AbstractDungeon.aiRng.random(0, 2) + ".ogg";
        Music music = MainMusic.newMusic(s);
        music.setLooping(true);
        music.play();
        music.setVolume(0.0F);
        Invoker.setField(tempMusic, "music", music);
        tempMusic.isFadingOut = false;
        tempMusic.isDone = false;
        return tempMusic;
    }


    public Delirium(float x, float y) {
        super(NAME, "Delirium", 10000, -8.0F, 0.0F, 360.0F, 240.0F, null, x, y);
        this.img = new Texture("images/monsters/Delirium.png");
        this.type = EnemyType.BOSS;
        this.setHp(10000);
        this.damage.add(new DamageInfo(this, base));
        this.setMove((byte) Move.UNKNOWN.id, Intent.UNKNOWN);
    }

    private boolean addedPower = false;

    private void addBuff() {
        if (!hasPower(DeliriousPower.POWER_ID)) {
            addedPower = true;
            this.addPower(new DeliriousPower(this, 0));
            this.addPower(new ImpatiencePower(this));
        }
    }

    @Override
    public void takeTurn() {
        Move nextMove = Move.getMove(this.nextMove);
        if (isWeaken) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Settings.language == Settings.GameLanguage.ZHS
                    ? "哦，不，我似乎清不了debuff" : "No! I can't remove the debuff anymore~~", 1.0f, 2.0f));
        }
        switch (nextMove) {
            case MULATTACK:
                DamageInfo damageInfo = new DamageInfo(this, base);
                for (int i = 0; i < 111; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damageInfo, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
                }
                break;
            case BUFF:
                addBuff();
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, InvinciblePower.POWER_ID));
                break;
            case UNKNOWN:
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, 2000), 2000));
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        ArrayList<TempMusic> tempMusics = Invoker.getField(CardCrawlGame.music, "tempTrack");
        if (tempMusics != null) {
            tempMusics.add(getTempMusic());
        }
        CardCrawlGame.music.silenceBGMInstantly();
        this.imageEventText.loadImage("images/1024PortraitsBeta/curse/shame.png");
        int language = Settings.language == Settings.GameLanguage.ZHS ? 0 : 1;
        this.imageEventText.setDialogOption(options[2 * language]);
        imageEventText.optionList.get(0).slot = -3;
        this.imageEventText.setDialogOption(options[2 * language + 1]);
        imageEventText.optionList.get(1).slot = -2;
//        this.imageEventText.setDialogOption("", true);
        this.imageEventText.show(titleText[language], bodyText[language]);
        AbstractDungeon.actionManager.addToBottom(new DeliriumAction(this));
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private static String bodyText[] = {
            "你感受到了一股前所未有的强大力量正在逼近，这让你不由得 #r胆怯 了起来。 NL 幸运的是，前面有一个按钮可以让你选择。 NL 你上前仔细一看，按钮上歪歪斜斜的每个角落都写着 #p~丢人~ 二字。 NL 你要如何选择呢？",
            "You feel that an unprecedented force is approaching, which makes you feel #rtimid. NL Fortunately, there are two buttons in front that let you choose. NL When you look forward, the one writes proof of the #ybrave, and the other writes compromise of the #rweak NL, what do you want to choose?"
    };

    private static String titleText[] = {
            "以前我没得选，现在我。。。",
            "It's up to you"
    };

    private static String options[] = {
            "开玩笑，我超勇的。",
            "⑧说了，我率先丢人。",
            "Face challenge.",
            "Play safe."
    };

    @Override
    public void update() {
        super.update();
        if (!addedPower && currentHealth <= maxHealth * 0.7 && !hasPower(DeliriousPower.POWER_ID)) {
            addBuff();
        }
    }

    @Override
    public void die() {
        super.die();
        ConceitedPower.canSee = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        imageEventText.render(sb);
    }

    @Override
    protected void getMove(int i) {
        if (!this.hasPower(ImpatiencePower.POWER_ID)) {
            this.setMove((byte) Move.BUFF.id, Intent.BUFF);
            return;
        }
        this.setMove((byte) Move.MULATTACK.id, Intent.ATTACK, base, 111, true);
    }

    @Override
    public void createIntent() {
        super.createIntent();
//        AbstractDungeon.actionManager.addToTop(new TalkAction(this, "Pure Damage!", 1.0f, 2.0f));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        EnemyMoveInfo moveInfo = Invoker.getField(this, "move");
        if (moveInfo != null && moveInfo.intent == Intent.ATTACK) {
            if ((int) Invoker.getField(this, "intentDmg") != -1) {
                Invoker.setField(this, "intentDmg", 1);
            }
            Invoker.invoke(this, "updateIntentTip");
        }
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "百变怪";
        } else {
            NAME = "Delirium";
        }
    }

}
