package ru.pahanjes.beautysaloon.crm.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;
import ru.pahanjes.beautysaloon.crm.UI.views.login.LoginView;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    // Прослушивание инициализации пользовательского интерфейса (внутренний корневой компонент в Vaadin),
    // А затем добавление прослушивателя (Listener) перед каждым переходом View.
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    // Перенаправление всех запросов на логин, если пользователь не авторизован.
    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }
}