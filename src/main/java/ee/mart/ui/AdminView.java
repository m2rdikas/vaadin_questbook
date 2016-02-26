package ee.mart.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import ee.mart.model.GuestEntry;
import ee.mart.service.GuestbookService;

import com.vaadin.ui.Grid;

@Secured("ROLE_ADMIN")
@SpringView(name = AdminView.VIEW_NAME)
public class AdminView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "admin";
	
	@Autowired
	GuestbookService service;
	
	TextField firstName = new TextField("First name");
	TextField lastName = new TextField("Last name");
	Button searchButton = new Button("Search");
	Grid resultList = new Grid();
	
	@PostConstruct
	void init() {
		setMargin(true);
	    setSpacing(true);
	    addComponent(new Label("This is a admin view, that only an admin can see"));
	    addComponent(new Label("You can search for entries by first name and/or last name"));
	    searchButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doSearch();
				
			}
		});
	    resultList.setSizeFull();
	    addComponents(firstName, lastName, searchButton, resultList);
	}
	
	private void doSearch() {
		resultList.setContainerDataSource(new BeanItemContainer<>(
                GuestEntry.class, service.filterEntries(firstName.getValue(), lastName.getValue())));
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		//View is constructed in the init()
		
	}

}
