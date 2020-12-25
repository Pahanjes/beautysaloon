package ru.pahanjes.beautysaloon.crm.UI.views.dashboard;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import ru.pahanjes.beautysaloon.crm.backend.entity.Customer;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
import ru.pahanjes.beautysaloon.crm.backend.service.CustomerService;
import ru.pahanjes.beautysaloon.crm.backend.service.EmployeeService;

import java.util.List;

@Route(value = "lk/dashboard")
@PageTitle("Расписание | BS")
public class DashBoardView extends VerticalLayout {

    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private List<Customer> customerList;
    private Grid<Customer> timetable;

    public DashBoardView(CustomerService customerService, EmployeeService employeeService){
        this.customerService = customerService;
        this.employeeService = employeeService;
        setSizeFull();
        addClassName("dashboard-view");
        timetable = new Grid<>(Customer.class);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        configureList();
        configureGrid();
        updateTimetable();
        add(
                getCustomerStats(),
                timetable
        );
    }

    private void configureList() {
        customerList = VaadinSession.getCurrent().getAttribute(User.class).getEmployee().getClients();
    }

    private void configureGrid() {
        timetable.addClassName("timetable");
        timetable.setSizeFull();
        timetable.removeAllColumns();
        timetable.addColumn(customer -> customer.getTimetable().getYear() == 2000 ? "Не установлена" : customer.getTimetable().getYear() + "/" + customer.getTimetable().getMonthValue() + "/" + customer.getTimetable().getDayOfMonth()).setHeader("Дата").setSortable(true);
        timetable.addColumn(customer -> customer.getTimetable().getYear() == 2000 ? "Не установлено" : customer.getTimetable().getHour() + ":" + customer.getTimetable().getMinute()).setHeader("Время").setSortable(true);
        timetable.addColumn(customer -> customer.getLastName() == null ? "-" : customer.getLastName()).setHeader("Фамилия").setSortable(true);
        timetable.addColumn(customer -> customer.getFirstName() == null ? "-" : customer.getFirstName()).setHeader("Имя").setSortable(true);
        timetable.addColumn(customer -> customer.getStatus() == null ? "-" : customer.getStatus().getValue()).setHeader("Статус").setSortable(true);
        timetable.addColumn(customer -> customer.getPhoneNumber() == null ? "-" : customer.getPhoneNumber()).setHeader("Номер телефона").setSortable(true);
        timetable.getColumns().forEach(customerColumn -> customerColumn.setAutoWidth(true));
    }

    private Span getCustomerStats() {
        Span stats = new Span(VaadinSession.getCurrent().getAttribute(User.class).getEmployee().getClients().size() + " клиентов");
        stats.addClassName("customer-stats");
        return stats;
    }

    private void updateTimetable(){
        timetable.setItems(customerList);
    }
}
