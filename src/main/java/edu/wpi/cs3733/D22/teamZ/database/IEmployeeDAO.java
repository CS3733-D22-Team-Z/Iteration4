package edu.wpi.cs3733.D22.teamZ.database;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;

import java.util.List;

public interface IEmployeeDAO {
    /**
     * Gets all of the employees in the database
     *
     * @return List of employees
     */
    List<Employee> getAllEmployees();

    /**
     * Gets ONE Employee from the database based on the provided EmployeeID
     *
     * @param employeeID
     * @return Employee object with provided employeeID
     */
    Employee getEmployeeByID(String employeeID);

    /**
     * Adds a new Employee to database. Will automatically check if already in database
     *
     * @param emp
     * @return True if successful, false if not
     */
    boolean addEmployee(Employee emp);

    /**
     * Updates a Employee in the database. Will automatically check if exists in database
     *
     * @param emp
     * @return True if successful, false if not
     */
    boolean updateEmployee(Employee emp);

    /**
     * Deletes a Employee from database. Will automatically check if exists in database
     *
     * @param emp
     * @return True if successful, false if not
     */
    boolean deleteEmployee(Employee emp);

    /**
     * Exports the Employee table into a csv file to the working directory
     *
     * @return True if successful, false if not
     */
    boolean exportToLocationCSV();
}
