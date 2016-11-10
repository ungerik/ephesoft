/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.gxt.core.client.ui.widget;

import java.util.ArrayList;
import java.util.List;

import org.moxieapps.gwt.uploader.client.Stats;
import org.moxieapps.gwt.uploader.client.Uploader;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadProgressEvent;
import org.moxieapps.gwt.uploader.client.events.UploadProgressHandler;
import org.moxieapps.gwt.uploader.client.events.UploadStartEvent;
import org.moxieapps.gwt.uploader.client.events.UploadStartHandler;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessEvent;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessHandler;

import com.ephesoft.gxt.core.client.i18n.LocaleConstants;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ProgressBar;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class MultiFileUploader extends Composite {

	@UiField(provided = true)
	protected Uploader uploader;

	@UiField
	protected Label orLabel;

	@UiField
	protected Label dragDropLabel;

	@UiField
	protected SimpleContainer parentContainer;

	@UiField
	protected VerticalLayoutContainer uploaderContainer;

	private boolean showProgress;

	private Timer layoutTimer;

	private ProgressBar uploadProgress;

	private static int FONT_SIZE = 12;

	protected UploadCompleteHandler uploadCompleteHandler;
	protected UploadStartHandler uploadStartHandler;
	protected UploadProgressHandler uploadProgressHandler;
	protected FileDialogCompleteHandler fileDialogCompleteHandler;
	protected UploadSuccessHandler uploadSuccessHandler;
	protected UploadErrorHandler uploadErrorHandler;
	private Timer timer;
	private static String DRAG_OVER_STYLE = "uploadPanelDragOver";
	private boolean isInitialized = false;

	private List<UploadCompleteHandler> uploadCompleteHandlersList;
	private List<UploadStartHandler> uploadStartHandlersList;
	private List<FileDialogCompleteHandler> fileDialogCompleteHandlersList;
	private List<UploadProgressHandler> uploadProgressHandlersList;
	private List<FileQueuedHandler> fileQueuedHandlersList;
	private List<UploadSuccessHandler> uploadSuccessHandlerList;
	private List<UploadErrorHandler> uploadErrorHandlerList;
	protected FileQueuedHandler fileQueuedHandler;

	private static int UPLOADER_BUTTON_HEIGHT = 20;

	interface Binder extends UiBinder<Widget, MultiFileUploader> {
	}

	private int totalFilesQueued = 0;

	private int totalFilesCompleted = 0;

	private double currentFileUploadRatio = 0;

	private static final Binder binder = GWT.create(Binder.class);

	// variable to hold enable/disable value of drag drop label.
	private boolean enableDragDrop = true;

	public MultiFileUploader() {
		initialize();
		initWidget(binder.createAndBindUi(this));
		uploader.setButtonText(LocaleConstants.SELECT_FILES);
		showProgress = true;
		dragDropLabel.setText(LocaleConstants.DRAG_DROP_FILE_HERE);
		orLabel.setText(LocaleConstants.OR_LABEL);
		uploader.addStyleName("uploaderPanel");
		orLabel.addStyleName("uploadORLabel");
		uploader.setWidth("100%");
		orLabel.setWidth("100%");
		dragDropLabel.addStyleName("dragDropLabel");
		uploadProgress = new ProgressBar();
		uploadProgress.addStyleName("uploadProgressBar");
		this.addStyleName("multiFileUploader");
		uploadCompleteHandlersList = new ArrayList<UploadCompleteHandler>();
		uploadStartHandlersList = new ArrayList<UploadStartHandler>();
		fileDialogCompleteHandlersList = new ArrayList<FileDialogCompleteHandler>();
		uploadProgressHandlersList = new ArrayList<UploadProgressHandler>();
		fileQueuedHandlersList = new ArrayList<FileQueuedHandler>();
		uploadSuccessHandlerList = new ArrayList<UploadSuccessHandler>();
		uploadErrorHandlerList = new ArrayList<UploadErrorHandler>();
		this.initializeUploadStartHandler();
		this.initializeUploadCompleteHandler();
		this.initializeFileDialogCompleteHandler();
		this.initializeUploadProgressHandler();
		this.initialiseUploadSuccessHandler();
		this.initialiseUploadErrorHandler();
		uploader.setUploadStartHandler(uploadStartHandler);
		uploader.setUploadCompleteHandler(uploadCompleteHandler);
		uploader.setUploadProgressHandler(uploadProgressHandler);
		uploader.setFileDialogCompleteHandler(fileDialogCompleteHandler);
		uploader.setUploadSuccessHandler(uploadSuccessHandler);
		uploader.setUploadErrorHandler(uploadErrorHandler);
		WidgetUtil.setID(uploader, "commonMultiFileUploader");
		addDropHandler();

		this.initializeTimer();
		fileQueuedHandler = new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(final FileQueuedEvent fileQueuedEvent) {
				boolean eventResult = true;
				for (FileQueuedHandler fileQueuedHandler : fileQueuedHandlersList) {
					if (null != fileQueuedHandler) {
						eventResult = eventResult && fileQueuedHandler.onFileQueued(fileQueuedEvent);
					}
				}
				return eventResult;
			}
		};
		uploader.setFileQueuedHandler(fileQueuedHandler);
	}

	private void initialize() {
		uploader = new Uploader() {

			@Override
			protected void onLoad() {
				if (!isInitialized) {
					super.onLoad();
				}
			}
		};
	}

	protected void initializeUploadProgressHandler() {
		uploadProgressHandler = new UploadProgressHandler() {

			@Override
			public boolean onUploadProgress(final UploadProgressEvent uploadProgressEvent) {
				boolean eventResult = true;
				long bytesTotal = uploadProgressEvent.getBytesTotal();
				if (bytesTotal > 0) {
					currentFileUploadRatio = (uploadProgressEvent.getBytesComplete() * 1.0) / bytesTotal;
				}
				for (final UploadProgressHandler handler : uploadProgressHandlersList) {
					eventResult = eventResult && handler.onUploadProgress(uploadProgressEvent);
				}
				return eventResult;
			}
		};
	}

	protected void initializeUploadStartHandler() {
		uploadStartHandler = new UploadStartHandler() {

			@Override
			public boolean onUploadStart(final UploadStartEvent uploadStartEvent) {
				boolean eventResult = true;
				for (final UploadStartHandler handler : uploadStartHandlersList) {
					eventResult = eventResult && handler.onUploadStart(uploadStartEvent);
				}
				return eventResult;
			}
		};
	}

	protected void initializeUploadCompleteHandler() {
		uploadCompleteHandler = new UploadCompleteHandler() {

			@Override
			public boolean onUploadComplete(final UploadCompleteEvent uploadCompleteEvent) {
				boolean eventResult = true;
				currentFileUploadRatio = 0;
				int filesQueued = uploader.getStats().getFilesQueued();
				totalFilesCompleted = totalFilesQueued - filesQueued;
				if (filesQueued > 0) {
					uploader.startUpload();
				}
				for (final UploadCompleteHandler handler : uploadCompleteHandlersList) {
					if (null != handler) {
						eventResult = eventResult && handler.onUploadComplete(uploadCompleteEvent);
					}
				}

				if (filesQueued == 0) {
					setUploaderPanel();
					cancelTimer();
				}
				return eventResult;
			}
		};
	}

	protected void initializeFileDialogCompleteHandler() {
		fileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(final FileDialogCompleteEvent fileDialogCompleteEvent) {
				boolean result = true;
				totalFilesQueued = uploader.getStats().getFilesQueued();
				scheduleTimer();
				setProgressBars();
				// if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
				// if (getUploadsInProgress() <= 0) {
				// startUpload();
				// }
				// }
				for (final FileDialogCompleteHandler completeHandler : fileDialogCompleteHandlersList) {
					if (null != completeHandler) {
						result = result && completeHandler.onFileDialogComplete(fileDialogCompleteEvent);
					}
					if (!result) {
						
						//To remove all previously uploaded files.
						for(int i = 0 ; i< totalFilesQueued; i++){
							uploader.cancelUpload();
						}
						setUploaderPanel();
						break;
					}
				}
				return result;
			}
		};
	}

	protected void initialiseUploadSuccessHandler() {
		uploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				boolean eventResult = true;
				for (final UploadSuccessHandler handler : uploadSuccessHandlerList) {
					eventResult = eventResult && handler.onUploadSuccess(uploadSuccessEvent);
				}
				return eventResult;
			}
		};

	}

	protected void initialiseUploadErrorHandler() {
		uploadErrorHandler = new UploadErrorHandler() {

			@Override
			public boolean onUploadError(UploadErrorEvent uploadErrorEvent) {
				boolean eventResult = true;
				for (final UploadErrorHandler handler : uploadErrorHandlerList) {
					eventResult = eventResult && handler.onUploadError(uploadErrorEvent);
				}
				return eventResult;
			}
		};
	}

	@Override
	protected void onAttach() {
		super.onAttach();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		initializeDimensions(200);

		Timer timer = new Timer() {

			@Override
			public void run() {
				layoutTimer.scheduleRepeating(800);
			}
		};
		timer.schedule(5000);
	}

	private void initializeDimensions(final int timeInMillis) {
		final Widget parent = this.getParent();
		layoutTimer = new Timer() {

			@Override
			public void run() {
				setDimension(parent);
			}
		};
		setDimension(parent);
		layoutTimer.schedule(timeInMillis);
	}

	private void setDimension(final Widget parent) {
		if (null != parent) {
			final Element element = dragDropLabel.getElement();
			final Style style = element.getStyle();
			final int height = parent.getOffsetHeight() - UPLOADER_BUTTON_HEIGHT - 2;
			final int heightToAssign = (height / 2) - FONT_SIZE;
			style.setHeight(heightToAssign, Unit.PX);
			style.setFontSize(FONT_SIZE, Unit.PX);
			style.setPaddingTop(heightToAssign, Unit.PX);
			style.setLineHeight(1, Unit.PX);
			dragDropLabel.setWidth("99%");
			style.setLeft(6, Unit.PX);
			isInitialized = true;
			Style uploadProgressStyle = uploadProgress.getElement().getStyle();
			uploadProgressStyle.setMarginTop(height / 2, Unit.PX);
		}
	}

	protected void addDropHandler() {

		dragDropLabel.addDragOverHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				if (isEnableDragDrop()) {
					dragDropLabel.addStyleName(DRAG_OVER_STYLE);
				}
			}
		});

		dragDropLabel.addDragLeaveHandler(new DragLeaveHandler() {

			@Override
			public void onDragLeave(DragLeaveEvent event) {
				if (isEnableDragDrop()) {
					dragDropLabel.removeStyleName(DRAG_OVER_STYLE);
				}
			}
		});
		dragDropLabel.addDropHandler(new DropHandler() {

			@Override
			public void onDrop(DropEvent event) {
				if (isEnableDragDrop()) {
					dragDropLabel.removeStyleName(DRAG_OVER_STYLE);
					final JsArray<JavaScriptObject> droppedFiles = Uploader.getDroppedFiles(event.getNativeEvent()).cast();
					uploader.addFilesToQueue(droppedFiles);
					event.preventDefault();
				}
			}
		});
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		timer.cancel();
	}

	public void addUploadStartHandler(final UploadStartHandler uploadStartHandler) {
		if (null != uploadStartHandler) {
			uploadStartHandlersList.add(uploadStartHandler);
		}
	}

	public void addUploadCompleteHandler(final UploadCompleteHandler uploadCompleteHandler) {
		if (null != uploadCompleteHandler) {
			uploadCompleteHandlersList.add(uploadCompleteHandler);
		}
	}

	private void initializeTimer() {
		timer = new Timer() {

			@Override
			public void run() {
				double filesCompletionPercentage = 0;
				if (totalFilesQueued > 0) {
					filesCompletionPercentage = (totalFilesCompleted * 1.0 + currentFileUploadRatio) / totalFilesQueued;
				}
				uploadProgress.updateProgress(filesCompletionPercentage, "{0}% Completed");
			}
		};
	}

	protected void scheduleTimer() {
		timer.scheduleRepeating(500);
	}

	protected void cancelTimer() {
		timer.cancel();
		totalFilesQueued = 0;
		totalFilesCompleted = 0;
		currentFileUploadRatio = 0;
		uploadProgress.updateProgress(0, "{0}% completed");
	}

	protected void setProgressBars() {
		if (isShowProgress()) {
			parentContainer.remove(uploaderContainer);
			parentContainer.add(uploadProgress);
			parentContainer.addStyleName("parentContainerProgress");
		}
	}

	protected void setUploaderPanel() {
		if (isShowProgress()) {
			parentContainer.remove(uploadProgress);
			parentContainer.add(uploaderContainer);
			parentContainer.removeStyleName("parentContainerProgress");
		}
	}

	public void setUploadURL(String url) {
		uploader.setUploadURL(url);
	}

	public void addFileQueuedHandler(FileQueuedHandler fileQueuedHandler) {
		if (null != fileQueuedHandler) {
			fileQueuedHandlersList.add(fileQueuedHandler);
		}
	}

	public void addFileDialogCompleteHandler(final FileDialogCompleteHandler fileDialogCompleteHandler) {
		if (null != uploadCompleteHandler) {
			fileDialogCompleteHandlersList.add(fileDialogCompleteHandler);
		}
	}

	public void addUploadSuccessHandler(final UploadSuccessHandler uploadSuccessHandler) {
		if (null != uploadSuccessHandler) {
			uploadSuccessHandlerList.add(uploadSuccessHandler);
		}
	}

	public void addUploadProgressHandler(final UploadProgressHandler uploadProgressHandler) {
		if (null != uploadProgressHandler) {
			uploadProgressHandlersList.add(uploadProgressHandler);
		}
	}

	public void addUploadErrorHandler(UploadErrorHandler uploadErrorHandler) {
		if (null != uploadErrorHandler) {
			uploadErrorHandlerList.add(uploadErrorHandler);
		}
	}

	public boolean isShowProgress() {
		return this.showProgress;
	}

	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public int getUploadsInProgress() {
		return uploader.getStats().getUploadsInProgress();
	}

	public void startUpload() {
		this.uploader.startUpload();
	}

	public int getFilesQueued() {
		return uploader.getStats().getFilesQueued();
	}

	public void cancelUpload(String fileId, boolean triggerEvent) {
		uploader.cancelUpload(fileId, triggerEvent);
	}

	public void setDragAndDropLabelText(String labelText) {
		this.dragDropLabel.setText(labelText);
	}

	public void setButtonText(String buttonText) {
		this.uploader.setButtonText(buttonText);
	}

	public Stats getStats() {
		return uploader.getStats();
	}

	
	public void setEnabled(boolean enabled) {
		parentContainer.setEnabled(enabled);
		uploader.setButtonDisabled(!enabled);
		setEnableDragDrop(enabled);
	}

	public boolean isEnableDragDrop() {
		return enableDragDrop;
	}

	public void setEnableDragDrop(boolean enableDragDrop) {
		this.enableDragDrop = enableDragDrop;
	}

	public void setUploaderID(String id){
		WidgetUtil.setID(uploader, id); 
	}
}
