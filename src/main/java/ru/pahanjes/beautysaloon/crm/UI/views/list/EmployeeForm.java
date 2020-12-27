package ru.pahanjes.beautysaloon.crm.UI.views.list;

import com.vaadin.componentfactory.multiselect.MultiComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.entity.Service;
import ru.pahanjes.beautysaloon.crm.backend.repository.EmployeeRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.ServiceRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.UserRepository;
import ru.pahanjes.beautysaloon.crm.backend.service.UserService;
import ru.pahanjes.beautysaloon.crm.security.AuthService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class EmployeeForm extends FormLayout {

    private final List<Employee> employees;
    private final ServiceRepository serviceRepository;
    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;
    private AuthService authService;
    private UserService userService;
    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");
    TextField position = new TextField("Должность");
    BigDecimalField salary = new BigDecimalField("Зарплата");
    EmailField email = new EmailField("Электронная почта");
    TextField phoneNumber = new TextField("Номер телефона");
    ComboBox<Employee.Status> status = new ComboBox<>("Статус");
    MultiComboBox<Service> services = new MultiComboBox<>("Оказываемые услуги");
    TextField login = new TextField("Логин");
    PasswordField password = new PasswordField("Пароль");
    ComboBox<Role> role = new ComboBox<Role>("Права");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отменить");

    Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);

    public EmployeeForm(List<Employee> employees, ServiceRepository serviceRepository, UserRepository userRepository, EmployeeRepository employeeRepository, AuthService authService){
        this.employees = employees;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.authService = authService;
        this.userService = userService;
        addClassName("employee-form");

        salary.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        salary.setPrefixComponent(new Icon(VaadinIcon.MONEY));
        salary.setValue(new BigDecimal(0));
        services.setItems(serviceRepository.findAll());
        services.setItemLabelGenerator(Service::getService);
        status.setItems(Employee.Status.values());
        add(
                firstName,
                lastName,
                position,
                salary,
                phoneNumber,
                email,
                status,
                services,
                login,
                password,
                role,
                createButtonsLayout()
        );
    }
   /*
    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");
    EmailField email = new EmailField("Электронная почта");
    TextField phoneNumber = new TextField("Номер телефона");
    ComboBox<Customer.Status> status = new ComboBox<>("Статус");
    DateTimePicker timetable = new DateTimePicker();
    MultiComboBox<Service> services = new MultiComboBox<>("Услуги");
    ComboBox<Employee> employee = new ComboBox<>("Сотрудник");
    */

    private void configureBinder() {
        binder.forField(firstName)
                .withValidator(
                        fname -> !fname.isEmpty(), "Введите имя")
                .bind(Employee::getFirstName, Employee::setFirstName);
        binder.forField(lastName)
                .withValidator(
                        lname -> !lname.isEmpty(), "Введите фамилию")
                .bind(Employee::getLastName, Employee::setLastName);
        binder.forField(position)
                .withValidator(
                        pos -> !pos.isEmpty(), "Введите должность")
                .bind(Employee::getPosition, Employee::setPosition);
        binder.bind(salary, Employee::getSalary, Employee::setSalary);
        binder.forField(email)
                .withValidator(
                new EmailValidator("Введите корректный адрес электронной почты"))
                .bind(Employee::getEmail, Employee::setEmail);
        binder.forField(phoneNumber)
                .withValidator(
                        phone -> !phone.isEmpty(), "Введите номер телефона")
                .bind(Employee::getPhoneNumber, Employee::setPhoneNumber);
        binder.forField(status)
                .withValidator(
                        Objects::nonNull, "Выберите статус")
                .bind(Employee::getStatus, Employee::setStatus);
        binder.forField(services)
                .withValidator(
                        services1 -> services1.size() > 0, "Выберите услуги")
                .bind(Employee::getServices, Employee::setServices);
        if(binder.getBean() != null) {
            if (binder.getBean().getUser() == null) {
                firstName.setPlaceholder("Введите имя");
                firstName.setRequired(true);
                firstName.setRequiredIndicatorVisible(true);

                lastName.setPlaceholder("Введите фамилию");
                lastName.setRequired(true);
                lastName.setRequiredIndicatorVisible(true);

                position.setPlaceholder("Введите олжность");
                position.setRequired(true);
                position.setRequiredIndicatorVisible(true);

                salary.setRequiredIndicatorVisible(true);

                phoneNumber.setPlaceholder("Введите номер телефона");
                phoneNumber.setRequired(true);
                phoneNumber.setRequiredIndicatorVisible(true);

                email.setPlaceholder("Введите email");
                email.setRequiredIndicatorVisible(true);

                status.setPlaceholder("Выберие статус");
                status.setRequired(true);
                status.setRequiredIndicatorVisible(true);

                services.setPlaceholder("Выберите услуги");
                services.setRequired(true);
                services.setRequiredIndicatorVisible(true);

                login.setValue("");
                login.setPlaceholder("Введите логин: ");
                login.setRequiredIndicatorVisible(true);
                login.setRequired(true);
                login.setReadOnly(false);
                password.setValue("");
                password.setPlaceholder("Введите пароль");
                password.setRequiredIndicatorVisible(true);
                password.setRequired(true);
                password.setEnabled(true);
                role.setPlaceholder("Выберите права: ");
                role.setItems(Role.values());
                role.setRequired(true);
                role.setRequiredIndicatorVisible(true);
                role.setReadOnly(false);
            } else {
                firstName.setRequired(false);
                firstName.setRequiredIndicatorVisible(false);

                lastName.setRequired(false);
                lastName.setRequiredIndicatorVisible(false);

                position.setRequired(false);
                position.setRequiredIndicatorVisible(false);

                salary.setRequiredIndicatorVisible(false);

                phoneNumber.setRequired(false);
                phoneNumber.setRequiredIndicatorVisible(false);

                email.setRequiredIndicatorVisible(false);

                status.setRequired(false);
                status.setRequiredIndicatorVisible(false);

                services.setRequired(false);
                services.setRequiredIndicatorVisible(false);

                login.setValue(binder.getBean().getUser().getUsername());
                login.setRequired(false);
                login.setRequiredIndicatorVisible(false);
                login.setReadOnly(true);
                password.setEnabled(false);
                password.setRequired(false);
                password.setRequiredIndicatorVisible(false);
                role.setItems(Role.values());
                role.setValue(binder.getBean().getUser().getRole().iterator().next());
                role.setRequired(false);
                role.setRequiredIndicatorVisible(false);
                role.setReadOnly(true);
            }
        }
    }

    public void setEmployee(Employee employee){
        binder.setBean(employee);
        configureBinder();
    }

    private Component createButtonsLayout(){
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(click -> {
            if(login.getValue().isEmpty()) {
                Notification.show("Введите логин");
            } else if (password.getValue().isEmpty()) {
                Notification.show("Введите пароль");
            } else if (status.getValue() == null) {
                Notification.show("Выберите статус");
            } else {
                validateAndSave();
            }
        }/*validateAndSave()*/);
        delete.addClickListener(delete -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(close -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {

        if(binder.isValid()){
            if(binder.getBean().getUser() == null) {
                if(authService.register(login.getValue(), password.getValue(), binder.getBean(), role.getValue()) == -1) {
                    Notification.show("Пользователь с таким именем уже существует");
                    return;
                }/* else if(login.getValue().isEmpty()) {
                    Notification.show("Введите логин");
                    return;
                } else if (password.getValue().isEmpty()) {
                    Notification.show("Введите пароль");
                    return;
                } else if (status.getValue() == null) {
                    Notification.show("Выберите статус");
                    return;
                }*/
            }
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public static abstract class EmployeeFormEvent extends ComponentEvent<EmployeeForm> {
        private Employee employee;

        protected EmployeeFormEvent(EmployeeForm source, Employee employee){
            super(source, false);
            this.employee = employee;
        }

        public Employee getEmployee(){
            return employee;
        }
    }

    public static class SaveEvent extends EmployeeFormEvent {
        public SaveEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }
    }

    public static class DeleteEvent extends EmployeeFormEvent {
        public DeleteEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }
    }

    public static class CloseEvent extends EmployeeFormEvent {
        public CloseEvent(EmployeeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener (Class<T> eventType,
                                                                   ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
