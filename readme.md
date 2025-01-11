# Task Tracker CLI

Task Tracker CLI is a command-line application designed to help you manage and track your tasks efficiently. With this tool, you can easily add, update, delete, and categorize tasks based on their status. This project allows you to practice essential programming skills, including working with the filesystem, handling user inputs, and building a simple yet functional CLI application.

## Features

- **Add Tasks**: Quickly add tasks with a description.
- **Update Tasks**: Modify task descriptions as needed.
- **Delete Tasks**: Remove completed or irrelevant tasks.
- **Status Management**: Mark tasks as **To Do**, **In Progress**, or **Done**.
- **Task Listing**: List tasks by status (all, done, to do, in progress).

## Requirements

The Task Tracker CLI application should run directly from the command line. It accepts user actions and inputs as arguments and stores all tasks in a local JSON file. Below are the core requirements and constraints:

- **Add**: Add new tasks with a description.
- **Update**: Modify existing task descriptions.
- **Delete**: Remove tasks from the list.
- **Status Updates**: Mark tasks as "in progress" or "done."
- **View Tasks**:
    - List all tasks
    - List tasks by status (done, to do, in progress)

### Constraints

- Use any programming language of your choice.
- Accept user inputs via positional arguments in the command line.
- Store tasks in a JSON file located in the current directory.
- Automatically create the JSON file if it does not already exist.
- Use only the native file system module of your language to interact with the JSON file.
- Avoid using external libraries or frameworks.
- Handle errors and edge cases gracefully.

## Example Commands

Below are the commands available for Task Tracker CLI:

```bash
# Adding a new task
task-cli add "Buy groceries"
# Output: Task added successfully (ID: 1)

# Updating a task
task-cli update 1 "Buy groceries and cook dinner"

# Deleting a task
task-cli delete 1

# Marking a task as in progress or done
task-cli mark-in-progress 1
task-cli mark-done 1

# Listing all tasks
task-cli list

# Listing tasks by specific status
task-cli list done
task-cli list todo
task-cli list in-progress
```
## Task Properties
Each task consists of the following properties:

- **id**: A unique identifier for the task.
- **description**: A brief description of the task.
- **status**: Current status of the task - either todo, in-progress, or done.
- **createdAt**: Date and time of task creation.
- **updatedAt**: Date and time of the last update.

The JSON file will contain these properties for each task, and they will be updated accordingly whenever a task is added or modified.


### Usage 
Go on terminal in a project's root directory then run the command below:
```declarative
javac src/service/TaskService.java src/entity/Status.java src/entity/Task.java src/Main.java 
```
After that, type: 
```
cd src/
```
You can do other things:
```
java Main <command> <params> ...
```

Project URI: https://roadmap.sh/projects/task-tracker
