# MenuFi Server

This is part of a larger MenuFi solution that includes the following projects:
* https://github.com/MenuFi/MenuFiWebPortal
* https://github.com/MenuFi/MenuFiAndroidApp

## RELEASE NOTES v1.0
### NEW FEATURES
* Added controllers services for web and android apps:
  - Login
  - Restaurant
  - Menu
  - Metrics
  - Rating
* Added authentication of requests using MenuFi tokens

### BUG FIXES:
* Corrected token generation

### KNOWN BUGS:
* Authentication works with only one instance on google cloud
* Password comparison is insecure

## Install Guide
### PREREQUISITE:
You must have following softwares installed on your host before proceeding:
1. Gradle
2. Java
3. Google App Engine
4. MYSQL
5. (optional but highly recommended if your host is MACOS) Homebrew

### DOWNLOAD:
1. Navigate to https://github.com/MenuFi/MenuFiWebPortal
2. Click `Clone or download`
3. Click `Download ZIP`

### DEPENDENCIES

All other dependencies are resolved in build.gradle file.

### BUILD

Run the following command in the shell under the directory of your package:
> build gradle

### INSTALLING AND RUNNING APPLICATION

On a local host, run the following command in the shell under the directory of your package:
> gradle appengineRun

To run on remote services like google app engine, set up your google appengine account on your host and run the following command:
> gradle appengineDeploy


#### Configuration
Edit `spring.profiles.active` property in _src/main/resources/application.properties_ to either:
* `default` (uses `CloudSqlQuerier`)
* `test` (uses `MockQuerier`)

#### Gradle Wrapper
Change gradle properties in `gradle/wrapper`

To run locally: `./gradlew appengineRun`

More tasks: `./gradlew tasks`

## TROUBLESHOOTING
### Google App Engine
Set scaling options to manual, set at 1 instance.

Make sure to authenticate before using gradle tasks:
https://cloud.google.com/sdk/gcloud/reference/auth/login

Running the local version of the server still uses the live data store, so check that the SQL server is up on Google Cloud.

### Entry Point
com.menufi.backend.springboot.MenuFiSpringBootApplication

### Public API Routes
All responses are of the form:
> { status: string, data: [] or {}, message: string }

Authorization schema requires Authentication header in the form of:
> Authorization: MenuFi YourLoginToken

#### Login
`POST` [/patron/loginToken](http://menufi-192821.appspot.com/patron/loginToken)

`POST` [/patron/registration](http://menufi-192821.appspot.com/patron/registration)

`POST` [/restaurant/loginToken](http://menufi-192821.appspot.com/restaurant/loginToken)

`POST` [/restaurant/registration](http://menufi-192821.appspot.com/patron/registration)

#### Menu
`GET` [/preferences](http://menufi-192821.appspot.com/preferences)

`GET` [/restaurants/{restaurantId}/items](http://menufi-192821.appspot.com/restaurants/1/items)

`POST` [/restaurants/{restaurantId}/items](http://menufi-192821.appspot.com/restaurants/1/items)

`GET` [/restaurants/{restaurantId}/items/{menuItemId}](http://menufi-192821.appspot.com/restaurants/1/items/5)

`PUT` [/restaurants/{restaurantId}/items/{menuItemId}](http://menufi-192821.appspot.com/restaurants/1/items/5)

#### Metrics
`GET` [/restaurants/{restaurantId}/items/{menuItemId}/clicks](http://menufi-192821.appspot.com/restaurants/1/items/5/clicks)

`POST` [/restaurants/{restaurantId}/items/{menuItemId}/clicks](http://menufi-192821.appspot.com/restaurants/1/items/5/clicks)

#### Rating
`GET` [/items/{menuItemId}/rating](http://menufi-192821.appspot.com/items/5/rating)

`GET` [/items/{menuItemId}/rating/0](http://menufi-192821.appspot.com/items/5/rating/0)

`PUT` [/items/{menuItemId}/rating/0](http://menufi-192821.appspot.com/items/5/rating/0)

#### Restaurant
`GET` [items/restaurants](http://menufi-192821.appspot.com/restaurants)
