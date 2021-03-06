package com.container.test;

import com.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.container.component.Component.*;

public class ConditionComponentTest
{
    @Test
    public void test()
    {
        Component p1 = value(true);
        Component p2=  value(false);

        Component c1 = condition(p1, value(123), value("hello"));
        assertEquals(123, c1.create());
        Component c2 = condition(p2, value(123), value("hello"));
        assertEquals("hello", c2.create());
    }
}
