/**
 * 
 */
package com.jae.eclipse.ui.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.EventListener;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TypedListener;

/**
 * @author hongshuiqiao
 *
 */
public class DetailMessageDialog extends MessageDialog implements SelectionListener {
	private static final int MINIMUM_HEIGHT = 100;
	private String detailMessage;
	private boolean showDetail = false;
	private StyledText text;
	private int detailButtonIndex = -1;

	public DetailMessageDialog(Shell parentShell, String dialogTitle,
			Image dialogTitleImage, String dialogMessage, String detailMessage, int dialogImageType,
			String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
				dialogImageType, dialogButtonLabels, defaultIndex);
		this.detailMessage = detailMessage;
	}

	protected void toggleDetailsArea(){
		Composite parent = (Composite) getContents();
		Button detailButton = getButton(this.detailButtonIndex);
		boolean opened = false;
		Shell shell = parent.getShell();
		Point windowSize = shell.getSize();
		if(this.showDetail){
			text.dispose();
			detailButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
			this.showDetail = false;
			opened = false;
		}else{
			text = createDetail(parent);
			detailButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
			text.setText(detailMessage);
			this.showDetail = true;
			opened = true;
			shell.layout();
		}
		
		Point newSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int diffY = newSize.y - windowSize.y;
		// increase the dialog height if details were opened and such increase is necessary
		// decrease the dialog height if details were closed and empty space appeared
		if ((opened && diffY > 0) || (!opened && diffY < 0)) {
			shell.setSize(new Point(windowSize.x, windowSize.y + (diffY)));
		}
	}
	
	private StyledText createDetail(Composite parent) {
		StyledText text = new StyledText(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI
				| SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		text.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData gd = new GridData(GridData.FILL_BOTH);
//		gd.grabExcessHorizontalSpace = true;
//		gd.grabExcessVerticalSpace = true;
//		gd.widthHint = 250;
		gd.horizontalSpan = 2;
		gd.minimumHeight = MINIMUM_HEIGHT;
		gd.heightHint = 2*MINIMUM_HEIGHT;
		text.setLayoutData(gd);
		return text;
	}

	/**
	 * Convenience method to open a simple dialog as specified by the
	 * <code>kind</code> flag.
	 * 
	 * @param kind
	 *            the kind of dialog to open, one of {@link #ERROR},
	 *            {@link #INFORMATION}, {@link #QUESTION}, {@link #WARNING},
	 *            {@link #CONFIRM}, or {@link #QUESTION_WITH_CANCEL}.
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none
	 * @param title
	 *            the dialog's title, or <code>null</code> if none
	 * @param message
	 *            the message
	 * @param detailMessage
	 *            the detailMessage
	 * @param style
	 *            {@link SWT#NONE} for a default dialog, or {@link SWT#SHEET} for
	 *            a dialog with sheet behavior
	 * @return <code>true</code> if the user presses the OK or Yes button,
	 *         <code>false</code> otherwise
	 * @since 3.5
	 */
	public static boolean open(int kind, Shell parent, String title, String message, String detailMessage, int style) {
		String[] labels = getButtonLabels(kind);
		int index = labels.length;
		String[] buttonLabels = new String[index+1];
		System.arraycopy(labels, 0, buttonLabels, 0, index);
		buttonLabels[index]=IDialogConstants.SHOW_DETAILS_LABEL;
		
		DetailMessageDialog dialog = new DetailMessageDialog(parent, title, null, message, detailMessage, kind, buttonLabels, 0);
		style &= SWT.SHEET;
		dialog.setShellStyle(dialog.getShellStyle() | style);
		
		dialog.detailButtonIndex = index;
		
		return dialog.open() == 0;
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control composite = super.createContents(parent);
		
		Button detailButton = getButton(this.detailButtonIndex);
		
		Listener[] listeners = detailButton.getListeners(SWT.Selection);
		for (Listener listener : listeners) {
			if (listener instanceof TypedListener) {
				EventListener eventListener = ((TypedListener) listener).getEventListener();
				if (eventListener instanceof SelectionListener) {
					detailButton.removeSelectionListener((SelectionListener) eventListener);
				}
			}
		}
		
		detailButton.addSelectionListener(this);
		return composite;
	}
	
	/**
	 * @param kind
	 * @return
	 */
	static String[] getButtonLabels(int kind) {
		String[] dialogButtonLabels;
		switch (kind) {
		case ERROR:
		case INFORMATION:
		case WARNING: {
			dialogButtonLabels = new String[] { IDialogConstants.OK_LABEL };
			break;
		}
		case CONFIRM: {
			dialogButtonLabels = new String[] { IDialogConstants.OK_LABEL,
					IDialogConstants.CANCEL_LABEL };
			break;
		}
		case QUESTION: {
			dialogButtonLabels = new String[] { IDialogConstants.YES_LABEL,
					IDialogConstants.NO_LABEL };
			break;
		}
		case QUESTION_WITH_CANCEL: {
			dialogButtonLabels = new String[] { IDialogConstants.YES_LABEL,
                    IDialogConstants.NO_LABEL,
                    IDialogConstants.CANCEL_LABEL };
			break;
		}
		default: {
			throw new IllegalArgumentException(
					"Illegal value for kind in MessageDialog.open()"); //$NON-NLS-1$
		}
		}
		return dialogButtonLabels;
	}
	
	/**
     * Convenience method to open a simple confirm (OK/Cancel) dialog.
     * 
     * @param parent
     *            the parent shell of the dialog, or <code>null</code> if none
     * @param title
     *            the dialog's title, or <code>null</code> if none
     * @param message
     *            the message
	 * @param detailMessage
	 *            the detailMessage
     * @return <code>true</code> if the user presses the OK button,
     *         <code>false</code> otherwise
     */
    public static boolean openConfirm(Shell parent, String title, String message, String detailMessage) {
        return open(CONFIRM, parent, title, message, detailMessage, SWT.NONE);
    }

    /**
     * Convenience method to open a standard error dialog.
     * 
     * @param parent
     *            the parent shell of the dialog, or <code>null</code> if none
     * @param title
     *            the dialog's title, or <code>null</code> if none
     * @param message
     *            the message
	 * @param detailMessage
	 *            the detailMessage
     */
    public static void openError(Shell parent, String title, String message, String detailMessage) {
        open(ERROR, parent, title, message, detailMessage, SWT.NONE);
    }
    
    public static void openError(Shell parent, String title, String message, Throwable throwable) {
        open(ERROR, parent, title, message, generateDetailMessage(throwable), SWT.NONE);
    }

    protected static String generateDetailMessage(Throwable throwable) {
		StringWriter writer = new StringWriter();
		if(null != throwable){
			throwable.printStackTrace(new PrintWriter(writer));
			return writer.toString();
		}
		return "";
	}

	/**
     * Convenience method to open a standard information dialog.
     * 
     * @param parent
     *            the parent shell of the dialog, or <code>null</code> if none
     * @param title
     *            the dialog's title, or <code>null</code> if none
     * @param message
     *            the message
	 * @param detailMessage
	 *            the detailMessage
     */
    public static void openInformation(Shell parent, String title, String message, String detailMessage) {
        open(INFORMATION, parent, title, message, detailMessage, SWT.NONE);
    }

    /**
     * Convenience method to open a simple Yes/No question dialog.
     * 
     * @param parent
     *            the parent shell of the dialog, or <code>null</code> if none
     * @param title
     *            the dialog's title, or <code>null</code> if none
     * @param message
     *            the message
	 * @param detailMessage
	 *            the detailMessage
     * @return <code>true</code> if the user presses the Yes button,
     *         <code>false</code> otherwise
     */
    public static boolean openQuestion(Shell parent, String title, String message, String detailMessage) {
        return open(QUESTION, parent, title, message, detailMessage, SWT.NONE);
    }

    /**
     * Convenience method to open a standard warning dialog.
     * 
     * @param parent
     *            the parent shell of the dialog, or <code>null</code> if none
     * @param title
     *            the dialog's title, or <code>null</code> if none
     * @param message
     *            the message
	 * @param detailMessage
	 *            the detailMessage
     */
    public static void openWarning(Shell parent, String title, String message, String detailMessage) {
        open(WARNING, parent, title, message, detailMessage, SWT.NONE);
    }

	@Override
	public void widgetSelected(SelectionEvent event) {
		toggleDetailsArea();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent event) {
		widgetSelected(event);
	}
}
