package jvm.meetup.confluence.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.labels.LabelManager;
import com.atlassian.confluence.labels.Labelable;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.opensymphony.util.TextUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RelatedPagesMacro implements Macro {

    public static final String PARAM_LOGIC = "logic";

    private final LabelManager labelManager;

    public RelatedPagesMacro(LabelManager labelManager) {
        this.labelManager = labelManager;
    }

    @Override
    public String execute(Map<String, String> parameters, String body, ConversionContext conversionContext) throws MacroExecutionException {

        List<Label> labels = conversionContext.getPageContext().getEntity().getLabels();
        Set<Labelable> contentWithLabel = new HashSet<Labelable>();
        if (TextUtils.stringSet(parameters.get(PARAM_LOGIC)) && parameters.get(PARAM_LOGIC).equalsIgnoreCase("AND")){
            contentWithLabel.addAll(labelManager.getContentForAllLabels(labels, 1000, 0)) ;
        } else {
            for (Label label : labels ){
                contentWithLabel.addAll(labelManager.getContentForLabel(label, 1000));
            }
        }
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
