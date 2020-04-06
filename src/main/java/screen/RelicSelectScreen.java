package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static com.megacrit.cardcrawl.relics.AbstractRelic.relicPage;

public abstract class RelicSelectScreen {
    private static final float X = 1670.0F * Settings.scale;
    private static final float Y = 500.0F * Settings.scale;
    private static final float TEXT_X = 1400.0F * Settings.scale;
    private static final float SPACE_Y = 58.0F * Settings.scale;

    private static final float SPACE = 80.0F * Settings.scale;
    private static final float START_X = 600.0F * Settings.scale;
    private static final float START_Y = Settings.HEIGHT - 300.0F * Settings.scale;
    private float scrollY = START_Y;
    private int row = 0;
    private int col = 0;
    private ConfirmButton button;
    private static final Color RED_OUTLINE_COLOR = new Color(-10132568);
    private static final Color GREEN_OUTLINE_COLOR = new Color(2147418280);
    private static final Color BLUE_OUTLINE_COLOR = new Color(-2016482392);
    private static final Color BLACK_OUTLINE_COLOR = new Color(168);
    private AbstractRelic hoveredRelic;
    private AbstractRelic clickStartedRelic;
    private String bottomDesc = "提示";
    private String infoTitle = "标题";
    private String infoDesc = "描述";
    private static boolean isPrevMouseDown = false;

    private float scrollLowerBound;
    private float scrollUpperBound;
    private boolean grabbedScreen;
    private float targetY;
    private float grabStartY;

    /**
     * 被选中的遗物
     */
    public AbstractRelic selectedRelic;

    /**
     * 用于选择遗物的遗物列表
     */
    protected ArrayList<AbstractRelic> relics = new ArrayList<AbstractRelic>();

    /**
     * 该窗口当前是否开启，开启之前不会显示内容
     */
    protected boolean isOpen = false;

    /**
     * 当前的选择窗口，设计上不支持同时存在多个遗物选择窗口，所以使用这个静态变量来存储当前窗口
     */
    public static RelicSelectScreen screen = null;

    public RelicSelectScreen() {
        this(null, true);
    }

    public RelicSelectScreen(boolean canSkip) {
        this(null, canSkip);
    }

    public RelicSelectScreen(Collection<? extends AbstractRelic> c) {
        this(c, true);
    }

    public RelicSelectScreen(String bDesc, String title, String desc) {
        this(true, bDesc, title, desc);
    }

    public RelicSelectScreen(boolean canSkip, String bDesc, String title, String desc) {
        this(null, canSkip, bDesc, title, desc);
    }

    public RelicSelectScreen(Collection<? extends AbstractRelic> c, boolean canSkip) {
        this(c, canSkip, null, null, null);
    }

    /**
     * 构造方法，自动将构造出的实例赋值给静态变量screen以便于使用
     *
     * @param c       要显示的遗物集合
     * @param canSkip 能否跳过
     * @param bDesc   在选择界面下方的小字提醒
     * @param title   遗物列表的标题
     * @param desc    遗物列表的描述
     */
    public RelicSelectScreen(Collection<? extends AbstractRelic> c, boolean canSkip, String bDesc, String title, String desc) {
        this.button = new ConfirmButton("跳过");
        this.button.isDisabled = !canSkip;
        this.setDescription(bDesc, title, desc);
        if (c != null)
            this.relics.addAll(c);
        isPrevMouseDown = false;
        screen = this;
        this.scrollLowerBound = 1000.0F * Settings.scale;
        this.scrollUpperBound = 3500.0F * Settings.scale;
        this.targetY = this.scrollY;
        this.grabbedScreen = false;
    }

    /**
     * 设置窗口的提示信息
     *
     * @param bDesc 在选择界面下方的小字提醒
     * @param title 遗物列表的标题
     * @param desc  遗物列表的描述
     */
    public void setDescription(String bDesc, String title, String desc) {
        if (bDesc != null)
            this.bottomDesc = bDesc;
        if (title != null)
            this.infoTitle = title;
        if (desc != null)
            this.infoDesc = desc;
    }

