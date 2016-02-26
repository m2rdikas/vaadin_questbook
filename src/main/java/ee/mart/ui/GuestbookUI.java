package ee.mart.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;

@SpringUI
@Theme("runo")
@Push
public class GuestbookUI extends UI{
	private static final long serialVersionUID = 1L;
	
	@Autowired
    public SpringViewProvider viewProvider;
	
	@Autowired
	VaadinSecurity vaadinSecurity;
	
	@Autowired
	LoginView loginView;
	
	@Autowired
    EventBus.SessionEventBus eventBus;
	
	
	@Override
    public void attach() {
        super.attach();
        eventBus.subscribe(this);
    }

    @Override
    public void detach() {
        eventBus.unsubscribe(this);
        super.detach();
    }
    
    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(Runo.BUTTON_SMALL);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

	@Override
	protected void init(VaadinRequest request) {
		if (vaadinSecurity.isAuthenticated()) {
			initMainScreen();
		} else {
			setContent(loginView);
		}
    }
	
	
	@EventBusListenerMethod
    void onLogin(SuccessfulLoginEvent loginEvent) {
        if (loginEvent.getSource().equals(this)) {
            access(new Runnable() {
                @Override
                public void run() {
                	initMainScreen();
                }
            });
        } else {
            getPage().reload();
        }
    }
	
	private void initMainScreen() {
		final HorizontalLayout root = new HorizontalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addComponent(createNavigationButton("Main view",
                MainView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Guestbook view",
                GuestbookView.VIEW_NAME));
        if(vaadinSecurity.hasAuthority("ROLE_ADMIN")){
        	navigationBar.addComponent(createNavigationButton("Admin", AdminView.VIEW_NAME));
        }
        Button logoutButton = new Button("Logout",
                new Button.ClickListener() {
                    // inline click-listener
					@Override
                    public void buttonClick(ClickEvent event) {
						vaadinSecurity.logout();
                    }
                });
        
        root.addComponent(logoutButton);
        root.addComponent(navigationBar);

        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0f);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);
	}
}
