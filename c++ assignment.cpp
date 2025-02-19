#include<stdio.h>
#include<iostream>
#include<fstream>
#include<string.h>
#include<time.h>
#include<string>
using namespace std;


int main(){
  printf("\n");
  cout<<("\t\t\t\t    ****************************************\n");
  cout<<("\t\t\t\t    *    STUDENT RECORD MANAGEMENT SYSTEM  *\n");
  cout<<("\t\t\t\t    ****************************************\n");
  cout<<("\n\n")<<("\t\t\t\t\t")<<("NAME      \t")<<(" ID NUMBER")<<("\n\n")
      <<("\t\t\t\t")<<"1, ERMIAS MULUGETA \t"<<"UGR/34317/16"<<("\n\n")<<("\t\t\t\t")
      <<"2, ISSACHORD GIRMA\t"<<"UGR/34669/16"<<("\n\n")<<("\t\t\t\t")<<"3, JADON TABOR\t\t"
      <<"UGR/34670/16"<<("\n\n")<<("\t\t\t\t")<<"4, PHILLIMON SOLOMON\t"<<"UGR/35214/16"
      <<("\n\n")<<("\t\t\t\t")<<"5, YARED GIDEY\t\t"<<"UGR/35587/16"<<("\n\n")<<("\t\t\t\t\t\t ")
      <<"SUMITED TO :  "<<"MR BUSHIRA ALI YASIN"<<("\n\n")<<("\t\t\t\t\t\t ")<<"SUMITRD DATE :  "
      <<"JUL 3/2024"<<("\n\n\n")<<("\t\t\t\t ")<<("  Final Project  Assignment ")<<("\n\n")
      <<("\t\t")<<("\t   Press Enter to continue......")<<("\n\n");
     getchar();
	char data[15];
	int n = 0, option = 0, count_n = 0;
	string empty = "00";
	string proctor = "";
	//Name of the file in which DB is stored.
	ifstream f("er.txt");
	string line;
	//The following for loop counts the total number of lines in the file.
	for (int i = 0; std::getline(f, line); ++i){
		count_n++;}
	while(option != 5){
//This prints out all the available options in the DB
	cout<<"\nAvailable operations: \n1. Add New Students\n2."
		<< "Student Login\n3. Faculty Login\n4. Admin View\n"
		<< "5. Exit\nEnter option: ";
	cin >> option;
	if(option == 1){
	cout << "Enter the number of students: ";
	cin >> n;
	count_n = count_n + n;
	for (int i = 0;i < n;i++){
		ofstream outfile;
			outfile.open("er.txt",ios::app);
		//The entire data of a single student is stored line-by-line.
		cout << "Enter your registration number: ";
		cin >> data;
		outfile << data << "\t";
		cout << "Enter your Name : ";
		cin >> data;
		int len = strlen(data);
		while (len < 15){
			data[len] = ' ';
			len = len + 1;
		}
		outfile << data << "\t";
		//Inserting empty data initially into the file
		outfile << empty << "\t";
		outfile << empty << "\t";
		cout << "Enter your ID: ";
		cin >> proctor;
		outfile << proctor << endl;
	}}
	else if (option == 2){
		char regno[9];
		cout << "Enter your registration number: ";
		cin >> regno;
		ifstream infile;
		int check = 0;
		infile.open("er.txt",ios::in);
		while (infile >> data){
		if (strcmp(data,regno) == 0){
			cout << "\nRegistration Number: " << data << endl;
			infile >> data;
			cout << "Name: " << data << endl;
			infile >> data;
				cout << "CSE1102 mark: " << data << endl;
			infile>>data;
			cout<<"CSE1104 mark: "<<data<<endl;
			infile.close();
			check = 1;}
		}
		if (check == 0){
			cout<<"No such registration number found!"<<endl;}
	}
	//This loop is used to view and add marks to the database of a student.
	else if (option == 3){
		char subcode[7];
		cout << "Enter your subject code: ";
		cin >> subcode;
		string code1 = "CSE1102", code2 = "CSE1104",mark = "";
		ifstream infile;
		int check = 0;
		cout << "\nAvailable operations: \n1. Add data about marks\n"
				<< "2. View data\nEnter option: ";
		cin >> option;
		if (option == 1){
			cout << "Warning! You would need to add mark"
					<< "details for all the students!" << endl;
			for(int i = 0;i < count_n;i++){
				fstream file("er.txt");
			if(strcmp(subcode,code1.c_str()) == 0){
						file.seekp(26+37*i,std::ios_base::beg);
					cout << "Enter the mark of student#" << (i+1) << " : ";
				cin >> mark;
				file.write(mark.c_str(),2);}
			if(strcmp(subcode,code2.c_str()) == 0){
				file.seekp(29+37*i,std::ios_base::beg);
				cout << "Enter the mark of student#" << (i+1) << " : ";
				cin >> mark;
				file.write(mark.c_str(),2);}
			}
		}
		//This loop is used to view marks of a student.
		else if(option == 2){
		infile.open("er.txt",ios::in);
		if (strcmp(subcode,code1.c_str()) == 0){
			cout << "Registration number - Marks\n" << endl;
			while(infile >> data){
			cout << data;
			infile >> data;
				infile >> data;
			cout << " - " << data << endl;
			infile >> data;
			infile >> data;
			check = 1;
			}
		}
		infile.close();
		infile.open("er.txt",ios::in);
		if(strcmp(subcode,code2.c_str()) == 0){
			cout << "Registration number - Marks\n" << endl;
				while(infile >> data){
			cout << data;
			infile >> data;
			infile >> data;
			infile >> data;
			cout << " - " << data << endl;
			infile >> data;
			check = 1;
			}
		}}
		infile.close();

		if (check == 0){
			cout << "No such subject code found!" << endl;
		}
	}
	//This loop acts as an admin view to see all the data in the file.
	else if(option == 4){
		char password[25];
		cout << "Enter the admin password: ";
		cin >> password;
		//This variable value can be changed according to your requirement
		//of the administrator password.
		string admin_pass = "adminguru";
		if (strcmp(password,admin_pass.c_str()) == 0){
			cout << "Reg No.	 \t Name \tCSE101\t CSE102" << endl;
			ifstream infile;
			infile.open("er.txt",ios::in);
			char data[20];
			while(infile >> data){
				cout << data << "\t";
				infile >> data;
				cout << data << "\t";
				infile >> data;
				cout << data << "\t";
				infile >> data;
				cout << data << "\t";
				infile >> data;
				cout << data << endl;
			}
		}
	}
}}




