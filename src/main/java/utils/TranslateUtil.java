package utils;

import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Keeper
 *
 * 你可能会缺少包 注意配置一下maven，并且有尖塔的依赖。
 * 修改from 和 to 可以支持不同语言的json转换 语言表见：https://ai.youdao.com/docs/doc-trans-api.s#p07
 * 翻译很方便，translateJson(json目录, 对应的类型) 是卡牌文本还是遗物文本等等
 * 会输出到你的项目根目录translations文件夹下面
 * !!! 注意API使用的是付费的有道翻译，请不要重复玩，以免余额用尽 !!!
 */
public class TranslateUtil {

    public static String from = "en";
    public static String to = "zh-CHS";

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static String cardPath = "D:\\projects\\IsaacMod\\src\\main\\resources\\isaacLocalization\\eng\\card-strings.json";
    public static String relicPath = "xxxxxxx";

    public static void main(String[] args) throws IOException {
        translateJson(cardPath, CardStrings.class);
//        translateJson(relicPath, RelicStrings.class);
    }

    public static <T> void translateJson(String path, Class<T> clazz) throws IOException {
        LwjglFiles files = new LwjglFiles();
        String load = files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));
        final Type type = new TypeToken<Map<String, T>>() {
        }.getType();
        Map<String, LinkedTreeMap<String, String>> map = gson.fromJson(load, type);

        Field[] fields = clazz.getFields();
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            list.add(field.getName());
        }
        for (String key : map.keySet()) {
            System.out.println(key + " :");
            for (int i = 0; i < list.size(); i++) {
                Object node = map.get(key).get(list.get(i));
                if (node != null) {
                    String value = node.toString();
                    String translate = TranslationAPI.translate(value, from, to);
                    if (translate.length() > 2) {
                        translate = translate.substring(1, translate.length() - 1);
                    }
                    map.get(key).put(list.get(i), translate);
                    System.out.println(list.get(i) + ": " + value);
                    System.out.println(list.get(i) + ": " + translate);
                }
            }
            System.out.println();
        }
        String val = gson.toJson(map);
        String filePath = "translations/" + clazz.getSimpleName() + ".json";
        writeStringToFile(filePath, val);
    }

    public static void writeStringToFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(content);
            bw.close();
            fw.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
