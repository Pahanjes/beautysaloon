package ru.pahanjes.beautysaloon.crm.UI.views;

import com.vaadin.componentfactory.multiselect.MultiComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import ru.pahanjes.beautysaloon.crm.backend.entity.Customer;
import ru.pahanjes.beautysaloon.crm.backend.entity.Service;
import ru.pahanjes.beautysaloon.crm.backend.service.CustomerService;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;
import ru.pahanjes.beautysaloon.crm.backend.service.ServiceService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

/*@Route("register")
@PageTitle("Запись")*/
public class RegisterDialog extends Dialog {

    private TextField firstName = new TextField("Имя");
    private TextField lastName = new TextField("Фамилия");
    private EmailField email = new EmailField("Электронная почта");
    private TextField phoneNumber = new TextField("Номер телефона");
    private MultiComboBox<Service> services = new MultiComboBox<>("Услуги");
    private DateTimePicker timetable = new DateTimePicker();
    private CustomerService customerService;
    private EmployeeService employeeService;
    private ServiceService serviceService;
    private Customer customer;

    private Button save = new Button("Записаться");
    private Button close = new Button("Отменить");

    public RegisterDialog(CustomerService customerService, EmployeeService employeeService, ServiceService serviceService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.serviceService = serviceService;

        configureDialog();
        add(
                getDialogForm()
        );
    }

    private Component[] getDialogForm() {
        HorizontalLayout name = new HorizontalLayout();
        name.setWidthFull();
        name.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        name.add(firstName, lastName);

        HorizontalLayout contacts = new HorizontalLayout();
        contacts.setWidthFull();
        contacts.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        contacts.add(email, phoneNumber);

        HorizontalLayout services = new HorizontalLayout();
        services.setWidthFull();
        services.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        services.add(this.services);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttons.add(save, close);

        VerticalLayout timetableAndButtons = new VerticalLayout();
        timetableAndButtons.setWidthFull();
        timetableAndButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        timetableAndButtons.add(timetable, buttons);

        return new Component[] {
                name,
                contacts,
                services,
                timetableAndButtons
        };
    }

    private void configureDialog() {
        firstName.setPlaceholder("Виктория");
        firstName.setRequired(true);
        firstName.setRequiredIndicatorVisible(true);
        firstName.setErrorMessage("Введите имя");

        lastName.setPlaceholder("Павловна");
        lastName.setRequired(true);
        lastName.setRequiredIndicatorVisible(true);
        lastName.setErrorMessage("Введите фамилию");

        email.setPlaceholder("angelina_pavlovla@gmail.com");
        email.setRequiredIndicatorVisible(true);
        email.setErrorMessage("Введите корректный адрес электронной почты");

        phoneNumber.setPlaceholder("+7-xxx-xxx-xx-xx");
        phoneNumber.setRequired(true);
        phoneNumber.setRequiredIndicatorVisible(true);
        phoneNumber.setErrorMessage("Введите номер телефона");

        services.setItems(serviceService.findAll());
        services.setItemLabelGenerator(Service::getService);
        services.setRequired(true);
        services.setRequiredIndicatorVisible(true);
        services.setPlaceholder("Стрижка");
        services.setErrorMessage("Выберите услуги");
        services.setI18n(new MultiComboBox.MultiComboBoxI18n()
                .setClear("Очистить")
                .setSelect("Выбрать все"));

        timetable.setMin(LocalDateTime.now().plusDays(1L));
        timetable.setMax(LocalDateTime.now().plusMonths(1L));
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
        timetable.setRequiredIndicatorVisible(true);
        timetable.setErrorMessage("Выберите дату и время");

        save.addClickListener(save -> saveCustomer());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        close.addClickListener(close -> close());
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    }

    private void saveCustomer() {
        if (firstName.getValue().isEmpty()) {
            Notification notification = new Notification("Введите имя", 3000, Notification.Position.TOP_CENTER);
            notification.open();
            return;
        } else if (lastName.getValue().isEmpty()) {
            Notification notification = new Notification("Введите фамилию", 3000, Notification.Position.TOP_CENTER);
            notification.open();
            return;
        } else if (email.getValue().isEmpty()) {
            Notification notification = new Notification("Введите адрес электронной почты", 3000, Notification.Position.TOP_CENTER);
            notification.open();
            return;
        }  else if (phoneNumber.getValue().isEmpty()) {
            Notification notification = new Notification("Введите номер", 3000, Notification.Position.TOP_CENTER);
            notification.open();
            return;
        } else if (services.getValue().size() == 0) {
            Notification notification = new Notification("Выберите услуги", 3000, Notification.Position.TOP_CENTER);
            notification.open();
            return;
        } else if(timetable.getValue().getDayOfWeek() == DayOfWeek.SUNDAY || timetable.getValue().getHour() > 21 || timetable.getValue().getHour() < 8) {
            Notification notification = new Notification("Запись может быть произведена только с понедельника по субботу с 8:00 до 21:00",
                    3000, Notification.Position.TOP_CENTER
            );
            notification.open();
            return;
        }
        Optional<Service> notActiveService = services.getValue().stream().findFirst().filter(service -> !service.isActive());
        if(notActiveService.isPresent()) {
            Notification notification = new Notification("Услуга \"" + notActiveService.get().getService() + "\" временно недоступна",
                    3000, Notification.Position.TOP_CENTER);
            notification.open();
            return;
        }

        customer = new Customer();
        customer.setFirstName(firstName.getValue());
        customer.setLastName(lastName.getValue());
        customer.setEmail(email.getValue());
        customer.setPhoneNumber(phoneNumber.getValue());
        customer.setServices(services.getValue());
        customer.setTimetable(timetable.getValue());
        customer.setStatus(Customer.Status.SignedUp);
        customerService.save(customer);
        Notification.show(firstName.getValue() + ", Вы были успешно записаны");
        close();
    }

}
