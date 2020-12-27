package ru.pahanjes.beautysaloon.crm.UI.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.pahanjes.beautysaloon.crm.backend.entity.Customer;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.Service;
import ru.pahanjes.beautysaloon.crm.backend.repository.ServiceRepository;
import ru.pahanjes.beautysaloon.crm.backend.service.CustomerService;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;
import ru.pahanjes.beautysaloon.crm.backend.service.UserService;

import java.util.Set;

@Route(value = "lk/customer")
@PageTitle("Клиенты | BS CRM")
@CssImport("./styles/views/customer-view.css")
public class CustomerListView extends VerticalLayout {

    private Grid<Customer> customerGrid = new Grid<>(Customer.class);
    private Grid<Employee> employeeGrid = new Grid<>(Employee.class);
    private TextField filter = new TextField();
    private CustomerService customerService;
    private EmployeeService employeeService;
    private ServiceRepository serviceRepository;
    private UserService userService;
    private CustomerForm customerForm;

    public CustomerListView(CustomerService customerService, EmployeeService employeeService, ServiceRepository serviceRepository, UserService userService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.serviceRepository = serviceRepository;
        this.userService = userService;
        addClassName("customer-list-view");
        setSizeFull();
        configureGrids();
        /*getToolBar();*/

        customerForm = new CustomerForm(employeeService.findAll(), serviceRepository, employeeService);
        customerForm.addListener(CustomerForm.SaveEvent.class, this::saveCustomer);
        customerForm.addListener(CustomerForm.DeleteEvent.class, this::deleteCustomer);
        customerForm.addListener(CustomerForm.CloseEvent.class, closeEvent -> closeCustomerEditor());

        Div content = new Div(customerGrid, customerForm);
        content.addClassName("customer-content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateCustomerList();
        closeCustomerEditor();
    }

    private HorizontalLayout getToolBar() {
        filter.setPlaceholder("Введите фильтр...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(event -> updateCustomerList());

        Button addCustomerButton = new Button("Добавить клиента", click -> addCustomer());
        HorizontalLayout toolbar = new HorizontalLayout(filter, addCustomerButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addCustomer() {
        customerGrid.asSingleSelect().clear();
        editCustomer(new Customer());
    }

    private void configureGrids() {
        customerGrid.addClassName("customer-grid");
        customerGrid.setSizeFull();
        customerGrid.removeAllColumns();
        customerGrid.addColumn(customer -> customer.getLastName() == null ? "-" : customer.getLastName()).setHeader("Фамилия").setSortable(true);
        customerGrid.addColumn(customer -> customer.getFirstName() == null ? "-" : customer.getFirstName()).setHeader("Имя").setSortable(true);
        customerGrid.addColumn(customer -> customer.getPhoneNumber() == null ? "-" : customer.getPhoneNumber()).setHeader("Номер телефона").setSortable(true);
        customerGrid.addColumn(customer -> customer.getEmail() == null ? "-" : customer.getEmail()).setHeader("email").setSortable(true);
        customerGrid.addColumn(customer -> customer.getServices().size() == 0 ? "-" : setToString(customer.getServices())).setHeader("Услуги").setSortable(false);
        customerGrid.addColumn(customer -> customer.getStatus() == null ? "-" : customer.getStatus().getValue()).setHeader("Статус").setSortable(true);
        customerGrid.getColumns().forEach(customerColumn -> customerColumn.setAutoWidth(true));
        customerGrid.asSingleSelect().addValueChangeListener(event -> editCustomer(event.getValue()));
        customerGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
    }

    private static String setToString(Set<Service> serviceSet) {
        StringBuilder result = new StringBuilder();
        for(Service service : serviceSet) {
            result.append(service.getService()).append(", ");
        }
        result.delete(result.length()-2, result.length()-1);
        return result.toString();
    }

    private void saveCustomer(CustomerForm.SaveEvent saveEvent){
        Employee employee = employeeService.findById(saveEvent.getCustomer().getEmployee().getId());
        employee.addCustomer(saveEvent.getCustomer());
        employeeService.save(employee);
        saveEvent.getCustomer().setEmployee(employeeService.findById(employee.getId()));
        customerService.save(saveEvent.getCustomer());

        updateCustomerList();
        closeCustomerEditor();
    }

    private void deleteCustomer(CustomerForm.DeleteEvent deleteEvent){
        customerService.delete(deleteEvent.getCustomer());
        updateCustomerList();
        closeCustomerEditor();
    }

    private void editCustomer(Customer customer){
        if(customer == null){
            closeCustomerEditor();
        } else {
            customerForm.setCustomer(customer);
            customerForm.setVisible(true);
            addClassName("customer-editing");
        }
    }

    private void closeCustomerEditor() {
        customerForm.setCustomer(null);
        customerForm.setVisible(false);
        removeClassName("customer-editing");
    }

    private void updateCustomerList(){
        customerGrid.setItems(customerService.findAll(filter.getValue()));
    }

}
