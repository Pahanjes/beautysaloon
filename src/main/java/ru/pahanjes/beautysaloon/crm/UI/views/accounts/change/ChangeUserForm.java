package ru.pahanjes.beautysaloon.crm.UI.views.accounts.change;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;

import java.util.Collections;
import java.util.Set;


public class ChangeUserForm extends VerticalLayout {

    private TextField username = new TextField("Логин");
    private PasswordField password = new PasswordField("Пароль");
    private PasswordField confirmPassword = new PasswordField("Подтвердите пароль");
    private ComboBox<Role> role = new ComboBox<>("Права");
    private RadioButtonGroup<String> isActive = new RadioButtonGroup<>();
    private Checkbox changePassword = new Checkbox();
    private Button save = new Button("Сохранить");
    private Button delete = new Button("Удалить");
    private Button close = new Button("Отменить");
    private User user;

    public ChangeUserForm(User user) {
        this.user = user;
        addClassName("user-form");
        configureRadioButton();
        configureForm();
        add(
                createTopLayout(),
                createCenterLayout(),
                isActive,
                createButtonsLayout()
        );
    }

    public ChangeUserForm() {
        addClassName("user-form");
        configureRadioButton();
        add(
                createTopLayout(),
                createCenterLayout(),
                isActive,
                createButtonsLayout()
        );
    }

    private void configureForm() {
        username.setValue(user.getUsername());
        password.setEnabled(false);
        confirmPassword.setEnabled(false);
        changePassword.setLabel("Сменить пароль");
        changePassword.setValue(false);
        changePassword.addValueChangeListener(change -> {
            if (change.getValue()) {
                password.setEnabled(true);
                confirmPassword.setEnabled(true);
            } else if (!change.getValue()) {
                password.setEnabled(false);
                confirmPassword.setEnabled(false);
            }
        });
        role.setItems(Role.values());
        role.setValue(user.getRole().iterator().next());
        isActive.setValue(user.isActive() ? "Активен" : "Неактивен");
    }

    private void configureRadioButton() {
        isActive.setItems("Активен", "Неактивен");
    }

    private Component createButtonsLayout() {
        save.addClickListener(save -> {
            updateUser();
            fireEvent(new SaveUserEvent(this, user));
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(delete -> fireEvent(new DeleteUserEvent(this, user)));
        close.addClickListener(close -> fireEvent(new CloseUserEvent(this, user)));
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        return new HorizontalLayout(save, delete, close);
    }

    private Component createTopLayout() {
        return new HorizontalLayout(
                username,
                role
        );
    }

    private Component createCenterLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(password, confirmPassword, changePassword);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        return horizontalLayout;
    }

    private void updateUser() {
        if(changePassword.getValue()) {
            if(password.getValue().equals(confirmPassword.getValue())) {
                user.setPassword(password.getValue());
            } else {
                Notification.show("Пароли не совпадают");
                return;
            }
        }
        user.setUsername(username.getValue());
        user.setRole(Collections.singleton(role.getValue()));
        user.setActive(isActive.getValue().equals("Активен"));
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        configureForm();
    }

    public String getUsername() {
        return username.getValue();
    }

    public Set<Role> getRole() {
        return Collections.singleton(role.getValue());
    }

    public boolean isChangePassword() {
        return changePassword.getValue();
    }

    public String getPassword() {
        return  password.getValue();
    }

    public String getConfirmPassword() {
        return confirmPassword.getValue();
    }

    public boolean isActive() {
        return isActive.getValue().equals("Активен");
    }

    public static abstract class ChangeUserFormEvent extends  ComponentEvent<ChangeUserForm> {
        private User user;

        protected ChangeUserFormEvent(ChangeUserForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveUserEvent extends ChangeUserFormEvent {
        public SaveUserEvent(ChangeUserForm source, User user) {
            super(source, user);
        }
    }

    public static class DeleteUserEvent extends ChangeUserFormEvent {
        public DeleteUserEvent(ChangeUserForm source, User user) {
            super(source, user);
        }
    }

    public static class CloseUserEvent extends ChangeUserFormEvent {
        public CloseUserEvent(ChangeUserForm source, User user) {
            super(source, user);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
