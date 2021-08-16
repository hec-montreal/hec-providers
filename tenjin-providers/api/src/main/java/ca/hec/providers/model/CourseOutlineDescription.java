/**
 * 
 */
package ca.hec.providers.model;

import lombok.Data;

/**
 * <p>
 * CourseOutlineDescription is the object for official course outline description
 * 
 * </p>
 */
@Data
public class CourseOutlineDescription {
	private String courseId;
	private String catalogNbr;
	private String subject;
	private String description;
	private String shortDescription;
	private String themes;
	private String sessionCode;
}
