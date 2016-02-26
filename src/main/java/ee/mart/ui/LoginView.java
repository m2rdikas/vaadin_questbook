package ee.mart.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

@PrototypeScope
@SpringComponent
public class LoginView extends CustomComponent{
	
	private final EventBus.SessionEventBus eventBus;
	private final VaadinSecurity vaadinSecurity;
	
	private TextField userName;

	private PasswordField passwordField;

	private Button login;
	
	private Label loginFailedLabel;

    @Autowired
    public LoginView(VaadinSecurity vaadinSecurity, EventBus.SessionEventBus eventBus) {
        this.vaadinSecurity = vaadinSecurity;
        this.eventBus = eventBus;
        initLayout();
    }
    
	public void initLayout() {
		FormLayout loginForm = new FormLayout();
        loginForm.setSizeUndefined();

        loginForm.addComponent(userName = new TextField("Username"));
        loginForm.addComponent(passwordField = new PasswordField("Password"));
        loginForm.addComponent(login = new Button("Login"));
        login.addStyleName(Runo.BUTTON_DEFAULT);
        login.setDisableOnClick(true);
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                login();
            }
        });

        VerticalLayout loginLayout = new VerticalLayout();
        loginLayout.setSizeUndefined();

        loginLayout.addComponent(loginFailedLabel = new Label());
        loginLayout.setComponentAlignment(loginFailedLabel, Alignment.BOTTOM_CENTER);
        loginFailedLabel.setSizeUndefined();
        loginFailedLabel.addStyleName(Runo.LABEL_H2);
        loginFailedLabel.setVisible(false);

        loginLayout.addComponent(loginForm);
        loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

        VerticalLayout rootLayout = new VerticalLayout(loginLayout);
        rootLayout.setSizeFull();
        rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
        setCompositionRoot(rootLayout);
        setSizeFull();
		
	}

	private void login() {
        try {
            String password = passwordField.getValue();
            passwordField.setValue("");

            final Authentication authentication = vaadinSecurity.login(userName.getValue(), password);
            eventBus.publish(this, new SuccessfulLoginEvent(getUI(), authentication));
        } catch (AuthenticationException ex) {
            userName.focus();
            userName.selectAll();
            loginFailedLabel.setValue(String.format("Login failed: %s", ex.getMessage()));
            loginFailedLabel.setVisible(true);
        } catch (Exception ex) {
            Notification.show("An unexpected error occurred", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        } finally {
            login.setEnabled(true);
        }
    }

}
