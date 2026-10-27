
# MiniBank Banking Application

## Description
MiniBank is a Java console-based banking application developed as an OOPJ mini project.

## Features
- Create savings, current, and fixed-deposit accounts
- Deposit, withdraw, and transfer money
- View account statements and list accounts
- Validate mobile, email, PAN, and IFSC details
- Handle banking errors using custom exceptions
- Use interfaces, abstract classes, annotations, and reflection
- Process tasks with a thread pool
- Demonstrate producer-consumer and deadlock prevention
- Save and load accounts from a file
- Maintain transaction logs and generate reports
- Store and search accounts using collections and a generic repository

## Technologies
- Java
- Object-Oriented Programming
- Collections Framework
- Multithreading
- File Handling and Serialization
- Reflection and Annotations

## Build and Run
Open PowerShell in the MiniBank project folder.

Compile the Java source files:

    javac -d out (Get-ChildItem -Recurse -Filter *.java).FullName

Create the runnable JAR:

    jar cfm minibank.jar MANIFEST.MF -C out .

Run the application:

    java -jar minibank.jar

## Data Files
The application creates an `accounts.dat` file for saved account data, a `transactions.log` file for transaction history, and a `report.txt` file for reports.

## Author
OOPJ Mini Project — MiniBank
