package edu.gatech.youngguns.labassistant

import grails.plugins.springsecurity.Secured

/**
 * 
 * @author Kyle Petrovich
 *
 */

class CourseController {

	 /**
	 * Dependency injection for the springSecurityService.
	 */
    def springSecurityService

    /**
	 * Default action; If the user is not logged in, redirects to login, otherwise displays
	 * all courses.
	 * @Secured logged in remembered, roles: all
	 */
	@Secured(["IS_AUTHENTICATED_REMEMBERED"])
    def index = {
	   list()
	}
	
   /**
    * List all courses.
    * list is different depending on current user's role
    * @Secured logged in remembered, roles: all
    */
	@Secured(["IS_AUTHENTICATED_REMEMBERED"])
   def list = {
	   //TODO: Add "join" link next to each one if Student.
	   if(springSecurityService.currentUser.hasRole("ROLE_ADMINISTRATOR")) {
		   render(view: 'list', model: [courseList: Course.list(), courseTotal: Course.count()])
	   }
	   else if(springSecurityService.currentUser.hasRole("ROLE_INSTRUCTOR")) {
	   		def courseList = Course.findAllByInstructor(springSecurityService.currentUser)
	   		render(view: 'list', model: [courseList: courseList, courseTotal: courseList.size()])
	   }
	   else if(springSecurityService.currentUser.hasRole("ROLE_STUDENT")) {
			render(view: 'list', model: [courseList: Course.list(), courseTotal: Course.count()]) 
			//Students can view and join any course.
	   }
	   else {
		   render(view: '/index')
	   }
	}
   
	/**
	 * Display create course page if user has proper permissions.
	 * @Secured logged in fully, roles: Instructor, Admin
	 */
	@Secured(["IS_AUTHENTICATED_FULLY", "ROLE_INSTRUCTOR"])
	def create = {
		render(view: 'create')
	}
	
	/**
	 * Save course object from form if coming from create, then redirect to list.
	 * @Secured logged in fully, roles: Instructor, Admin
     */
	@Secured(["IS_AUTHENTICATED_FULLY", "ROLE_INSTRUCTOR"])
	def save = {
		//TODO: Redirect to list if HTTPrequest didn't come from create?
		String name = params['name']
		User instructor
		if (params['instructor']) { 
			instructor = User.findByName(params['instructor'])
		}
		else {
			instructor = springSecurityService.currentUser
		}
		def course = new Course(name: name, instructor: instructor)
		course.save()
		redirect(action:'list')
	}
	@Secured(["IS_AUTHENTICATED_REMEMBERED", "ROLE_STUDENT"])
	def join = {
		StudentCourse.create(springSecurityService.currentUser, Course.get(params['course']))
		render view: 'success'
	}
	
	   
}