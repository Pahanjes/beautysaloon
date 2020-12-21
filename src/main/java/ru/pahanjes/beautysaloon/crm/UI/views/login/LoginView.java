package ru.pahanjes.beautysaloon.crm.UI.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import ru.pahanjes.beautysaloon.crm.UI.views.register.RegisterView;

import java.util.Collections;


@Route("login")
@PageTitle("Login | BS CRM")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    LoginForm loginForm = new LoginForm();

    public LoginView(){
        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        LoginI18n loginI18n = LoginI18n.createDefault();
        loginI18n.getForm().setForgotPassword("Зарегистрироваться");
        loginForm.setAction("login");
        loginForm.setI18n(loginI18n);
        loginForm.setForgotPasswordButtonVisible(true);
        loginForm.addForgotPasswordListener(click -> {
            UI.getCurrent().navigate("register");
        });

        add(
                new H1("Beauty Saloon CRM"),
                loginForm,
                new RouterLink("Register", RegisterView.class)
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(!beforeEnterEvent.getLocation()
        .getQueryParameters()
        .getParameters()
        .getOrDefault("error", Collections.emptyList())
        .isEmpty()) {
            loginForm.setError(true);
        }
    }

}
