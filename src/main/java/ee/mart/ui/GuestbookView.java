package ee.mart.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;

import ee.mart.form.GuestbookForm;
import ee.mart.model.GuestEntry;
import ee.mart.service.GuestbookService;

@SpringView(name = GuestbookView.VIEW_NAME)
public class GuestbookView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "guestbook";
	private static final String ADMIN_ROLE = "ROLE_ADMIN";
	
	@Autowired
	public GuestbookService service;
	
	@Autowired
	public VaadinSecurity vaadinSecurity;
	
	@Autowired
	GuestbookForm guestbookForm;
	
	Window guestbookWindow;
	Button newEntryButton;
	Grid entryList = new Grid();
	
	@PostConstruct
	void init() {
		configureComponents();
		buildLayout();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		//View is constructed in the init()
		
	}
	
	private void configureComponents() {
		guestbookWindow = new Window("New guestbook entry");
		guestbookWindow.setWidth("300px");
		guestbookWindow.setHeight("300px");
		guestbookWindow.setModal(true);
		guestbookWindow.setContent(guestbookForm);
		
		newEntryButton = new Button("Add new entry",
                new Button.ClickListener() {
                    // inline click-listener
					@Override
                    public void buttonClick(ClickEvent event) {
                        if (guestbookWindow.getParent() == null) {
                            // Open the subwindow by adding it to the parent
                            // window
                            getUI().addWindow(guestbookWindow);
                        }

                        // Center the window
                        guestbookWindow.center();
                        guestbookForm.edit(new GuestEntry(), GuestbookView.this);
                    }
                });
		newEntryButton.setStyleName(Runo.BUTTON_BIG);
		BeanItemContainer<GuestEntry> dataSource = new BeanItemContainer<>(GuestEntry.class);
		entryList.setContainerDataSource(dataSource);
		entryList.setColumnOrder("firstName", "lastName", "text");
		entryList.removeColumn("id");
		if(vaadinSecurity.hasAuthority(ADMIN_ROLE)) {
			entryList.addSelectionListener(new SelectionListener() {
				
				@Override
				public void select(SelectionEvent event) {
					if (guestbookWindow.getParent() == null) {
                        // Open the subwindow by adding it to the parent
                        // window
                        getUI().addWindow(guestbookWindow);
                    }
					 // Center the window
                    guestbookWindow.center();
                    guestbookForm.edit((GuestEntry) entryList.getSelectedRow(), GuestbookView.this);
				}
			});
		}
		refreshData();
	}
	
	public void refreshData() {
		entryList.setContainerDataSource(new BeanItemContainer<>(
                GuestEntry.class, service.findAllEntries()));
	}
	
	public void closeWindow() {
		getUI().removeWindow(guestbookWindow);
	}
	
	private void buildLayout() {
		HorizontalLayout actions = new HorizontalLayout(newEntryButton);
        actions.setWidth("100%");
        
        VerticalLayout layout = new VerticalLayout(actions, entryList);
        layout.setSpacing(true);
        layout.setSizeFull();
        entryList.setSizeFull();
        layout.setExpandRatio(entryList, 1);

        addComponent(layout);
	}

}
