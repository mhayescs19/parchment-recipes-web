# Development Docs
This guide will track the development of the parchment recipes Spring backend in Java.

## How to Track Development
In this project, [GitHub Projects](https://docs.github.com/en/issues/planning-and-tracking-with-projects/learning-about-projects/about-projects) will track progress. Sprints will be scheduled with milestones and issues will be placed in a Kanban board.  

I will be using the following issue template which I have derived from [Atlassian](https://community.atlassian.com/t5/Jira-articles/How-to-write-a-useful-Jira-ticket/ba-p/2147004), GitHub's built-in issue template suggestions, and [New Relic's Agile handbook](https://docs.newrelic.com/docs/agile-handbook/appendices/ticket-best-practices/#:~:text=Browser%20API%3A%20Update%20custom%20attribute%2Drelated%20docs%20or%20Distributed%20tracing%3A%20Add%20more%20detail%20about%20CAT%20relationship):

```markdown
**Objective**
Summarize the new feature with a user story or a description.

**Acceptance Criteria**  
List the criteria necessary for the feature to be added properly.

**Tasks**  
- [ ] Task 1
- [ ] Task 2

**Extra Details**  
Add any other context or screenshots about the feature request here.
```

## Configure the Spring Initializr
Access the [spring initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.3.1&packaging=jar&jvmVersion=17&groupId=com.example&artifactId=demo&name=demo&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.demo&dependencies=web,lombok,mysql,data-jpa) and configure with Java and Maven to manage dependencies. This project uses Spring Web, Spring Data JPA, MySQL Driver, and Lombok.

## Installing a MySQL Local Instance
First, download MySQL community for your machine specifications and install. On macOS, toggling of the database is visible in the system preferences panel.  

Next, access MySQL by using the `mysql` command. Note that MySQL is **not** immediately added to the `PATH` variable. Notice that using the following terminal command will not work if you are not at `/usr/local/mysql-version_number-os-architecture`.
```sh
mysql -u root -p
```
Remediate the issue by adding `mysql` to the `PATH` and make the command accessible anywhere by using the following steps:
1. Access the zsh profile for editing.
      ```sh
      nano ~/.zshrc
      ```
2. Add the following path based on the specific naming of the mysql folder.
      ```sh
      # in zshrc file add
      export PATH=${PATH}:/usr/local/mysql-9.0.0-macos14-arm64/bin
      ```
3. Source the changes.
      ```sh
      source ~/.zshrc
      ```
Now, enter the root user password to access the MySQL interface. Upon success, the mysql interface will look similar to below:
```
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 8
Server version: 9.0.0 MySQL Community Server - GPL

Copyright (c) 2000, 2024, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>
```

Despite installing correctly and configuring a root password, you may recieve the following error: 
```sql
ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password: YES)
```
I faced this issue becuase I had a different version of MySQL in `/usr/local/`. Once I removed the duplicate instance, the root password from the configuration was accepted. If those steps do not work, you may need to change the root password using the following steps:
1. Stop the MySQL server in the systems panel.
2. Access MySQL in safe mode with the following command.
   ```sh
   sudo mysqld_safe --skip-grant-tables &
   ```
3. Login without a password.
   ```sh
   mysql -u root
   ```
4. Reset the password
   ```sql
   FLUSH PRIVILEGES;
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
   ```
Before adding a database, let's [create a new user](https://www.digitalocean.com/community/tutorials/how-to-create-a-new-user-and-grant-permissions-in-mysql#:~:text=Run%20the%20following%20command%20to%20create%20a%20user%20that%20authenticates%20with%20caching_sha2_password.%20Be%20sure%20to%20change%20sammy%20to%20your%20preferred%20username%20and%20password%20to%20a%20strong%20password%20of%20your%20choosing%3A) and give explicit permissions for clarity.
```sql
CREATE USER 'username'@'host' IDENTIFIED BY 'password';
GRANT CREATE, ALTER, INSERT, UPDATE, SELECT, DELETE, DROP ON *.* TO 'username'@'host'; -- allow create, edit, read, delete
```
Verify that the user has been created by listing all users. I created a new user parchment which is visible below:
```sql
SELECT User, Host FROM mysql.user;
+------------------+-----------+
| User             | Host      |
+------------------+-----------+
| mysql.infoschema | localhost |
| mysql.session    | localhost |
| mysql.sys        | localhost |
| parchment        | localhost |
| root             | localhost |
+------------------+-----------+
```
Exit mysql and login to the new user by entering the user password when prompted.
```sql
mysql -u username -p
```
Before creating configuring a database, ensure your MySQL server is listening on the correct port (the default is 3306). I had to reinitialize the server via the system preferences panel to allow login and for the server to listen on the correct port.
```sql
SHOW GLOABL VARIABLES LIKE 'port';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| port          | 3306  | -- if the port is 0, the server is improperly configured
+---------------+-------+
```
## Create a Database
Create a new database called parchment_dev. Also, view all databases to confirm creation.
```sql
CREATE DATABASE parchment_dev;
SHOW DATABASES;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| parchment_dev      |
| performance_schema |
| sys                |
+--------------------+
```
The new database `parchment_dev` is now ready for connection with Spring.
## Connect the MySQL Database to Spring
The MySQL database must be connected to Spring via `application.properties` file by listing the database url, username, and password. The database URL is composed with the host, port, and database name as follows: `jdbc:mysql://host:port/database_name`.
```
spring.application.name=parchment-recipes-web
spring.datasource.url=jdbc:mysql://localhost:3306/parchment_dev
spring.datasource.username=user_name
spring.datasource.password=password
```
Also, provide the same details to the [IntelliJ database tool](https://www.jetbrains.com/help/idea/database-tool-window.html) to view the data, users, and other details that are helpful during development. Now, changes will be visible in the Database sidebar. During development, the toolbar will enable you to view tables (`columns, keys, foeign keys, and indexes`) and visual the schema with an [entity relationship diagram](https://www.lucidchart.com/pages/er-diagrams#:~:text=An%20Entity%20Relationship%20(ER)%20Diagram%20is%20a%20type%20of%20flowchart%20that%20illustrates%20how%20%E2%80%9Centities%E2%80%9D%20such%20as%20people%2C%20objects%20or%20concepts%20relate%20to%20each%20other%20within%20a%20system).

## Designing the SQL Model
The constraints of SQL requires the use of relationships to model even moderately complex data. Atlassian provides an example of modeling data for an album. In SQL, a respective tracks, albums and artists table models the data optimizing to eliminate duplicate values. View the [Atlassian article](https://www.atlassian.com/data/sql/joins) to learn more about primary keys and foreign keys.

In the first iteration of the project, the focus is on creating recipe persistence and successfully achieving CRUD operations on all portions of a recipe.

SQL uses structured tables which introduces constraints on how data is modeled. In the context of a recipe, because we do not know the varying size of the ingredient and direction lists before the recipe is created, multiple different tables must be associated with each other using relationships. An intuitive non-relational approach may include creating a table with a set number of columns (eg. 50 for ingredients and 50 for directions). While this would achieve persisting a recipe, a recipe with less than 50 ingredients or 50 directions would result in unused fields. 

### First Iteration: A Simple Entity

Before using tables with relationships, I created a single recipe table and API endpoint to access the database. Validating the basic table first abides by [incremental development practices](https://en.wikipedia.org/wiki/Iterative_and_incremental_development). During the first iteration, the recipe table follows the model below:  

**Recipe Table**

| id | title          | source_url                                                             |
|----|----------------|------------------------------------------------------------------------|
| 1  | My Test Recipe | https://start.spring.io/                                               |
| 2  | Test recipe 2  | https://www.jetbrains.com/help/idea/database-tool-window.html#overview |

Now, experimentation with the simple recipe table can be executed along with tests for verification.

I created a simple endpoint to verify the functionality by the JSON response.

```java
@Autowired // field injection of bean
private RecipeRepository recipeRepository;

@GetMapping("/")
public ResponseEntity<List<Recipe>> listRecipes() {
    Recipe myRecipe = Recipe.builder()
                        .title("My Test Recipe")
                        .source_url("https://start.spring.io/")
                        .build();

    recipeRepository.save(myRecipe);

    Recipe myRecipe2 = Recipe.builder()
                        .title("Test recipe 2")
                        .source_url("https://www.jetbrains.com/help/idea/database-tool-window.html#overview")
                        .build();

    recipeRepository.save(myRecipe2);

    return new ResponseEntity<>(recipeRepository.findAll(), HttpStatus.OK);
}
```
Valid JSON response:
```json
[
  {
    "id": 1,
    "title": "My Test Recipe",
    "source_url": "https://start.spring.io/"
  },
  {
    "id": 2,
    "title": "Test recipe 2",
    "source_url": "https://www.jetbrains.com/help/idea/database-tool-window.html#overview"
  }
]
```
The objects above are created using the [Builder design pattern](https://medium.com/javarevisited/builder-design-pattern-in-java-3b3bfee438d9#:~:text=How%20to%20Implement%20Builder%20Design%20Pattern%20in%20Java%3F) which allows for the chaining of methods to set object attributes. The builder pattern will scale well to clearly create objects with a large number of attributes.

### Second Iteration: Adding a Bidirectional One-To-Many Relationship
In my design, for a recipe to have multiple ingredients, a one-to-many relationship enables for there to be a variable number of ingredients for a given recipe. Since both the recipe must know what the ingredients are and an ingredient must know what recipe it is a part of, a bidirectional relationship is necessary.  

In the database, a one-to-many relationship will represent itself in two separate tables with a foreign key in the ingredient table referencing its respective recipe.

**Recipe Table**

| id | title            | source_url                                                             |
|----|------------------|------------------------------------------------------------------------|
| 1  | My Test Recipe   | https://start.spring.io/                                               |
| 2  | My Test Recipe 2 | https://www.jetbrains.com/help/idea/database-tool-window.html#overview |

**Ingredient Table**

| id | amount | unit        | ingredient_type | recipe_id |
|----|--------|-------------|-----------------|-----------|
| 1  | 5      | cups        | flour           | 1         |
| 2  | 2      | tablespoons | oil             | 1         |
| 3  | 0.3    | cups        | sugar           | 2         |
| 4  | 2.5    | teaspoons   | vanilla         | 2         |
| 5  | 3      | tablespoons | water           | 2         |

When data that contain relationships are serialized into JSON, a recursion issue causes infinite nesting. `@JsonManagedReference` is used to prevent recursive serialization. Read more about recursive serialization problem from [Alexander Obregon](https://medium.com/@AlexanderObregon/understanding-springs-jsonbackreference-and-jsonmanagedreference-annotations-783090468572#:~:text=The%20first%20Post,its%20Post%20objects.).
```java
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Recipe {

    /*
        ...
     */

    /*
    mapped by foreign key to one recipe
    cascadeType ensures that if a recipe is deleted, all remaining ingredients are also removed
    orphanRemoval ensures that there are no orphan ingredients without a valid FK to a recipe
    jsonManagedReference - serializes the object forwards, in this case looking for ingredients
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ingredient> ingredients = new ArrayList<>();
}
```
```java
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Ingredient {

    /*
         ...
     */

   /*
    JsonBackReference - prevents recursion on the reference entity, in this case the details of the recipe in the relationship is ignored to prevent infinite recursion
    */
    @ManyToOne
    @JoinColumn(name = "recipe_id") // foreign key field
    @JsonBackReference
    private Recipe recipe;
}
```
In summary, `@JsonManagedReference` serializes the relationship forwards and continually looks for more ingredients. Once an ingredient is located, `@JsonBackReference` ensures that another recipe is not nested inside of the ingredient causing infinite recursion.  

Now, the serialized result of multiple objects looks like this:
```json
[
  {
    "id": 1,
    "title": "My Test Recipe",
    "source_url": "https://start.spring.io/",
    "ingredients": [
      {
        "id": 1,
        "amount": 5.0,
        "unit": "cups",
        "ingredientType": "flour"
      },
      {
        "id": 2,
        "amount": 2.0,
        "unit": "tablespoons",
        "ingredientType": "oil"
      }
    ]
  },
  {
    "id": 2,
    "title": "My Test Recipe 2",
    "source_url": "https://www.jetbrains.com/help/idea/database-tool-window.html#overview",
    "ingredients": [
      {
        "id": 3,
        "amount": 0.3,
        "unit": "cup",
        "ingredientType": "sugar"
      },
      {
        "id": 4,
        "amount": 2.5,
        "unit": "teaspoons",
        "ingredientType": "vanilla"
      },
      {
        "id": 5,
        "amount": 3.0,
        "unit": "tablespoons",
        "ingredientType": "water"
      }
    ]
  }
]
```

## Creating the First Tests
Now that we have code with sample data we need to verify that each component properly relates to the expected data. Let's create JUnit5 tests for Spring. [This video](https://www.youtube.com/watch?v=pNiRNRgi5Ws) by Dan Vega explains basic Spring testing.  

### Given, When, Then Testing Structure

I have decided to write my first test using the [given-when-then](https://martinfowler.com/bliki/GivenWhenThen.html#:~:text=The%20given%20part,the%20specified%20behavior.) test structure. The different components are:
- **Given**: setup of the conditions for the test (eg. data load, object creation)
- **When**: the subject of the test; usually an operation that causes a data change
- **Then**: check of the expected result

Given-when-then is similar to another test structure called [arrange-act-assert](https://automationpanda.com/2020/07/07/arrange-act-assert-a-pattern-for-writing-good-tests/#:~:text=Arrange%20inputs%20and,passes%20or%20fails.) with the same three component approach.

### JPA Test
I am narrowing my testing to focus on the persistence layer of the Spring application. I created a special profile, `appliction-test.properties` and configured a test MySQL database via terminal. 

Tests in Spring live in the `test/java` directory and can either be autogenerated or created manually. Inside the test directory, the package hierarchy parallels the `src/` directory with corresponding tests for each class.

Spring provides multiple types tests that load a varying amount of the application. `@WebMvcTest` enables testing at the Controller and Service layer, `@SpringBootTest` will load in the whole application context, and `@DataJpaTest` powers validating the persistence layer.

Given that this project is currently build from the persistence layer outwards, I used the `@DataJpaTest` annotation for my first test. The JPA Test [disables all autoconfiguration except those essential for JPA operations](https://docs.spring.io/spring-boot/api/java/org/springframework/boot/test/autoconfigure/orm/jpa/DataJpaTest.html#:~:text=instead%20apply%20only%20configuration%20relevant%20to%20JPA%20tests.). Consider the following JPA test:

```java
/*
DataJpaTest - disables all autoconfiguration except those essential for JPA operations
ActiveProfiles - "test" tells spring to use application-test.properties file
AutoConfigureTestDatabase.Replace.NONE - spring will not replace the current 
application datasource. since the ActiveProfiles annotation is set to "test",
spring will use the DB that is configured in the properties(-test) file

Typically, AutoConfigureTestDatabase will try to configure an H2 in-memory database 
which we do not want as there is a connection to test database in the properties file
 */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;

    private Recipe myTestRecipe1;
    private Recipe myTestRecipe2;

    @Test
    @DisplayName("Verify ingredient foreign keys are set correctly")
    public void givenRecipeWithIngredients_whenSave_thenAllIngredientsHaveRecipeForeignKey() {
        /*
        Given: recipe with ingredients
         */
        Recipe myTestRecipe1 = new Recipe();
        myTestRecipe1.setTitle("My Test Recipe");
        myTestRecipe1.setSource_url("https://start.spring.io/");

        List<Ingredient> ingredientList = new ArrayList<>();

        ingredientList.add(Ingredient.builder() // prep ingredients list
                .amount(5.0)
                .unit("cups")
                .ingredientType("flour")
                .recipe(myTestRecipe1)
                .build());
        ingredientList.add(Ingredient.builder() // prep ingredients list
                .amount(2.0)
                .unit("tablespoons")
                .ingredientType("oil")
                .recipe(myTestRecipe1)
                .build());
        myTestRecipe1.setIngredients(ingredientList);

        Recipe myTestRecipe2 = new Recipe();
        myTestRecipe2.setTitle("My Test Recipe 2");
        myTestRecipe2.setSource_url("https://www.jetbrains.com/help/idea/database-tool-window.html#overview");

        List<Ingredient> ingredientList2 = new ArrayList<>();

        ingredientList2.add(Ingredient.builder() // prep ingredients list
                .amount(0.3)
                .unit("cup")
                .ingredientType("sugar")
                .recipe(myTestRecipe2)
                .build());
        ingredientList2.add(Ingredient.builder() // prep ingredients list
                .amount(2.5)
                .unit("teaspoons")
                .ingredientType("vanilla")
                .recipe(myTestRecipe2)
                .build());
        ingredientList2.add(Ingredient.builder()
                .amount(3.0)
                .unit("tablespoons")
                .ingredientType("water")
                .recipe(myTestRecipe2)
                .build());

        myTestRecipe2.setIngredients(ingredientList2);

        /*
        When: save recipe
         */
        Recipe myPersistedRecipe1 = recipeRepository.save(myTestRecipe1);
        Recipe myPersistedRecipe2 = recipeRepository.save(myTestRecipe2);
        /*
        Then: all ingredients have the correct recipe foreign key
         */
        myPersistedRecipe1.getIngredients().forEach(ingredient -> {
            assertEquals(myPersistedRecipe1.getId(),ingredient.getRecipe().getId()); // persisted recipe id matches the recipe id stored as a foreign key in the ingredient
        });

        myPersistedRecipe2.getIngredients().forEach(ingredient -> {
            assertEquals(myPersistedRecipe2.getId(),ingredient.getRecipe().getId());
        });

    }
}
```
Take note of the configuration that is applied through the annotations on the test class. 
- `@DataJpaTeset` disables all autoconfiguration except those essential for JPA operations.
- `@ActiveProfiles` signals to Spring to reference the `application-test.properties` file which has a JDBC connection to a test database.
- `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)` informs Spring that the current application datasource should not be replaced. Typically, AutoConfigureTestDatabase will try to configure an H2 in-memory database which is no the desired functionality there is a connection to test database in the `application-test.properties` file.

In my development, I found that the standard .properties configuration (including the use of MySQL) was causing errors in the generation of the H2 in-memory database which led to me to configuration a separate local MySQL database strictly for testing.  

## Types of HTTP Requests
- `GET`: read of data/resource (`@GetMapping`)
- `POST`: creates a new piece of data/resource (`@PostMapping`)
- `PUT`: Updates an **entire** resource or creates one if the data is not present (`@PutMapping`)
- `PATCH`: Updates a **portion** of the resource. The data to update is passed. (`@PatchMapping`)
- `DELETE`: delete data/resource (`@DeleteMapping`)

Resources: [FreeCodeCamp](https://www.freecodecamp.org/news/http-request-methods-explained/) | [Suhas Chatekar](https://medium.com/@suhas_chatekar/why-you-should-use-the-recommended-http-methods-in-your-rest-apis-981359828bf7)



