package com.container.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Reflection utility tools
 */
public class ReflectUtils
{
    /**
     * Create object
     * @param type
     * @param params constructor parameters
     * @param <T> type
     * @return An object created by calling a specific constructor
     */
    public static <T> T create(Class<T> type, Object... params)
    {
        try
        {
            Constructor<?> constructor = type.getConstructor(getTypes(params));
            return type.cast(constructor.newInstance(params));
        }
        catch (Exception e)
        {
            for (Constructor<?> constructor : type.getConstructors())
            {
                if (constructor.getParameterCount() == params.length)
                {
                    try
                    {
                        return type.cast(constructor.newInstance(params));
                    }
                    catch (Exception ignored) {}
                }
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Call static method
     * @param type
     * @param methodName method name
     * @param params parameters
     * @return return value of static method
     */
    public static Object call(Class<?> type, String methodName, Object... params)
    {
        try
        {
            Method method = type.getMethod(methodName, getTypes(params));
            return method.invoke(null, params);
        }
        catch (Exception e)
        {
            for (Method method : type.getMethods())
            {
                if (method.getName().equals(methodName) && method.getParameterCount() == params.length)
                {
                    try
                    {
                        return method.invoke(null, params);
                    }
                    catch (Exception ignored) {}
                }
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Call object method
     * @param obj object
     * @param methodName method name
     * @param params method parameters
     * @return method return value
     */
    public static Object call(Object obj, String methodName, Object... params)
    {
        try
        {
            Method method = obj.getClass().getMethod(methodName, getTypes(params));
            return method.invoke(obj, params);
        }
        catch (Exception e)
        {
            for (Method method : obj.getClass().getMethods())
            {
                if (method.getName().equals(methodName) && method.getParameterCount() == params.length)
                {
                    try
                    {
                        return method.invoke(obj, params);
                    }
                    catch (Exception ignored) {}
                }
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Set the properties of JavaBean
     * @param bean JavaBean instance
     * @param propertyName property name
     * @param value
     */
    public static void setProperty(Object bean, String propertyName, Object value)
    {
        try
        {
            PropertyDescriptor pd = new PropertyDescriptor(propertyName, bean.getClass());
            Method setter = pd.getWriteMethod();
            setter.invoke(bean, value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] getTypes(Object... params)
    {
        Class<?>[] types = new Class[params.length];
        for (int i = 0; i < params.length; ++i)
        {
            types[i] = params[i].getClass();
        }
        return types;
    }
}
