package com.container.test;

import com.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.container.component.Component.*;

public class InvokeSetterTest
{
    public static class User
    {
        private Integer id;
        private String username;
        private String password;

        public void setId(Integer id)
        {
            this.id = id;
        }

        public void setUsernameAndPassword(String username, String password)
        {
            this.username = username;
            this.password = password;
        }

        public Integer getId()
        {
            return id;
        }

        public String getUsername()
        {
            return username;
        }

        public String getPassword()
        {
            return password;
        }
    }

    @Test
    public void test()
    {
        Component c = constructor(User.class)
                .invokeSetter("setId", value(1001))
                .invokeSetter("setUsernameAndPassword", value("com"), value("123456"));
        User u = (User) c.create();
        assertEquals(1001, u.getId());
        assertEquals("com", u.getUsername());
        assertEquals("123456", u.getPassword());

        Component builder = constructor(StringBuilder.class)
                .invokeSetter("append", value("hello"))
                .invokeSetter("append", value(" world"));
        Component str = instanceFactory(builder, "toString");
        assertEquals("hello world", str.create());
    }
}
