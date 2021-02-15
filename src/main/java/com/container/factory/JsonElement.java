package com.container.factory;

import java.util.Set;

/**
 * Encapsulation of JSON read operation
 */
public interface JsonElement
{
    boolean isInteger();
    boolean isDouble();
    boolean isString();
    boolean isBoolean();
    boolean isNull();
    boolean isArray();
    boolean isObject();

    int getInteger();
    double getDouble();
    String getString();
    boolean getBoolean();
    int getLength();
    JsonElement getElement(int index);
    JsonElement getElement(String key);

    boolean containsKey(String key);
    Set<String> keySet();

    String getJsonString();

    default boolean isPrimitive()
    {
        return isInteger() || isDouble() || isString() || isBoolean() || isNull();
    }
}
