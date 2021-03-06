/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.ssl.upload;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.client.ssl.SslMessages;
import com.codenvy.ide.ui.window.Window;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UploadSslKeyDialogViewImpl extends Window implements UploadSslKeyDialogView {
    interface UploadSshKeyViewImplUiBinder extends UiBinder<Widget, UploadSslKeyDialogViewImpl> {
    }

    private static UploadSshKeyViewImplUiBinder ourUiBinder = GWT.create(UploadSshKeyViewImplUiBinder.class);

    @UiField
    protected Label                             message;

    @UiField
    protected TextBox                           keyAlias;

    protected Button                            btnCancel;

    protected Button                            btnUpload;

    @UiField(provided = true)
    final protected SslMessages                 locale;

    @UiField
    protected FormPanel                         uploadForm;
    @UiField
    protected VerticalPanel                     uploadFormVPanel;
    protected FileUpload                        certFile;
    protected FileUpload                        keyFile;

    private ActionDelegate                      delegate;

    @Inject
    protected UploadSslKeyDialogViewImpl(SslMessages locale) {
        this.locale = locale;

        Widget widget = ourUiBinder.createAndBindUi(this);

        setTitle(locale.dialogUploadSslKeyTitle());
        setWidget(widget);
        btnCancel = createButton(locale.cancelButton(), "SslKeyDialogCancel", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                delegate.onCancelClicked();
            }
        });

        btnUpload = createButton(locale.uploadButton(), "SslLeyDialogUpload", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                delegate.onUploadClicked();
            }
        });
        getFooter().add(btnCancel);
        getFooter().add(btnUpload);

        bind();
    }

    private void bind() {
        uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                delegate.onSubmitComplete(event.getResults());
            }
        });
    }

    @NotNull
    @Override
    public String getAlias() {
        return keyAlias.getText();
    }

    @Override
    public void setHost(@NotNull String host) {
        keyAlias.setText(host);
    }

    @NotNull
    @Override
    public String getCertFileName() {
        return certFile.getFilename();
    }

    @NotNull
    @Override
    public String getKeyFileName() {
        return keyFile.getFilename();
    }

    @Override
    public void setEnabledUploadButton(boolean enabled) {
        btnUpload.setEnabled(enabled);
    }

    @Override
    public void setMessage(@NotNull String message) {
        this.message.setText(message);
    }

    @Override
    public void setEncoding(@NotNull String encodingType) {
        uploadForm.setEncoding(encodingType);
    }

    /** {@inheritDoc} */
    @Override
    public void setAction(@NotNull String url) {
        uploadForm.setAction(url);
        uploadForm.setMethod(FormPanel.METHOD_POST);
    }

    @Override
    public void submit() {
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.submit();
    }

    @Override
    public void showDialog() {
        uploadFormVPanel.clear();
        certFile = new FileUpload();
        certFile.setHeight("22px");
        certFile.setWidth("100%");
        certFile.setName("certFile");
        certFile.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                delegate.onFileNameChanged();
            }
        });
        uploadFormVPanel.add(certFile);
        keyFile = new FileUpload();
        keyFile.setHeight("22px");
        keyFile.setWidth("100%");
        keyFile.setName("keyFile");
        keyFile.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                delegate.onFileNameChanged();
            }
        });
        uploadFormVPanel.add(keyFile);

        this.show();
    }

    @Override
    public void close() {
        hide();

        uploadForm.remove(certFile);
        certFile = null;
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onClose() {
    }
}
