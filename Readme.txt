Code Exercise
-------------


Spring boot based solution, which could:
 
1)Read a CSV file (as part of the class path) at application start-up, store each record(Employee record) in a DB(Choose mysql or postgres),
 with below columns:

EmployeeID | EmployeeName | Title | BusinessUnit | Place | SupervisorID | Competencies | Salary
 
2)Create a /employee/place/{place}/salary/{percentage} PUT endpoint that will update each record by increasing their salary
by percentage(percentage path param) for employees that have place matching with place path param.(Use java 8 for transformation)
 
3)The end point will return the refreshed list of employee record

4)Have an in-memory cache solution (choose any caching solution that u are comfortable with).
 Which will store employee record that have been recently updated)

5)Create an endpoint to return the list of employees for a given place from the cache. If not found fetch from the DB.

6)Create a GET endpoint to return the nested list of all supervisees of a given supervisor e.g. consider D, C report into B, B report into A, if I query for A, I should get A->B->C,D
			
7)Create an endpoint to return the total salary of 
	a)given BU
	b)given supervisor
	c)given place

8)Create an endpoint to return the range of salaries for a given title

9)Persist cache on shutdown so that last state is available on restart 
 
10)The code must be modular/follow design principles(solid)
 
11)It must have Swagger and Unit tests.
 