    /**
     * 开启当前选择窗口，可以在子类的构造方法的最后调用来达到自动开启的效果
     */
    public static void openScreen() {
        screen.open();
    }

    /**
     * 开启选择窗口，可以在子类的构造方法的最后调用来达到自动开启的效果，也可以在构造方法后直接调用
     */
    public void open() {
        this.isOpen = true;
        if (this.relics.isEmpty())
            this.addRelics();
        this.button.show();
        this.scrollY = (Settings.HEIGHT - 300.0F * Settings.scale);
        this.targetY = (float)Settings.HEIGHT - 300.0F * Settings.scale;
        CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.RELIC_VIEW;
    }

    /**
     * 添加这个选遗物窗口里应该有的遗物
     */
    protected abstract void addRelics();

    /**
     * 当选完遗物后执行
     */
    protected abstract void afterSelected();

    /**
     * 当取消选择后执行
     */
    protected abstract void afterCanceled();

    private void update() {
        if (this.hoveredRelic != null) {
            CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
            if (InputHelper.justClickedLeft) {
                this.clickStartedRelic = this.hoveredRelic;
            }
            if ((InputHelper.justReleasedClickLeft) && (this.hoveredRelic == this.clickStartedRelic)) {
                this.selectedRelic = this.hoveredRelic;
                this.afterSelected();
                screen = null;
            }
        } else {
            this.clickStartedRelic = null;
        }
        if (!this.button.isDisabled) {
            this.button.update();
            if ((this.button.hb.clicked) || (InputHelper.pressedEscape)) {
                CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.NONE;
                screen = null;
                InputHelper.pressedEscape = false;
                AbstractDungeon.player.reorganizeRelics();
                this.button.hide();
                this.afterCanceled();
                CardCrawlGame.mainMenuScreen.panelScreen.refresh();
            }
        }
        InputHelper.justClickedLeft = false;
        this.hoveredRelic = null;
        this.updateScrolling();
        updateList(relics);
    }

