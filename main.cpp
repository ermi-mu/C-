#include <iostream>
#include <unordered_map>
#include <queue>
#include <cstdlib>
using namespace std;
// ANSI escape codes for colors
#define RESET "\033[0m"  // Reset the color formatting
#define RED "\033[31m"  // Red color
#define GREEN "\033[32m"  // Green color
#define YELLOW "\033[33m"  // Yellow color
#define BLUE "\033[34m"  // Blue color
#define MAGENTA "\033[35m"  // Magenta color
#define CYAN "\033[36m"  // Cyan color
#define WHITE "\033[37m"  // White color

struct Book {
    string id;
    string title;
    string author;
    int yearPublished;
    int edition;
    bool isLent;
    Book* next;
};

struct Borrower {
    string studentID;
    string studentName;
    string borrowedBook;
    bool isReturned;
};

struct Category {
    string name;
    Book* bookHead;
    Category* next;
};
// Global Variables
Category* categoryHead = nullptr;
queue<Borrower> borrowersQueue;
queue<Borrower> borrowingHistory;
// Users
struct User {
    string name;
    string password;
    string email;
    string role;  // "admin", "librarian", "student"
};

// User database
unordered_map<string, User> users;

// Function Prototypes
void adminMenu(const string& userID);
void librarianMenu(const string& userID);
void studentMenu(const string& userID);

void addCategory(const string& name, int numBooks) {
    Category* newCategory = new Category{name, nullptr, nullptr};
    if (!categoryHead) {
        categoryHead = newCategory;
    } else {
        Category* temp = categoryHead;
        while (temp->next) {
            temp = temp->next;
        }
        temp->next = newCategory;
    }

    // Add books to the new category
    cout << "Enter details for " << numBooks << " books for category \"" << name << "\":\n";
    for (int i = 0; i < numBooks; ++i) {
        Book* newBook = new Book;
        cout << "Enter Book " << i + 1 << " ID: ";
        cin >> newBook->id;
        cin.ignore();
        cout << "Enter Book " << i + 1 << " Title: ";
        getline(cin, newBook->title);
        cout << "Enter Book " << i + 1 << " Author: ";
        getline(cin, newBook->author);
        cout << "Enter Book " << i + 1 << " Year Published: ";
        cin >> newBook->yearPublished;
        cout << "Enter Book " << i + 1 << " Edition: ";
        cin >> newBook->edition;
        newBook->isLent = false;
        newBook->next = newCategory->bookHead;
        newCategory->bookHead = newBook;
    }
}

// Function to find a category by name
Category* findCategory(const string& name) {
    Category* temp = categoryHead;
    while (temp) {
        if (temp->name == name) {
            return temp;
        }
        temp = temp->next;
    }
    return nullptr;
}
void displayBooksByCategory() {
    if (!categoryHead) {
        cout << "No categories available.\n";
        return;
    }
    cout << "\nBooks by Category:\n";
    Category* temp = categoryHead;
    while (temp) {
        cout << "\nCategory: " << temp->name << "\n";
        if (!temp->bookHead) {
            cout << "  No books in this category.\n";
        } else {
            Book* current = temp->bookHead;
            while (current) {
                cout << "  ID: " << current->id
                     << ", Title: \"" << current->title
                     << "\", Author: " << current->author
                     << ", Year: " << current->yearPublished
                     << ", Edition: " << current->edition
                     << (current->isLent ? " (Not Available)" : " (Available)") << endl;
                current = current->next;
            }
        }
        temp = temp->next;
    }
}
void displayBorrowingStudents() {
    if (borrowersQueue.empty()) {
        cout << "No students currently borrowing books.\n";
        return;
    }
    cout << "\nList of Borrowing Students:\n";
    queue<Borrower> tempQueue = borrowersQueue;  // Make a copy of the queue to traverse
    while (!tempQueue.empty()) {
        Borrower borrower = tempQueue.front();
        tempQueue.pop();
        cout << "Student Name: " << borrower.studentName
             << ", Borrowed Book: " << borrower.borrowedBook
             << ", Status: " << (borrower.isReturned ? "Returned" : "Not Returned") << "\n";
    }
}
void displayBorrowingHistory() {
    if (borrowingHistory.empty()) {
        cout << "No borrowing history available.\n";
        return;
    }

    cout << "\nBorrowing History:\n";
    queue<Borrower> tempHistory = borrowingHistory;  // Make a copy of the history queue to traverse
    while (!tempHistory.empty()) {
        Borrower borrower = tempHistory.front();
        tempHistory.pop();
        cout << "Student Name: " << borrower.studentName
             << ", Borrowed Book: " << borrower.borrowedBook
             << ", Status: " << (borrower.isReturned ? "Returned" : "Not Returned") << "\n";
    }
}

