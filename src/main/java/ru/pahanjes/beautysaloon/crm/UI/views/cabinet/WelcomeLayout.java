package ru.pahanjes.beautysaloon.crm.UI.views.cabinet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;

import java.time.LocalDateTime;

@Route(value = "lk/welcome")
@PageTitle("Добро пожаловать | BS CRM")
public class WelcomeLayout extends VerticalLayout {

    /*@CssImport("./styles/divs/news-block.css")*/
    private static class NewsBlock extends Div {
        private final String datetime;
        private final String news;

        public NewsBlock(String datetime, String news) {
            this.datetime = datetime;
            this.news = news;

            /*setId("news-block");*/

            /*getStyle().set("background-color", "white");*/
            getStyle().set("border", " black");

            add(getNewsBlock());
        }

        private Component getNewsBlock() {
            VerticalLayout layout = new VerticalLayout();
            layout.add(new Span(datetime));
            layout.add(new Paragraph(news));

            return layout;
        }
    }

    public WelcomeLayout() {
        add(
                createHeaderLayout(),
                createNewsLayout()
        );
    }

    private Component createHeaderLayout(){
        H2 header; /*= new H2("Добро пожаловать, " + VaadinSession.getCurrent().getAttribute(User.class).getEmployee().getFirstName());*/
        if(VaadinSession.getCurrent().getAttribute(User.class).getEmployee() == null) {
            header = new H2("Добро пожаловать");
        } else {
            header = new H2("Добро пожаловать, " + VaadinSession.getCurrent().getAttribute(User.class).getEmployee().getFirstName());
        }
        HorizontalLayout headerLayout = new HorizontalLayout(header);
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return  headerLayout;
    }

    private Component createNewsLayout() {
        VerticalLayout newsLayout = new VerticalLayout();
        newsLayout.setSizeFull();
        newsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        HorizontalLayout newsBlock = new HorizontalLayout();
        newsBlock.setWidthFull();
        newsBlock.setJustifyContentMode(JustifyContentMode.CENTER);
        newsBlock.add(
                new NewsBlock(LocalDateTime.now().getYear() + "/" + LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getDayOfMonth(), "Нашей компанией было достигнуто важное деловое соглашение."),
                new NewsBlock(LocalDateTime.now().getYear() + "/" + LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getDayOfMonth(), "Что-то там было сделано, где-то что-то было подписано.")
        );
        newsLayout.add(
                newsBlock
        );

        return newsLayout;
    }
}
