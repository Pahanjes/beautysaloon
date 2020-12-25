package ru.pahanjes.beautysaloon.crm.UI.views.list;

import com.vaadin.componentfactory.multiselect.MultiComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.entity.Service;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
import ru.pahanjes.beautysaloon.crm.backend.repository.EmployeeRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.ServiceRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class EmployeeForm extends FormLayout {

    private final List<Employee> employees;
    private final ServiceRepository serviceRepository;
    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;
    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");
    TextField position = new TextField("должность");
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

    public EmployeeForm(List<Employee> employees, ServiceRepository serviceRepository, UserRepository userRepository, EmployeeRepository employeeRepository){
        this.employees = employees;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        addClassName("employee-form");

        salary.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        salary.setPrefixComponent(new Icon(VaadinIcon.MONEY));
        salary.setValue(new BigDecimal(0));
        services.setItems(serviceRepository.findAll());
        services.setItemLabelGenerator(Service::getService);
        /*binder.bindInstanceFields(this);*/
        /*configureBinder();*/
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

    private void configureBinder() {
        binder.bind(firstName, Employee::getFirstName, Employee::setFirstName);
        binder.bind(lastName, Employee::getLastName, Employee::setLastName);
        binder.bind(position, Employee::getPosition, Employee::setPosition);
        binder.bind(salary, Employee::getSalary, Employee::setSalary);
        binder.bind(email, Employee::getEmail, Employee::setEmail);
        binder.bind(phoneNumber, Employee::getPhoneNumber, Employee::setPhoneNumber);
        binder.bind(status, Employee::getStatus, Employee::setStatus);
        binder.bind(services, Employee::getServices, Employee::setServices);
        if(binder.getBean() != null) {
            if (binder.getBean().getUser() == null) {
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

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(delete -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(close -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {

        if(binder.isValid()){
            if(binder.getBean().getUser() == null) {
                //employeeRepository.save(binder.getBean());
                User user = new User();
                user.setUsername(login.getValue());
                user.setPassword(password.getValue());
                user.setRole(Collections.singleton(role.getValue()));
                user.setEmployee(binder.getBean());

                employeeRepository.save(binder.getBean());
                userRepository.save(user);
                binder.getBean().setUser(user);
                employeeRepository.save(binder.getBean());
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
