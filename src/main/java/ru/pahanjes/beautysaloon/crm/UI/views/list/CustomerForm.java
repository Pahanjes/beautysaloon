package ru.pahanjes.beautysaloon.crm.UI.views.list;

import com.vaadin.componentfactory.multiselect.MultiComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import ru.pahanjes.beautysaloon.crm.backend.entity.*;
import ru.pahanjes.beautysaloon.crm.backend.repository.ServiceRepository;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;
import ru.pahanjes.beautysaloon.crm.util.Constants;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CustomerForm extends FormLayout {
    private final List<Employee> employees;
    private final ServiceRepository serviceRepository;
    private final EmployeeService employeeService;
    private final TextField firstName = new TextField("Имя");
    private final TextField lastName = new TextField("Фамилия");
    private final EmailField email = new EmailField("Электронная почта");
    private final TextField phoneNumber = new TextField("Номер телефона");
    private final ComboBox<Customer.Status> status = new ComboBox<>("Статус");
    private  DateTimePicker timetable = new DateTimePicker();
    private final MultiComboBox<Service> services = new MultiComboBox<>("Услуги");
    private final ComboBox<Employee> employee = new ComboBox<>("Сотрудник");

    private final Button save = new Button("Сохранить");
    private final Button delete = new Button("Удалить");
    private final Button close = new Button("Отменить");

    Binder<Customer> binder = new BeanValidationBinder<>(Customer.class);

    public CustomerForm(List<Employee> employees, ServiceRepository serviceRepository, EmployeeService employeeService){
        this.employees = employees;
        this.serviceRepository = serviceRepository;
        this.employeeService = employeeService;
        addClassName("customer-form");

        configureBinder();
        status.setItems(Customer.Status.values());
        status.setItemLabelGenerator(Customer.Status::getValue);
        timetable.setLabel("Дата и время приема");
        timetable.setMin(Constants.COMPANY_FOUNDATION_DATE);
        timetable.setMax(LocalDateTime.now().plusMonths(2));
        timetable.setRequiredIndicatorVisible(true);
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

    private void configureBinder() {
        binder.forField(firstName)
                .withValidator(
                        fname -> !fname.isEmpty(), "Введите имя")
                .bind(Customer::getFirstName, Customer::setFirstName);
        binder.forField(lastName)
                .withValidator(
                        lname -> !lname.isEmpty(), "Введите фамилию")
                .bind(Customer::getLastName, Customer::setLastName);
        binder.forField(email)
                .withValidator(
                        new EmailValidator("Введите корректный адрес электронной почты"))
                .bind(Customer::getEmail, Customer::setEmail);
        binder.forField(phoneNumber)
                .withValidator(
                        phone -> !phone.isEmpty(), "Введите номер телефона")
                .bind(Customer::getPhoneNumber, Customer::setPhoneNumber);
        binder.forField(status)
                .withValidator(
                        Objects::nonNull, "Выберите статус")
                .bind(Customer::getStatus, Customer::setStatus);
        binder.forField(timetable)
                .withValidator(
                        Objects::nonNull, "Выберите дату и время"
                )
                .withValidator(time -> !DayOfWeek.SUNDAY.equals(time.getDayOfWeek())
                                        && time.getHour() >= 8 && time.getHour() <= 21,
                        "Запись может быть произведена только с понедельника по субботу с 8:00 до 21:00")
                .bind(Customer::getTimetable, Customer::setTimetable);
        binder.forField(services)
                .withValidator(
                        services1 -> services1.size() > 0, "Выберите услуги")
                .bind(Customer::getServices, Customer::setServices);
        binder.forField(employee)
                .withValidator(
                        Objects::nonNull, "Выберите работника")
                .bind(Customer::getEmployee, Customer::setEmployee);
        firstName.setRequired(true);
        firstName.setRequiredIndicatorVisible(true);
        firstName.setPlaceholder("Введите имя: ");

        lastName.setRequired(true);
        lastName.setRequiredIndicatorVisible(true);
        lastName.setPlaceholder("Введите фамилию: ");

        phoneNumber.setRequired(true);
        phoneNumber.setRequiredIndicatorVisible(true);
        phoneNumber.setPlaceholder("Введите номер телефона: ");

        email.setRequiredIndicatorVisible(true);
        email.setPlaceholder("Введите адрес электронной почты: ");

        status.setRequired(true);
        status.setRequiredIndicatorVisible(true);
        status.setPlaceholder("Выберите статус: ");

        timetable.setRequiredIndicatorVisible(true);
        timetable.setDatePickerI18n(new DatePicker.DatePickerI18n()
        .setWeek("Неделя").setCalendar("Календарь").setClear("Очистить")
        .setToday("Сегодня").setCancel("Отмена").setFirstDayOfWeek(1)
        .setMonthNames(Arrays.asList(
                "Январь", "Февраль", "Март", "Апрель",
                "Май", "Июнь", "Июль", "Август", "Сентябрь",
                "Октябрь", "Ноябрь", "Декабрь"))
        .setWeekdays(Arrays.asList(
                "Воскресенье", "Понедельник", "Вторник", "Среда",
                "Четверг", "Пятница", "Суббота"))
        .setWeekdaysShort(Arrays.asList(
                "Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"))
        );

        services.setRequired(true);
        services.setRequiredIndicatorVisible(true);
        services.setPlaceholder("Выберите услуги: ");
        services.setI18n(new MultiComboBox.MultiComboBoxI18n()
        .setClear("Очистить")
        .setSelect("Выбрать все"));

        employee.setRequired(true);
        employee.setRequiredIndicatorVisible(true);
        employee.setPlaceholder("Выберите сотрудника: ");
    }

    public void setCustomer(Customer customer){
        binder.setBean(customer);
        if(VaadinSession.getCurrent().getAttribute(User.class).getRole().iterator().next() == Role.USER) {
            employee.setReadOnly(true);
            delete.setEnabled(false);
            timetable.setReadOnly(false);
            if(customer != null) {
                if(customer.getEmployee() == null) {
                    employee.setReadOnly(false);
                }
            }
            if(customer != null && customer.getEmployee() != null) {
                if(!VaadinSession.getCurrent().getAttribute(User.class).getEmployee().getFullNameWithPosition().equals(customer.getEmployee().getFullNameWithPosition())) {
                    timetable.setReadOnly(true);
                }
            }
        }
        if(customer != null) {
            if (customer.getTimetable() == null) {
                timetable.clear();
            }
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(delete -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(close -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(timetable.isEmpty()) {
            Notification.show("Установите дату приема");
            return;
        }
        Optional<Service> notActiveService = binder.getBean().getServices().stream().filter(service -> !service.isActive()).findFirst();
        if (notActiveService.isPresent()) {
            Notification.show("Услуга \"" + notActiveService.get().getService() + "\" в данный момент недоступна");
            return;
        }
        if(binder.isValid()){
            Employee employee = employeeService.findById(this.employee.getValue().getId());
            if(employee.getCustomers().stream().anyMatch(
                    customer -> customer
                            .getFirstName().equals(binder.getBean().getFirstName())
                            && customer.getLastName().equals(binder.getBean().getLastName())
                            && customer.getPhoneNumber().equals(binder.getBean().getPhoneNumber())
                    )
            ) {
                fireEvent(new SaveEvent(this, binder.getBean(), false));
            } else {
                fireEvent(new SaveEvent(this, binder.getBean(), true));
            }
        }
    }

    public static abstract class CustomerFormEvent extends ComponentEvent<CustomerForm> {
        private Customer customer;
        private boolean isNewCustomer;

        protected CustomerFormEvent(CustomerForm source, Customer customer) {
            super(source, false);
            this.customer = customer;
        }

        protected CustomerFormEvent(CustomerForm source, Customer customer, boolean isNewCustomer) {
            super(source, false);
            this.customer = customer;
            this.isNewCustomer = isNewCustomer;
        }

        public Customer getCustomer(){
            return customer;
        }

        public boolean isNewCustomer() {
            return this.isNewCustomer;
        }
    }

    public static class SaveEvent extends CustomerFormEvent {
        public SaveEvent(CustomerForm source, Customer customer, boolean isNewCustomer) {
            super(source, customer, isNewCustomer);
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
