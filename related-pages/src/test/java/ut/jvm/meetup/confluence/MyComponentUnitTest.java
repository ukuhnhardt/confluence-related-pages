package ut.jvm.meetup.confluence;

import org.junit.Test;
import jvm.meetup.confluence.MyPluginComponent;
import jvm.meetup.confluence.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}