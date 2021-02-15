# Container——Lightweight IOC container
Container is a lightweight IOC container written in Java, with the following characteristics:
* Use the configuration file in JSON format
* Support constructor injection, static factory injection, instance factory injection, property injection, setter injection, conditional injection
* Lazy loading of components and singleton components
* Register and get the components in the container according to the id

## Configuration file

Container uses JSON as the format of configuration files. 
You can name the configuration file any name and place it in any path you like.

The basic framework of the Container configuration file is as follows:
```
{
    "typeAlias":
    {
        // configure typealiases used in components
    },
    "components":
    {
        // define all components in the container
    }
}
```

|Key|Type|Description|Required|
|-|-|-|-|
|`typeAlias`|object|typealiases used in `components`|no|
|`components`|object|define all components in the container|yes|

For the use of typealiases, please refer to [Here](#Typealiases)。

## Components
Container uses components to manage all objects in the system. The component represents an object in the system and encapsulates the creation process of the object. Each component has a unique key. When we register a component with Container, we need to specify a unique key for the component, and at the same time define the creation process and dependencies of the component.

All components of Container are defined in `components`. Components are written in the `components` object in the form of key-value pairs. The key is the id of the component, and the value is the definition of the component.

There are many ways to define components, see below for details.

```
{
    "components":
    {
        "c1": ... // definition of component c1
        "c2": ... // definition of component c1
        "c3": ... // definition of component c1
        ...
    }
}
```
## Loading and using Container

In the initialization code of the application, load the configuration file in the following way and initialize the container:
```java
InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("Configuration file path");
ContainerFactory factory = new JsonContainerFactory(inputStream);
Container container = factory.create();
```
After the container is initialized, you need to use the id when registering the component to get a specific component:
```java
// get the component with id c1
SomeType c1 = container.getComponent("c1");
```

## The way components are created
In Container, the following four component creation ways are supported:
* Constant
* Constructor
* Static factory
* Instance factory

### Constant
Container supports all basic types of constants in JSON, including integers, floating-point numbers, strings, Booleans, and null. Their corresponding types in Java are: `int`, `double`, `String`, `boolean`, ` null`.
```json
{
    "components":
    {
        "intValue": 123,
        "doubleValue": 3.14,
        "stringValue": "hello",
        "trueValue": true,
        "falseValue": false,
        "nullValue": null
    }
}
```
The above configuration declares several different types of constant components.

### Constructor

When configuring the constructor creation method, you need to specify the fully qualified class name, and if you want to pass parameters, specify the parameter array.

|Key|Type|Description|Required|
|-|-|-|-|
|`class`|string|Fully qualified class name|yes|
|`parameter`|array|Constructor parameters|no|

```json
{
    "components":
    {
        "a1":
        {
            "class": "com.test.A"
        },
        "a2":
        {
            "class": "com.test.A",
            "parameters": ["hello", 123]
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
A a1 = new A();
A a2 = new A("hello", 123);
```

Note: The elements in the `parameters` array can be constants or the definitions of other components can be nested:
```json
{
    "components":
    {
        "a3":
        {
            "class": "com.test.A",
            "parameters": 
            [
                "hello", 
                123, 
                {"class": "com.test.B"}
            ]
        }
    }
}
```

The above configuration is equivalent to the following Java code:
```java
A a3 = new A("hello", 123, new B());
```
### Static factory
When configuring the static factory creation method, you need to specify the class name and factory function name of the factory class, and specify the parameter array if you want to pass parameters.

|Key|Type|Description|Required|
|-|-|-|-|
|`factory`|string|Factory class fully qualified class|yes|
|`method`|string|Factory class static method|yes|
|`parameter`|array|parameter|no|

```json
{
    "components":
    {
        "a1":
        {
            "factory": "com.test.Factory",
            "method": "createDefault"
        },
        "a2":
        {
            "factory": "com.test.Factory",
            "method": "create",
            "parameters": ["hello", 123]
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
A a1 = Factory.createDefault();
A a2 = Factory.create("hello", 123);
```
### Instance factory
When configuring the instance factory creation method, you need to specify the instance component and factory function name, and if you want to pass parameters, specify the parameter array.

|Key|Type|Description|Required|
|-|-|-|-|
|`instance`|Object or primitive|Instance component definition|yes|
|`method`|string|Instance factory method|yes|
|`parameter`|array|Parameter|no|

```json
{
    "components":
    {
        "a1":
        {
            "instance": {"class": "com.test.B"},
            "method": "createDefault"
        },
        "a2":
        {
            "instance": {"class": "com.test.B"},
            "method": "create",
            "parameters": ["hello", 123]
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
A a1 = new B().createDefault();
A a2 = new B().create("hello", 123);
```
## Component dependency declaration
After declaring how the component is created, you can continue to declare the dependencies of the component. Container supports the following two dependency declaration methods:
* Property
* setter
### Property
Container supports setting the properties of the component after the component is created. The property here is equivalent to the JavaBean Property.

|Key|Type|Description|Required|
|-|-|-|-|
|`properties`|object|(Property name, Property value) Key-Value Pair|no|

```json
{
    "components":
    {
        "a": 
        {
            "class": "com.test.A",
            "properties":
            {
                "id": 1001,
                "name": "com",
                "score": 97.5
            }
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
A a = new A();
a.setId(1001);
a.setName("com");
a.setScore(97.5);
```
Note: The property value in `properties` can be a constant or nested definitions of other components:
```json
{
    "components":
    {
        "a": 
        {
            "class": "com.test.A",
            "properties":
            {
                "id": 1001,
                "b": {"class": "com.test.B"}
            }
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
A a = new A();
a.setId(1001);
a.setB(new B());
```
### setter
Container supports calling the setter method of the component and passing parameters after the component is created.

|Key|Type|Description|Required|
|-|-|-|-|
|`setters`|object|(setter method name, parameters) Key-Value Pair|no|

```json
{
    "components":
    {
        "a": 
        {
            "class": "com.test.A",
            "setters":
            {
                "setId": [1001],
                "setNameAndScore": ["com", 97.5]
            }
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
A a = new A();
a.setId(1001);
a.setNameAndScore("com", 97.5);
```
## Other component definitions
Container also supports the following special component definitions:
* Collection component
* Reference component
* Condition component
### 集合组件
Container supports the three collection components `List`, `Set` and `Map`.

The following is the definition of these three collection components:
```json
{
    "components":
    {
        "c1": {"list": [1, 2, 3]},
        "c2": {"set": [4, 5, 6]},
        "c3": 
        {
            "map":
            {
                "k1": 100,
                "k2": 200,
                "k3": 300
            }
        },
        "c4":
        {
            "map":
            [
                {"key": 400, "value": "v1"},
                {"key": 500, "value": "v2"},
                {"key": 600, "value": "v3"}
            ]
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
List<Object> c1 = List.of(1, 2, 3);
Set<Object> c2 = Set.of(4, 5, 6);
Map<Object> c3 = Map.of("k1", 100, "k2", 200, "k3", 300);
Map<Object> c4 = Map.of(400, "v1", 500, "v2", 600, "v3");
```
Note:
* The elements of a collection component can be constants or nested definitions of other components
* `c3` and `c4` are two different ways of defining the `Map` component. The first type only supports keys of type `String`, the second type supports keys of any data type
### Reference component
Container not only supports references to global components, but also supports references to local components.

|Key|Type|Description|Required|
|-|-|-|-|
|`ref`|string|Referenced component key|yes|

```json
{
    "components":
    {
        "a1": {"class": "com.test.A"},
        "a2": {"ref": "a1"}
    }
}
```
The above configuration is equivalent to the following Java code:
```java
A a1 = new A();
A a2 = a1;
```
Note: For local components, please refer to[Here](#Local components)。
### Condition component
Container supports different component definitions based on different conditions.

|Key|Type|Description|Required|
|-|-|-|-|
|`if`|object or primitive|Component definition as a condition|yes|
|`then`|object or primitive|Components produced when the condition is true|yes|
|`else`|object or primitive|Components produced when the condition is false|yes|

```json
{
    "components":
    {
        "flag": true,
        "c":
        {
            "if": {"ref": "flag"},
            "then": {"class": "com.com.A"},
            "else": {"class": "com.com.B"}
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
boolean flag = true;
Object c;
if (flag)
    c = new A();
else
    c = new B();
```
Note: When `if` is set as a component definition, the container will interpret the object created by the component as a `boolean` value. If the object created by the component is not a `boolean` type, it will be treated as `false`.
## Advanced features
Container also supports the following advanced features:
* Local components
* Singleton component
* Customized component
* Customized converter
* Typealiases
### Local component
Sometimes, other components are needed in the creation of certain components, such as the following example
```java
A a = new A("hello", "123", new B());
```
We can configure it as follows:
```json
{
    "components":
    {
        "msg": "hello",
        "val": 123,
        "b": {"class": "com.test.B"},
        "a":
        {
            "class": "com.test.A",
            "parameters": [{"ref": "msg"}, {"ref": "val"}, {"ref": "b"}]
        }
    }
}
```
The three parameters passed in the constructor of `A` belong to three different components, but these three components are only used to construct the `A` object. We don't want these temporary components to occupy a key in the container. Of course, you can use a "in-place initialization" way of writing:
```json
{
    "components":
    {
        "a":
        {
            "class": "com.test.A",
            "parameters": 
            [
                "hello", 
                123, 
                {"class": "com.test.B"}
            ]
        }
    }
}
```
This way of writing can hide temporary components, but when the component creation process is more complicated, this way of writing will form a deep nested structure, resulting in poor readability. Moreover, this way of writing can't handle the situation where a temporary component is used multiple times, for example:
```java
B b = new B();
A a = new A(b, b);
```
The above problems can be solved by using local components provided by Container:
```json
{
    "components":
    {
        "a":
        {
            "class": "com.test.A",
            "parameters": [{"ref": "msg"}, {"ref": "val"}, {"ref": "b"}],
            "locals":
            {
                "msg": "hello",
                "val": 123,
                "b": {"class": "com.test.B"}
            }
        }
    }
}
```
Inside the `a` component definition, there is a `locals` block, which contains the local component definitions belonging to `a`. These local components do not occupy the global key of the container. It can only be used within the scope of the component `a` Reference. If the key of the local component is the same as the key of the global component, the local component will override the global component.
### Singleton component
By default, all components in Container are singletons, which means that components will only be created once at most. When you get the same component multiple times, you get the same object instance:
```java
Object c1 = container.getComponent("c1");
Object c2 = container.getComponent("c2");
// c1 and c2 point to the same object
```
If you want to change this default behavior, you only need to add the following configuration to the component definition:
```json
"singleton": false
```
### Customized component
Container supports users to define their own components and use them in configuration files.

Users can create their own components by implementing the `Component` interface:
```java
public interface Component
{
    Object create();
}
```
The `create` method in the `Component` interface encapsulates the details of object creation:
```java
public class MyComponent implements Component
{
    private final String msg;
    private final int val;

    public MyComponent()
    {
        MyComponent("hello", 123);
    }

    public MyComponent(String msg, int val)
    {
        this.msg = msg;
        this.val = val;
    }

    @Override
    public Object create()
    {
        return new A(msg, val);
    }
}
```

To use a custom component in the configuration file, you need to specify the fully qualified class name of the custom component. If you want to pass constructor parameters, you need to specify the parameter list.

|Key|Type|Description|Required|
|-|-|-|-|
|`custom`|string|The fully qualified class name of the customized component|yes|
|`parameters`|array|Constructor parameters passed to the customized component|no|

```json
{
    "components":
    {
        "a1":
        {
            "custom": "com.test.MyComponent"
        },
        "a2":
        {
            "custom": "com.test.MyComponent",
            "parameters": ["hi", 456]
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
A a1 = new MyComponent().create();
A a2 = new MyComponent("hi", 456).create();
```
### Customized converter
Container supports further conversion of objects created by components. The user needs to create a custom converter by implementing the `Mapper` interface:
```java
public interface Mapper
{
    Object map(Object obj);
}
```
The `map` method in the `Mapper` interface encapsulates the processing of the result of component creation:
```java
public class MyMapper implements Mapper
{
    private final int val;

    public MyMapper()
    {
        MyMapper(1);
    }

    public MyMapper(int val)
    {
        this.val = val;
    }

    @Override
    public Object map(Object obj)
    {
        if (obj instanceof Integer)
            return ((int) obj) + val;
        return obj;
    }
}
```
To use a custom converter in the configuration file, you need to specify the `mapper` attribute in the component definition:
* When the `mapper` attribute is a string, the string represents the fully qualified class name of the custom `mapper`, and the default constructor is used to create the `mapper`
* When the `mapper` attribute is an object, the object must contain the `class` and `parameters` attributes, where `class` represents the fully qualified class name of `mapper`, and `parameters` represents passing to the constructor when creating `mapper` Parameters
```json
{
    "components":
    {
        "c1":
        {
            "class": "java.lang.Integer",
            "parameters": [123],
            "mapper": "com.test.MyMapper"
        },
        "c1":
        {
            "class": "java.lang.Integer",
            "parameters": [123],
            "mapper":
            {
                "class": "com.test.MyMapper",
                "parameters": [100]
            }
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
int c1 = (int) new MyMapper().map(container.getComponent("c1"));
int c2 = (int) new MyMapper(100).map(container.getComponent("c2"));
```
### Typealiases
In order to avoid typing a lot of lengthy fully qualified class names, Container provides a type alias function, just add the `typeAlias` configuration at the outermost layer of the container definition:
```json
{
    "typeAlias":
    {
        "A": "com.test.A",
        "B": "com.test.B"
    },
    "components":
    {
        "a":
        {
            "class": "A",
            "parameters": ["hello", 123]
        },
        "b":
        {
            "class": "B"
        }
    }
}
```
The above configuration is equivalent to the following Java code:
```java
A a = new A("hello", 123);
B b = new B();
```
Note: All the places where the class name appears in `components` can use the aliases defined in `typeAlias`, including `class`, `factory`, `custom` and so on.