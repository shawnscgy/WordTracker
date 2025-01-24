# WordTracker: Binary Search Tree Implementation

## Overview
The WordTracker application is a Java-based tool designed to read text files and track all unique words, their occurrences, and locations using a Binary Search Tree (BST). The BST is implemented according to the `BSTreeADT` and includes additional features for report generation and serialization. The program processes text files, stores the results, and allows users to generate customizable reports.

---

## Features
1. **Binary Search Tree (BST):**
   - Implements a generic BST for storing and managing words.
   - Tracks occurrences, file names, and line numbers for each word.
   - Case-insensitive word tracking while preserving original case.

2. **Serialization:**
   - Stores the BST to a binary file (`repository.ser`) for persistence.
   - Restores data from the binary file on subsequent executions.
   - Handles serialization errors gracefully with appropriate error messages.

3. **Report Generation:**
   - `-pf`: List all words alphabetically with associated files.
   - `-pl`: List all words alphabetically with files and line numbers.
   - `-po`: List all words alphabetically with files, line numbers, and frequency of occurrences.
   - Optional file output with automatic `.txt` extension handling.

4. **Command-Line Interface:**
   - Process files via command-line arguments.
   - Supports both absolute and relative file paths.
   - Automatically checks 'res/' directory if file not found in current directory.
   - Optionally redirect output to a file.

---

## Requirements
- **Software:**
  - Eclipse IDE
  - Java 8
  - JUnit 4

- **Files Required:**
  - `BSTreeADT.java` (interface)
  - `Iterator.java` (interface)
  - `WordTracker.java`
  - `WordMetadata.java`
  - JUnit test cases

---

## Installation
1. **Clone or Download the Project:**
   - Ensure the project directory contains all necessary files, including the implementation files and dependencies.

2. **Import into Eclipse:**
   - Open Eclipse IDE.
   - Import the project (`File > Import > Existing Projects into Workspace`).

3. **Compile the Code:**
   - Ensure all files compile without errors.

4. **Run JUnit Tests:**
   - Verify the implementation passes all test cases provided.

---

## Usage
### Command-Line Arguments
```bash
java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f<output.txt>]
```
- `<input.txt>`: Path to the text file to process (can be absolute or relative path).
- `-pf`: Generate a report listing words and associated files.
- `-pl`: Generate a report listing words, files, and line numbers.
- `-po`: Generate a report listing words, files, line numbers, and frequencies.
- `-f<output.txt>`: (Optional) Redirect output to a specified file (`.txt` extension added automatically if missing).

### Example Usage
1. **Initial Run:**
   ```bash
   java -jar WordTracker.jar test1.txt -pf
   ```
   - Displays word and file information alphabetically.
   - Creates repository.ser for persistence.

2. **Subsequent Run with Line Numbers:**
   ```bash
   java -jar WordTracker.jar test2.txt -pl
   ```
   - Updates BST with new file data.
   - Displays word, file, and line number information.

3. **File Output with Occurrences:**
   ```bash
   java -jar WordTracker.jar test3.txt -po -fresults.txt
   ```
   - Processes new file.
   - Stores complete occurrence information in results.txt.
   - Displays output on console and confirms file export.

---

## Class Structure
### WordMetadata.java
A utility class that encapsulates word tracking details:
- Case-insensitive word storage with original case preservation
- File and line number tracking using HashMap
- Comparable interface implementation for BST ordering
- Multiple report format support (files, lines, occurrences)
- Serializable for persistent storage

### WordTracker.java
Main application class handling:
- File processing with support for various paths
- BST management and persistence
- Report generation in multiple formats
- Command-line argument processing
- Error handling and logging

---

## Key Features
1. **Case Insensitivity:**
   - Words are stored and compared case-insensitively
   - Original case is preserved in output
   - Consistent handling across all operations

2. **File Handling:**
   - Support for both absolute and relative paths
   - Automatic 'res/' directory checking
   - Proper file closure and resource management

3. **Error Handling:**
   - Graceful handling of missing files
   - Proper exception management for I/O operations
   - Clear error messages for user feedback

4. **Report Generation:**
   - Multiple format options
   - Consistent output formatting
   - Optional file export with automatic extension handling

---

## Deliverables
1. **Executable JAR:**
   - `WordTracker.jar` to run the application.
2. **Documentation:**
   - Generated Javadoc in the `doc` directory (use `-private` option).
3. **Eclipse Project Directory:**
   - Exported project folder, including source code, tests, and resources.
4. **Marking Criteria Document:**
   - Completed group evaluation form.

---

## Support
For any issues or questions, please refer to the assignment guidelines or contact your instructor. Ensure `repository.ser` is in the working directory for data persistence.
