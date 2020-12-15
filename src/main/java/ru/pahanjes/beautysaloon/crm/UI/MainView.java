package ru.pahanjes.beautysaloon.crm.UI;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import ru.pahanjes.beautysaloon.crm.backend.entity.Customer;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.service.CustomerService;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;

@Route("")
public class MainView extends VerticalLayout {

    private Grid<Customer> customerGrid = new Grid<>(Customer.class);
    private Grid<Employee> employeeGrid = new Grid<>(Employee.class);
    private TextField filter = new TextField();
    private CustomerService customerService;
    private EmployeeService employeeService;

    public MainView(CustomerService customerService, EmployeeService employeeService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        addClassName("list-view");
        setSizeFull();

        HorizontalLayout horizontalLayoutButtons = new HorizontalLayout();
        Button showCustomersGridButton = new Button("Клиенты");
        Button showEmployeesGridButton = new Button("Сотрудники");
        showCustomersGridButton.addClickListener(click -> setGrid(customerGrid));
        showEmployeesGridButton.addClickListener(click -> setGrid(employeeGrid));
        horizontalLayoutButtons.add(showCustomersGridButton, showEmployeesGridButton);

        configureGrids();
        add(horizontalLayoutButtons, customerGrid);
    }

    private void configureGrids() {
        customerGrid.addClassName("customer-grid");
        customerGrid.setSizeFull();
        customerGrid.removeAllColumns();
        customerGrid.addColumn(customer -> customer.getLastName() == null ? "-" : customer.getLastName()).setHeader("Фамилия").setSortable(true);
        customerGrid.addColumn(customer -> customer.getFirstName() == null ? "-" : customer.getFirstName()).setHeader("Имя").setSortable(true);
        customerGrid.addColumn(customer -> customer.getPhoneNumber() == null ? "-" : customer.getPhoneNumber()).setHeader("Номер телефона").setSortable(true);
        customerGrid.addColumn(customer -> customer.getEmail() == null ? "-" : customer.getEmail()).setHeader("email").setSortable(true);
        customerGrid.addColumn(customer -> customer.getStatus() == null ? "-" : customer.getStatus()).setHeader("Статус").setSortable(true);
        customerGrid.getColumns().forEach(customerColumn -> customerColumn.setAutoWidth(true));

        employeeGrid.addClassName("employee-grid");
        employeeGrid.setSizeFull();
        employeeGrid.removeAllColumns();
        employeeGrid.addColumn(employee -> employee.getLastName() == null ? "-" : employee.getLastName()).setHeader("Фамилия").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getFirstName() == null ? "-" : employee.getFirstName()).setHeader("Имя").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getPosition() == null ? "-" : employee.getPosition()).setHeader("Должность").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getSalary() == 0.0F ? "-" : employee.getSalary()).setHeader("Зарплата").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getPhoneNumber() == null ? "-" : employee.getPhoneNumber()).setHeader("Номер телефона").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getEmail() == null ? "-" : employee.getEmail()).setHeader("email").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getStatus() == null ? "-" : employee.getStatus()).setHeader("Статус").setSortable(true);
        employeeGrid.getColumns().forEach(customerColumn -> customerColumn.setAutoWidth(true));
    }

    private <T> void setGrid(Grid<T> grid) {
        if(grid == customerGrid){
            remove(employeeGrid);
            add(customerGrid);
        } else {
            remove(customerGrid);
            add(employeeGrid);
        }
    }

}
