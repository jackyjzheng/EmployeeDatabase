package com.kenzan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class EmployeeDatabase {
    HashMap<String, Employee> employeeDatabase;

    EmployeeDatabase(int initialEmployeeNumber) {
        this.employeeDatabase = new HashMap<String, Employee>(initialEmployeeNumber);
    }

    Employee getEmployee(String ID) {
        if (employeeDatabase.containsKey(ID)) {
            return employeeDatabase.get(ID);
        } else {
            return null;
        }
    }

    Boolean addEmployee(String ID, String FirstName, String LastName) {
        Employee newEmployee = new Employee(ID, FirstName, LastName);
        if (!employeeDatabase.containsKey(ID)) {
            employeeDatabase.put(ID, newEmployee);
            return true;
        } else {
            return false;
        }
    }

    Boolean deleteEmployee(String ID) {
        if (employeeDatabase.containsKey(ID)) {
            employeeDatabase.remove(ID);
            return true;
        } else {
            return false;
        }
    }

    ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> allEmployees = new ArrayList<Employee>();
        Set<String> keys = employeeDatabase.keySet();
        for (String key: keys) {
            allEmployees.add(employeeDatabase.get(key));
        }
        return allEmployees;
    }

    int numberOfEmployees() { return this.employeeDatabase.size(); }
}
