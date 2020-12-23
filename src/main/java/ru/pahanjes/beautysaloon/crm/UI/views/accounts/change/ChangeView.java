package ru.pahanjes.beautysaloon.crm.UI.views.accounts.change;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
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

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
            configureForm();
        }
    }

    private Grid<User> userGrid = new Grid<>(User.class);
    private TextField filter = new TextField();
    private UserService userService;
    private EmployeeService employeeService;
    private ChangeUserForm userForm;
    private ShowEmployeeForm employeeForm;
    private Div userInfo = new Div();

    public ChangeView (UserService userService, EmployeeService employeeService){
        this.userService = userService;
        this.employeeService = employeeService;
        addClassName("change-view");
        userInfo.addClassName("user-info");
        userForm = new ChangeUserForm();
        userForm.addListener(ChangeUserForm.SaveUserEvent.class, this::saveUser);
        userForm.addListener(ChangeUserForm.DeleteUserEvent.class, this::deleteUser);
        userForm.addListener(ChangeUserForm.CloseUserEvent.class, this::closeUser);
        userForm.addClassName("user-form");
        employeeForm = new ShowEmployeeForm();
        employeeForm.addClassName("employee-form");
        setSizeFull();
        configureGrid();
        add(getToolBar(), userGrid, userInfo);
        updateUserGrid();
    }

    private void configureGrid() {
        userGrid.addClassName("usersss-grid");
        userGrid.removeAllColumns();
        userGrid.addColumn(user -> user.getUsername() == null ? "-" : user.getUsername()).setHeader("Логин").setSortable(true);
        userGrid.addColumn(user -> user.getRole() == null ? "-" : user.getRole()).setHeader("Права").setSortable(true);
        userGrid.addColumn(user -> user.isActive() ? "Активен" : "Неактивен").setHeader("Состояние").setSortable(true);
        userGrid.getColumns().forEach(userColumn -> userColumn.setAutoWidth(true));
        userGrid.asSingleSelect().addValueChangeListener(select -> {
            if(select.getValue() == null) {
                if(select.getOldValue().getEmployees().size() == 0) {
                    configureDiv(select.getOldValue());
                } else {
                    configureDiv(select.getOldValue(), select.getOldValue().getEmployees().get(0));
                }
            } else{
                if(select.getValue().getEmployees().size() == 0) {
                    configureDiv(select.getValue());
                } else {
                    configureDiv(select.getValue(), select.getValue().getEmployees().get(0));
                }
            }
            userInfo.setSizeFull();
            userInfo.setVisible(true);
        });
    }

    private void configureDiv(User user, Employee employee) {
        userForm.setUser(user);
        employeeForm.setEmployee(employee);
        userInfo.removeAll();
        userInfo.add(userForm, employeeForm);
    }

    private void configureDiv(User user) {
        userForm.setUser(user);
        employeeForm.setEmployee(null);
        userInfo.removeAll();
        userInfo.add(userForm, employeeForm);
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
        closeUserInfo();
    }

    private void deleteUser(ChangeUserForm.DeleteUserEvent deleteUserEvent) {
        userService.delete(deleteUserEvent.getUser());
        updateUserGrid();
        closeUserInfo();
    }

    private void closeUser(ChangeUserForm.CloseUserEvent closeUserEvent) {
        closeUserInfo();
    }

    private void closeUserInfo() {
        userInfo.setVisible(false);
    }

}
