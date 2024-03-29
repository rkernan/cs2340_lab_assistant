/**
* @author Robert Kernan
*
* model for a Team of Students
*/

package edu.gatech.youngguns.labassistant

class Team {

    String name

    static belongsTo = [lab: Lab]
    static hasMany = [students: User]

    static constraints = {
        name(blank: false) // if we want unique names, we'll have to redo how we're naming random and individual teams
    }

    static mapping = {
        relationships(cascade: 'delete')
    }

    /**
     * accessor for the team capacity
     * @return the max size this team can be (from it's associated lab)
     */
    public int capacity () {
        return this.lab.maxTeamSize
    }

    /**
     * accessor for team size
     * @return the current size of the team
     */
    public int size () {
        return this.students?.size() ?: 0
    }
}
