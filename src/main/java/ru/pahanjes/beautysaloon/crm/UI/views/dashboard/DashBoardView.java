package ru.pahanjes.beautysaloon.crm.UI.views.dashboard;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.pahanjes.beautysaloon.crm.UI.CabinetLayout;
import ru.pahanjes.beautysaloon.crm.backend.service.CustomerService;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;

@PageTitle("Расписание | BS")
@Route(value = "lk/dashboard", layout = CabinetLayout.class)
public class DashBoardView extends VerticalLayout {

    private final CustomerService customerService;
    private final EmployeeService employeeService;

    public DashBoardView(CustomerService customerService, EmployeeService employeeService){
        this.customerService = customerService;
        this.employeeService = employeeService;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(
                getCustomerStats()
        );
    }

    private Span getCustomerStats() {
        Span stats = new Span(customerService.count() + " клиентов");
        stats.addClassName("customer-stats");

        return stats;
    }
}
