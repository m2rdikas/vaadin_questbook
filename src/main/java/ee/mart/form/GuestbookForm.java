package ee.mart.form;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Runo;

import ee.mart.model.GuestEntry;
import ee.mart.service.GuestbookService;
import ee.mart.ui.GuestbookUI;
import ee.mart.ui.GuestbookView;

@SpringComponent
@UIScope
public class GuestbookForm extends FormLayout {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	GuestbookService service;
	
	Button save = new Button("Save", this::save);
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextArea text = new TextArea("Text");
    
    GuestEntry guestEntry;
    
    BeanFieldGroup<GuestEntry> formFieldBindings;
    
    View view;
    
    public GuestbookForm() {
        configureComponents();
        buildLayout();
    }
    
    private void configureComponents() {
    	firstName.setNullRepresentation("");
    	lastName.setNullRepresentation("");
    	text.setNullRepresentation("");
        save.setStyleName(Runo.BUTTON_DEFAULT);
    	save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    	setVisible(false);
    }
    
    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save);
        actions.setSpacing(true);
        
        addComponents(actions, firstName, lastName, text);
    }
    
    public void save(Button.ClickEvent event) {
       try {
		formFieldBindings.commit();
		service.save(guestEntry);
		((GuestbookView) view).refreshData();
		((GuestbookView) view).closeWindow();
       } catch (CommitException e) {
    	   e.printStackTrace();
       }
    }
    
    public void delete(Button.ClickEvent event) {
    	service.delete(guestEntry);
    }
    
    public void edit(GuestEntry guestEntry, View view) {
        this.guestEntry = guestEntry;
        this.view = view;
        if(guestEntry != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(guestEntry, this);
            firstName.focus();
        }
        setVisible(guestEntry != null);
    }
    
    @Override
    public GuestbookUI getUI() {
        return (GuestbookUI) super.getUI();
    }

}
