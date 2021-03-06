package com.container.factory;

import com.container.exception.ContainerException;
import com.container.exception.Message;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Adapt the object in Fastjson to the adapter of JsonElement
 */
public class JsonElementAdapterForFastjson implements JsonElement
{
    private final Object obj;

    public JsonElementAdapterForFastjson(Object obj)
    {
        this.obj = obj;
    }

    @Override
    public boolean isInteger()
    {
        return obj instanceof Integer;
    }

    @Override
    public boolean isDouble()
    {
        return obj instanceof BigDecimal;
    }

    @Override
    public boolean isString()
    {
        return obj instanceof String;
    }

    @Override
    public boolean isBoolean()
    {
        return obj instanceof Boolean;
    }

    @Override
    public boolean isNull()
    {
        return obj == null;
    }

    @Override
    public boolean isArray()
    {
        return obj instanceof JSONArray;
    }

    @Override
    public boolean isObject()
    {
        return obj instanceof JSONObject;
    }

    @Override
    public int getInteger()
    {
        if (!isInteger())
            throw new ContainerException(Message.incorrectJsonElementType(getJsonString(), "integer"));
        return (int) obj;
    }

    @Override
    public double getDouble()
    {
        if (!isDouble())
            throw new ContainerException(Message.incorrectJsonElementType(getJsonString(), "double"));
        return ((BigDecimal)obj).doubleValue();
    }

    @Override
    public String getString()
    {
        if (!isString())
            throw new ContainerException(Message.incorrectJsonElementType(getJsonString(), "string"));
        return (String) obj;
    }

    @Override
    public boolean getBoolean()
    {
        if (!isBoolean())
            throw new ContainerException(Message.incorrectJsonElementType(getJsonString(), "boolean"));
        return (boolean) obj;
    }

    @Override
    public boolean containsKey(String key)
    {
        if (!isObject())
            throw new ContainerException(Message.incorrectJsonElementType(getJsonString(), "object"));
        return ((JSONObject)obj).containsKey(key);
    }

    @Override
    public JsonElement getElement(int index)
    {
        if (!isArray())
            throw new ContainerException(Message.incorrectJsonElementType(getJsonString(), "array"));
        return new JsonElementAdapterForFastjson(((JSONArray)obj).get(index));
    }

    @Override
    public JsonElement getElement(String key)
    {
        if (!isObject())
            throw new ContainerException(Message.incorrectJsonElementType(getJsonString(), "object"));
        if (!containsKey(key))
            throw new ContainerException(Message.jsonKeyNotFound(getJsonString(), key));
        return new JsonElementAdapterForFastjson(((JSONObject)obj).get(key));
    }

    @Override
    public int getLength()
    {
        if (!isArray())
            throw new ContainerException(Message.incorrectJsonElementType(getJsonString(), "array"));
        return ((JSONArray)obj).size();
    }

    @Override
    public Set<String> keySet()
    {
        if (!isObject())
            throw new ContainerException(Message.incorrectJsonElementType(getJsonString(), "object"));
        return ((JSONObject)obj).keySet();
    }

    @Override
    public String getJsonString()
    {
        return JSON.toJSONString(obj, true);
    }
}
