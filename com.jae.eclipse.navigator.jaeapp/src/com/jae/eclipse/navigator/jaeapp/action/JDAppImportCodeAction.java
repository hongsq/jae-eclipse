/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.lang.reflect.Field;
import java.util.Set;

import org.eclipse.egit.ui.internal.clone.GitImportWizard;
import org.eclipse.egit.ui.internal.clone.GitSelectRepositoryPage;
import org.eclipse.egit.ui.internal.components.RepositorySelectionPage;
import org.eclipse.egit.ui.internal.repository.tree.RepositoryNode;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import com.jae.eclipse.core.util.ObjectUtil;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings("restriction")
public class JDAppImportCodeAction extends AbstractJDAction implements IPageChangedListener {

	public JDAppImportCodeAction(ISelectionProvider provider, String text) {
		super(provider, text);
		
		this.setId("jdapp.action.import");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("jdapp.action.import"));
		this.setMustSelect(true);
		this.setMultiable(false);
		this.setSelectType(JDApp.class);
		this.setEnabled(false);
	}

	@Override
	public void run() {
		GitImportWizard wizard = new GitImportWizard();
		
		wizard.init(PlatformUI.getWorkbench(), this.getStructuredSelection());
		
		StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		Shell shell = viewer.getControl().getShell();
		
		WizardDialog dialog = new WizardDialog(shell, wizard);
		
		dialog.addPageChangedListener(this);
		
		if(Window.OK == dialog.open()){
			
		}
	}

	@Override
	public void pageChanged(PageChangedEvent event) {
		IWizardPage page = (IWizardPage) event.getSelectedPage();
		
		JDApp app = (JDApp) this.getStructuredSelection().getFirstElement();
		if(page instanceof GitSelectRepositoryPage){
			GitSelectRepositoryPage gitPage = (GitSelectRepositoryPage) page;
			
			Tree tree = getRepositoryTree((Composite)gitPage.getControl());
			TreeItem[] items = tree.getItems();
			for (TreeItem treeItem : items) {
				RepositoryNode node = (RepositoryNode) treeItem.getData();

				Repository repository = node.getRepository();
				
				Set<String> remotes = repository.getConfig().getSubsections("remote");
				
				for (String remote : remotes) {
					String url =  repository.getConfig().getString("remote", remote, "url");
					if(null != url && url.equals(app.getRepositoryURL())){
						tree.select(treeItem);
						return;
					}
				}
			}
		}else if (page instanceof RepositorySelectionPage) {
			RepositorySelectionPage repPage = (RepositorySelectionPage) page;
			
			try {
				Field uriField = ObjectUtil.getField(repPage.getClass(), "uriText", false);
				uriField.setAccessible(true);
				Text uriText = (Text) uriField.get(repPage);
				
				uriText.setText(app.getRepositoryURL());
				
				Field userField = ObjectUtil.getField(repPage.getClass(), "userText", false);
				userField.setAccessible(true);
				Text userText = (Text) userField.get(repPage);
				userText.setText(app.getParent().getName());
				return;
			} catch (Exception e) {
			}
		}
	}

	private Tree getRepositoryTree(Composite composite) {
		Control[] children = composite.getChildren();
		for (Control control : children) {
			if(control instanceof Tree)
				return (Tree) control;
			
			if (control instanceof Composite) {
				Tree tree = getRepositoryTree((Composite) control);
				if(null != tree)
					return tree;
			}
		}
		return null;
	}
}
