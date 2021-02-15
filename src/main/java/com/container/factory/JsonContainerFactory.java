package com.container.factory;

import com.container.ContainerImp;
import com.container.Container;
import com.container.component.Component;
import com.container.component.DelegateComponent;
import com.container.component.Mapper;
import com.container.exception.*;
import com.container.util.ReflectUtils;
import com.alibaba.fastjson.JSON;
import com.container.exception.ContainerException;
import com.container.exception.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create a container from a configuration file in Json format
 */
public class JsonContainerFactory implements ContainerFactory
{
    private final String json;
    private Container container;
    private List<Map<String, Component>> scopes;
    private Map<String, String> typeAlias;

    // Reserved key value
    private static final String RESERVED_COMPONENTS = "components";
    private static final String RESERVED_LIST = "list";
    private static final String RESERVED_SET = "set";
    private static final String RESERVED_MAP = "map";
    private static final String RESERVED_KEY = "key";
    private static final String RESERVED_VALUE = "value";
    private static final String RESERVED_REF = "ref";
    private static final String RESERVED_LOCALS = "locals";
    private static final String RESERVED_CLASS = "class";
    private static final String RESERVED_PARAMETERS = "parameters";
    private static final String RESERVED_FACTORY = "factory";
    private static final String RESERVED_METHOD = "method";
    private static final String RESERVED_INSTANCE = "instance";
    private static final String RESERVED_PROPERTIES = "properties";
    private static final String RESERVED_SETTERS = "setters";
    private static final String RESERVED_IF = "if";
    private static final String RESERVED_THEN = "then";
    private static final String RESERVED_ELSE = "else";
    private static final String RESERVED_SINGLETON = "singleton";
    private static final String RESERVED_MAPPER = "mapper";
    private static final String RESERVED_CUSTOM = "custom";
    private static final String RESERVED_TYPE_ALIAS = "typeAlias";

    /**
     * Create JsonContainerFactory from file stream
     * @param inputStream file stream
     */
    public JsonContainerFactory(InputStream inputStream)
    {
        if (inputStream == null)
            throw new ContainerException(Message.parameterNotNull("inputStream"));
        this.json = readJsonFile(inputStream);
    }

