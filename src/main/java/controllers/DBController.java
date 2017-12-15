package controllers;

import models.Patient;
import models.Symptom;

import java.sql.*;
import java.util.ArrayList;

public class DBController {

    private Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:Db.db";
            connection = DriverManager.getConnection(dbURL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public int getLastID(String table) {
        int id = 0;
        try {
            Connection connection = connect();
            if (connection != null) {
                String query = "select max("+table+"ID) from "+table;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                id = resultSet.getInt(1);

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public ArrayList<Patient> selectPatientRecords() {
        ArrayList<Patient> patientRecords = new ArrayList<Patient>();
        try {
            Connection connection = connect();
            if (connection != null) {
                String query = "Select * from Patient";
                System.out.println(query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                System.out.println("Select successful!");

                while (resultSet.next()) {
                    int patientID = resultSet.getInt(1);
                    String nationalID = resultSet.getString(2);
                    String firstName = resultSet.getString(3);
                    String lastName = resultSet.getString(4);
                    String sex = resultSet.getString(5);
                    String dateOfBirth = resultSet.getString(6);
                    String age = resultSet.getString(7);
                    String bloodGroup = resultSet.getString(8);
                    String nationality = resultSet.getString(9);
                    String religion = resultSet.getString(10);
                    String telNumber = resultSet.getString(11);
                    String[] intolerances = resultSet.getString(12).split("\n");
                    patientRecords.add(new Patient(patientID, nationalID, firstName, lastName, sex, dateOfBirth,
                            age, bloodGroup, nationality, religion, telNumber, intolerances));
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientRecords;
    }

    public Patient selectPatientRecord(int key) {
        Patient patient = null;
        try {
            Connection connection = connect();
            if (connection != null) {
                String query = "Select * from Patient where PatientID = '"+key+"';";
                System.out.println(query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                System.out.println("Select successful!");

                int patientID = resultSet.getInt(1);
                String nationalID = resultSet.getString(2);
                String firstName = resultSet.getString(3);
                String lastName = resultSet.getString(4);
                String sex = resultSet.getString(5);
                String dateOfBirth = resultSet.getString(6);
                String age = resultSet.getString(7);
                String bloodGroup = resultSet.getString(8);
                String nationality = resultSet.getString(9);
                String religion = resultSet.getString(10);
                String telNumber = resultSet.getString(11);
                String[] intolerances = resultSet.getString(12).split("\n");
                patient = new Patient(patientID, nationalID, firstName, lastName, sex, dateOfBirth,
                        age, bloodGroup, nationality, religion, telNumber, intolerances);
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    public void insertPatientRecord(Patient patient) {
        try {
            Connection connection = connect();
            if (connection != null) {
                String intolerances = patient.getIntolerances()[0];
                for (int i = 1; i < patient.getIntolerances().length; i++) {
                    intolerances += "\\n"+patient.getIntolerances()[i];
                }
                String query = "insert into Patient (PatientID, NationalID, FirstName, LastName, Sex, DateOfBirth," +
                        "Age, BloodGroup, Nationality, Religion, TelNumber, Intolerances, Status) values (" +
                        "'"+patient.getPatientID()+"',"+
                        "'"+patient.getNationalID()+"',"+
                        "'"+patient.getFirstName()+"',"+
                        "'"+patient.getLastName()+"',"+
                        "'"+patient.getSex().toString()+"',"+
                        "'"+patient.getDateOfBirth()+"',"+
                        "'"+patient.getAge()+"',"+
                        "'"+patient.getBloodGroup().toString()+"',"+
                        "'"+patient.getNationality()+"',"+
                        "'"+patient.getReligion()+"',"+
                        "'"+patient.getTelNumber()+"',"+
                        "'"+intolerances+"',"+
                        "'STANDBY')";
                System.out.println(query);
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                System.out.println("Insert successful!");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Patient> searchPatient(String name) {
        ArrayList<Patient> patientRecords = new ArrayList<Patient>();
        try {
            Connection connection = connect();
            if (connection != null) {
                String query = "";
                String[] search = name.split(" ");
                if (search.length == 1) {
                    query = "Select * from Patient where FirstName like '%"+search[0]+"%' or LastName like '%"+search[0]+"%'";
                } else {
                    query = "Select * from Patient where FirstName like '%"+search[0]+"%' or LastName like '%"+search[1]+"%'";
                }

                System.out.println(query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                System.out.println("Select successful!");

                while (resultSet.next()) {
                    int patientID = resultSet.getInt(1);
                    String nationalID = resultSet.getString(2);
                    String firstName = resultSet.getString(3);
                    String lastName = resultSet.getString(4);
                    String sex = resultSet.getString(5);
                    String dateOfBirth = resultSet.getString(6);
                    String age = resultSet.getString(7);
                    String bloodGroup = resultSet.getString(8);
                    String nationality = resultSet.getString(9);
                    String religion = resultSet.getString(10);
                    String telNumber = resultSet.getString(11);
                    String[] intolerances = resultSet.getString(12).split("\n");
                    patientRecords.add(new Patient(patientID, nationalID, firstName, lastName, sex, dateOfBirth,
                            age, bloodGroup, nationality, religion, telNumber, intolerances));
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientRecords;
    }

    public void updateStatus(String status, int key) {
        try {
            Connection connection = connect();
            if (connection != null) {
                String query = "update Patient set Status = '"+status+"' where PatientID = '"+key+"'";
                System.out.println(query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                System.out.println("Update successful!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
