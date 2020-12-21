package ru.pahanjes.beautysaloon.crm.security;

import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;
import ru.pahanjes.beautysaloon.crm.UI.views.cabinet.CabinetLayout;
import ru.pahanjes.beautysaloon.crm.UI.views.dashboard.DashBoardView;
import ru.pahanjes.beautysaloon.crm.UI.views.list.CustomerListView;
import ru.pahanjes.beautysaloon.crm.UI.views.list.EmployeeListView;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
import ru.pahanjes.beautysaloon.crm.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AuthService extends Exception {

    public static class AuthException extends Exception {

    }

    private AuthorizedRoute route;
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.findByUsername(username);
        if (user != null && user.checkPassword(password)) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
        } else {
            throw new AuthException();
        }
    }

    private void createRoutes(Set<Role> role) {
        getAuthorizedRoutes(role).stream()
        .forEach(route ->
                RouteConfiguration.forSessionScope().setRoute(
                        route.getRoute(), route.getView(), CabinetLayout.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Set<Role> role) {
        ArrayList<AuthorizedRoute> routes = new ArrayList<>();

        if (role.toArray()[0].equals(Role.USER)){
            routes.add(new AuthorizedRoute("lk/customer", "Клиенты", CustomerListView.class));
            routes.add(new AuthorizedRoute("lk/dashboard", "Расписание", DashBoardView.class));
        } else if (role.toArray()[0].equals(Role.ADMIN)) {
            routes.add(new AuthorizedRoute("lk/customer", "Клиенты", CustomerListView.class));
            routes.add(new AuthorizedRoute("lk/employee", "Сотрудники", EmployeeListView.class));
            routes.add(new AuthorizedRoute("lk/dashboard", "Расписание", DashBoardView.class));
        }
        return routes;
    }
}
