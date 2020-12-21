package ru.pahanjes.beautysaloon.crm.UI.views.cabinet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
import ru.pahanjes.beautysaloon.crm.security.AuthService;

@Route(value = "lk")
@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/cabinet-view.css")
public class CabinetLayout extends AppLayout {

    private final Tabs menu;
    private AuthService authService;
    /*private H1 viewTitle;*/

    public CabinetLayout(AuthService authService) {
        this.authService = authService;
        setPrimarySection(Section.DRAWER);
        menu = createMenu();
        createHeader();
        createDrawBarContent();
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        return authService.getAuthorizedRoutes(user.getRole()).stream()
        .map(routes -> createTab(routes.getName(), routes.getView())).toArray(Component[]::new);
    }

    private Tab createTab(String text, Class<? extends  Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        return tab;
    }

    private void createDrawBarContent() {
        VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.setSizeFull();
        menuLayout.setPadding(false);
        menuLayout.setSpacing(false);
        menuLayout.getThemeList().set("spacing-s", true);
        menuLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "Beauty Saloon CRM"));
        logoLayout.add(new H1("CRM"));
        menuLayout.add(logoLayout, menu);
        addToDrawer(menuLayout);
    }

    private void createHeader() {
        H1 logo = new H1("Личный кабинет");
        logo.setId("logo");
        HorizontalLayout header = new HorizontalLayout();
        header.setId("header");
        header.getThemeList().set("dark", true);
        header.setWidthFull();
        header.setSpacing(false);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.add(new DrawerToggle());
        Anchor logout = new Anchor("logout", "Выход");
        logout.setId("anchor");
        header.add(logo, new H5("Имя Фамилия, должность"), logout);
        header.expand(logo);
        addToNavbar(header);
    }

}