void lendBook(const string& userID) {
    cout << "Choose a category to lend the book from:\n";
    Category* temp = categoryHead;
    int categoryNumber = 1;

    while (temp) {
        cout << categoryNumber++ << ". " << temp->name << "\n";
        temp = temp->next;
    }

    int categoryChoice;
    cout << "Enter the number of the category: ";
    cin >> categoryChoice;

    temp = categoryHead;
    for (int i = 1; i < categoryChoice && temp; i++) {
        temp = temp->next;
    }

    if (!temp) {
        cout << "Invalid choice. Returning to menu.\n";
        return;
    }

    string chosenCategory = temp->name;
    Book* current = temp->bookHead;

    if (!current) {
        cout << "No books available in \"" << chosenCategory << "\".\n";
        return;
    }

    cout << "Books available in \"" << chosenCategory << "\":\n";
    int bookNumber = 1;
    vector<Book*> books;

    while (current) {
        if (!current->isLent) {
            cout << bookNumber++ << ". " << current->title << "\n";
            books.push_back(current);
        }
        current = current->next;
    }

    if (books.empty()) {
        cout << "No books available for lending in \"" << chosenCategory << "\".\n";
        return;
    }

    int bookChoice;
    cout << "Enter the number of the book to lend: ";
    cin >> bookChoice;

    if (bookChoice < 1 || bookChoice > books.size()) {
        cout << "Invalid choice. Returning to menu.\n";
        return;
    }

    Book* selectedBook = books[bookChoice - 1];
    Borrower borrower;
    borrower.studentID = userID;
    borrower.studentName = users[userID].name;
    borrower.borrowedBook = selectedBook->title;
    borrower.isReturned = false;

    borrowersQueue.push(borrower);
    borrowingHistory.push(borrower);
    selectedBook->isLent = true;

    cout << "Book \"" << selectedBook->title << "\" has been lent to " << borrower.studentName << ".\n";
}

void returnBook() {
    if (borrowersQueue.empty()) {
        cout << "No books are currently lent out.\n";
        return;
    }

    string borrowerID;
    cout << "Enter Borrower ID to return the book: ";
    cin >> borrowerID;

    queue<Borrower> tempQueue;
    bool bookReturned = false;

    while (!borrowersQueue.empty()) {
        Borrower borrower = borrowersQueue.front();
        borrowersQueue.pop();

        if (borrower.studentID == borrowerID && !borrower.isReturned) {
            Category* temp = categoryHead;
            while (temp) {
                Book* current = temp->bookHead;
                while (current) {
                    if (current->title == borrower.borrowedBook && current->isLent) {
                        current->isLent = false;
                        borrower.isReturned = true;
                        cout << "Book \"" << borrower.borrowedBook << "\" returned by " << borrower.studentName << ".\n";
                        bookReturned = true;
                        break;
                    }
                    current = current->next;
                }
                if (bookReturned) break;
                temp = temp->next;
            }
        }

        tempQueue.push(borrower);
    }

    borrowersQueue = tempQueue;

    if (!bookReturned) {
        cout << "Borrower ID " << borrowerID << " not found or has not borrowed any books.\n";
    }
}


void pauseScreen() {
    cout << "Press Enter to continue...";
    cin.ignore();
    cin.get();
}
void setupAdminAccount() {
   // clearScreen();
    cout <<GREEN<< "========================================================\n"<<RESET;
    cout <<GREEN<< "No admin account found. Please set up the admin account.\n"<<RESET;
    cout <<GREEN<< "========================================================\n"<<RESET;
    User admin;
    cout <<YELLOW<< "Enter Admin Name: "<<RESET;
    getline(cin, admin.name);
    cout <<YELLOW<< "Create Admin Password: "<<RESET;
    getline(cin, admin.password);
    cout <<YELLOW<< "Enter Admin Email: "<<RESET;
    getline(cin, admin.email);
    admin.role = "admin";
    users["admin"] = admin; // Store admin account
    cout <<CYAN<< "Admin account created successfully!\n"<<RESET;
    pauseScreen();
}

void displayBorrowingStudents();
void displayBorrowingHistory();
void resetPassword();