    /**
     * Read json file
     */
    private static String readJsonFile(InputStream inputStream)
    {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                json.append(line);
            }
            return json.toString();
        }
        catch (Exception e)
        {
            throw new ContainerException("Error occurs when reading json file.", e);
        }
    }

    /**
     * Parse container
     */
    private Container parseContainer(JsonElement element)
    {
        this.container = new ContainerImp();

        // Parse typealiases
        typeAlias = new HashMap<>();
        if (element.containsKey(RESERVED_TYPE_ALIAS))
        {
            JsonElement typeAliasElem = element.getElement(RESERVED_TYPE_ALIAS);
            for (String alias : typeAliasElem.keySet())
            {
                typeAlias.put(alias, typeAliasElem.getElement(alias).getString());
            }
        }

        // Parse components
        scopes = new ArrayList<>();
        JsonElement components = element.getElement(RESERVED_COMPONENTS);
        for (String key : components.keySet())
        {
            Component c = parseComponent(components.getElement(key));
            container.addComponent(key, c);
        }
        return container;
    }

    /**
     * Load class
     */
    private Class<?> getClass(String className)
    {
        try
        {
            if (typeAlias.containsKey(className))
                className = typeAlias.get(className);
            return Class.forName(className);
        }
        catch (Exception e)
        {
            throw new ContainerException(Message.invalidClassName(className), e);
        }
    }

    /**
     * Load mapper
     */
    private Class<?> getMapper(String mapperClassName)
    {
        Class<?> type = getClass(mapperClassName);
        if (!Mapper.class.isAssignableFrom(type))
            throw new ContainerException(Message.invalidMapperClassName(mapperClassName));
        return type;
    }

    /**
     * Load components
     */
    private Class<?> getComponent(String componentClassName)
    {
        Class<?> type = getClass(componentClassName);
        if (!Component.class.isAssignableFrom(type))
            throw new ContainerException(Message.invalidComponentClassName(componentClassName));
        return type;
    }

    /**
     * Parse components
     */
    private Component parseComponent(JsonElement element)
    {
        // Constant
        if (element.isPrimitive()) return parseValue(element);

        // Parse local components and create current scope
        if (element.containsKey(RESERVED_LOCALS)) parseLocals(element);
        else scopes.add(new HashMap<>());

        Component component;

        // List
        if (element.containsKey(RESERVED_LIST)) component = parseList(element);
        // Collection
        else if (element.containsKey(RESERVED_SET)) component = parseSet(element);
        // map
        else if (element.containsKey(RESERVED_MAP)) component = parseMap(element);
        // Reference
        else if (element.containsKey(RESERVED_REF)) component = parseRef(element);
        // Constructor injection
        else if (element.containsKey(RESERVED_CLASS))
        {
            String className = element.getElement(RESERVED_CLASS).getString();
            Component[] params = new Component[0];
            if (element.containsKey(RESERVED_PARAMETERS))
            {
                params = parseComponentList(element.getElement(RESERVED_PARAMETERS));
            }
            component = Component.constructor(getClass(className), params);
        }
        // Static factory
        else if (element.containsKey(RESERVED_FACTORY))
        {
            String factory = element.getElement(RESERVED_FACTORY).getString();
            String method = element.getElement(RESERVED_METHOD).getString();
            Component[] params = new Component[0];
            if (element.containsKey(RESERVED_PARAMETERS))
            {
                params = parseComponentList(element.getElement(RESERVED_PARAMETERS));
            }
            component = Component.staticFactory(getClass(factory), method, params);
        }
        // Instance factory
        else if (element.containsKey(RESERVED_INSTANCE))
        {
            Component instance = parseComponent(element.getElement(RESERVED_INSTANCE));
            String method = element.getElement(RESERVED_METHOD).getString();
            Component[] params = new Component[0];
            if (element.containsKey(RESERVED_PARAMETERS))
            {
                params = parseComponentList(element.getElement(RESERVED_PARAMETERS));
            }
            component = Component.instanceFactory(instance, method, params);
        }
        // Condition injection
        else if (element.containsKey(RESERVED_IF))
        {
            Component predicate = parseComponent(element.getElement(RESERVED_IF));
            Component c1 = parseComponent(element.getElement(RESERVED_THEN));
            Component c2 = parseComponent(element.getElement(RESERVED_ELSE));
            component = Component.condition(predicate, c1, c2);
        }
        // Custom component
        else if (element.containsKey(RESERVED_CUSTOM))
        {
            component = parseCustomComponent(element);
        }
        // Unknown injection method
        else
        {
            throw new ContainerException(Message.unknownComponentType(element.getJsonString()));
        }

        // Parse properties
        if (element.containsKey(RESERVED_PROPERTIES))
        {
            component = parseProperties(element.getElement(RESERVED_PROPERTIES), component);
        }

        // Parse setters
        if (element.containsKey(RESERVED_SETTERS))
        {
            component = parseSetters(element.getElement(RESERVED_SETTERS), component);
        }

        // Singleton
        boolean singleton = true;
        if (element.containsKey(RESERVED_SINGLETON))
        {
            singleton = element.getElement(RESERVED_SINGLETON).getBoolean();
        }

        // mapper
        if (element.containsKey(RESERVED_MAPPER))
        {
            component = parseMapper(element.getElement(RESERVED_MAPPER), component);
        }

        // Pop up the current scope
        scopes.remove(scopes.size() - 1);
        return singleton ? component.singleton() : component;
    }

    /**
     * Parse constant
     */
    private Component parseValue(JsonElement element)
    {
        if (element.isInteger()) return Component.value(element.getInteger());
        else if (element.isDouble()) return Component.value(element.getDouble());
        else if (element.isString()) return Component.value(element.getString());
        else if (element.isBoolean()) return Component.value(element.getBoolean());
        else return Component.value(null);
    }

    /**
     * Parse list
     */
    private Component parseList(JsonElement element)
    {
        Component[] components = parseComponentList(element.getElement(RESERVED_LIST));
        return Component.list(components);
    }

    /**
     * Parse collection
     */
    private Component parseSet(JsonElement element)
    {
        Component[] components = parseComponentList(element.getElement(RESERVED_SET));
        return Component.set(components);
    }

    /**
     * Parse map
     */
    private Component parseMap(JsonElement element)
    {
        JsonElement mapElem = element.getElement(RESERVED_MAP);
        Map<Component, Component> componentMap = new HashMap<>();
        if (mapElem.isObject())
        {
            for (String key : mapElem.keySet())
            {
                componentMap.put(Component.value(key), parseComponent(mapElem.getElement(key)));
            }
        }
        else
        {
            for (int i = 0; i < mapElem.getLength(); ++i)
            {
                JsonElement item = mapElem.getElement(i);
                componentMap.put(parseComponent(item.getElement(RESERVED_KEY)), parseComponent(item.getElement(RESERVED_VALUE)));
            }
        }
        return Component.map(componentMap);
    }

    /**
     * Parse reference
     */
    private Component parseRef(JsonElement element)
    {
        JsonElement refName = element.getElement(RESERVED_REF);
        String id = refName.getString();
        for (int i = scopes.size() - 1; i >= 0; --i)
        {
            if (scopes.get(i).containsKey(id))
            {
                return scopes.get(i).get(id);
            }
        }
        return Component.reference(container, id);
    }

    /**
     * Parse local component
     */
    private void parseLocals(JsonElement element)
    {
        Map<String, Component> scope = new HashMap<>();
        JsonElement locals = element.getElement(RESERVED_LOCALS);
        for (String key : locals.keySet())
        {
            scope.put(key, new DelegateComponent());
        }
        scopes.add(scope);
        for (String key : locals.keySet())
        {
            DelegateComponent c = (DelegateComponent) scope.get(key);
            c.setComponent(parseComponent(locals.getElement(key)));
        }
    }

    /**
     * Parse component list
     */
    private Component[] parseComponentList(JsonElement element)
    {
        List<Component> components = new ArrayList<>();
        for (int i = 0; i < element.getLength(); ++i)
        {
            components.add(parseComponent(element.getElement(i)));
        }
        return components.toArray(new Component[0]);
    }

    /**
     * Parse properties
     */
    private Component parseProperties(JsonElement element, Component component)
    {
        for (String name : element.keySet())
        {
            Component value = parseComponent(element.getElement(name));
            component = component.setProperty(name, value);
        }
        return component;
    }

    /**
     * Parse setter
     */
    private Component parseSetters(JsonElement element, Component component)
    {
        for (String setterName : element.keySet())
        {
            Component[] params = parseComponentList(element.getElement(setterName));
            component = component.invokeSetter(setterName, params);
        }
        return component;
    }

    /**
     * Parse mapper
     */
    private Component parseMapper(JsonElement element, Component component)
    {
        if (element.isString())
        {
            String className = element.getString();
            Class<?> type = getMapper(className);
            Mapper mapper = (Mapper) ReflectUtils.create(type);
            return component.map(mapper);
        }
        else
        {
            String className = element.getElement(RESERVED_CLASS).getString();
            Class<?> type = getMapper(className);
            Component[] components = parseComponentList(element.getElement(RESERVED_PARAMETERS));
            return component.map(obj -> ((Mapper) Component.constructor(type, components).create()).map(obj));
        }
    }

    /**
     * Parse customized component
     */
    private Component parseCustomComponent(JsonElement element)
    {
        String componentClassName = element.getElement(RESERVED_CUSTOM).getString();
        Class<?> type = getComponent(componentClassName);
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS))
        {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS));
        }
        Component customComponentCreator = Component.constructor(type, params);
        return () -> ((Component) customComponentCreator.create()).create();
    }

    @Override
    public Container create()
    {
        System.out.println(JSON.parse(json));
        return parseContainer(new JsonElementAdapterForFastjson(JSON.parse(json)));
    }
}
