package com.kenzan;

public class Employee {
    private String ID;
    private String FirstName;
    private String MiddleInitial;
    private String LastName;
    private String DateOfBirth;
    private String DateOfEmployment;
    private Boolean Status;

    public Employee(String ID, String FirstName, String LastName) {
        this.ID = ID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Status = true;
    }

    public Employee(String ID, String FirstName, String MiddleInitial, String LastName) {
        this(ID, FirstName, LastName);
        this.MiddleInitial = MiddleInitial;
    }

    public String getID() { return this.ID; }
    public String getFirstName() { return this.FirstName; }
    public String getLastName() { return this.LastName; }
}
