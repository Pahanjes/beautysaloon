package ru.pahanjes.beautysaloon.crm.UI.views.accounts.change;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
import ru.pahanjes.beautysaloon.crm.backend.service.CustomerService;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;
import ru.pahanjes.beautysaloon.crm.backend.service.UserService;

@CssImport("./styles/views/change-view.css")
public class ChangeView extends VerticalLayout {

    private class ShowEmployeeForm extends FormLayout {

        private TextField firstName = new TextField("Имя");
        private TextField lastName = new TextField("Фамилия");
        private TextField position = new TextField("должность");
        private EmailField email = new EmailField("Электронная почта");
        private TextField phoneNumber = new TextField("Номер телефона");
        private TextField status = new TextField("Статус");
        private Employee employee;

        public ShowEmployeeForm(){
            addClassName("employee-form");
            add(
                    firstName,
                    lastName,
                    position,
                    phoneNumber,
                    email,
                    status
            );
        }

        public ShowEmployeeForm(Employee employee){
            addClassName("employee-form");
            configureForm();
            add(
                    firstName,
                    lastName,
                    position,
                    phoneNumber,
                    email,
                    status
            );
        }

        private void configureForm() {

            firstName.setValue(employee.getFirstName());
            firstName.setReadOnly(true);
            lastName.setValue(employee.getLastName());
            lastName.setReadOnly(true);
            position.setValue(employee.getPosition());
            position.setReadOnly(true);
            email.setValue(employee.getEmail());
            email.setReadOnly(true);
            phoneNumber.setValue(employee.getPhoneNumber());
            phoneNumber.setReadOnly(true);
            status.setValue(employee.getStatus().getValue());
            status.setReadOnly(true);
        }

        private void setFieldsReadOnly() {
            firstName.setReadOnly(true);
            lastName.setReadOnly(true);
            position.setReadOnly(true);
            email.setReadOnly(true);
            phoneNumber.setReadOnly(true);
            status.setReadOnly(true);
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
            setFieldsReadOnly();
            if(employee != null) {
                configureForm();
            }
        }
    }

    private Grid<User> userGrid = new Grid<>(User.class);
    private TextField filter = new TextField();
    private UserService userService;
    private EmployeeService employeeService;
    private CustomerService customerService;
    private ChangeUserForm userForm;
    private ShowEmployeeForm employeeForm;
    private HorizontalLayout userInfoAndAboutLayout = new HorizontalLayout();

    public ChangeView (UserService userService, EmployeeService employeeService, CustomerService customerService){
        this.userService = userService;
        this.employeeService = employeeService;
        this.customerService = customerService;
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("change-view");
        userForm = new ChangeUserForm();
        userForm.addListener(ChangeUserForm.SaveUserEvent.class, this::saveUser);
        userForm.addListener(ChangeUserForm.DeleteUserEvent.class, this::deleteUser);
        userForm.addListener(ChangeUserForm.CloseUserEvent.class, closeUserEvent ->  closeEditor());
        userForm.addClassName("user-form");
        employeeForm = new ShowEmployeeForm();
        employeeForm.addClassName("employee-form");
        setSizeFull();
        configureGrid();
        configureLayout();
        add(getToolBar(), userGrid, userInfoAndAboutLayout);
        updateUserGrid();
    }

    private void configureGrid() {
        userGrid.addClassName("user-grid");
        userGrid.removeAllColumns();
        userGrid.addColumn(user -> user.getUsername() == null ? "-" : user.getUsername()).setHeader("Логин").setSortable(true);
        userGrid.addColumn(user -> user.getRole() == null ? "-" : user.getRole()).setHeader("Права").setSortable(true);
        userGrid.addColumn(user -> user.isActive() ? "Активен" : "Неактивен").setHeader("Состояние").setSortable(true);
        userGrid.getColumns().forEach(userColumn -> userColumn.setAutoWidth(true));
        userGrid.asSingleSelect().addValueChangeListener(select -> {
            editUser(select.getValue());
        });
        userGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
    }

    private void configureLayout() {
        userInfoAndAboutLayout.add(userForm, employeeForm);
        userInfoAndAboutLayout.setVisible(false);
    }

    private void editUser(User user) {
        if(user == null) {
            closeEditor();
        } else {
            userForm.setUser(user);
            employeeForm.setEmployee(user.getEmployee());
            userInfoAndAboutLayout.setVisible(true);
            addClassName("user-editor");
        }
    }

    private void closeEditor() {
        userForm.setUser(null);
        employeeForm.setEmployee(null);
        userInfoAndAboutLayout.setVisible(false);
        removeClassName("user-editor");
    }

    private HorizontalLayout getToolBar() {
        filter.setPlaceholder("Введите фильтр");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(change -> updateUserGrid());

        HorizontalLayout toolBar = new HorizontalLayout(filter);
        toolBar.addClassName("tool-bar");
        return toolBar;
    }

    private void updateUserGrid() {
        userGrid.setItems(userService.findAll(filter.getValue()));
    }

    private void saveUser(ChangeUserForm.SaveUserEvent saveUserEvent) {
        userService.save(saveUserEvent.getUser());
        updateUserGrid();
        closeEditor();
    }

    private void deleteUser(ChangeUserForm.DeleteUserEvent deleteUserEvent) {
        userService.delete(deleteUserEvent.getUser());
        updateUserGrid();
        closeEditor();
    }

    private void deleteUserFromEmployee(User user) {
        if(user.getEmployee() != null) {
            user.getEmployee().setUser(null);
            employeeService.save(user.getEmployee());
            employeeService.delete(user.getEmployee());
            user.setEmployee(null);
        }
    }

}
