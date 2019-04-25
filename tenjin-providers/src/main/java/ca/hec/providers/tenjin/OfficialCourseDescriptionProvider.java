package ca.hec.providers.tenjin;

/*import ca.hec.portal.api.OfficialCourseDescriptionDao;
import ca.hec.portal.model.OfficialCourseDescription;
import ca.hec.tenjin.api.model.syllabus.AbstractSyllabusElement;
import ca.hec.tenjin.api.model.syllabus.SyllabusRubricElement;
import ca.hec.tenjin.api.model.syllabus.SyllabusTextElement;
import ca.hec.tenjin.api.provider.ExternalDataProvider;
import lombok.Setter;

import java.util.ArrayList;*/

public class OfficialCourseDescriptionProvider /*implements ExternalDataProvider*/ {

    /*@Setter
    OfficialCourseDescriptionDao officialCourseDescriptionDao;

    @Override
    public AbstractSyllabusElement getAbstractSyllabusElement(String siteId, String locale) {

        SyllabusRubricElement descriptionRubric = new SyllabusRubricElement();
        descriptionRubric.setTitle("Description");

        String description = null;
        if (siteId.contains(".")) {
            String catalogNbr = siteId.substring(0, siteId.indexOf('.')).replace("-", "");
            description = getOfficialDescriptionString(catalogNbr, locale);
        }

        if (description == null) {
            return null;
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

    private String getOfficialDescriptionString(String catalogNbr, String locale) {
        String officialDescription = "";
        OfficialCourseDescription co = officialCourseDescriptionDao.getOfficialCourseDescription(catalogNbr);
        if (co == null)
            return null;

        if (co.getShortDescription() != null)
            officialDescription += "<p>"+co.getShortDescription().replace("\n", "</br>")+"</p>";
        if (co.getDescription() != null)
            officialDescription += "<p>"+co.getDescription().replace("\n", "</br>")+"</p>";
        if (co.getThemes() != null) {
            String themesTitle =
                    locale.equals("en_US") ? "Themes" : locale.equals("es_ES") ? "Temas" : "Thèmes";
            officialDescription += "<h3>" + themesTitle + "</h3><p>" + co.getThemes().replace("\n", "</br>") + "</p>";
        }

        return officialDescription;
    }*/
}

