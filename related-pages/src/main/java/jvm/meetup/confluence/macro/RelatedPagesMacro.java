package jvm.meetup.confluence.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.renderer.macro.BaseMacro;

import java.util.Map;

public class RelatedPagesMacro implements Macro {
    @Override
    public String execute(Map<String, String> stringStringMap, String s, ConversionContext conversionContext) throws MacroExecutionException {
        return "";
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}