// Utility Functions
void clearScreen() {
#ifdef _WIN32
    system("cls");
#else
    system("clear");
#endif
}

void resetPassword() {
    string userID;
    cout <<YELLOW<< "Enter User ID to Reset Password: "<<RESET;
    cin >> userID;

    if (users.find(userID) != users.end()) {
        cout <<YELLOW<< "Enter New Password: "<<RESET;
        cin.ignore();
        getline(cin, users[userID].password);
        cout <<GREEN<< "Password reset successfully for " << userID << "!\n"<<RESET;
    } else {
        cout <<RED<< "User ID not found.\n"<<RESET;
    }
}

bool login(string& loggedInUserID) {
    clearScreen();
    cout <<YELLOW<< "======== Login ========\n"<<RESET;
    cout <<YELLOW<< "1. Admin Login\n";
    cout <<YELLOW<< "2. Regular User Login (Student/Librarian)\n"<<RESET;
    cout <<MAGENTA<< "Enter your choice: "<<RESET;
    int choice;
    cin >> choice;
    cin.ignore();

    if (choice == 1) {
        // Admin Login
        string adminID = "admin";

        // Check if admin account exists
        if (users.find(adminID) == users.end()) {
            setupAdminAccount(); // If admin doesn't exist, set up the admin account
            return false;        // Prompt login again after setup
        }

        string adminName, adminPassword;
        cout << "Enter Admin Name: "<<RESET;
        getline(cin, adminName);
        cout << "Enter Admin Password: "<<RESET;
        getline(cin, adminPassword);

        if (users[adminID].name == adminName && users[adminID].password == adminPassword) {
            cout << "Admin login successful! Welcome, " << adminName << "!\n";
            pauseScreen();
            adminMenu(adminID);
            return true;
        } else {
            cout <<RED<< "Invalid admin name or password. Try again.\n"<<RESET;
            pauseScreen();
            return false;
        }
    } else if (choice == 2) {
        // Regular User Login
        cout << "Enter User ID: "<<RESET;
        cin >> loggedInUserID;
        cin.ignore();

        if (users.find(loggedInUserID) != users.end()) {
            // If the user exists, proceed with login
            string password;
            cout << "Enter Password: "<<RESET;
            getline(cin, password);

            if (users[loggedInUserID].password == password) {
                cout << "Login successful! Welcome, " << users[loggedInUserID].name << "!\n";
                pauseScreen();

                // Redirect to the appropriate menu based on user role
                if (users[loggedInUserID].role == "student") {
                    studentMenu(loggedInUserID);
                } else if (users[loggedInUserID].role == "librarian") {
                    librarianMenu(loggedInUserID);
                }
                return true;
            } else {
                cout <<RED<< "Incorrect password. Try again.\n"<<RESET;
                pauseScreen();
                return false;
            }
        } else {
            // If the user doesn't exist, allow creating a new account
            cout <<YELLOW<< "User ID not found. Please create a new account.\n"<<RESET;

            string userID, userName, userPassword, userRole;
            cout <<BLUE<< "Enter User ID (starting with UGR/ugr or LIB/lib ): "<<RESET;
            cin >> userID;
            cin.ignore();

            int accountType;
            cout <<MAGENTA<< "Choose Account Type:\n"<<RESET;
            cout <<MAGENTA<< "1. Student\n"<<RESET;
            cout <<MAGENTA<< "2. Librarian\n"<<RESET;
            cout <<MAGENTA<< "Enter your choice: "<<RESET;
            cin >> accountType;
            cin.ignore();

            if (accountType == 1) {
                userRole = "student";
                if (userID.substr(0, 3) != "UGR" && userID.substr(0, 3) != "ugr") {
                    cout <<RED<< "Invalid ID. Student ID must start with UGR/ugr.\n"<<RESET;
                    pauseScreen();
                    return false;
                }
            } else if (accountType == 2) {
                userRole = "librarian";
                if (userID.substr(0, 3) != "LIB" && userID.substr(0, 3) != "lib") {
                    cout <<RED<< "Invalid ID. Librarian ID must start with LIB/lib.\n"<<RESET;
                    pauseScreen();
                    return false;
                }
            } else {
                cout <<RED<< "Invalid account type. Try again.\n"<<RESET;
                pauseScreen();
                return false;
            }

            cout << "Enter Name: "<<RESET;
            getline(cin, userName);
            cout << "Create Password: "<<RESET;
            getline(cin, userPassword);

            // Create a new user and add it to the users map
            User newUser;
            newUser.name = userName;
            newUser.password = userPassword;
            newUser.role = userRole;
            users[userID] = newUser;  // Add user to the map

            cout <<CYAN<< "Account created successfully! Please log in again.\n"<<RESET;
            pauseScreen();
            return false; // Prompt user to log in again after account creation
        }
    } else {
        cout <<RED<< "Invalid choice. Please try again.\n"<<RESET;
        pauseScreen();
        return false;
    }
}

