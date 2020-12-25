package ru.pahanjes.beautysaloon.crm.UI.views.service;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.pahanjes.beautysaloon.crm.backend.entity.Service;
import ru.pahanjes.beautysaloon.crm.backend.service.ServiceService;

@Route(value = "lk/servicelist")
@PageTitle("Список услуг | BS")
@CssImport("./styles/views/service-view.css")
public class ServiceView extends VerticalLayout {
    private final ServiceService serviceService;
    private ServiceForm serviceForm;
    private Button addServiceButton;
    private Grid<Service> serviceGrid = new Grid<>(Service.class);;

    public ServiceView(ServiceService serviceService) {
        this.serviceService = serviceService;
        addClassName("service-view");
        setSizeFull();
        addServiceButton = new Button("Добавить");
        addServiceButton.addClickListener(click -> addService());
        serviceForm = new ServiceForm();
        serviceForm.addListener(ServiceForm.SaveEvent.class, this::saveService);
        serviceForm.addListener(ServiceForm.DeleteEvent.class, this::deleteService);
        serviceForm.addListener(ServiceForm.CloseEvent.class, closeEvent -> closeServiceEditor());
        configureGrid();
        Div content = new Div(serviceGrid, serviceForm);
        content.addClassName("service-content");
        content.setSizeFull();//////
        add(
                new HorizontalLayout(addServiceButton),
                content
        );
        updateGrid();
        closeServiceEditor();
    }

    private void addService() {
        serviceGrid.asSingleSelect().clear();
        editService(new Service());

    }

    private void saveService(ServiceForm.SaveEvent saveEvent) {
        serviceService.save(saveEvent.getService());
        updateGrid();
        closeServiceEditor();
    }

    private void deleteService(ServiceForm.DeleteEvent deleteEvent) {
        serviceService.delete(deleteEvent.getService());
        updateGrid();
        closeServiceEditor();
    }

    private void editService(Service service) {
        if(service == null) {
            closeServiceEditor();
        } else {
            serviceForm.setService(service);
            serviceForm.setVisible(true);
            addClassName("service-editing");
        }
    }

    private void closeServiceEditor() {
        serviceForm.setService(null);
        serviceForm.setVisible(false);
        removeClassName("service-editing");
    }

    private void configureGrid() {
        serviceGrid.setSizeFull();
        serviceGrid.addClassName("service-grid");
        serviceGrid.removeAllColumns();
        serviceGrid.addColumn(service -> service.getService() == null ? "-" : service.getService()).setHeader("Наименование").setSortable(true);
        serviceGrid.addColumn(service -> service.getPrice() == null ? "-" : service.getPrice()).setHeader("Стоимость").setSortable(true);
        serviceGrid.getColumns().forEach(serviceColumn -> serviceColumn.setAutoWidth(true));
        serviceGrid.asSingleSelect().addValueChangeListener(event -> editService(event.getValue()));
    }

    private void updateGrid() {
        serviceGrid.setItems(serviceService.findAll());
    }
}
