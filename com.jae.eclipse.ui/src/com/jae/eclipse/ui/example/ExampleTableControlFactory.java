/**
 * 
 */
package com.jae.eclipse.ui.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IValidator;
import com.jae.eclipse.ui.factory.table.AbstractTableFactory;
import com.jae.eclipse.ui.factory.table.ColumnModel;
import com.jae.eclipse.ui.factory.table.DefaultTableValueTranslator;
import com.jae.eclipse.ui.factory.table.ITableValueTranslator;
import com.jae.eclipse.ui.impl.ControlFactoryDialog;
import com.jae.eclipse.ui.validator.ColumnValidator;
import com.jae.eclipse.ui.validator.NotEmptyValidator;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings({ "rawtypes" })
public class ExampleTableControlFactory extends AbstractTableFactory {
	private ITableValueTranslator translator = new DefaultTableValueTranslator(this);
	
	@Override
	public ITableValueTranslator getValueTranslator() {
		return this.translator;
	}
	
	@Override
	protected ColumnModel[] createColumns(TableViewer viewer) {
		List<ColumnModel> columns = new ArrayList<ColumnModel>();
		
		{
			ColumnModel column = new ColumnModel();
			column.setPropertyName("aa");
			column.setTitle("aa");
			column.setWidth(200);
			column.setResizable(true);
			column.setEditable(true);
			column.setDescription("aaaaaaaaaaaaaa");
			column.setSortable(true);
//			column.setAlignment(SWT.RIGHT);
			column.addValidator(new ColumnValidator(new NotEmptyValidator("aa")));
			column.addValidator(new ColumnValidator(new IValidator() {
				
				public boolean validate(IMessageCaller messageCaller, Object source, Object value) {
					if (value instanceof String) {
						if(((String) value).startsWith("aa"))
							messageCaller.warn("不能以aa开头");

						if(((String) value).equals("aaa")){
							messageCaller.error("不能为aaa");
						}
						
						if(((String) value).endsWith("aa")){
							messageCaller.warn("不能以aa结尾");
						}
						
						if(((String) value).equals("aaa"))
							return false;
					}
					return true;
				}
			}));
			column.setCellEditor(new TextCellEditor(viewer.getTable(), SWT.BORDER));
			columns.add(column);
		}
		{
			ColumnModel column = new ColumnModel();
			column.setPropertyName("bb");
			column.setTitle("bb");
			column.setWidth(200);
			column.setSortable(true);
//			column.setAlignment(SWT.LEFT);
			columns.add(column);
		}
		
		return columns.toArray(new ColumnModel[columns.size()]);
	}

	@Override
	protected void fillActionToolBars(IToolBarManager toolBarManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void fillContextMenu(IMenuManager manager) {
		// TODO Auto-generated method stub
		
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
	
	private static void createDialog(Shell shell) {
		ExampleTableControlFactory factory = createFactory();
		ControlFactoryDialog dialog = new ControlFactoryDialog(shell, factory);
		dialog.open();
		
		Map[] list = (Map[]) factory.getValue();
		for (Map map : list) {
			System.out.println("*******************");
			System.out.println("aa="+map.get("aa"));
			System.out.println("bb="+map.get("bb"));
			System.out.println("cc="+map.get("cc"));
		}
	}

	protected static Control createUI(Composite parent) {
//		ExampleTableControlFactory factory = createFactory();
//		factory.createControl(parent);
//		return factory.getControl();
		
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

	private static ExampleTableControlFactory createFactory() {
		ExampleTableControlFactory factory = new ExampleTableControlFactory();
		factory.setTitle("title");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("aa", "1");
		map.put("bb", "2");
		map.put("cc", "3");
		
		Map[] input = new Map[2];
		input[0]=map;
		
		map = new HashMap<String, String>();
		map.put("aa", "11");
		map.put("bb", "22");
		map.put("cc", "33");
		
		input[1]=map;
		
		factory.setValue(input);
		return factory;
	}
}