void adminMenu(const string& userID) {
    while (true) {
        clearScreen();
        cout <<YELLOW<< "========== Admin Menu ==========\n"<<RESET;
        cout <<YELLOW<< "1. Create User Account\n"<<RESET;
        cout <<YELLOW<< "2. Reset User Password\n"<<RESET;
        cout <<YELLOW<< "3. Delete User Account\n"<<RESET;
        cout <<YELLOW<< "4. Show All Users\n"<<RESET;  // New option to show users
        cout <<YELLOW<< "5. Logout\n"<<RESET;
        cout <<YELLOW<< "================================\n"<<RESET;
        cout <<CYAN<< "Enter your choice: "<<RESET;
        int choice;
        cin >> choice;

        switch (choice) {
            case 1: {
                string userID, userName, userPassword, userRole;
                cout <<CYAN<< "Enter User ID (starting with UGR/ugr or LIB/lib ):\n "<<RESET;
                cin >> userID;
                cin.ignore();  // To consume the newline character after the user input

                // Validate User ID prefix
                if ((userID.substr(0, 3) == "UGR" || userID.substr(0, 3) == "ugr") && userID.length() >= 3) {
                    userRole = "student";
                } else if ((userID.substr(0, 3) == "LIB" || userID.substr(0, 3) == "lib") && userID.length() >= 3) {
                    userRole = "librarian";
                } else {
                    cout <<RED<< "Invalid ID. Student ID must start with UGR/ugr and Librarian ID must start with LIB/lib.\n"<<RESET;
                    pauseScreen();
                    return;  // Exit to menu if invalid ID
                }

                cout << "Enter Name: ";
                getline(cin, userName);
                cout << "Create Password: ";
                getline(cin, userPassword);

                // Create a new user and add it to the global users map
                User newUser;
                newUser.name = userName;
                newUser.password = userPassword;
                newUser.role = userRole;
                users[userID] = newUser;

                cout <<CYAN<< "Account created successfully!\n"<<RESET;
                pauseScreen();
                break;
            }

            case 2:
                resetPassword();
                break;

            case 3: {
                string deleteID;
                cout <<YELLOW<< "Enter User ID to delete: "<<RESET;
                cin >> deleteID;
                if (users.erase(deleteID)) {
                    cout <<YELLOW<< "User account deleted successfully!\n"<<RESET;
                } else {
                    cout <<YELLOW<< "User ID not found.\n"<<RESET;
                }
                pauseScreen();
                break;
            }

            case 4: {  // New feature to show all users
                cout <<CYAN<< "========== List of All Users ==========\n"<<RESET;
                for (const auto& [userID, user] : users) {
                    cout << "User ID: " << userID << "\n"
                         << "Name: " << user.name << "\n"
                         << "Email: " << user.email << "\n"
                         << "Role: " << user.role << "\n"
                         <<CYAN<< "--------------------------------------\n"<<RESET;
                }
                pauseScreen();
                break;
            }

            case 5:
                cout <<CYAN<< "Logging out...\n"<<RESET;
                return;  // Exit the admin menu

            default:
                cout <<RED<< "Invalid choice. Try again.\n"<<RESET;
                pauseScreen();
                break;
        }
    }
}

