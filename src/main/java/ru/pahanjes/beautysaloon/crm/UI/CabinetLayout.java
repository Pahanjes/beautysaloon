package ru.pahanjes.beautysaloon.crm.UI;

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
import com.vaadin.flow.router.RouterLink;
import ru.pahanjes.beautysaloon.crm.UI.views.dashboard.DashBoardView;
import ru.pahanjes.beautysaloon.crm.UI.views.list.CustomerListView;
import ru.pahanjes.beautysaloon.crm.UI.views.list.EmployeeListView;

@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/cabinet-view.css")
public class CabinetLayout extends AppLayout {

    private final Tabs menu;
    /*private H1 viewTitle;*/

    public CabinetLayout() {
        setPrimarySection(Section.DRAWER);
        /*addToNavbar(true, createHeader());
        menu = createMenu();
        addToDrawer(createDrawBarContent(menu));*/
        menu = createMenu();
        createHeader();
        createDrawBarContent(menu);
        /*createHeader();
        createDrawer();*/
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
        return new Tab[]{
                createTab("Клиенты", CustomerListView.class),
                createTab("Сотрудники", EmployeeListView.class),
                createTab("Расписание", DashBoardView.class)
        };
    }

    private Tab createTab(String text, Class<? extends  Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        //ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    private void createDrawBarContent(Tabs sourceMenu) {
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
        Anchor logout = new Anchor("/logout", "Выход");
        logout.setId("anchor");
        header.add(logo, new H5("Имя Фамилия, должность"), logout);
        header.expand(logo);
        addToNavbar(header);
    }

    /*private Component createDrawBarContent(Tabs sourceMenu) {
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
        return menuLayout;
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
        return new Tab[]{
                createTab("Клиенты", CustomerListView.class),
                createTab("Сотрудники", EmployeeListView.class),
                createTab("Расписание", DashBoardView.class)
        };
    }

    private Tab createTab(String text, Class<? extends  Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setId("header");
        header.getThemeList().set("dark", true);
        header.setWidthFull();
        header.setSpacing(false);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.add(new DrawerToggle());
        viewTitle = new H1();
        Anchor logout = new Anchor("/logout", "Выход");
        logout.setId("anchor");
        header.add(viewTitle, new H5("Имя Фамилия, должность"), logout);
        header.expand(viewTitle);
        return header;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }*/

    /*private void createHeader() {
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
    }*/

}
