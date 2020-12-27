package ru.pahanjes.beautysaloon.crm.UI.views.service;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ru.pahanjes.beautysaloon.crm.backend.entity.Service;

import java.math.BigDecimal;

public class ServiceForm extends FormLayout {
    private TextField service = new TextField("Наименование");
    private BigDecimalField price = new BigDecimalField();
    private Binder<Service> binder = new BeanValidationBinder<>(Service.class);
    private Button save = new Button("Сохранить");
    private Button delete = new Button();
    private Button close = new Button("Отменить");

    public ServiceForm() {
        addClassName("service-form");
        binder.bindInstanceFields(this);
        price.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        price.setPrefixComponent(new Icon(VaadinIcon.MONEY));
        price.setValue(new BigDecimal(0));
        add(
                service,
                price,
                createButtonLayout()
        );
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(save -> validateAndSave());
        delete.addClickListener(delete -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(close -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setService(Service service) {
        binder.setBean(service);
        if(binder.getBean() != null) {
            if(binder.getBean().isActive()) {
                delete.setEnabled(true);
                delete.setText("Деактивировать");
            } else {
                delete.setEnabled(true);
                delete.setText("Активировать");
            }
        } else {
            delete.setText("Активация");
            delete.setEnabled(false);
        }
    }

    private void validateAndSave() {
        if(binder.isValid()){
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public static abstract class ServiceFormEvent extends ComponentEvent<ServiceForm> {
        private Service service;

        protected ServiceFormEvent(ServiceForm source, Service service){
            super(source, false);
            this.service = service;
        }

        public Service getService(){
            return service;
        }
    }

    public static class SaveEvent extends ServiceFormEvent {
        public SaveEvent(ServiceForm source, Service service) {
            super(source, service);
        }
    }

    public static class DeleteEvent extends ServiceFormEvent {
        public DeleteEvent(ServiceForm source, Service service) {
            super(source, service);
        }
    }

    public static class CloseEvent extends ServiceFormEvent {
        public CloseEvent(ServiceForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener (Class<T> eventType,
                                                                   ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
