/**
 * 
 */
package com.jae.eclipse.ui.example;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.jae.eclipse.ui.impl.AbstractWizard;
import com.jae.eclipse.ui.impl.ControlFactoryWizardPage;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings("rawtypes")
public class ExampleWizard extends AbstractWizard {
	private Map[] input1 = new Map[2];
	private Map[] input2 = new Map[2];
	
	@Override
	public void addPages() {
		ExampleTableControlFactory factory1 = createFactory(input1,1);
		ExampleTableControlFactory factory2 = createFactory(input2,2);
		
		this.addPage(new ControlFactoryWizardPage("abc", factory1));
		this.addPage(new ControlFactoryWizardPage("cba", factory2));
	}

	private ExampleTableControlFactory createFactory(Map[] input, int index) {
		ExampleTableControlFactory factory = new ExampleTableControlFactory();
		factory.setTitle("title");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("aa", "1");
		map.put("bb", "2");
		map.put("cc", "3");
		
		
		input[0]=map;
		
		map = new HashMap<String, String>();
		map.put("aa", "11");
		map.put("bb", "22");
		map.put("cc", "33");
		
		input[1]=map;
		
		factory.setValue(input);
		factory.getUIDescription().setWinTitle("winTitle"+index);
		factory.getUIDescription().setTitle("Title"+index);
		factory.getUIDescription().setDescription("description"+index);
		return factory;
	}
	
	@Override
	protected void doFinish(IProgressMonitor monitor) {
		System.out.println("dofinish");
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		
		Shell shell = new Shell(display);
		
		shell.setText("UI Example");
		shell.setSize(500, 500);
		shell.setLocation(300, 200);
		
		shell.setLayout(new FillLayout());
		
		createUI(shell);
		
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
	}

	protected static Control createUI(Composite parent) {
		Button button = new Button(parent, SWT.NONE);
		button.setText("...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createDialog(e.display.getActiveShell());
			}
		});
		
		return button;
	}
	
	private static void createDialog(Shell shell) {
		ExampleWizard newWizard = new ExampleWizard();
		WizardDialog dialog = new WizardDialog(shell, newWizard);
		dialog.open();
		
		for (Map map : newWizard.input1) {
			System.out.println("*******************");
			System.out.println("aa="+map.get("aa"));
			System.out.println("bb="+map.get("bb"));
			System.out.println("cc="+map.get("cc"));
		}
		
		for (Map map : newWizard.input2) {
			System.out.println("*******************");
			System.out.println("aa="+map.get("aa"));
			System.out.println("bb="+map.get("bb"));
			System.out.println("cc="+map.get("cc"));
		}
	}
}
