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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        return HushsDoor.guppyCount >= 3 || isSuit(set, set.size());
    }

    private static String[] books = {BelialBook.ID, DeathBook.ID, TheBible.ID, BookofShadows.ID, AnarchistCookbook.ID, SatanicBible.ID, BookOfRevelations.ID, HowToJump.ID};

    public static boolean areBookworm() {
        List<String> set = new ArrayList<>(Arrays.asList(books));
        return HushsDoor.bookCount >= 3 || isSuit(set, set.size());
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

    public static Method getDeclaredMethod(Class clazz, String method, Class<?>... params) {
        try {
            Method method1 = clazz.getDeclaredMethod(method, params);
            method1.setAccessible(true);
            return method1;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invoke(Object toInvoke, Method method, Object... params) {
        try {
            if (method != null) {
                return method.invoke(toInvoke, params);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        Interpreter interpreter = new Interpreter();
//        try {
//            interpreter.eval("Utils.get");
//        } catch (EvalError evalError) {
////            evalError.printStackTrace();
//            System.out.println(evalError.getMessage());
//        }
        try {
            File f = new File("mods");
            if (f.isDirectory()) {
                String s[] = f.list();
                if (s != null) {
                    for (String x : s) {
                        JarFile jarFile = new JarFile("mods/" + x);
                        Enumeration enu = jarFile.entries();
                        while (enu.hasMoreElements()) {
                            JarEntry jarEntry = (JarEntry) enu.nextElement();
                            String name = jarEntry.getName();
                            if (name.endsWith(".class")) {
                                System.out.println("import " + name.replace("/", ".").replace(".class", ""));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ReadAllFile(String filePath) {
        File f = null;
        f = new File(filePath);
        File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。
        List<File> list = new ArrayList<File>();
        for (File file : files) {
            if (file.isDirectory()) {
                //如何当前路劲是文件夹，则循环读取这个文件夹下的所有文件
                ReadAllFile(file.getAbsolutePath());
            } else {
                list.add(file);
            }
        }
        for (File file : files) {
            String s = file.getAbsolutePath();
            if (s.endsWith(".java")) {
                int len = "D:\\projects\\decompiled-desktop-1.0\\".length();
                System.out.println("\"" + s.substring(len, s.length()).replace(".java", "").replace("\\", ".") + "\",");
            }
        }

    }


    public static Object[] getInstancesByClass(Class clazz) {
        Constructor[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            return new Object[]{getBasic(clazz)};
        }
        Object res[] = new Constructor[constructors.length];
        int index = 0;
        for (Constructor constructor : constructors) {
            Class[] paramTypes = constructor.getParameterTypes();
            Object[] params = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i].getConstructors().length == 0) {
                    params[i] = getBasic(paramTypes[i]);
                } else if (hasNoParamCon(paramTypes[i])) {
                    try {
                        params[i] = paramTypes[i].newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    params[i] = getInstancesByClass(clazz)[0];
                }
            }
            try {
                res[index++] = constructor.newInstance(params);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static Object getInstanceByClass(Class clazz) {
        Constructor[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            return getBasic(clazz);
        }
        for (Constructor constructor : constructors) {
            Class[] paramTypes = constructor.getParameterTypes();
            Object[] params = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i].getConstructors().length == 0) {
                    params[i] = getBasic(paramTypes[i]);
                } else if (hasNoParamCon(paramTypes[i])) {
                    try {
                        params[i] = paramTypes[i].newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    params[i] = getInstanceByClass(clazz);
                }
            }
            try {
                return constructor.newInstance(params);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static boolean hasNoParamCon(Class paramType) {
        for (Constructor c : paramType.getConstructors()) {
            if (c.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    private static List<Class> basics = Arrays.asList(
            byte.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class,
            boolean.class,
            char.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Boolean.class,
            Character.class
    );


    public static Object getBasic(Class c) {
        if (c == boolean.class || c == Boolean.class) {
            return false;
        } else if (basics.contains(c)) {
            return 0;
        }
        return null;
    }
}
