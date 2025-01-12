# DA50-Project

Student project - Code a website and an app in Java

## Repository organization
There is a main and 3 major branches: `Developpment`, `Documentation` and `website`.

In the Documentation branches, you will be able to find the specifications of the projet, the technical report, a pitch video, and so on...
In the website branch, you will be able to find all the files related to the website part of the project.
In the Developpment and main branches, you will be able to find all the files related to the desktop app part of the project. 

## Installation

This application requires **MySQL 8.4.3** to be installed locally and **Java JDK 21 or later**.

**Steps:**

1.  **Install MySQL 8.4.3 LTS:** Download and install the MySQL 8.4.3 LTS version from: [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)
    *   **Important:** During the installation, you will be asked to set a **root password**. Remember this password, as you will need it for the next step.

2.  **Install Java JDK (if not already installed):**
    *   Verify that you have Java JDK 21 or later installed. Open a command prompt or terminal and run: `java -version`
    *   If you don't have the required version, download and install the latest Java JDK from [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)
    *   Set up `JAVA_HOME` environment variable and add `%JAVA_HOME%\bin` to the `PATH` variable.

3.  **Build the project with Maven:**
    *   Open a command prompt or terminal.
    *   Navigate to the root directory of the project (where the `pom.xml` file is located).
    *   Execute the command:
        ```bash
        mvnw clean install
        ```
        (Use `mvnw.cmd` on Windows if you don't have Maven installed globally).
        This will compile the code, run the tests, package the application, and copy the necessary dependencies to the `target/lib` directory.

4.  **Run the installation script:**
    *   After the build is successful, run the `install.bat` script from the project's root directory:
        ```bash
        install.bat
        ```
    *   The script will perform the following actions:
        *   Prompt you to enter the path to your MySQL 8.4 installation directory (e.g., `C:\Program Files\MySQL\MySQL Server 8.4`).
        *   Prompt you to enter the MySQL root password.
        *   Create the `da50` database if it doesn't exist.
        *   Create the `hibernate_user` user with the password 'root' if it doesn't exist, and grant it all privileges on the `da50` database.
        *   Ask if you want to create an admin account. If you answer "y" (yes), it will run the `CreateAdminUser` Java program to guide you through the process.

5.  **Run the application:**
    *   After the database is initialized and the admin account is created (optional), you can run the application using one of the following methods:
        *   **Using Maven Wrapper (Recommended):** From the project's root directory, run:

            ```bash
            mvnw javafx:run
            ```

            This command will use the Maven Wrapper (`mvnw` or `mvnw.cmd`) included in the project. It will automatically download the correct Maven version if necessary and run the JavaFX application.
            **You do not need to install Maven separately if you use the Wrapper.**

        *   **Using a local Maven installation:** If you have Maven installed and configured in your `PATH`, you can use the standard `mvn` command:

            ```bash
            mvn javafx:run
            ```

## Database Information

*   **Database Name:** `da50`
*   **Username:** `hibernate_user`
*   **Password:** `root`

**Note:** The `hibernate_user` is created with limited privileges for security reasons. The admin account (if created) can be used for administrative tasks.

## Manual Database Setup (Alternative)

If you prefer to set up the database manually, you can use the following SQL commands after installing MySQL and opening the `MySQL 8.4 Command Line Client`:

```sql
CREATE DATABASE IF NOT EXISTS da50;
CREATE USER IF NOT EXISTS 'hibernate_user'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON da50.* TO 'hibernate_user'@'localhost';
FLUSH PRIVILEGES;
```
## Usage

1. After launching the application for the first time, you will be directed to the login page.
2. In order to create lessons or quizzes, you must login with an admin account. If you just want to access the lessons pages, or the quizzes pages, you can use any type of account.

### Creating a Lesson

3. To create a lesson, go ahead and click on the `Create Course` button and enter the lesson's name, the tag for the difficulty, and other elements that you can choose from the select input.
4. You can rearrange the elements by using the arrow button besides the elements in the preview window.
5. When you are done, go ahead and click the `Save` button to save or simply the `Back to Main Menu` button to go back to the main menu.
6. Once you are at the main menu, you will have the choice to modify the lesson if needed.

### Creating a Quiz

7. To create a quiz, go ahead and click the `Quizz` button from the main menu, and click `Create Quiz` .
8. Then you will have the option to create a quiz with multiple correct answers or only single correct answers only (irrevisible for the moment).
9. Then, enter the Quiz's title, and add questions by clicking the `Add Question` button and add options by clicking the `Add Option` button.
10. If the option is the correct response for the question, go ahead and tick the checkbox (only 1 checkbox can be ticked for single correct answer quizzes).
11. When you are done, go ahead and click the `Save Quiz` button to save the quiz or `Home` to go to the main menu or `Back` to go to the list of quizzes view.
12. An update/modify button will be present if you want to update/modify the quizzes if needed. 
