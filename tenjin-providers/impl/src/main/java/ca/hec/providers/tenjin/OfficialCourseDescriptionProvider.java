package ca.hec.providers.tenjin;

import ca.hec.providers.dao.OfficialCourseDescriptionDao;
import ca.hec.providers.model.CourseOutlineDescription;
import ca.hec.tenjin.api.model.syllabus.AbstractSyllabusElement;
import ca.hec.tenjin.api.model.syllabus.SyllabusRubricElement;
import ca.hec.tenjin.api.model.syllabus.SyllabusTextElement;
import ca.hec.tenjin.api.provider.ExternalDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Setter;

import java.util.ArrayList;

public class OfficialCourseDescriptionProvider implements ExternalDataProvider {
	private static Logger log = LoggerFactory.getLogger(OfficialCourseDescriptionProvider.class);

    @Setter
    OfficialCourseDescriptionDao officialCourseDescriptionDao;

    public void init() {
        log.info("init");
    }

    @Override
    public AbstractSyllabusElement getAbstractSyllabusElement(String siteId, String locale) {

        SyllabusRubricElement descriptionRubric = new SyllabusRubricElement();

        String title = locale.equals("es_ES") ? "Descripción" : "Description";
        descriptionRubric.setTitle(title);

        String description = null;
        if (siteId.contains(".")) {
            String catalogNbr = siteId.substring(0, siteId.indexOf('.')).replace("-", "");
            description = getOfficialDescriptionString(catalogNbr, "2213", locale);
        }

        if (description == null) {
            return descriptionRubric;
        }

        SyllabusTextElement descriptionText = new SyllabusTextElement();
        descriptionText.setDescription(description);
        descriptionText.setTemplateStructureId(-1L);
        descriptionText.setCommon(true);
        descriptionText.setPublicElement(true);
        descriptionText.setHidden(false);
        descriptionText.setImportant(false);

        ArrayList elements = new ArrayList<AbstractSyllabusElement>();
        elements.add(descriptionText);

        descriptionRubric.setElements(elements);
        return descriptionRubric;
    }

    private String getOfficialDescriptionString(String catalogNbr, String sessionCode, String locale) {
        String officialDescription = "";
        CourseOutlineDescription co = officialCourseDescriptionDao.getOfficialCourseDescription(catalogNbr, sessionCode);
        if (co == null)
            return null;

        if (co.getShortDescription() != null)
            officialDescription += "<p>" + co.getShortDescription().replace("\n", "</br>") + "</p>";
        if (co.getDescription() != null)
            officialDescription += "<p>" + co.getDescription().replace("\n", "</br>") + "</p>";
        if (co.getThemes() != null) {
            String themesTitle = locale.equals("en_US") ? "Themes" : locale.equals("es_ES") ? "Temas" : "Thèmes";
            officialDescription += "<h3>" + themesTitle + "</h3><p>" + co.getThemes().replace("\n", "</br>") + "</p>";
        }

        return officialDescription;
    }
}
