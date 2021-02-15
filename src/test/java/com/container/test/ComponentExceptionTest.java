package com.container.test;

import com.container.ContainerImp;
import com.container.Container;
import com.container.exception.*;
import com.container.exception.ContainerException;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static com.container.component.Component.*;

public class ComponentExceptionTest
{
    public static class User
    {
        private String username;
        private String password;

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }
    }

    @Test
    public void test()
    {
        // constructor
        assertThrows(ContainerException.class,
                () -> constructor(String.class, value(123), value("hello")).create());
        assertThrows(ContainerException.class,
                () -> constructor(String.class, staticFactory(String.class, "create", value(123))).create());

        // staticFactory
        assertThrows(ContainerException.class,
                () -> staticFactory(String.class, "valueOf", value(123), value("hello")).create());
        assertThrows(ContainerException.class,
                () -> staticFactory(String.class, "create", value(123)).create());
        assertThrows(ContainerException.class,
                () -> staticFactory(String.class, "valueOf", constructor(Integer.class, value(3.14))).create());

        // instanceFactory
        assertThrows(ContainerException.class,
                () -> instanceFactory(value("static"), "length", value(123)).create());
        assertThrows(ContainerException.class,
                () -> instanceFactory(value("static"), "length1").create());
        assertThrows(ContainerException.class,
                () -> instanceFactory(value("static"), "length", constructor(Integer.class, value(3.14))).create());

        // reference
        Container container = new ContainerImp();
        assertThrows(ContainerException.class,
                () -> reference(container, "hello").create());

        // setProperty
        assertThrows(ContainerException.class,
                () -> constructor(User.class).setProperty("id", value(1001)).create());
        assertThrows(ContainerException.class,
                () -> constructor(User.class).setProperty("username", value(123)).create());
        assertThrows(ContainerException.class,
                () -> constructor(User.class, value("hello")).setProperty("id", value(1001)).create());
        assertThrows(ContainerException.class,
                () -> constructor(User.class).setProperty("password", staticFactory(String.class, "create")).create());

        // invokeSetter
        assertThrows(ContainerException.class,
                () -> constructor(User.class).invokeSetter("setId", value(1001)).create());
        assertThrows(ContainerException.class,
                () -> constructor(User.class).invokeSetter("setUsername", staticFactory(String.class, "create")).create());
        assertThrows(ContainerException.class,
                () -> constructor(User.class, value(123)).invokeSetter("setPassword", value("123456")).create());

        // list
        assertThrows(ContainerException.class,
                () -> list(constructor(User.class, value(123))).create());

        // set
        assertThrows(ContainerException.class,
                () -> list(staticFactory(User.class, "create")).create());

        // map
        assertThrows(ContainerException.class,
                () -> map(Map.of(value(123), constructor(User.class, value("hello")))).create());
    }
}
