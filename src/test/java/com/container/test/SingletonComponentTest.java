package com.container.test;

import com.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.container.component.Component.*;

public class SingletonComponentTest
{
    public static class A {}

    @Test
    public void test()
    {
        Component c1 = constructor(String.class, value("hello")).singleton();
        assertSame(c1.create(), c1.create());
        Component c2 = constructor(A.class).singleton();
        assertSame(c2.create(), c2.create());
    }
}
