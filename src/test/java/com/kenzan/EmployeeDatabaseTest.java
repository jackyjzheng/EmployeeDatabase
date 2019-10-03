package com.kenzan;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EmployeeDatabaseTest {
    private EmployeeDatabase testDatabase;
    private Employee testEmployee;

    @Before
    public void setUp() throws Exception {
        this.testDatabase = new EmployeeDatabase(1);
        this.testEmployee = new Employee("1", "Jacky", "Zheng");
    }

    @Test
    public void testAddDeleteEmployee() {
        String testID = this.testEmployee.getID();
        String testFirstName = this.testEmployee.getFirstName();
        String testLastName = this.testEmployee.getLastName();

        Boolean success = this.testDatabase.addEmployee(testID, testFirstName, testLastName);
        assertEquals(true, success);
        assertEquals(1, testDatabase.numberOfEmployees());
        assertNotEquals(null, testDatabase.getEmployee(testID));

        Boolean deleteSuccess = this.testDatabase.deleteEmployee(testID);
        assertEquals(true, deleteSuccess);
        assertEquals(0, testDatabase.numberOfEmployees());
    }
}
