package ee.mart.ui;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = MainView.VIEW_NAME)
public class MainView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "";
	
	@PostConstruct
	void init() {
		setMargin(true);
	    setSpacing(true);
	    addComponent(new Label("This is a main view everyone can see it"));
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		//View is constructed in the init()
		
	}

}
