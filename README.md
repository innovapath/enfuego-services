# Repository of services provided by Enfuego.Tech

## Parser Services 
These services take in input a single PDF file(RESUME or LinkedIn PDF) and returns a User object as in a JSON format.

* **POST** : To create a new user profile from a resume(.pdf, .doc, .docx, .rtf, .txt)
	* *URL structure* : {API_URL}/parse/resume
* **POST** : To create a new user profile from a LinkedIn(.pdf)
	* *URL structure* : {API_URL}/parse/linkedIn

## User Data Services
Services which will help us to GET, PUT, POST, DELETE User information.

* **GET** : To get an existing user
	* *URL structure* : {API_URL}/users/{email}/profile
* **POST** : To create a new user
	* *URL structure* : {API_URL}/users/
* **PUT** : To modify an existing user profile
	* *URL structure* : {API_URL}/users/{email}/profile


## Job Data Services
Services which will help us to GET, PUT, POST, DELETE Job History information for a user.

* **GET** : To get an existing job for a user
	* *URL structure* : {API_URL}/users/{email}/jobs/{jobId}
* **POST** : To create a new job
	* *URL structure* : {API_URL}/users/{email}/jobs
* **PUT** : To modify an existing job
	* *URL structure* : {API_URL}/users/{email}/jobs/{jobId}
* **DELETE** : To delete an existing job
	* *URL structure* :  {API_URL}/users/{email}/jobs/{jobId}

## Skills Data Services
Services which will help us to GET, PUT, POST, DELETE Skills information for a user.

* **GET** : To get an existing skill for a user
	* *URL structure* : {API_URL}/users/{email}/skills/{skillId}
* **POST** : To create a skill for the user
	* *URL structure* : {API_URL}/users/{email}/skills
* **PUT** : To modify an existing skill
	* *URL structure* : {API_URL}/users/{email}/skills/{skillId}
* **DELETE** : To delete an existing skill
	* *URL structure* :  {API_URL}/users/{email}/skills/{skillId}


## Degree Data Services
Services which will help us to GET, PUT, POST, DELETE Degree(Education) information for a user.

* **GET** : To get an existing degree for a user
	* *URL structure* : {API_URL}/users/{email}/degrees/{degreeId}
* **POST** : To create a degree for the user
	* *URL structure* : {API_URL}/users/{email}/degrees
* **PUT** : To modify an existing degree
	* *URL structure* : {API_URL}/users/{email}/degrees/{degreeId}
* **DELETE** : To delete an existing degree
	* *URL structure* :  {API_URL}/users/{email}/degrees/{degreeId}


## Accomplishment Data Services
Services which will help us to GET, PUT, POST, DELETE  information for a user.

* **GET** : To get an existing accomplishment for a user
	* *URL structure* : {API_URL}/users/{email}/accomplishments/{accomplishmentId}
* **POST** : To create a skill for the user
	* *URL structure* : {API_URL}/users/{email}/accomplishments
* **PUT** : To modify an existing skill
	* *URL structure* : {API_URL}/users/{email}/accomplishments/{accomplishmentId}
* **DELETE** : To delete an existing job
	* *URL structure* :  {API_URL}/users/{email}/accomplishments/{accomplishmentId}


## Certifications Data Services
Services which will help us to GET, PUT, POST, DELETE Job History information for a user.

* **GET** : To get an existing certification for a user
	* *URL structure* : {API_URL}/users/{email}/certifications/{certificationId}
* **POST** : To create a certification for the user
	* *URL structure* : {API_URL}/users/{email}/certifications
* **PUT** : To modify an existing certification
	* *URL structure* : {API_URL}/users/{email}/certifications/{certificationId}
* **DELETE** : To delete an existing certification
	* *URL structure* :  {API_URL}/users/{email}/certifications/{certificationId}
	
## SearchMarkets Data Services
Services which will help us to GET, PUT, POST, DELETE Search Market information for a user.

 **GET** : To get an existing Search Market for a user
	* *URL structure* : {API_URL}/users/{email}/searchMarkets/{searchMarketId}
* **POST** : To create a Search Market for the user
	* *URL structure* : {API_URL}/users/{email}/searchMarkets
* **PUT** : To modify an existing Search Market
	* *URL structure* : {API_URL}/users/{email}/searchMarkets/{searchMarketId}
* **DELETE** : To delete an existing Search Market
	* *URL structure* :  {API_URL}/users/{email}/searchMarkets/{searchMarketId}




