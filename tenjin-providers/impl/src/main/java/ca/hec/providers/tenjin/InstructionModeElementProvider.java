package ca.hec.providers.tenjin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.io.FileHandler;
*/

import ca.hec.tenjin.api.model.syllabus.SyllabusRubricElement;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.memory.api.MemoryService;

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
	//	cache = memoryService.newCache(CACHE_NAME);
	}

	@Override
	public AbstractSyllabusElement getAbstractSyllabusElement(String siteId, String locale) {
		/*
		String bundlePath = CONFIGURATION_FILE;
		if (locale != null) {
			bundlePath = bundlePath.replace(".properties", "_" + locale + ".properties");
		}
		Configuration bundle = getBundle(bundlePath);
*/
		SyllabusCompositeElement instructionModePage = null;
		if (true /*bundle != null*/) {
			instructionModePage = new SyllabusCompositeElement();
			//title is set from DB
			instructionModePage.setTitle("InstructionModePage" /*bundle.getString("page.title")*/);
			List<AbstractSyllabusElement> instructionModePageElements = new ArrayList<AbstractSyllabusElement>();
			instructionModePage.setElements(instructionModePageElements);
			instructionModePage.setCommon(true);
			instructionModePage.setEqualsPublished(false);

			SyllabusRubricElement rubric = new SyllabusRubricElement();
			List<AbstractSyllabusElement> children = new ArrayList<AbstractSyllabusElement>();
			rubric.setElements(children);
			rubric.setTitle("Description");
			rubric.setTemplateStructureId(1552L);
			rubric.setCommon(true);
			rubric.setEqualsPublished(false);
		
			SyllabusTextElement text = new SyllabusTextElement();
			text.setDescription("this is a test (damn commons-config don't work)");
			text.setTemplateStructureId(1553L);
			text.setCommon(true);
			text.setEqualsPublished(false);
			children.add(text);

			/*
			SyllabusRubricElement rubric2 = new SyllabusRubricElement();
			List<AbstractSyllabusElement> children2 = new ArrayList<AbstractSyllabusElement>();
			rubric2.setElements(children2);
			rubric2.setTitle("Description");
			rubric2.setTemplateStructureId(1560L);
			rubric2.setCommon(true);
			rubric2.setEqualsPublished(false);
		
			SyllabusTextElement text2 = new SyllabusTextElement();
			text2.setDescription("this is a test (damn commons-config don't work)");
			text2.setTemplateStructureId(1562L);
			text2.setCommon(true);
			text2.setEqualsPublished(false);
			children2.add(text2);

			
			SyllabusRubricElement rubric3 = new SyllabusRubricElement();
			List<AbstractSyllabusElement> children3 = new ArrayList<AbstractSyllabusElement>();
			rubric3.setElements(children3);
			rubric3.setTitle("Description");
			rubric3.setTemplateStructureId(1561L);
			rubric3.setCommon(true);
			rubric3.setEqualsPublished(false);

			SyllabusTextElement text3 = new SyllabusTextElement();
			text3.setDescription("this is a test (damn commons-config don't work)");
			text3.setTemplateStructureId(1569L);
			text3.setCommon(true);
			text3.setEqualsPublished(false);
			children3.add(text3);
*/
			instructionModePageElements.add(rubric);
//			instructionModePageElements.add(rubric2);
//			instructionModePageElements.add(rubric3);
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
		if (destinationSiteId.endsWith("HS")) {
			return true;
		}
		else {
			return false;
		}
	}

	/*
	private Configuration getBundle(String path) {

		PropertiesConfiguration conf = null;
//		if (cache != null && cache.containsKey(path)) {
//			rb = cache.get(path);
//		}

		if (conf == null) {
			try {
				ContentResource resource = ContentHostingService.getResource(path);
				conf =  new BasicConfigurationBuilder<>(PropertiesConfiguration.class).configure(new Parameters().properties()).getConfiguration();
				FileHandler fh = new FileHandler(conf);
				fh.load(resource.streamContent());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conf;
	}
	*/

}
