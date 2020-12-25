package ru.pahanjes.beautysaloon.crm.UI.views.list;

import com.vaadin.componentfactory.multiselect.MultiComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import ru.pahanjes.beautysaloon.crm.backend.entity.*;
import ru.pahanjes.beautysaloon.crm.backend.repository.ServiceRepository;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerForm extends FormLayout {
    private final List<Employee> employees;
    private final ServiceRepository serviceRepository;
    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");
    EmailField email = new EmailField("Электронная почта");
    TextField phoneNumber = new TextField("Номер телефона");
    ComboBox<Customer.Status> status = new ComboBox<>("Статус");
    DateTimePicker timetable = new DateTimePicker();
    MultiComboBox<Service> services = new MultiComboBox<>("Услуги");
    ComboBox<Employee> employee = new ComboBox<>("Сотрудник");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отменить");

    Binder<Customer> binder = new BeanValidationBinder<>(Customer.class);

    public CustomerForm(List<Employee> employees, ServiceRepository serviceRepository){
        this.employees = employees;
        this.serviceRepository = serviceRepository;
        addClassName("customer-form");

        //Областная 5 корпус 1 парадная 2 (обед с 12 до часу)

        binder.bindInstanceFields(this);
        status.setItems(Customer.Status.values());
        timetable.setValue(LocalDateTime.now());
        timetable.setLabel("Дата и время приема");
        services.setItems(serviceRepository.findAll());
        services.setItemLabelGenerator(Service::getService);
        employee.setItems(employees);
        employee.setItemLabelGenerator(Employee::getFullNameWithPosition);
        add(
                firstName,
                lastName,
                phoneNumber,
                email,
                status,
                timetable,
                services,
                employee,
                createButtonsLayout()
        );
    }

    public void setCustomer(Customer customer){
        binder.setBean(customer);
        if(VaadinSession.getCurrent().getAttribute(User.class).getRole().iterator().next() == Role.USER) {
            employee.setReadOnly(true);
            delete.setEnabled(false);
            timetable.setReadOnly(false);
            if(customer != null && customer.getEmployee() != null) {
                if(!VaadinSession.getCurrent().getAttribute(User.class).getEmployees().get(0).getFullNameWithPosition().equals(customer.getEmployee().getFullNameWithPosition())) {
                    timetable.setReadOnly(true);
                }
            }
        }
    }

    private Component createButtonsLayout() {
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
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public static abstract class CustomerFormEvent extends ComponentEvent<CustomerForm> {
        private Customer customer;

        protected CustomerFormEvent(CustomerForm source, Customer customer) {
            super(source, false);
            this.customer = customer;
        }

        public Customer getCustomer(){
            return customer;
        }
    }

    public static class SaveEvent extends CustomerFormEvent {
        public SaveEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }
    }

    public static class DeleteEvent extends CustomerFormEvent {
        public DeleteEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }
    }

    public static class CloseEvent extends CustomerFormEvent {
        public CloseEvent(CustomerForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener (Class<T> eventType,
                                                                   ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
