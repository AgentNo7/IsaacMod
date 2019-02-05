package events;

import cards.Bomb;
import cards.DoctorFetus;
import cards.EpicFetus;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.TheBomb;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import mymod.IsaacMod;
import relics.DoctorsRemote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HidenRoomEvent extends AbstractImageEvent {
    public static final String ID = "HidenRoomEvent";
    private static final EventStrings eventStrings;
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
        super(NAME, DIALOG_1, "images/events/fengshui.png");
        this.screen = HidenRoomEvent.CurScreen.INTRO;
        //1
        if (hasCard(Bomb.ID) || hasCard(TheBomb.ID)) {
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
        this.imageEventText.setDialogOption(OPTIONS[6]);
        this.imageEventText.setDialogOption(OPTIONS[7]);
    }

    private void battle() {
        this.imageEventText.updateBodyText(DIALOG_2);
        logMetric("HidenRoomEvent", "Fight");
        List<String> list = new ArrayList<>();
        list.add("3 Cultists");
        list.add("Colosseum Nobs");
        list.add("2 Thieves");
        Collections.shuffle(list, new Random(AbstractDungeon.miscRng.randomLong()));
        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(list.get(0));
        if (AbstractDungeon.floorNum < 17) {
            list.set(0, "Colosseum Slavers");
        }
        if (list.get(0).equals("2 Thieves")) {
            AbstractDungeon.getCurrRoom().monsters.addMonster(new Looter(-200.0F, 415.0F));
            AbstractDungeon.getCurrRoom().monsters.addMonster(new Mugger(80.0F, 400.0F));
        }
        AbstractDungeon.getCurrRoom().rewards.clear();
        AbstractDungeon.getCurrRoom().addGoldToRewards(50);
        AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.UNCOMMON);
        AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.COMMON);
        this.enterCombatFromImage();
        leave();
        AbstractDungeon.lastCombatMetricKey = "Hiden Room Battle";
    }

    private void leave() {
        this.imageEventText.clearRemainingOptions();
        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
        this.screen = CurScreen.LEAVE;
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        if (hasCard(TheBomb.ID)) {
                            AbstractDungeon.player.masterDeck.removeCard(TheBomb.ID);
                        } else {
                            AbstractDungeon.player.masterDeck.removeCard(Bomb.ID);
                        }
                        battle();
                        break;
                    case 1:
                        if (AbstractDungeon.eventRng.randomBoolean(0.75F)) {
                            if (AbstractDungeon.eventRng.randomBoolean(0.25F)) {
                                battle();
                            } else {
                                this.imageEventText.updateBodyText(DIALOG_2);
                                AbstractDungeon.getCurrRoom().rewards.clear();
                                AbstractDungeon.getCurrRoom().addGoldToRewards(100);
                                AbstractDungeon.combatRewardScreen.open();
                                leave();
                            }
                        } else {
                            this.imageEventText.updateBodyText(DIALOG_3);
                            leave();
                        }
                        break;
                    case 2:
                        if (AbstractDungeon.eventRng.randomBoolean(0.75F)) {
                            if (AbstractDungeon.eventRng.randomBoolean(0.25F)) {
                                battle();
                            } else {
                                this.imageEventText.updateBodyText(DIALOG_2);
                                if (AbstractDungeon.player.hasRelic(DoctorsRemote.ID)) {
                                    AbstractDungeon.player.getRelic(DoctorsRemote.ID).counter = 0;
                                }
                                AbstractDungeon.player.gainGold(300);
                                for (int i = 0; i < 300; ++i) {
                                    AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                                }
                                leave();
                            }
                        } else {
                            this.imageEventText.updateBodyText(DIALOG_3);
                            leave();
                        }
                        break;
                    case 3:
                        AbstractDungeon.player.damage(/*EL:77*/new DamageInfo(AbstractDungeon.player, 15, DamageInfo.DamageType.HP_LOSS));
                        if (AbstractDungeon.eventRng.randomBoolean(0.25F)) {
                            battle();
                        } else {
                            if (AbstractDungeon.eventRng.randomBoolean(0.85F)) {
                                this.imageEventText.updateBodyText(DIALOG_2);
                                if (AbstractDungeon.eventRng.randomBoolean(0.35F)) {
                                    IsaacMod.obtain(AbstractDungeon.player, AbstractDungeon.returnRandomRelic(RelicTier.COMMON), false);
                                } else {
                                    int gold = AbstractDungeon.eventRng.random(40, 100);
                                    AbstractDungeon.player.gainGold(gold);
                                    for (int i = 0; i < gold; ++i) {
                                        AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                                    }
                                }
                                leave();
                            } else {
                                this.imageEventText.updateBodyText(DIALOG_3);
                                leave();
                            }
                        }
                        break;
                    case 4:
                        this.openMap();
                        break;
                    default:
                        this.openMap();
                }
                break;
            case LEAVE:
                this.openMap();
                break;
            default:
                this.openMap();
        }

    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString("HidenRoomEvent");
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
//        NAME = "风水宝地？";
//        DESCRIPTIONS = new String[]{"你走到这个房间时，发现这个房间布局不太一般,好像是个风水宝地。现在你看了下自己手里的 #p~……炸弹……~ NL NL 里面可能有各种 #r怪物 与 #y财宝。 NL 到底要不要相信自己的直觉呢？",
//                "#r~bang！炸了。~ NL  NL 真的是隐藏房，不愧是风水大师。",
//                "#r~bang！炸了。~ NL  NL 什么都没有发生，风水大师的陨落。"
//        };
//        OPTIONS = new String[]{"[放个炸弹]删除牌库中的炸弹并进入隐藏房",
//                "[锁定] 需要：炸弹。",
//                "[使用史诗或者博士]进入隐藏房。",
//                "[锁定] 需要：史诗婴儿或婴儿博士。",
//                "[使用博士的遥控器]消耗遥控器的充能并进入隐藏房。",
//                "[失去15生命] 没有炸弹的你打算用头把这堵墙撞破。",
//                "[锁定] 需要：博士的遥控器并且满充能。",
//                "[离开]"
//        };
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

