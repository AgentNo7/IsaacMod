package utils;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yhf on 2019/2/14.
 */
public class InstanceMaker {

    @SuppressWarnings("unchecked")
    public static <T> T getInstanceByClass(Class clazz) {
        T o = (T) getInstanceByClass(clazz, true, false);
        return o != null ? o : (T) getInstanceByClass(clazz, false, false);
    }

    private static Object getInstanceByClass(Class clazz, boolean noNull, boolean skipSelf) {
        if (basics.contains(clazz)) {
            return getBasic(clazz);
        }
        if (hasNoParamCon(clazz)) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ignore) {
            }
        }
        Constructor[] constructors = clazz.getConstructors();
        GO:
        for (Constructor constructor : constructors) {
            Class[] paramTypes = constructor.getParameterTypes();
            Object[] params = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i].getConstructors().length == 0) {
                    params[i] = getBasic(paramTypes[i]);
                } else if (hasNoParamCon(paramTypes[i])) {
                    try {
                        params[i] = paramTypes[i].newInstance();
                    } catch (InstantiationException | IllegalAccessException ignore) {
                    }
                } else {
                    if (paramTypes[i] == clazz) {
                        if (skipSelf) {
                            continue GO;
                        } else {
                            params[i] = getInstanceByClass(paramTypes[i], noNull, true);
                        }
                    } else {
                        params[i] = getInstanceByClass(paramTypes[i], noNull, false);
                    }
                }
                if (noNull && params[i] == null) {
                    continue GO;
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

    @SuppressWarnings("unchecked")
    private static boolean hasNoParamCon(Class paramType) {
        try {
            return paramType.getConstructor() != null;
        } catch (NoSuchMethodException ignored) {
        }
        return false;
    }

    private static final List<Class> basics = Arrays.asList(
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


    private static Object getBasic(Class c) {
        if (c == boolean.class || c == Boolean.class) {
            return false;
        } else if (basics.contains(c)) {
            return 0;
        }
        return null;
    }

}
