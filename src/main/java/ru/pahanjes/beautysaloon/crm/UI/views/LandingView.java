package ru.pahanjes.beautysaloon.crm.UI.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.pahanjes.beautysaloon.crm.backend.service.CustomerService;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;
import ru.pahanjes.beautysaloon.crm.backend.service.ServiceService;

@Route(value = "main")
@PageTitle("Салон красоты | BS main")
public class LandingView extends VerticalLayout {

    private final Tabs menu = new Tabs();
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private ServiceService serviceService;
    private final RegisterDialog registerDialog;

    public LandingView(CustomerService customerService, EmployeeService employeeService, ServiceService serviceService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.serviceService = serviceService;
        registerDialog = new RegisterDialog(customerService, employeeService, serviceService);

        add(
                getAllComponents()
        );
    }

    private Component getMenu() {
        Tab login = new Tab("Вход");
        Image logo = new Image("images/logo.png", "Beauty Saloon");
        logo.setWidth("65px");
        logo.setHeight("34px");
        Tab main = new Tab(logo);
        Tab register = new Tab("Запись");

        menu.add(login, main, register);
        menu.setAutoselect(false);
        menu.setSelectedIndex(1);

        menu.addSelectedChangeListener(selectedChangeEvent -> {
           if(selectedChangeEvent.getSelectedTab().getLabel().equals("Вход")) {
               UI.getCurrent().navigate("login");
           } else if(selectedChangeEvent.getSelectedTab().getLabel().equals("Запись")) {
               registerDialog.open();
           }
           menu.setSelectedIndex(1);
        });

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.CENTER);
        header.add(menu);
        return header;
    }

    private Component getBody() {
        VerticalLayout body = new VerticalLayout();
        HorizontalLayout blocksRow = new HorizontalLayout();
        Image haircut = new Image("images/haircut.jpeg", "");
        haircut.setWidth("150px");
        haircut.setHeight("150px");
        haircut.setTitle("Стрижка");

        body.add(blocksRow);

        return body;
    }

    private Component[] getAllComponents() {
        return new Component[] {
                getMenu(),
                getBody()
        };
    }

}
