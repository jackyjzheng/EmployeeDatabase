package com.kenzan;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class Employee {
    private int id;
    private String firstName;
    private String middleInitial = "";
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" )
    private Date dateOfEmployment;
    private Boolean status = true;

    public Employee() {}
    public Employee(int id, String firstName, String lastName,
                    Date dateOfBirth, Date dateOfEmployment) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfEmployment = dateOfEmployment;
    }
    public Employee(int id, String firstName, String middleInitial, String lastName,
                    Date dateOfBirth, Date dateOfEmployment) {
        this(id, firstName, lastName, dateOfBirth, dateOfEmployment);
        this.middleInitial = middleInitial;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) { return true; }
        else if (!(o instanceof Employee)) { return false; }
        else {
            Employee employee = (Employee) o;
            return firstName.equals(employee.getFirstName()) &&
                    middleInitial.equals(employee.getMiddleInitial()) &&
                    lastName.equals(employee.getLastName()) &&
                    dateOfBirth.equals(employee.getDateOfBirth()) &&
                    dateOfEmployment.equals(employee.getDateOfEmployment());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getID() { return this.id; }
    public String getFirstName() { return this.firstName; }
    public String getMiddleInitial() { return this.middleInitial; }
    public String getLastName() { return this.lastName; }
    public Date getDateOfBirth() { return this.dateOfBirth; }
    public Date getDateOfEmployment() { return this.dateOfEmployment; }
    public Boolean getStatus() { return this.status; }

    public void setID(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMiddleInitial(String middleInitial) { this.middleInitial = middleInitial; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setDateOfEmployment(Date dateOfEmployment) { this.dateOfEmployment = dateOfEmployment; }
    public void setStatus(Boolean status) { this.status = status; }
}
