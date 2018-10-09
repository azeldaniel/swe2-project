# Sprint Backlog
This document contains the tasks for the first sprint.

## Construct basic classes to hold input of course data
- (1)Create an android app base
- (1)Create User Class (to hold GPA and semesters)
- (1)Create Course Class (to hold Assignments and Exams and their grades)
- (1)Create Semester Class (to hold courses for a semester)
- (1)Create Year Class (to hold semesters in a year)**

## Construct basic UI for input of  data
- (4)Create home page UI with placeholders (this has the years listed on it)
- (4)Create year detail page UI (list of semesters) with placeholders
- (3)Create semester detail page UI (list of courses) with placeholders
- (4)Create course detail page UI (for input of course data)
- (2)Add functionality to home page for adding years
- (2)Add functionality to home page for navigation to the year’s details (year detail page)
- (2)Add functionality to year page for adding semesters
- (2)Add functionality to year page for navigation to the semester’s details (semester detail page)
- (2)Add functionality to semester page for adding courses
- (2)Add functionality to semester page for navigation to the course’s details (course detailpage)

## Allow for a final grade to be entered without having to enter other details in the course(to calculate semester GPA)
- (4)Add functionality to the course detail page to save course data (this involves allowing the input of final grade to nullify having to input exam, project and assignment details) Accept/Save Course data (locally) and implementing onClick response  
- (1)Add a button on the course detail page
- (3)Add functionality to the button so that when it is pressed, the course data is saved to the device. Load Information on App Launch
- (4)Access and display locally saved information on app open

## Implement GPA calculation logic
- (2)Create calculation interface (for GPA: cumulative, degree, semester)
- (2)Cumulative: (credit hours attained * total grade points)/total credit hours
- (2)Degree: (degree-related credit hours attained * total degree-related grade points)/total degree-related credit hours
- (2)Course: Translate final grade into lettering, translate lettering into GPA
- (2)Semester: 	Take GPA from the course lettering, multiply each by their relevant credit hours to get quality points. Add all quality points together and divide the sum by the total credit hours(of the courses) Explanation: (https://www.pdx.edu/registration/calculating-grade-point-average)
- (1)This takes the User’s semester GPA that was calculated previously and it outputs to the GUI 


## Display the user’s degree GPA
- (1)This takes the User’s degree GPA that was calculated previously and it outputs to the GUI

## Display user’s cumulative GPA
- (1)This takes the User’s cumulative GPA that was calculated previously and outputs it to the GUI

## Improve upon the Course Detail UI 
- (2)Add functionality to add an assignment, exam, project to a course
- (1)Add functionality to include more course information such as grades and weightings for assignments, exams, projects
- (1)Calculate the user’s final grade

## Update Information Stored in Phone
- (4)Add functionality while editing course information such that stored course information is updated when a field is edited