    /**
     * 需要写在mod主类的receiveRender方法里
     */
    public static void updateRender(SpriteBatch sb) {
        if (screen != null && screen.isOpen) {
            sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
            screen.render(sb);
            Color c = new Color(1.0F, 1.0F, 1.0F, 0.0F);
            sb.setColor(c);
            sb.draw(ImageMaster.MAP_LEGEND, X - 256.0F, Y - 400.0F - DungeonMapScreen.offsetY / 50.0F, 256.0F, 400.0F,
                    512.0F, 800.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 800, false, false);
            Color c2 = new Color(MapRoomNode.AVAILABLE_COLOR.r, MapRoomNode.AVAILABLE_COLOR.g,
                    MapRoomNode.AVAILABLE_COLOR.b, c.a);
            FontHelper.renderFontCentered(sb, FontHelper.menuBannerFont, "IsaacMod", X,
                    Y + 170.0F * Settings.scale - DungeonMapScreen.offsetY / 50.0F, c2);
            sb.setColor(c2);
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.eventBodyText, screen.bottomDesc, TEXT_X - 50.0F * Settings.scale,
                    Y - SPACE_Y * 0.0F + 113.0F * Settings.scale - DungeonMapScreen.offsetY / 50.0F, c);
        }
    }

    /**
     * 需要写在mod主类的receivePreUpdate或receivePostUpdate里
     */
    public static void updateScreen() {
        if (screen != null) {
            if (screen.isOpen) {
                InputHelper.isMouseDown = Gdx.input.isButtonPressed(0);
                if ((!isPrevMouseDown) && (InputHelper.isMouseDown)) {
                    InputHelper.justClickedLeft = true;
                    isPrevMouseDown = true;
                } else if ((isPrevMouseDown) && (!InputHelper.isMouseDown)) {
                    InputHelper.justReleasedClickLeft = true;
                    isPrevMouseDown = false;
                }
                screen.update();
            }
        }
    }

    private void updateList(ArrayList<AbstractRelic> list) {
        for (AbstractRelic r : list) {
            r.hb.move(r.currentX, r.currentY);
            r.update();
            if (AbstractDungeon.player != null && AbstractDungeon.player.relics.indexOf(r) / 25 != relicPage) {
                r.hb.update();
                if (r.hb.hovered && AbstractDungeon.topPanel.potionUi.isHidden) {
                    r.scale = Settings.scale * 1.25F;
                    CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
                } else {
                    r.scale = MathHelper.scaleLerpSnap(r.scale, Settings.scale);
                }
            }
            if (r.hb.hovered) {
                this.hoveredRelic = r;
            }
        }
//        Iterator<AbstractRelic> var2 = list.iterator();
//        while (var2.hasNext()) {
//            AbstractRelic r = var2.next();
//            r.hb.move(r.currentX, r.currentY);
//            r.update();
//            if (r.hb.hovered) {
//                this.hoveredRelic = r;
//            }
//        }
    }

    private void render(SpriteBatch sb) {
        this.row = -1;
        this.col = 0;
        renderList(sb, this.infoTitle, this.infoDesc, relics);
        this.button.render(sb);
    }

    private void renderList(SpriteBatch sb, String msg, String desc, ArrayList<AbstractRelic> list) {
        this.row += 2;
        FontHelper.renderSmartText(sb, FontHelper.buttonLabelFont, msg, START_X - 50.0F * Settings.scale,
                this.scrollY + 4.0F * Settings.scale - SPACE * this.row, 99999.0F, 0.0F, Settings.GOLD_COLOR);
        FontHelper.renderSmartText(sb, FontHelper.cardDescFont_N, desc,
                START_X - 50.0F * Settings.scale
                        + FontHelper.getSmartWidth(FontHelper.buttonLabelFont, msg, 99999.0F, 0.0F),
                this.scrollY - 0.0F * Settings.scale - SPACE * this.row, 99999.0F, 0.0F, Settings.CREAM_COLOR);
        this.row += 1;
        this.col = 0;
        for (Iterator<AbstractRelic> var5 = list.iterator(); var5.hasNext(); this.col += 1) {
            AbstractRelic r = (AbstractRelic) var5.next();
            r.isSeen = true;
            if (this.col == 10) {
                this.col = 0;
                this.row += 1;
            }
            r.currentX = (START_X + SPACE * this.col);
            r.currentY = (this.scrollY - SPACE * this.row);
            r.hb.update();
            if (RelicLibrary.redList.contains(r)) {
                r.render(sb, false, RED_OUTLINE_COLOR);
            } else if (RelicLibrary.greenList.contains(r)) {
                r.render(sb, false, GREEN_OUTLINE_COLOR);
            } else if (RelicLibrary.blueList.contains(r)) {
                r.render(sb, false, BLUE_OUTLINE_COLOR);
            } else {
                r.render(sb, false, BLACK_OUTLINE_COLOR);
            }
        }
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (!this.grabbedScreen) {
            if (InputHelper.scrolledDown) {
                this.targetY += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                this.targetY -= Settings.SCROLL_SPEED;
            }

            if (InputHelper.justClickedLeft) {
                this.grabbedScreen = true;
                this.grabStartY = (float)y - this.targetY;
            }
        } else if (InputHelper.isMouseDown) {
            this.targetY = (float)y - this.grabStartY;
        } else {
            this.grabbedScreen = false;
        }

        this.scrollY = MathHelper.scrollSnapLerpSpeed(this.scrollY, this.targetY);
        this.resetScrolling();
    }

    private void resetScrolling() {
        if (this.targetY < this.scrollLowerBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollLowerBound);
        } else if (this.targetY > this.scrollUpperBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollUpperBound);
        }

    }

}