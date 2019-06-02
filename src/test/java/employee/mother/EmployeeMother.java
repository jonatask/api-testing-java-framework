package employee.mother;

import employee.entities.EmployeeEntity;

import java.util.Random;

public class EmployeeMother {

    private static EmployeeEntity validEmployee;

    public static void createValidEmployee() {
        EmployeeEntity employeeEntity = new EmployeeEntity();

        String id = "" + new Random().nextInt();

        employeeEntity.setAge("30");
        employeeEntity.setId(id);
        employeeEntity.setName("John"+id);
        employeeEntity.setSalary("1000.01");

        validEmployee = employeeEntity;
    }

    public static EmployeeEntity getValidEmployee() {
        return validEmployee;
    }
}
