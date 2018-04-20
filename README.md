# MenuFi Server
## Entry Point
com.menufi.backend.springboot.MenuFiSpringBootApplication

## Installation
### MACOS 10.12.6
Prerequisite: Homebrew, Gradle, MySQL, Java, Google App Engine
1. Clone the github repo to your local host: 
> git clone https://github.com/MenuFi/MenuFiBackend.git
2. Redirect to the repo directory and: 
> build gradle
3. Run the backend local: 
> gradle appengineRun


## Configuration
Edit `spring.profiles.active` property in _src/main/resources/application.properties_ to either:
* `default` (uses `CloudSqlQuerier`)
* `test` (uses `MockQuerier`)

## Gradle Wrapper
Change gradle properties in `gradle/wrapper`

To run locally: `./gradlew appengineRun`

More tasks: `./gradlew tasks`

## Public API Routes
All responses are of the form:
> { status: string, data: [] or {}, message: string }

Authorization schema requires Authentication header in the form of:
> Authentication: MenuFi YourLoginToken

### Login
`POST` [/patron/loginToken](http://menufi-192821.appspot.com/patron/loginToken)

`POST` [/patron/registration](http://menufi-192821.appspot.com/patron/registration)

`POST` [/restaurant/loginToken](http://menufi-192821.appspot.com/restaurant/loginToken)

`POST` [/restaurant/registration](http://menufi-192821.appspot.com/patron/registration)

### Menu
`GET` [/preferences](http://menufi-192821.appspot.com/preferences)

`GET` [/restaurants/{restaurantId}/items](http://menufi-192821.appspot.com/restaurants/1/items)

`POST` [/restaurants/{restaurantId}/items](http://menufi-192821.appspot.com/restaurants/1/items)

`GET` [/restaurants/{restaurantId}/items/{menuItemId}](http://menufi-192821.appspot.com/restaurants/1/items/5)

`PUT` [/restaurants/{restaurantId}/items/{menuItemId}](http://menufi-192821.appspot.com/restaurants/1/items/5)

### Metrics
`GET` [/restaurants/{restaurantId}/items/{menuItemId}/clicks](http://menufi-192821.appspot.com/restaurants/1/items/5/clicks)

`POST` [/restaurants/{restaurantId}/items/{menuItemId}/clicks](http://menufi-192821.appspot.com/restaurants/1/items/5/clicks)

### Rating
`GET` [/items/{menuItemId}/rating](http://menufi-192821.appspot.com/items/5/rating)

`GET` [/items/{menuItemId}/rating/0](http://menufi-192821.appspot.com/items/5/rating/0)

`PUT` [/items/{menuItemId}/rating/0](http://menufi-192821.appspot.com/items/5/rating/0)

### Restaurant
`GET` [items/restaurants](http://menufi-192821.appspot.com/restaurants)
