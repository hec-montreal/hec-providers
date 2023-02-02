package ca.hec.providers.tenjin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
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

	private static final String CONFIGURATION_FILE = "/group/tenjin/learningMaterialProvider/learningMaterialText.properties";

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
		ResourceBundle bundle = getBundle(bundlePath);

		SyllabusCompositeElement instructionModePage = new SyllabusCompositeElement();
		instructionModePage.setTitle("InstructionMode");
		instructionModePage.setTemplateStructureId(1551L);

		SyllabusRubricElement descriptionRubric = new SyllabusRubricElement();
		List<AbstractSyllabusElement> children = new ArrayList<AbstractSyllabusElement>();
		descriptionRubric.setTitle("Description");
		descriptionRubric.setElements(children);
		descriptionRubric.setTemplateStructureId(-1L);

		instructionModePage.setElements(Stream.of(descriptionRubric).collect(Collectors.toList()));

		SyllabusTextElement textElement = new SyllabusTextElement();
		textElement.setDescription("<ul><li>Description de l'organisation du cours</li><li>https://enseigner.hec.ca/aidememoire-etudiant-hybride/</li></ul>");
//		textElement.setDescription(bundle.getString("learningMaterialText"));
		textElement.setTitle(null);
		textElement.setTemplateStructureId(-1L);
		textElement.setCommon(true);
		textElement.setPublicElement(true);
		textElement.setHidden(false);
		textElement.setImportant(false);

		children.add(textElement);

		/*
		if (bundle != null) {
			SyllabusTextElement textElement = new SyllabusTextElement();

			textElement.setDescription(bundle.getString("learningMaterialText"));
			textElement.setTitle(bundle.getString("learningMaterialTitle"));
			textElement.setTemplateStructureId(-1L);
			textElement.setCommon(true);
			textElement.setPublicElement(true);
			textElement.setHidden(false);
			textElement.setImportant(false);

			rubric.setTitle(bundle.getString("learningMaterialRubricTitle"));
			children.add(textElement);
		}
		else {
			log.error("bundle is null for some reason");
			rubric.setTitle("IMPORTANT");
		}
		*/
		return instructionModePage;
	}

	private ResourceBundle getBundle(String path) {
		ResourceBundle rb = null;

//		if (cache != null && cache.containsKey(path)) {
//			rb = cache.get(path);
//		}

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
