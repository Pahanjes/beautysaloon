package ru.pahanjes.beautysaloon.crm.UI.views.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.pahanjes.beautysaloon.crm.exception.AuthException;
import ru.pahanjes.beautysaloon.crm.security.AuthService;


@Route(value = "login")
@PageTitle("Login | BS CRM")
public class LoginView extends VerticalLayout {

    public LoginView(AuthService authService) {
        setId("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        H1 headerWelcome = new H1("Beauty Saloon CRM");
        headerWelcome.setId("welcome-header");
        H2 headerLogIn = new H2("Вход");
        headerLogIn.setId("login-header");

        TextField username = new TextField("Имя пользователя");
        username.setRequiredIndicatorVisible(true);
        username.setRequired(true);
        username.setErrorMessage("Поле не может быть пустым!");
        username.setId("textfield-username");
        PasswordField password = new PasswordField("Пароль");
        password.setRequiredIndicatorVisible(true);
        password.setRequired(true);
        password.setErrorMessage("Поле не может быть пустым!");
        password.setId("passwordfield-password");

        Button buttonLogIn = new Button("Войти", click -> {
            try {
                authService.authenticate(username.getValue(), password.getValue());
                UI.getCurrent().navigate("lk/welcome");
            } catch (AuthException exception) {
                Notification.show(exception.getMessage());
            }
        });

        buttonLogIn.addClickShortcut(Key.ENTER);

        add(
                headerWelcome,
                headerLogIn,
                username,
                password,
                buttonLogIn
        );
    }

}
