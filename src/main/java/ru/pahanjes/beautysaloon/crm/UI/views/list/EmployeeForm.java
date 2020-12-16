package ru.pahanjes.beautysaloon.crm.UI.views.list;

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
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeForm extends FormLayout {

    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");
    TextField position = new TextField("должность");
    BigDecimalField salary = new BigDecimalField("Зарплата");
    EmailField email = new EmailField("Электронная почта");
    TextField phoneNumber = new TextField("Номер телефона");
    ComboBox<Employee.Status> status = new ComboBox<>("Статус");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отменить");

    Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);

    public EmployeeForm(List<Employee> employees){
        addClassName("employee-form");

        salary.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        salary.setPrefixComponent(new Icon(VaadinIcon.MONEY));
        salary.setValue(new BigDecimal(0));

        binder.bindInstanceFields(this);
        status.setItems(Employee.Status.values());
        add(
                firstName,
                lastName,
                position,
                salary,
                phoneNumber,
                email,
                status,
                createButtonsLayout()
        );
    }

    public void setEmployee(Employee employee){
        binder.setBean(employee);
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
