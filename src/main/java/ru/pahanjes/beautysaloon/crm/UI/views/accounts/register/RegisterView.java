package ru.pahanjes.beautysaloon.crm.UI.views.accounts.register;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;
import ru.pahanjes.beautysaloon.crm.security.AuthService;

public class RegisterView extends VerticalLayout {

    Tabs tabs = new Tabs();
    TextField username = new TextField("Имя пользователя");
    PasswordField password = new PasswordField("Пароль");
    PasswordField confirmPassword = new PasswordField("Подтвердите пароль");
    ComboBox<Employee> employees = new ComboBox<>("Сотрудник");
    ComboBox<Role> role = new ComboBox<>("Права");
    Button register = new Button("Зарегистрировать");
    HorizontalLayout firstLayout = new HorizontalLayout();
    HorizontalLayout secondLayout = new HorizontalLayout();
    private AuthService authService;
    private EmployeeService employeeService;

    public RegisterView(AuthService authService, EmployeeService employeeService){
        this.authService = authService;
        this.employeeService = employeeService;

        setAlignItems(Alignment.CENTER);

        /*employees.setItems(employeeService.findAllWithoutAccount());
        employees.setItemLabelGenerator(Employee::getFullNameWithPosition);
        employees.setRequired(true);
        employees.setRequiredIndicatorVisible(true);
        employees.setErrorMessage("Выберите сотрудника");
        employees.setPlaceholder("Помогалова Альбина: преподаватель");*/

        configureComboBox(employees);

        username.setRequired(true);
        username.setRequiredIndicatorVisible(true);
        username.setErrorMessage("Введите имя");
        username.setPlaceholder("username");

        password.setRequired(true);
        password.setRequiredIndicatorVisible(true);
        password.setErrorMessage("Введите пароль");
        password.setPlaceholder("p@ssw0rd");

        confirmPassword.setRequired(true);
        confirmPassword.setRequiredIndicatorVisible(true);
        confirmPassword.setErrorMessage("Подтвердите пароль");
        confirmPassword.setPlaceholder("p@ssw0rd");

        role.setItems(Role.values());

        firstLayout.add(employees, username);
        secondLayout.add(password, confirmPassword);

        register.addClickListener(click ->
                register(username.getValue(), password.getValue(), confirmPassword.getValue())
        );
        add(firstLayout, secondLayout, role, register);
    }

    public void configureComboBox(ComboBox<Employee> employees) {
        employees.setItems(employeeService.findAllWithoutAccount());
        employees.setItemLabelGenerator(Employee::getFullNameWithPosition);
        employees.setRequired(true);
        employees.setRequiredIndicatorVisible(true);
        employees.setErrorMessage("Выберите сотрудника");
        employees.setPlaceholder("Помогалова Альбина: преподаватель");
    }

    public void updateComboBox(ComboBox<Employee> employees) {
        employees.setItems(employeeService.findAllWithoutAccount());
        employees.setItemLabelGenerator(Employee::getFullNameWithPosition);
    }

    private void register(String username, String password, String confirmPassword) {
        if(username.trim().isEmpty()) {
            Notification.show("Введите имя");
        } else if (password.isEmpty()) {
            Notification.show("Введите пароль");
        } else if (confirmPassword.isEmpty()) {
            Notification.show("Подтвердите пароль");
        } else if (!password.equals(confirmPassword)) {
            Notification.show("Пароли не совпадают");
        } else {
            if(employees.getValue() == null) {
                Notification.show("Выберите сотрудника");
            } else if (role.getValue() == null){
                Notification.show("Выберита права");
            } else {
                int res = authService.register(username, password, employees.getValue(), role.getValue());
                if(res == -1) {
                    Notification.show("Пользователь с таким именем уже существует");
                } else if (res == 0) {
                    updateComboBox(employees);
                    Notification.show("Пользователь зарегистрирован");
                }
            }
        }
    }

}
