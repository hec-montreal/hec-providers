package ca.hec.providers.tenjin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.io.FileHandler;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import ca.hec.tenjin.api.model.syllabus.SyllabusRubricElement;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.ServerOverloadException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;

import ca.hec.tenjin.api.model.syllabus.AbstractSyllabusElement;
import ca.hec.tenjin.api.model.syllabus.SyllabusCompositeElement;
import ca.hec.tenjin.api.model.syllabus.SyllabusTextElement;
import ca.hec.tenjin.api.model.template.Template;
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
		String bundlePath = CONFIGURATION_FILE;
		if (locale != null) {
			bundlePath = bundlePath.replace(".properties", "_" + locale + ".properties");
		}
		Configuration bundle = getBundle(bundlePath);

		SyllabusCompositeElement instructionModePage = null;
		if (bundle != null) {
			instructionModePage = new SyllabusCompositeElement();
			//title is set from DB
			instructionModePage.setTitle(bundle.getString("page.title"));
			List<AbstractSyllabusElement> instructionModePageElements = new ArrayList<AbstractSyllabusElement>();
			instructionModePage.setElements(instructionModePageElements);
			instructionModePage.setCommon(true);
			instructionModePage.setEqualsPublished(false);

			String[] rubricTitles = bundle.getStringArray("rubric.titles");

			for (int i = 1; i <= rubricTitles.length; i++) {
				SyllabusRubricElement rubric = new SyllabusRubricElement();
				List<AbstractSyllabusElement> children = new ArrayList<AbstractSyllabusElement>();
				rubric.setElements(children);
				rubric.setTitle(rubricTitles[i]);
				rubric.setTemplateStructureId(-1L);
				rubric.setCommon(true);
				rubric.setEqualsPublished(false);
		
				String[] textElements = bundle.getStringArray("rubric."+i+".text.elements");
				for (int j = 1; j <= textElements.length; j++) {
					SyllabusTextElement text = new SyllabusTextElement();
					text.setDescription(textElements[j]);
					text.setTemplateStructureId(-1L);
					text.setCommon(true);
					text.setEqualsPublished(false);
					children.add(text);		
				}
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

}
