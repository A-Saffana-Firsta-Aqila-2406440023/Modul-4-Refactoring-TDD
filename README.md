Deployment Link: [https://upset-tamma-firstaqila-156582f7.koyeb.app/](https://upset-tamma-firstaqila-156582f7.koyeb.app/)

<details>
<summary>Modul 1</summary>

# Reflection 1

## 1. Clean Code Principles Applied
- **Meaningful Naming**: I have applied meaningful naming for variables, methods, and classes. I choose this because it makes the code "self-documenting," allowing other developers (or my future self) to understand the logic without reading deep comments.
- **Small and Focused Functions**: I split the application logic into four focused packages. By keeping these roles separate, I ensure each function has a small scope. I chose this because it prevents logic from getting tangled, making the application much easier to test and maintain.
  - **Model**: Represents the data.
  - **Controller**: Only manages requests/responses and forwards them to the service.
  - **Service**: Implements the business logic.
  - **Repository**: Handles database access. 

## 2. Secure Coding Practices Applied
- **UUID for Product Identification**: I implemented UUID (Universally Unique Identifier) to give each product a unique ID. This makes it harder for anyone to guess or access other product data by simply changing a number in the URL, keeping the information more secure.

## 3. Areas for Improvement
- **Weak Form Validation (Quantity)**: I noticed that the quantity field still accepts text. This is a mistake because it could break the business logic or cause errors in calculations. After this, I can improve the code by changing the data type to an integer and adding the `@Min(0)` annotation to ensure we only store non-negative values.
- **Handling Empty Inputs**: Currently, the form accepts empty values, which could lead to a `NullPointerException` when the application tries to process the data. It is a risk for application stability. After this, I can implement validation using `@NotBlank` or `@NotNull` to reject empty submissions.

# Reflection 2
## 1. Unit Testing
After writing unit tests, I feel much more secure knowing that my code has a baseline of protection against errors. Regarding the number of tests, I believe there is no fixed number of test cases required for a class; instead, the focus should be on the quality and variety of scenarios. It is not enough to only test the "happy path" or successful scenarios; I must also account for negative cases and edge cases to ensure the program can handle unexpected inputs gracefully. While I use Code Coverage as a helpful metric to see which parts of my code have been executed, I am aware that 100% coverage does not guarantee that the code is 100% bug-free. For example, maybe we forgot to include certain logic scenarios that could still lead to errors in production.

## 2. Functional Testing and Code Quality
- **Potential Clean Code Issues**: If I create a new test suite by copying the same setup procedures and instance variables from a previous class, I am creating code duplication. This violates the DRY (Don’t Repeat Yourself) principle.
- **Why it reduces code quality**: Duplicated setup code makes the test suite harder to maintain. If I ever need to change the browser type (e.g., from Chrome to Firefox) or modify the server port, I would have to update it in multiple files. This redundancy increases the risk of errors.
- **Suggested Improvements**: To make the code cleaner, I should create a Base Class for my functional tests. This base class would contain the common setup (`@BeforeEach`) and teardown (`@AfterEach`) logic. I can simply extend this base class. This way, I only write the configuration once, keeping my test suites focused only on their unique testing logic.

</details>

<details>
<summary>Modul 2</summary>

## Code Quality Issues and Fix Strategy
I used **SonarCloud** to help analyze various issues in my codebase. Here are the specific issues detected and how I solved them:

1. **Dissimilar Types in Assertions**: I accidentally comparing incompatible data types in a unit test TvT. I fixed this by ensuring both arguments in the `assertEquals` and `assertNotEquals` functions share the same data type.
2. **Field Injection**: In `ProductController.java` and `ProductServiceImpl.java`, I found that `@Autowired` was used directly on fields, which allows objects to be created in an invalid state and makes testing more difficult. I fixed this by replacing field injection with constructor injection, declaring dependencies as `final` fields, making them explicit, immutable, and the class more testable.
3. **Unnecessary `throws Exception` Declaration**: I noticed this in functional tests where the exception was never actually thrown. I removed them from the method signatures to improve code clarity.
4. **Unused Imports**: There were several imports, such as `MockBean`, that were no longer being used but were still left in the test files. I fixed this by removing all unreferenced imports to keep the file structure clean.
5. ** Ungrouped Dependencies in `build.gradle.kts`**: Dependencies for different purposes were mixed together, making it hard to read. I fixed this by reorganizing the dependency declarations so that dependencies are grouped logically by their scope (e.g., `implementation`, `testImplementation`).

## Has the Current Implementation Met the Definition of CI and CD?
Yes, the current implementation has met the definition of both Continuous Integration and Continuous Deployment. On the CI side, tests suites and SonarCloud analysis are automatically run on every `push` and `pull request` across all branches, ensuring code changes are continuously validated and helping detect code quality issues early in the development process. In addition, Scorecard is also integrated to perform security checks on the codebase. On the CD side, the application is automatically deployed to Koyeb whenever changes are pushed to the `main` branch, ensuring the latest version is always delivered to production without any manual intervention.

</details>

<details>
<summary>Modul 3</summary>

## Explain what principles you apply to your project!
1. Single Responsibility Principle (SRP)
   - What was changed: Separated CarController from the ProductController.java file into its own standalone CarController.java file.
   - Why it was changed: Previously, ProductController was handling two different domains (Product and Car). By isolating them, each controller now has a single responsibility: managing its own specific entity.

2. Open-Closed Principle (OCP)
   - What was changed: Refactored the update method in CarRepository to replace the entire object using carData.set(i, updatedCar) instead of manually setting each attribute one by one.
   - Why it was changed: The previous method required modifying the repository every time a new field was added to the Car model, making it "open" for modification. The new approach is "closed" for modification because the repository no longer cares about the internal fields of the Car object. This allows the Car model to grow without needing constant changes to the persistence logic.

3. Liskov Substitution Principle (LSP)
   - What was changed: Removed the inheritance relationship where CarController previously extended ProductController.
   - Why it was changed: A CarController is not a specialized version of a ProductController, so the "Is-A" relationship was semantically incorrect. Inheriting from it forced CarController to carry unnecessary dependencies and potential behaviors of the parent that it didn't need. Removing this inheritance ensures that each class stands on its own without violating the expected behavior of a controller.

4. Interface Segregation Principle (ISP)
   No further changes were made as the existing interfaces (CarService and ProductService) are already lean and specific to their respective domains.

5. Dependency Inversion Principle (DIP)
   - What was changed: Modified the CarController to import and depend on the CarService interface instead of the concrete CarServiceImpl.
   - Why it was changed: High-level modules should not depend on low-level implementation details; both should depend on abstractions. By using the interface, the controller remains decoupled from the specific implementation of how cars are processed. This allows for easier testing (mocking) and makes it possible to swap the service implementation without touching the controller code.

  
## Explain the advantages of applying SOLID principles to your project with examples.
- By applying SOLID principles, our project has become significantly more maintainable and robust against changing requirements. We have successfully achieved loose coupling between components, ensuring that a modification in one part of the system doesn't trigger a cascade of bugs elsewhere. Furthermore, this approach enhances our project's testability because classes are smaller and depend on abstractions, allowing us to easily swap real implementations with mocks during unit testing. 
- One example: (LSP) By removing the CarController extends ProductController inheritance, we ensured that CarController doesn't inherit irrelevant behaviors. This prevents logic errors where our methods might expect one behavior but receive another.

## Explain the disadvantages of not applying SOLID principles to your project with examples.
- Without SOLID principles, our project would likely turn into "Spaghetti Code," where different functionalities are so tightly interwoven that they become difficult to untangle. This leads to Rigidity, where even a small feature request would require us to perform massive refactorings across multiple files due to hidden dependencies. Over time, our technical debt would grow so large that we would become hesitant to update the code, as every change would risk breaking unrelated parts of the application.
- One example: (ISP): A "Fat Interface" would have forced our CarController to acknowledge or even implement Product-related methods that it doesn't actually need, cluttering our implementation.

</details>

<details>
<summary>Modul 4</summary>

## Reflection on TDD Workflow
The TDD flow was actually pretty useful for me in this module because writing tests first forced me to figure out the code's purpose upfront. This really helped me avoid getting confused later, especially when juggling all the conditions for voucher validation and linking payment statuses to orders. Following the RED-GREEN-REFACTOR cycle kept me focused on writing only the exact code needed to make a test pass. Without this approach, I probably would have overcomplicated the logic or completely missed some important edge cases.

## Reflection on F.I.R.S.T. Principles
- **Fast**: My unit tests run fast because I used `Mockito` to handle external dependencies like the repository. Since there is no real database connection, the feedback is basically instant. 
- **Independent**: I made sure every test stands completely on its own by using the `@BeforeEach` annotation to set up fresh data. Because of this, the outcome of one test never accidentally messes up the results of another. 
- **Repeatable**: Since all the external parts are mocked, the tests give the exact same result every single time. This makes debugging way less stressful because a failed test actually means my code is broken, not the setup. 
- **Self-Validating**: I don't have to manually check if a test worked because I used clear assertions like `assertEquals` and `assertThrows`. The test suite automatically tells me if it passed or failed right away. 
- **Timely**: Because I actually stuck to the TDD rules, I wrote all these tests right before writing the production code. This is exactly when they are supposed to be written so they can effectively guide the actual development.
- 
</details>