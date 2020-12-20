package ru.pahanjes.beautysaloon.crm.UI.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.pahanjes.beautysaloon.crm.UI.CabinetLayout;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.service.CustomerService;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;

import java.math.BigDecimal;

@Route(value = "lk/employee", layout = CabinetLayout.class)
@PageTitle("Сотрудники | BS CRM")
@CssImport("./styles/views/employee-view.css")
public class EmployeeListView  extends VerticalLayout {

    private Grid<Employee> employeeGrid = new Grid<>(Employee.class);
    TextField filter = new TextField();
    private CustomerService customerService;
    private EmployeeService employeeService;
    private EmployeeForm employeeForm;

    public EmployeeListView(CustomerService customerService, EmployeeService employeeService){
        this.customerService = customerService;
        this.employeeService = employeeService;
        addClassName("employee-list-view");
        setSizeFull();
        configureGrid();
        getToolBar();

        employeeForm = new EmployeeForm(employeeService.finalAll());
        employeeForm.addListener(EmployeeForm.SaveEvent.class, this::saveEmployee);
        employeeForm.addListener(EmployeeForm.DeleteEvent.class, this::deleteEmployee);
        employeeForm.addListener(EmployeeForm.CloseEvent.class, closeEvent -> closeEmployeeEditor());

        Div content = new Div(employeeGrid, employeeForm);
        content.addClassName("employee-content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateEmployeeList();
        closeEmployeeEditor();
    }

    private HorizontalLayout getToolBar() {
        filter.setPlaceholder("Введите фильтр...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(event -> updateEmployeeList()); //!!!!!!!!!!!!!!!!!!!!!!!!!

        Button addEmployeeButton = new Button("Добавить сотрудника", click -> addEmployee());
        HorizontalLayout toolbar = new HorizontalLayout(filter, addEmployeeButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addEmployee() {
        employeeGrid.asSingleSelect().clear();
        editEmployee(new Employee());
    }

    private void configureGrid() {
        employeeGrid.addClassName("employee-grid");
        employeeGrid.setSizeFull();
        employeeGrid.removeAllColumns();
        employeeGrid.addColumn(employee -> employee.getLastName() == null ? "-" : employee.getLastName()).setHeader("Фамилия").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getFirstName() == null ? "-" : employee.getFirstName()).setHeader("Имя").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getPosition() == null ? "-" : employee.getPosition()).setHeader("Должность").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getSalary() == new BigDecimal(0) ? "-" : employee.getSalary()).setHeader("Зарплата").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getPhoneNumber() == null ? "-" : employee.getPhoneNumber()).setHeader("Номер телефона").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getEmail() == null ? "-" : employee.getEmail()).setHeader("email").setSortable(true);
        employeeGrid.addColumn(employee -> employee.getStatus() == null ? "-" : employee.getStatus()).setHeader("Статус").setSortable(true);
        employeeGrid.getColumns().forEach(customerColumn -> customerColumn.setAutoWidth(true));
        employeeGrid.asSingleSelect().addValueChangeListener(event -> editEmployee(event.getValue()));
    }

    private void saveEmployee(EmployeeForm.SaveEvent saveEvent){
        employeeService.save(saveEvent.getEmployee());
        updateEmployeeList();
        closeEmployeeEditor();
    }

    private void deleteEmployee(EmployeeForm.DeleteEvent deleteEvent){
        employeeService.delete(deleteEvent.getEmployee());
        updateEmployeeList();
        closeEmployeeEditor();
    }

    private void editEmployee(Employee employee){
        if(employee == null){
            closeEmployeeEditor();
        } else {
            employeeForm.setEmployee(employee);
            employeeForm.setVisible(true);
            addClassName("employee-editing");
        }
    }

    private void closeEmployeeEditor() {
        employeeForm.setEmployee(null);
        employeeForm.setVisible(false);
        removeClassName("employee-editing");
    }

    private void updateEmployeeList(){
        employeeGrid.setItems(employeeService.findAll(filter.getValue()));
    }
}
