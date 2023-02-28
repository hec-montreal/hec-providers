package ca.hec.providers.tenjin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;

import ca.hec.tenjin.api.model.syllabus.SyllabusRubricElement;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.memory.api.MemoryService;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.cover.SiteService;

import ca.hec.tenjin.api.model.syllabus.AbstractSyllabusElement;
import ca.hec.tenjin.api.model.syllabus.SyllabusCompositeElement;
import ca.hec.tenjin.api.model.syllabus.SyllabusTextElement;
import ca.hec.tenjin.api.provider.ExternalDataProvider;
import lombok.Setter;

/**
 *
 * @author <a href="mailto:curtis.van-osch@hec.ca">Curtis Van Osch</a>
 * @version $Id: $
 */
public class InstructionModeElementProvider implements ExternalDataProvider {
	private static Logger log = LoggerFactory.getLogger(LearningMaterialProvider.class);

	private static final String CONFIGURATION_FILE = "/group/tenjin/instructionModeProvider/instructionModeText.properties";

	//private static String CACHE_NAME = "ca.hec.commons.providers.LearningMaterialProvider";
	//private Cache<String, ResourceBundle> cache;

	@Setter
	private MemoryService memoryService;

	public void init() {
		//cache = null; //memoryService.newCache(CACHE_NAME);
	}

	private boolean isCorrectInstructionMode(String siteId) {
		try {
			Site site = SiteService.getSite(siteId);
			String instructionMode = site.getProperties().getProperty("instruction_mode");
			return instructionMode != null && instructionMode.equals("HS");
		}
		catch (Exception e) { 
			return false;
		}
	}

	@Override
	public AbstractSyllabusElement getAbstractSyllabusElement(String siteId, String locale) {
		// element only applies to HS instruction mode
		if (!isCorrectInstructionMode(siteId)) {
			return null;
		}

		String bundlePath = CONFIGURATION_FILE;
		if (locale != null) {
			bundlePath = bundlePath.replace(".properties", "_" + locale + ".properties");
		}
		ResourceBundle bundle = getBundle(bundlePath);

		SyllabusCompositeElement instructionModePage = null;
		if (bundle != null) {
			instructionModePage = new SyllabusCompositeElement();
			//title is set from DB
			instructionModePage.setTitle(bundle.getString("page.title"));
			List<AbstractSyllabusElement> instructionModePageElements = new ArrayList<AbstractSyllabusElement>();
			instructionModePage.setElements(instructionModePageElements);
			instructionModePage.setCommon(true);
			instructionModePage.setEqualsPublished(false);

			String rubricCountStr = bundle.getString("rubric.count");
			Integer rubricCount = Integer.decode(rubricCountStr);

			for (int i = 1; i <= rubricCount; i++) {
				String rubricTSIStr = bundle.getString("rubric." + i + ".template.structure.id");
				String textElementTSIStr = bundle.getString("text." + i + ".template.structure.id");
				Long rubricTemplateStructureId = null;
				Long textElementTemplateStructureId = null;
				try {
					rubricTemplateStructureId = Long.decode(rubricTSIStr);
					textElementTemplateStructureId = Long.decode(textElementTSIStr);
				}
				catch (NumberFormatException e) {
					log.error("Unable to parse template structure id");
					continue;
				}

				String rubricTitle = bundle.getString("rubric." + i + ".title");
				String textElementDescription = bundle.getString("text." + i + ".description");

				SyllabusRubricElement rubric = new SyllabusRubricElement();
				List<AbstractSyllabusElement> children = new ArrayList<AbstractSyllabusElement>();
				rubric.setElements(children);
				rubric.setTitle(rubricTitle);
				rubric.setTemplateStructureId(rubricTemplateStructureId);
				rubric.setCommon(true);
				rubric.setEqualsPublished(false);

				SyllabusTextElement text = new SyllabusTextElement();
				text.setDescription(textElementDescription);
				text.setTemplateStructureId(textElementTemplateStructureId);
				text.setCommon(true);
				text.setEqualsPublished(false);
				children.add(text);

				instructionModePageElements.add(rubric);
			}
		}
		else {
			log.error("bundle is null for some reason");
			// todo handle this better? 
			return null;
		}

		return instructionModePage;
	}

	@Override
	public boolean copyElementOnSiteCopy(String destinationSiteId) {
		return isCorrectInstructionMode(destinationSiteId);
	}

	private ResourceBundle getBundle(String path) {
		ResourceBundle rb = null;

		/*
		if (cache != null && cache.containsKey(path)) {
			rb = cache.get(path);
		}
		*/
		if (rb == null) {
			try {
				ContentResource resource = ContentHostingService.getResource(path);
				rb = new PropertyResourceBundle(resource.streamContent());
				//cache.put(path, rb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rb;
	}
}
