package ca.hec.providers.dao;

import ca.hec.providers.model.CourseOutlineDescription;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import java.util.*;

public class OfficialCourseDescriptionDaoImpl extends HibernateDaoSupport implements OfficialCourseDescriptionDao {
    private static Log log = LogFactory.getLog(OfficialCourseDescriptionDaoImpl.class);

    public void init() {
        log.info("init");
    }

    public CourseOutlineDescription getOfficialCourseDescription(String courseId, String sessionCode) {

        CourseOutlineDescription catDesc = null;

        DetachedCriteria dc =
                DetachedCriteria
                        .forClass(CourseOutlineDescription.class)
                        .add(Restrictions.eq("courseId",
                                courseId.toUpperCase()))
                        .add(Restrictions.eq("sessionCode",
                                sessionCode));

        List descList = getHibernateTemplate().findByCriteria(dc);

        if (descList != null && descList.size() != 0) {
            catDesc = (CourseOutlineDescription) descList.get(0);
        }

        return catDesc;
    }
}
