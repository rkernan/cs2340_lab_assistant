package edu.gatech.youngguns.labassistant

/**
 * 
 * @author Robert Kernan
 *
 */

class Lab {
	
	String name
	//static hasMany = [teams: Team]
	static belongsTo = [course: Course]
	Date startDate
	Date endDate

	static constraints = {
		name(blank: false, unique: true)
	}
	
	static mapping = {
		relationships(cascade: 'delete')
	}
}