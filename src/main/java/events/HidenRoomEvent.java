package events;

import cards.Bomb;
import cards.DoctorFetus;
import cards.EpicFetus;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import relics.DoctorsRemote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HidenRoomEvent extends AbstractImageEvent {
    public static final String ID = "HidenRoomEvent";
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static final String DIALOG_1;
    private static final String DIALOG_2;
    private static final String DIALOG_3;
    private HidenRoomEvent.CurScreen screen;

    private boolean hasCard(String id) {
        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard card : cards) {
            if (card.cardID.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public HidenRoomEvent() {
        super(NAME, DIALOG_1, "images/relics/mindBloom.jpg");
        this.screen = HidenRoomEvent.CurScreen.INTRO;
        //1
        if (hasCard(Bomb.ID)) {
            this.imageEventText.setDialogOption(OPTIONS[0]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[1], true);
        }
        //2
        if (hasCard(EpicFetus.ID) || hasCard(DoctorFetus.ID)) {
            this.imageEventText.setDialogOption(OPTIONS[2]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[3], true);
        }
        //3
        if (AbstractDungeon.player.hasRelic(DoctorsRemote.ID)) {
            this.imageEventText.setDialogOption(OPTIONS[4]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[5], true);
        }
        //4
//        if ()) {
//            this.imageEventText.setDialogOption(OPTIONS[6]);
//        } else {
//            this.imageEventText.setDialogOption(OPTIONS[7], true);
//        }
        this.imageEventText.setDialogOption(OPTIONS[6]);
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        AbstractDungeon.player.masterDeck.removeCard(Bomb.ID);
                        this.imageEventText.updateBodyText(DIALOG_2);
                        this.screen = HidenRoomEvent.CurScreen.FIGHT;
                        logMetric("HidenRoomEvent", "Fight");
                        ArrayList<String> list = new ArrayList();
                        list.add("Cultist");
                        Collections.shuffle(list, new Random(AbstractDungeon.miscRng.randomLong()));
                        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(list.get(0));
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().addGoldToRewards(100);
                        AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.RARE);
                        this.enterCombatFromImage();
                        AbstractDungeon.lastCombatMetricKey = "Hiden Room Boss Battle";
                        break;
                    case 1:
                        break;
                    case 2:
                        if (AbstractDungeon.player.hasRelic(DoctorsRemote.ID)) {
                            AbstractDungeon.player.getRelic(DoctorsRemote.ID).counter = 0;
                        }
                        break;
                    case 3:
                        this.openMap();
                        break;
                    case 4:
                        this.openMap();
                        break;
                    default:
                        this.openMap();
                }
//                this.imageEventText.updateDialogOption(0, OPTIONS[8]);
//                this.imageEventText.clearRemainingOptions();
                break;
            case LEAVE:
                this.openMap();
                break;
            default:
                this.openMap();
        }

    }

    static {
//        eventStrings = CardCrawlGame.languagePack.getEventString("HidenRoomEvent");
        NAME = "风水宝地？";
        DESCRIPTIONS = new String[]{"你走到这个房间时，发现这个房间布局不太一般,好像是个风水宝地。现在你看了下自己手里的 …… #p~炸弹……~ NL NL 里面可能有各种 #r怪物 与 #y财宝。 NL 到底要不要相信自己的直觉呢？",
                "bang！炸了。",
                "bang！炸了。"
        };
        OPTIONS = new String[]{"[放个炸弹]删除牌库中的炸弹并进入隐藏房",
                "[锁定] 需要：炸弹。",
                "[使用史诗或者博士]进入隐藏房。",
                "[锁定] 需要：史诗婴儿或婴儿博士。",
                "[使用博士的遥控器]消耗遥控器的充能并进入隐藏房。",
                "[锁定] 需要：博士的遥控器并且满充能。",
                "[离开]"
        };
        DIALOG_1 = DESCRIPTIONS[0];
        DIALOG_2 = DESCRIPTIONS[1];
        DIALOG_3 = DESCRIPTIONS[2];
    }

    private static enum CurScreen {
        INTRO,
        FIGHT,
        LEAVE;

        private CurScreen() {
        }
    }
}

