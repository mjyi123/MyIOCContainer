package com.container.component;

import com.container.Container;
import com.container.exception.ContainerException;
import com.container.exception.Message;
import com.container.util.ReflectUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Component: an object that can be obtained from the IOC container.
 * All components are managed by the IOC container, and each component encapsulates its dependencies with other components.
 * In other words, each component knows how to create itself, and this information is encapsulated in the create method.
 * Every time a component is obtained from the IOC container, the create method of the component will be called.
 * Component also encapsulates some commonly used static methods and default methods
 */
public interface Component
{
    /**
     * Create components
     * @return component object
     */
    Object create();

    /**
     * Create value component
     * @param value
     * @return ValueComponent constructed with value
     */
    static Component value(Object value)
    {
        return new ValueComponent(value);
    }

    /**
     * Create the constructor component
     * @param type
     * @param params parameter component
     * @return Constructor Component
     */
    static Component constructor(Class<?> type, Component... params)
    {
        return new ConstructorComponent(type, params);
    }

    /**
     * Create a static factory component
     * @param type
     * @param method method name
     * @param params parameter component
     * @return static factory Component
     */
    static Component staticFactory(Class<?> type, String method, Component... params)
    {
        return new StaticFactoryComponent(type, method, params);
    }

    /**
     * Create instance factory component
     * @param instance instance component
     * @param method method name
     * @param params parameter component
     * @return Instance Factory Component
     */
    static Component instanceFactory(Component instance, String method, Component... params)
    {
        return new InstanceFactoryComponent(instance, method, params);
    }

    /**
     * Convert the result created by the current component
     * @param mapper converter
     * @return MapperComponent
     */
    default Component map(Mapper mapper)
    {
        return new MapperComponent(this, mapper);
    }

    /**
     * Set the properties of the object created by the current component
     * @param property property name
     * @param value attribute value
     * @return MapperComponent
     */
    default Component setProperty(String property, Component value)
    {
        return this.map(obj ->
        {
            Object v = value.create();
            try
            {
                ReflectUtils.setProperty(obj, property, v);
                return obj;
            }
            catch (Exception e)
            {
                throw new ContainerException(Message.propertyNotFount(obj.getClass(), property, v.getClass()), e);
            }
        });
    }

    /**
     * Call the setter method on the object created by the current component
     * @param setter setter method name
     * @param params setter method parameters
     * @return MapperComponent
     */
    default Component invokeSetter(String setter, Component... params)
    {
        return this.map(obj ->
        {
            Object[] p = Arrays.stream(params).map(Component::create).toArray();
            try
            {
                ReflectUtils.call(obj, setter, p);
                return obj;
            }
            catch (Exception e)
            {
                throw new ContainerException(Message.setterNotFound(obj.getClass(), setter, p), e);
            }
        });
    }

    /**
     * Turn the current component into a singleton
     * @return SingletonComponent
     */
    default Component singleton()
    {
        return new SingletonComponent(this);
    }

    /**
     * Create reference components
     * @param container container
     * @param id The unique identifier of the component
     * @return ReferenceComponent
     */
    static Component reference(Container container, String id)
    {
        return new ReferenceComponent(container, id);
    }

    /**
     * Create a list component
     * @param components multiple components
     * @return ListComponent
     */
    static Component list(Component... components)
    {
        return new ListComponent(components);
    }

    /**
     * Create a set component
     * @param components component list
     * @return SetComponent
     */
    static Component set(Component... components)
    {
        return new SetComponent(components);
    }

    /**
     * Create a map component
     * @param componentMap component map
     * @return MapComponent
     */
    static Component map(Map<Component, Component> componentMap)
    {
        return new MapComponent(componentMap);
    }

    /**
     * Create conditional components
     * @param predicate predicate
     * @param c1 component returned when predicate is true
     * @param c2 component returned when predicate is false
     * @return ConditionComponent
     */
    static Component condition(Component predicate, Component c1, Component c2)
    {
        return new ConditionComponent(predicate, c1, c2);
    }
}
