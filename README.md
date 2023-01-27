# Vacation Tracker

## Instructions for starting the project

0. 	Create database (PostgreSQL)

		name - springbootdb
		user - root, root

1. 	Start app

		mvn spring-boot:run
    
  ***NOTE:***
  
  ***In VacationTrackerAdmin Postman Collection each request must be send separately - Run collection isn't working due to file upload.***

2. 	Login (VacationTrackerAdmin Postman Collection - LoginAdmin)

		POST method
		request URL: http://localhost:8080/api/auth/login
		auth - no
		body - raw: JSON ("userEmail":"bosko@rbt.bb","userPassword:"bosko")


3. 	Import employees file (VacationTrackerAdmin Postman Collection - ImportEmployees)

		POST method
		request URL: http://localhost:8080/api/admin/importEmployees
		auth - basic auth (username: bosko@rbt.bb, password: bosko)
		body - form-data: file (name: file), select file (employee_profiles.csv)


4. 	Import used vacation file (VacationTrackerAdmin Postman Collection - ImportUsedVacation)

		POST method
		request URL: http://localhost:8080/api/admin/importUsedVacation
		auth - basic auth (username: bosko@rbt.bb, password: bosko)
		body - form-data: file (name: file), select file (used_vacation_dates.csv)


5. 	Import vacation days

	  Variant A (all files at once): (VacationTrackerAdmin Postman Collection - ImportTotalVacationDays

		POST method
		request URL: http://localhost:8080/api/admin/importTotalVacationDays
		auth - basic auth (username: bosko@rbt.bb, password: bosko)
		body - form-data: file (name: file1), select file (example vacations_2019.csv), file (name: file2), select file (example vacations_2020.csv), file (name: file3),     select file (example vacations_2021.csv)


	  Variant B (each file separatly): 

		POST method
		request URL: http://localhost:8080/api/admin/importYearTotalVacationDays
		auth - basic auth (username: bosko@rbt.bb, password: bosko)
		body - form-data: file (name: file), select file (example vacations_2019)
		Repeat this 3 times if you have 3 files


6. 	Search total, used and available vacation days (employee) 

		6.1 Login: (VacationTrackerEmployee Postman Collection - LoginEmployee)

        POST method
        request URL: http://localhost:8080/api/auth/login
        auth - no
        body - raw: JSON ("userEmail":"user1@rbt.rs","userPassword:"Abc!@#$")
		  
	
		6.2 Search total/used/available days (VacationTrackerEmployee Postman Collection - SearchEmployee)

        GET method
        request URL: http://localhost:8080/api/employee/search
        auth - basic auth (username: user1@rbt.rs, password: Abc!@#$)
        params - 1. key: daysOption, value: "total" 2. key: year, value: "2019"


7.	Search used vacation days (employee) (VacationTrackerEmployee Postman Collection - SearchUsedDaysEmployee)

		GET method
		request URL: http://localhost:8080/api/employee/searchUsedVacationDays
		auth - basic auth (username: user1@rbt.rs, password: Abc!@#$)
		params - 1. key: dateFromParam, value: "2019-01-01" 2. key: dateToParam, value: "2019-10-10"
	

8.	Add used vacation days (employee) (VacationTrackerEmployee Postman Collection - AddUsedDaysEmployee)

		POST method
		request URL: http://localhost:8080/api/employee/addUsedVacationDays
		auth - basic auth (username: user1@rbt.rs, password: Abc!@#$)
		body - raw: JSON ("vacationStartDate":"2019-05-05","vacationEndDate:"2019-05-05")


9.	Search total, used and available vacation days (admin) (VacationTrackerAdmin Postman Collection - SearchAdmin)

		If you are logged as admin just proceed, if not, perform that step with admin details

		9.1 Search total/used/available days

		  GET method
		  request URL: http://localhost:8080/api/admin/adminSearch
		  auth - basic auth (username: bosko@rbt.bb, password: bosko)
		  params - 1. key: daysOption, value: "total" 2. key: year, value: "2019" 3. key: userEmail, value: "user1@rbt.rs"
	

10.	Serch used vacation days (admin) (VacationTrackerAdmin Postman Collection - SearchUsedDaysAdmin)

		If you are logged as admin just proceed, if not, perform that step with admin details

		GET method
		request URL: http://localhost:8080/api/admin/adminSearchUsedVacationDays
		auth - basic auth (username: bosko@rbt.bb, password: bosko)
		params - 1. key: dateFromParam, value: "2019-01-01" 2. key: dateToParam, value: "2019-10-10" 3. key: userEmailParam, value: "user1@rbt.rs"
	
		

11.	Data validation method - checkData (admin) (VacationTrackerAdmin Postman Collection - CheckData)

		If you are logged as admin just proceed, if not, perform that step with admin details

		GET method
		request URL: http://localhost:8080/api/admin/checkData
		auth - basic auth (username: bosko@rbt.bb, password: bosko)
		params - 1. key: yearFrom, value: "2019" 2. key: yearTo, value: "2021"
	
