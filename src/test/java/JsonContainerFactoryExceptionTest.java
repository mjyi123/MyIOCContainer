import com.container.Container;
import com.container.exception.ContainerException;
import com.container.factory.ContainerFactory;
import com.container.factory.JsonContainerFactory;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class JsonContainerFactoryExceptionTest
{
    /**
     * The root element is missing the components key
     */
    @Test
    public void test1()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test1.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * The static factory definition lacks the method key
     */
    @Test
    public void test2()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test2.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * The instance factory definition lacks the method key
     */
    @Test
    public void test3()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test3.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * Condition injection definition lacks then key
     */
    @Test
    public void test4()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test4.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * typeAlias ​​is not an object
     */
    @Test
    public void test5()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test5.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * components are not objects
     */
    @Test
    public void test6()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test6.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * Incorrect class name
     */
    @Test
    public void test7()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test7.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * Incorrect Mapper type
     */
    @Test
    public void test8()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test8.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * Incorrect Component type
     */
    @Test
    public void test9()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test9.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * Unrecognized component type
     */
    @Test
    public void test10()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test10.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * Incorrect list definition
     */
    @Test
    public void test11()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test11.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * Incorrect set definition
     */
    @Test
    public void test12()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test12.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * Incorrect map definition
     */
    @Test
    public void test13()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test13.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ContainerException.class, factory::create);
    }

    /**
     * Cannot find constructor
     */
    @Test
    public void test14()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test14.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();
        assertThrows(ContainerException.class,
                () -> container.getComponent("c1"));
    }

    /**
     * Cannot find static factory
     */
    @Test
    public void test15()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test15.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();
        assertThrows(ContainerException.class,
                () -> container.getComponent("c1"));
    }

    /**
     * The instance of the instance factory is null
     */
    @Test
    public void test16()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test16.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();
        assertThrows(ContainerException.class,
                () -> container.getComponent("c1"));
    }

    /**
     * Cannot find instance factory method
     */
    @Test
    public void test17()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test17.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();
        assertThrows(ContainerException.class,
                () -> container.getComponent("c1"));
    }
}
