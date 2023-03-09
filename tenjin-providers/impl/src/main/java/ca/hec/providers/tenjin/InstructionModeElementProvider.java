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
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.cover.SiteService;

import ca.hec.tenjin.api.model.syllabus.AbstractSyllabusElement;
import ca.hec.tenjin.api.model.syllabus.SyllabusTextElement;
import ca.hec.tenjin.api.provider.ExternalDataProvider;

/**
 *
 * @author <a href="mailto:curtis.van-osch@hec.ca">Curtis Van Osch</a>
 * @version $Id: $
 */
public class InstructionModeElementProvider implements ExternalDataProvider {
	private static Logger log = LoggerFactory.getLogger(LearningMaterialProvider.class);

	private static final String CONFIGURATION_PATH = "/group/tenjin/instructionModeProvider/";
	private static final String CONFIGURATION_FILE = "instructionModeText.properties";

	//private static String CACHE_NAME = "ca.hec.commons.providers.LearningMaterialProvider";
	//private Cache<String, ResourceBundle> cache;

	//@Setter
	//private MemoryService memoryService;

	public void init() {
		//cache = null; //memoryService.newCache(CACHE_NAME);
	}

	private String getBundlePath(String siteId, String locale) {
		String instMode = getInstructionMode(siteId);
		String bundlePath = CONFIGURATION_PATH + ((instMode==null)?"":instMode+"_") + CONFIGURATION_FILE;
		if (locale != null) {
			bundlePath = bundlePath.replace(".properties", "_" + locale + ".properties");
		}
		return bundlePath;
	}

	private String getInstructionMode(String siteId) {
		try {
			Site site = SiteService.getSite(siteId);
			String instructionMode = site.getProperties().getProperty("instruction_mode");
			if (instructionMode != null) {
				return  instructionMode;
			}
		}
		catch (Exception e) { 
		}
		return "";
	}

	@Override
	public AbstractSyllabusElement getAbstractSyllabusElement(String siteId, String locale) {
		ResourceBundle bundle = getBundle(getBundlePath(siteId, locale));
		// if resource bundle doesn't exist, don't create the element at all
		if (bundle == null) {
			return null;
		}

		// hardcoded based on template
		Long rubricTemplateStructureId = 1552L;
		Long textElementTemplateStructureId = 1553L;

		String rubricTitle = bundle.getString("rubric.title");
		String textElementDescription = bundle.getString("text.description");

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

		return rubric;
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
			} catch (IdUnusedException e) {
				// properties file doesn't exist
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rb;
	}
}