void librarianMenu(const string& userID) {
    while (true) {
        clearScreen();
        cout <<GREEN<< "========== Librarian Menu ==========\n"<<RESET;
        cout <<GREEN<< "1. Add Category\n"<<RESET;
        cout <<GREEN<< "2. Display Books by Category\n"<<RESET;
        cout <<GREEN<< "3. Lend Book\n"<<RESET;
        cout <<GREEN<< "4. Return Book\n"<<RESET;
        cout <<GREEN<< "5. Display Borrowing Students\n"<<RESET;  // Added option
        cout <<GREEN<< "6. Display Borrowing History\n"<<RESET;   // Added option
        cout <<GREEN<< "7. Logout\n"<<RESET;
        cout <<GREEN<< "====================================\n"<<RESET;
        cout <<CYAN<< "Enter your choice: "<<RESET;
        int choice;
        cin >> choice;

        if (choice == 1) {
            string categoryName;
            cout <<CYAN<< "Enter Category Name: "<<RESET;
            cin.ignore();
            getline(cin, categoryName);
            int numBooks;
            cout <<CYAN<< "Enter Number of Books to Add: "<<RESET;
            cin >> numBooks;
            addCategory(categoryName, numBooks);
        } else if (choice == 2) {
            displayBooksByCategory();
        } else if (choice == 3) {
            lendBook(userID);  // Passing userID for lending functionality
        } else if (choice == 4) {
            returnBook();
        } else if (choice == 5) {
            displayBorrowingStudents();  // Display current borrowing students
        } else if (choice == 6) {
            displayBorrowingHistory();   // Display full borrowing history
        } else if (choice == 7) {
            //break;
            return; // Logout
        } else {
            cout <<RED<< "Invalid choice. Try again.\n"<<RESET;
        }
        pauseScreen();
    }
}
void studentMenu(const string& userID) {
    while (true) {
        clearScreen();
        cout <<GREEN<< "========== Student Menu ==========\n"<<RESET;
        cout <<GREEN<< "1. Display Books by Category\n"<<RESET;
        cout <<GREEN<< "2. Lend Book\n"<<RESET;
        cout <<GREEN<< "3. Return Book\n"<<RESET;
        cout <<GREEN<< "4. Logout\n"<<RESET;
        cout <<GREEN<< "==================================\n"<<RESET;
        cout <<CYAN<< "Enter your choice: "<<RESET;
        int choice;
        cin >> choice;

        if (choice == 1) {
            displayBooksByCategory();
        } else if (choice == 2) {
            lendBook(userID);
        } else if (choice == 3) {
            returnBook();
        } else if (choice == 4) {
           // break;
           return;
        } else {
            cout <<RED<< "Invalid choice. Try again.\n"<<RESET;
        }
        pauseScreen();
    }
}
welcomePage()
{
    cout<<CYAN<<"\t\t\t=============================================\n\n"<<RESET;
    cout<<CYAN<<"\t\t\t  WELCOME TO ASTU LIBERARY MANAGMENT SYSTEM  \n\n"<<RESET;
    cout<<CYAN<<"\t\t\t=============================================\n"<<RESET;
    cout<<CYAN<<"\t\t\t=============================================\n\n"<<RESET;
    cout<<CYAN<<"\t\t\t............. GROUP 6 MEMBERS ...............\n\n"<<RESET;
    cout<<CYAN<<"\t\t\t1. ERMIAS MULUGETA            ID:UGR/34317/16\n\n"<<RESET;
    cout<<CYAN<<"\t\t\t2. ERMIAS NIGUSSIE            ID:UGR/34318/16\n\n"<<RESET;
    cout<<CYAN<<"\t\t\t3. SAMUEL TENKIR              ID:UGR/35343/16\n\n"<<RESET;
    cout<<CYAN<<"\t\t\t4. SAMUEL TOLESA              ID:UGR/35346/16\n\n"<<RESET;
    cout<<CYAN<<"\t\t\t5. KANU SHEWANGIZA            ID:UGR/34719/16\n\n"<<RESET;
    pauseScreen();
    clearScreen();
}

int main() {
    welcomePage();
    setupAdminAccount();

    string loggedInUserID;
    while (true) {
        clearScreen();
        cout <<GREEN<< "======== Library Management System ========\n"<<RESET;
        cout <<GREEN<< "1. Login\n"<<RESET;
        cout <<GREEN<< "2. Exit\n"<<RESET;
        cout <<GREEN<< "===========================================\n"<<RESET;
        cout <<CYAN<< "Enter your choice: "<<RESET;
        int choice;
        cin >> choice;

        if (choice == 1) {
            if (login(loggedInUserID)) {
                User& user = users[loggedInUserID];
                if (user.role == "admin") {
                    adminMenu(loggedInUserID);
                } else if (user.role == "librarian") {
                    librarianMenu(loggedInUserID);
                } else if (user.role == "student") {
                    studentMenu(loggedInUserID);
                }
                loggedInUserID.clear(); // Clear the logged-in user ID after logout
            }
        } else if (choice == 2) {
            cout <<YELLOW<< "Exiting the system. Goodbye!\n"<<RESET;
            break;
        } else {
            cout <<RED<< "Invalid choice. Try again.\n"<<RESET;
            pauseScreen();
        }
    }
    return 0;
}

