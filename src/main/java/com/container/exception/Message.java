package com.container.exception;

import java.util.Arrays;

/**
 * 错误消息生成
 */
public interface Message
{
    /**
     * Cannot find constructor
     */
    static String constructorNotFound(Class<?> type, Object[] params)
    {
        return String.format("Cannot find constructor of type \"%s\" with parameters %s.",
                type.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * Cannot find static factory
     */
    static String staticFactoryNotFound(Class<?> factory, String methodName, Object[] params)
    {
        return String.format("Cannot find static factory \"%s\" of type \"%s\" with parameters %s.",
                methodName, factory.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * Cannot find instance factory
     */
    static String instanceFactoryNotFound(Class<?> type, String methodName, Object[] params)
    {
        return String.format("Cannot find instance factory \"%s\" of type \"%s\" with parameters %s.",
                methodName, type.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * Cannot find property
     */
    static String propertyNotFount(Class<?> type, String property, Class<?> valueType)
    {
        return String.format("Cannot find property \"%s\" in \"%s\" of type \"%s\".",
                property, type.getCanonicalName(), valueType.getCanonicalName());
    }

    /**
     * Cannot find the setter method
     */
    static String setterNotFound(Class<?> type, String setterName, Object[] params)
    {
        return String.format("Cannot find setter \"%s\" of type \"%s\" with parameters %s.",
                setterName, type.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * The parameter cannot be null
     */
    static String parameterNotNull(String name)
    {
        return String.format("Parameter \"%s\" cannot be null.",
                name);
    }

    /**
     * Cannot find component
     */
    static String componentNotFound(String id)
    {
        return String.format("There is no component in container with id \"%s\".",
                id);
    }

    /**
     * Invalid class name
     */
    static String invalidClassName(String className)
    {
        return String.format("Incorrect class name: %s.",
                className);
    }

    /**
     * Invalid Mapper class name
     */
    static String invalidMapperClassName(String className)
    {
        return String.format("\"%s\" is not a \"Mapper\".",
                className);
    }

    /**
     * Invalid Component class name
     */
    static String invalidComponentClassName(String className)
    {
        return String.format("\"%s\" is not a \"Component\".",
                className);
    }

    /**
     * Incorrect Json element type
     */
    static String incorrectJsonElementType(String json, String type)
    {
        return String.format("This element is not %s: \n%s",
                type, json);
    }

    /**
     * Cannot find the specified key of the Json element
     */
    static String jsonKeyNotFound(String json, String key)
    {
        return String.format("Cannot find key \"%s\" in this element:\n%s",
                key, json);
    }

    /**
     * Unknown component type
     */
    static String unknownComponentType(String json)
    {
        return String.format("Unknown component type:\n%s",
                json);
    }
}
