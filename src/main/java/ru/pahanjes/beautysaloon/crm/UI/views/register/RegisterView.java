package ru.pahanjes.beautysaloon.crm.UI.views.register;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("register")
public class RegisterView extends VerticalLayout {

    TextField username = new TextField("Имя пользователя");
    PasswordField password = new PasswordField("Пароль");
    PasswordField confirmPassword = new PasswordField("Подтвердите пароль");

    public RegisterView(){
        addClassName("register-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        add(
                new H2("Регистрация"),
                username,
                password,
                confirmPassword,
                new Button("Зарегистрироваться", click -> register(username.getValue(), password.getValue(), confirmPassword.getValue()))
        );
    }

    /*@Override
    protected Component initContent() {
        TextField username = new TextField("Имя пользователя");
        PasswordField password = new PasswordField("Пароль");
        PasswordField confirmPassword = new PasswordField("Подтвердите пароль");
        return new VerticalLayout(
                new H2("Регистрация"),
                username,
                password,
                confirmPassword,
                new Button("Зарегистрироваться", click -> register(
                       username.getValue(),
                       password.getValue(),
                       confirmPassword.getValue()
                ))
        );
    }*/

    private void register(String username, String password, String confirmPassword) {
    }
}
