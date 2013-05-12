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
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import com.atlassian.upm.license.storage.lib.PluginLicenseStoragePluginUnresolvedException;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManagerImpl;
import com.opensymphony.util.TextUtils;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RelatedPagesMacro implements Macro {

    public static final String PARAM_LOGIC = "logic";

    private final LabelManager labelManager;
    private final ThirdPartyPluginLicenseStorageManagerImpl licenseManager;

    public RelatedPagesMacro(LabelManager labelManager, ThirdPartyPluginLicenseStorageManagerImpl licenseManager) {
        this.labelManager = labelManager;
        this.licenseManager = licenseManager;
    }

    @Override
    public String execute(Map<String, String> parameters, String body, ConversionContext conversionContext) throws MacroExecutionException {

        try {
            Option<PluginLicense> license =  licenseManager.getLicense();
            if (!license.isDefined() || !license.get().isValid()){
                return "<div class='aui-message error'>Invalid License!</div>";
            }
        } catch (PluginLicenseStoragePluginUnresolvedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            // do nothing
        }

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
