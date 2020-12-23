package ru.pahanjes.beautysaloon.crm.UI.views.accounts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.pahanjes.beautysaloon.crm.UI.views.accounts.change.ChangeView;
import ru.pahanjes.beautysaloon.crm.UI.views.accounts.register.RegisterView;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;
import ru.pahanjes.beautysaloon.crm.backend.service.UserService;
import ru.pahanjes.beautysaloon.crm.security.AuthService;

import java.util.HashMap;
import java.util.Map;

@Route(value = "lk/accounts")
@PageTitle("Учетные записи | BS CRM")
public class AccountsView extends VerticalLayout {

    private Tabs subMenu = new Tabs();
    private TextField username = new TextField("Имя пользователя");
    private PasswordField password = new PasswordField("Пароль");
    private PasswordField confirmPassword = new PasswordField("Подтвердите пароль");
    private ComboBox<Employee> employees = new ComboBox<>("Сотрудник");
    private ComboBox<Role> role = new ComboBox<>("Права");
    private Button register = new Button("Зарегистрировать");
    private HorizontalLayout firstLayout = new HorizontalLayout();
    private HorizontalLayout secondLayout = new HorizontalLayout();
    Div content = new Div();
    private RegisterView registerView;
    private ChangeView changeView;
    private AuthService authService;
    private EmployeeService employeeService;
    private UserService userService;

    public AccountsView(AuthService authService, EmployeeService employeeService, UserService userService){
        this.authService = authService;
        this.employeeService = employeeService;
        this.userService = userService;
        addClassName("accounts-view");
        setSizeFull();

        //setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        registerView = new RegisterView(authService, employeeService);
        changeView = new ChangeView(userService, employeeService);

        content.setId("content");
        content.setSizeFull();

        createMenuTabs(subMenu, content);
        subMenu.setId("subMenu");

        add(
                subMenu,
                content
        );
    }

    public void createMenuTabs(Tabs tabs, Div div) {
        Tab tab1 = new Tab("Регистрация");
        Div div1 = new Div(registerView);
        div1.setVisible(true);
        Tab tab2 = new Tab("Редактирование");
        Div div2 = new Div(changeView);
        div2.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, div1);
        tabsToPages.put(tab2, div2);
        tabs.add(tab1, tab2);
        div.add(div1, div2);

        tabs.setSelectedIndex(0);
        tabs.addSelectedChangeListener(selected -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            tabsToPages.get(tabs.getSelectedTab()).setVisible(true);
        });
    }

}
