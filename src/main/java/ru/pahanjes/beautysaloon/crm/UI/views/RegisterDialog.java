package ru.pahanjes.beautysaloon.crm.UI.views;

import com.vaadin.componentfactory.multiselect.MultiComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

import java.time.LocalDateTime;

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

        lastName.setPlaceholder("Павловна");
        lastName.setRequired(true);
        lastName.setRequiredIndicatorVisible(true);

        email.setPlaceholder("angelina_pavlovla@gmail.com");
        email.setRequiredIndicatorVisible(true);

        phoneNumber.setPlaceholder("+7-xxx-xxx-xx-xx");
        phoneNumber.setRequired(true);
        phoneNumber.setRequiredIndicatorVisible(true);

        services.setItems(serviceService.findAll());
        services.setItemLabelGenerator(Service::getService);

        timetable.setValue(LocalDateTime.now());

        save.addClickListener(save -> saveCustomer());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        close.addClickListener(close -> close());
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    }

    private void saveCustomer() {
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
