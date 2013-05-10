package jvm.meetup.confluence.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.labels.LabelManager;
import com.atlassian.confluence.labels.Labelable;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.AbstractPage;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.velocity.VelocityContextUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.opensymphony.util.TextUtils;

import javax.annotation.Nullable;
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

        ContentEntityObject self = conversionContext.getPageContext().getEntity();
        List<Label> labels = self.getLabels();
        Set<Labelable> contentWithLabel = new HashSet<Labelable>();
        if (TextUtils.stringSet(parameters.get(PARAM_LOGIC)) && parameters.get(PARAM_LOGIC).equalsIgnoreCase("AND")){
            contentWithLabel.addAll(labelManager.getContentForAllLabels(labels, 1000, 0)) ;
        } else {
            for (Label label : labels ){
                contentWithLabel.addAll(labelManager.getContentForLabel(label, 1000));
            }
        }
        Map<String, Object> context = MacroUtils.defaultVelocityContext();
        Set<AbstractPage> pages = new HashSet<AbstractPage>();
        for (Labelable ceo : contentWithLabel){
            if (ceo instanceof AbstractPage && !ceo.equals(self)){
                pages.add((AbstractPage)ceo );
            }
        }
        context.put("pages", pages);
        return VelocityUtils.getRenderedTemplate("templates/related-pages.vm", context);
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
