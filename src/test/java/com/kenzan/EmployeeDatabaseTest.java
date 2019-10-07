package com.kenzan;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import static org.junit.Assert.*;

public class EmployeeDatabaseTest {
    private EmployeeDatabase testDatabase;
    private Employee testEmployee;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void setUp() throws Exception {
        this.testDatabase = EmployeeDatabase.getDatabase();
        Date testDateOfBirth = format.parse("1997-02-10");
        Date testEmploymentDate = format.parse("2019-10-06");

        this.testEmployee = new Employee(1, "Jacky", "J", "Zheng",
                testDateOfBirth, testEmploymentDate);
        testDatabase.setCredentials("Jacky", "Zheng");
    }

    @After
    public void after() throws Exception {
        assert(testDatabase.numberOfEmployees() == 0);
    }

    @Test
    public void testAddEmployee() {
        assertEquals(0, testDatabase.numberOfEmployees());

        testDatabase.addEmployee(testEmployee);
        assertEquals(1, testDatabase.numberOfEmployees());
        assertNotEquals(null, testDatabase.getEmployee(1));

        boolean addEmployeeAgain = testDatabase.addEmployee(testEmployee);
        assertFalse(addEmployeeAgain);
        assertEquals(1, testDatabase.numberOfEmployees());

        testDatabase.deleteEmployee(1);
    }

    @Test
    public void testDeleteEmployee() {
        testDatabase.addEmployee(testEmployee);

        Boolean deleteSuccess = this.testDatabase.deleteEmployee(1);
        assertEquals(true, deleteSuccess);
        assertEquals(0, testDatabase.numberOfEmployees());

        assertNull(testDatabase.getEmployee(1));

        Boolean deleteAgain = testDatabase.deleteEmployee(1);
        assertFalse(deleteAgain);
    }

    @Test
    public void testPutEmployee() throws ParseException {
        testDatabase.addEmployee(testEmployee);

        Date newDateOfBirth = format.parse("1997-10-10");
        Date newDateOfEmployment = format.parse("2019-11-11");
        Employee newEmployee = new Employee(2, "Replace" , "Replace",
                "Replace", newDateOfBirth, newDateOfEmployment);
        testDatabase.putEmployee(1, newEmployee);

        Employee employeeInDatabase = testDatabase.getEmployee(1);
        assertEquals("Replace", employeeInDatabase.getFirstName());
        assertEquals("Replace", employeeInDatabase.getMiddleInitial());
        assertEquals("Replace", employeeInDatabase.getLastName());

        testDatabase.deleteEmployee(1);
    }

    @Test
    public void testAuthenticate() throws NoSuchAlgorithmException {
        String nonExistentUser = "BadUser";
        Long currentTime = System.currentTimeMillis();
        Long expiredTime = currentTime - 100000;

        MessageDigest digest = MessageDigest.getInstance("MD5");
        String correctMessage = "Jacky" + "Zheng" + currentTime;
        digest.update(correctMessage.getBytes());
        String correctDigest = Base64.getEncoder().encodeToString(digest.digest());

        String expiredMessage = "Jacky" + "Zheng" + expiredTime;
        digest.update(expiredMessage.getBytes());
        String expiredDigest = Base64.getEncoder().encodeToString(digest.digest());

        String incorrectPassword = "Jacky" + "Zhen" + currentTime;
        digest.update(incorrectPassword.getBytes());
        String badPasswordDigest = Base64.getEncoder().encodeToString(digest.digest());

        assertFalse(testDatabase.authenticate(nonExistentUser, currentTime, correctDigest));
        assertFalse(testDatabase.authenticate("Jacky", expiredTime, expiredDigest));
        assertFalse(testDatabase.authenticate("Jacky", currentTime, badPasswordDigest));
        assertTrue(testDatabase.authenticate("Jacky", currentTime, correctDigest));

        testDatabase.removeCredentials("Jacky");
        assertFalse(testDatabase.authenticate("Jacky", currentTime, correctDigest));
    }
}
