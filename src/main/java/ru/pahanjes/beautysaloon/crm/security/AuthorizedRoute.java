package ru.pahanjes.beautysaloon.crm.security;

import com.vaadin.flow.component.Component;

public class AuthorizedRoute {

    private String Route;
    private String name;
    private Class<? extends Component> view;

    public AuthorizedRoute(String route, String name, Class<? extends Component> view) {
        Route = route;
        this.name = name;
        this.view = view;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<? extends Component> getView() {
        return view;
    }

    public void setView(Class<? extends Component> view) {
        this.view = view;
    }
}
