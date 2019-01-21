package utils;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import mymod.IsaacMod;
import relics.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static mymod.IsaacMod.obtain;

public class Utils {
    public static void setPrivateField(Object o, String field, Object value) {
        try {
            Field vY = o.getClass().getDeclaredField("vY");
            vY.setAccessible(true);
            vY.set(o, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setPrivateField(Object o, String field, Object value, Class targetClass) {
        try {
            Field vY = targetClass.getDeclaredField("vY");
            vY.setAccessible(true);
            vY.set(o, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static int[] randomCombine(int N, int n) {
        ArrayList<Integer> all = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            all.add(i);
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int rnd = (int) (Math.random() * (N - i));
            res[i] = all.get(rnd);
            all.remove(rnd);
        }
        return res;
    }


    public static int[] randomCombineRng(int N, int n) {
        ArrayList<Integer> all = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            all.add(i);
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int rnd = AbstractDungeon.relicRng.random(0, N - i - 1);
            res[i] = all.get(rnd);
            all.remove(rnd);
        }
        return res;
    }

    public static List<String> getClassName(String packageName) {
        return getClassName(packageName, false);
    }

    public static List<String> getClassName(String packageName, boolean containChild) {
        String filePath = ClassLoader.getSystemResource("").getPath() + packageName.replace(".", "\\");
        List<String> fileNames;
        if (containChild) {
            fileNames = getClassName(filePath, null);
        } else {
            fileNames = getNoChildClassName(filePath, null);
        }
        return fileNames;
    }

    private static List<String> getNoChildClassName(String filePath, List<String> className) {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (!childFile.isDirectory()) {
                String childFilePath = childFile.getPath();
                childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                childFilePath = childFilePath.replace("\\", ".");
                myClassName.add(childFilePath);
            }
        }
        return myClassName;
    }

    private static List<String> getClassName(String filePath, List<String> className) {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                myClassName.addAll(getClassName(childFile.getPath(), myClassName));
            } else {
                String childFilePath = childFile.getPath();
                childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                childFilePath = childFilePath.replace("\\", ".");
                myClassName.add(childFilePath);
            }
        }
        return myClassName;
    }


    public static void removeFromPool(AbstractRelic r) {
        switch (r.tier) {
            case COMMON:
                for (Iterator<String> s = AbstractDungeon.commonRelicPool.iterator(); s.hasNext(); ) {
                    String derp = (String) s.next();
                    if (derp.equals(r.relicId)) {
                        s.remove();
                        return;
                    }
                }
            case UNCOMMON:
                for (Iterator<String> s = AbstractDungeon.uncommonRelicPool.iterator(); s.hasNext(); ) {
                    String derp = (String) s.next();
                    if (derp.equals(r.relicId)) {
                        s.remove();
                        return;
                    }
                }
            case RARE:
                for (Iterator<String> s = AbstractDungeon.rareRelicPool.iterator(); s.hasNext(); ) {
                    String derp = (String) s.next();
                    if (derp.equals(r.relicId)) {
                        s.remove();
                        return;
                    }
                }
            case BOSS:
                for (Iterator<String> s = AbstractDungeon.bossRelicPool.iterator(); s.hasNext(); ) {
                    String derp = (String) s.next();
                    if (derp.equals(r.relicId)) {
                        s.remove();
                        return;
                    }
                }
            case SHOP:
                for (Iterator<String> s = AbstractDungeon.shopRelicPool.iterator(); s.hasNext(); ) {
                    String derp = (String) s.next();
                    if (derp.equals(r.relicId)) {
                        s.remove();
                        return;
                    }
                }
            default:
        }
    }

    public static Point getCirclePoint(Point point, double angle, double r) {
        double x = point.x + r * Math.cos(angle);
        double y = point.y + r * Math.sin(angle);
        return new Point(x, y);
    }

    private static String[] guppys = {GuppysHead.ID, GuppysTail.ID, NineLifeCat.ID, GuppysPaw.ID, GuppysHairball.ID, GuppysCollar.ID};

    public static boolean areGuppy() {
        List<String> set = new ArrayList<>(Arrays.asList(guppys));
        return isSuit(set, set.size());
    }

    private static String[] books = {BelialBook.ID, DeathBook.ID, TheBible.ID, BookofShadows.ID, AnarchistCookbook.ID, SatanicBible.ID, BookOfRevelations.ID, HowToJump.ID};

    public static boolean areBookworm() {
        List<String> set = new ArrayList<>(Arrays.asList(books));
        return isSuit(set, set.size());
    }

    private static boolean isSuit(List<String> set, int size) {
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (set.contains(relic.relicId)) {
                set.remove(relic.relicId);
                if (size - set.size() >= 3) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void obtainRandomRelics(int num) {
        try {
            String[] rnd = getRandomRelics(IsaacMod.relics.size(), num);
            for (String relic : rnd) {
                Class c = Class.forName("relics." + relic);
                CustomRelic rndRelic = (CustomRelic) c.newInstance();
                obtain(AbstractDungeon.player, rndRelic, false);
                Utils.removeFromPool(rndRelic);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("你没找到类");
            e.printStackTrace();
        }
    }

    public static void obtainRandomCards(int num) {
        try {
            String[] rnd = getRandomCards(IsaacMod.cards.size(), num);
            for (String card : rnd) {
                Class c = Class.forName("cards." + card);
                AbstractCard rndCard = (AbstractCard) c.newInstance();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(rndCard, (float) Settings.WIDTH * 0.6F, (float) Settings.HEIGHT / 2.0F));
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("你没找到类");
            e.printStackTrace();
        }
    }

    public static CustomRelic getRandomRelic() {
        try {
            if (IsaacMod.relics.isEmpty()) {
                return null;
            }
            String[] rnd = getRandomRelics(IsaacMod.relics.size(), 1);
            for (String relic : rnd) {
                Class c = Class.forName("relics." + relic);
                CustomRelic rndRelic = (CustomRelic) c.newInstance();
                Utils.removeFromPool(rndRelic);
                removeRelicString(relic);
                return rndRelic;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("你没找到类");
            e.printStackTrace();
        }
        return null;
    }

    public static CustomRelic getRandomRelicRng(List<String> pool) {
        try {
            if (pool.isEmpty()) {
                return null;
            }
            String[] rnd = getRandomRelicsRng(pool.size(), 1, pool);
            for (String relic : rnd) {
                Class c = Class.forName("relics." + relic);
                CustomRelic rndRelic = (CustomRelic) c.newInstance();
                Utils.removeFromPool(rndRelic);
                removeRelicString(relic);
                return rndRelic;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("你没找到类");
            e.printStackTrace();
        }
        return null;
    }

    public static CustomRelic getRandomRelicRng() {
        try {
            if (IsaacMod.relics.isEmpty()) {
                return null;
            }
            String[] rnd = getRandomRelicsRng(IsaacMod.relics.size(), 1);
            for (String relic : rnd) {
                Class c = Class.forName("relics." + relic);
                CustomRelic rndRelic = (CustomRelic) c.newInstance();
                Utils.removeFromPool(rndRelic);
                removeRelicString(relic);
                return rndRelic;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("你没找到类");
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getRandomRelicsRng(int N, int n) {
        int[] rnd = randomCombineRng(N, n);
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            res[i] = IsaacMod.relics.get(rnd[i]);
        }
        return res;
    }

    public static String[] getRandomRelicsRng(int N, int n, List<String> pool) {
        int[] rnd = randomCombineRng(N, n);
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            res[i] = pool.get(rnd[i]);
        }
        return res;
    }

    public static String[] getRandomRelics(int N, int n) {
        int[] rnd = randomCombine(N, n);
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            res[i] = IsaacMod.relics.get(rnd[i]);
        }
        return res;
    }


    public static String[] getRandomCards(int N, int n) {
        int[] rnd = randomCombine(N, n);
        String[] result = new String[n];
        for (int i = 0; i < n; i++) {
            result[i] = IsaacMod.cards.get(rnd[i]);
        }
        return result;
    }

    public static void removeRelicString(String name) {
        IsaacMod.relics.remove(name);
        IsaacMod.devilRelics.remove(name);
        IsaacMod.devilOnlyRelics.remove(name);
    }

    public static void removeCardString(String name) {
        IsaacMod.cards.remove(name);
    }

//    public static boolean[] flyDie = new boolean[6];

//    public static void main(String[] args) throws InterruptedException {
////        List<Integer> list = new ArrayList<>();
////        list.add(1);
////        System.out.println(list.get(3));
//        System.out.println(-13%10);
//    }

}
