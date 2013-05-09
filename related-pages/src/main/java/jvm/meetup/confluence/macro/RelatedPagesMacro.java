package jvm.meetup.confluence.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.labels.LabelManager;
import com.atlassian.confluence.labels.Labelable;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;

import java.util.List;
import java.util.Map;

public class RelatedPagesMacro implements Macro {

    private final LabelManager labelManager;

    public RelatedPagesMacro(LabelManager labelManager) {
        this.labelManager = labelManager;
    }

    @Override
    public String execute(Map<String, String> parameters, String body, ConversionContext conversionContext) throws MacroExecutionException {

        List<Label> labels = conversionContext.getPageContext().getEntity().getLabels();
        List<? extends Labelable> contentWithLabel = labelManager.getContentForAllLabels(labels, 10, 1);
        return String.format("<h2>Related Pages</h2><p>Found %d pages</p>", contentWithLabel.size());
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
