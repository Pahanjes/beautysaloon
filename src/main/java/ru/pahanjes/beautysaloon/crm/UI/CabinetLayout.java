package ru.pahanjes.beautysaloon.crm.UI;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import ru.pahanjes.beautysaloon.crm.UI.views.dashboard.DashBoardView;
import ru.pahanjes.beautysaloon.crm.UI.views.list.CustomerListView;
import ru.pahanjes.beautysaloon.crm.UI.views.list.EmployeeListView;

@CssImport("./styles/shared-styles.css")
public class CabinetLayout extends AppLayout {

    public CabinetLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("BS: Личный кабинет");
        logo.addClassName("logo");

        Anchor logout = new Anchor("/logout", "Выход");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.addClassName("header");
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);    //add logo
    }

    private void createDrawer() {
        RouterLink customerListLink = new RouterLink("Клиенты", CustomerListView.class);
        customerListLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink employeeListLink = new RouterLink("Сотрудники", EmployeeListView.class);

        addToDrawer(new VerticalLayout(
                customerListLink,
                employeeListLink,
                new RouterLink("Расисание", DashBoardView.class)
        ));
    }

}
