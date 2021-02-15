package com.container.test;

import com.container.component.Component;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.container.component.Component.*;

public class StaticFactoryComponentTest
{
    @Test
    public void test()
    {
        Component c1 = staticFactory(Collections.class, "emptyList");
        assertEquals(Collections.emptyList(), c1.create());
        Component c2 = staticFactory(List.class, "of", value(1), value(2), value(3));
        assertEquals(List.of(1, 2, 3), c2.create());
    }
}
