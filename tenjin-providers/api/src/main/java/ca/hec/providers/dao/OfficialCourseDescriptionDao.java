package ca.hec.providers.dao;

import ca.hec.providers.model.CourseOutlineDescription;

/**
 * This is the interface for the Dao for our Catalog Description tool, it
 * handles the database access functionality of the tool
 * 
 * @author <a href="mailto:curtis.van-osch@hec.ca">Curtis van Osch</a>
 * @version $Id: $
 */
public interface OfficialCourseDescriptionDao {

    public void init();

    /**
     * Return the active catalog description based on the course id
     *
     * @param courseId - the course id of the catalog description
     * @return - the last version of the OfficialCourseDescription
     */
    public CourseOutlineDescription getOfficialCourseDescription(String courseId, String sessionId);

}
