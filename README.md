# **SPA (Single Page  Application) and OAuth2 demo.**

Host various web apps I have developed using React. All you need to have access to these apps is staring this project and sign in your github account.

### Web apps TODO
- [x] Calculator (no authorization)
- [ ] Online Epub to PDF service. (authorized)
- [ ] Real time currency exchange rate page. (authorized)


## **Spring Boot Backend**
Essential Configuration for SPA architecture:
* CSRF disabled
* CORS enabled and restriceted
* Stateless session management 
* JWT authentication

OAuth2 grant type success flow:
![OAuth2 grant type success flow](./pic/OAuth2%20grant%20type%20flow.png) 

### Backend TODO 
- [x] Authorization based on star status of this project.
- [x] Migrate deprecated jjwt libray.
- [x] Secret properties storage safety.




## **React Frontend**
Pure functional components. React core and router dom library are updated to lastest release. 

### Frontend TODO 
- [ ] UI for alert pop up.
- [ ] Token storage safety.
- [ ] More efficient authentication using React Context.



