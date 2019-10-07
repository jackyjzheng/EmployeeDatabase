package com.kenzan;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Set;

public class EmployeeDatabase {
    private static HashMap<Integer, Employee> employeeDatabase;
    private static HashMap<String, String> userCredentials;
    private static EmployeeDatabase instance = null;
    private static int size;
    private static long expirationTimeMillis = 1000000L;

    EmployeeDatabase() {
        size = 0;
        userCredentials = new HashMap<String, String>();
        employeeDatabase = new HashMap<Integer, Employee>();
    }

    /*
        Not a perfect solution against replay attacks, won't stop replay attacks that happen faster than
        expirationTime. But good simple authentication to prevent man-in-the-middle stealing credentials.
    */
    boolean authenticate(String user, Long receivedTime, String received) throws NoSuchAlgorithmException {
        if (System.currentTimeMillis() - receivedTime > expirationTimeMillis || !userCredentials.containsKey(user)) {
            return false;
        }

        MessageDigest digest = MessageDigest.getInstance("MD5");
        String completeMessage = user + userCredentials.get(user) + receivedTime;
        digest.update(completeMessage.getBytes());
        String result = Base64.getEncoder().encodeToString(digest.digest());
        return result.equals(received);
    }

    public void setCredentials(String user, String password) {
        userCredentials.put(user, password);
    }

    public void removeCredentials(String user) {
        userCredentials.remove(user);
    }

    public static EmployeeDatabase getDatabase() {
        if (instance == null) {
            instance = new EmployeeDatabase();
        }
        return instance;
    }

    Employee getEmployee(int id) {
        if (employeeDatabase.containsKey(id) && employeeDatabase.get(id).getStatus()) {
            return employeeDatabase.get(id);
        } else {
            return null;
        }
    }

    Boolean addEmployee(Employee employee) {
        int idToQuery = employee.getID();
        if (!employeeDatabase.containsKey(idToQuery) || !employeeDatabase.get(idToQuery).getStatus()) {
            size += 1;
            employeeDatabase.put(idToQuery, employee);
            return true;
        } else {
            return false; // resource already exists
        }
    }

    Boolean putEmployee(int id, Employee employee) {
        employee.setID(id);
        boolean didntAlreadyExist = false;
        if (!employeeDatabase.containsKey(id) || !employeeDatabase.get(id).getStatus()) {
            size += 1;
            didntAlreadyExist = true;
        }
        employeeDatabase.put(id, employee);
        return didntAlreadyExist;
    }

    Boolean deleteEmployee(int id) {
        if (employeeDatabase.containsKey(id) && employeeDatabase.get(id).getStatus()) {
            size -= 1;
            employeeDatabase.get(id).setStatus(false);
            return true;
        } else {
            return false;
        }
    }

    void cleanupDatabase() {
        for (Employee employee: employeeDatabase.values()) {
            if (!employee.getStatus()) {
                employeeDatabase.remove(employee.getID());
            }
        }
    }

    ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> allEmployees = new ArrayList<Employee>();
        for (Employee employee: employeeDatabase.values()) {
            if (employee.getStatus()) {
                allEmployees.add(employee);
            }
        }
        return allEmployees;
    }

    int numberOfEmployees() { return size; }
}
