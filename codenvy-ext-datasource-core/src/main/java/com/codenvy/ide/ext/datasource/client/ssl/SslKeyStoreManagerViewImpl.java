/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.datasource.client.ssl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.Resources;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.shared.ssl.SslKeyStoreEntry;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SslKeyStoreManagerViewImpl extends Composite implements SslKeyStoreManagerView {
    interface SshKeyManagerViewImplUiBinder extends UiBinder<Widget, SslKeyStoreManagerViewImpl> {
    }

    private static SshKeyManagerViewImplUiBinder ourUiBinder = GWT.create(SshKeyManagerViewImplUiBinder.class);

    @UiField
    Button                                       btnUpload;

    @UiField(provided = true)
    CellTable<SslKeyStoreEntry>                  keys;

    @UiField(provided = true)
    final SslMessages                            locale;

    private ActionDelegate                       delegate;

    @Inject
    protected SslKeyStoreManagerViewImpl(SslMessages locale, Resources res) {
        this.locale = locale;
        initSslKeyTable(res);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    protected void initSslKeyTable(CellTable.Resources res) {
        keys = new CellTable<SslKeyStoreEntry>(15, res);
        addAliasColumn(keys, "Client keys");
        addTypeColumn(keys, "");
        Column<SslKeyStoreEntry, String> deleteColumn = addDeleteColumn(keys, "");
        // Creates handler on button clicked
        deleteColumn.setFieldUpdater(new FieldUpdater<SslKeyStoreEntry, String>() {
            @Override
            public void update(int index, SslKeyStoreEntry object, String value) {
                delegate.onDeleteClicked(object);
            }
        });
        // don't show loading indicator
        keys.setLoadingIndicator(null);
    }

    protected void addAliasColumn(CellTable<SslKeyStoreEntry> cellTable, String columnHeaderName) {
        Column<SslKeyStoreEntry, String> aliasColumn = new Column<SslKeyStoreEntry, String>(new TextCell()) {
            @Override
            public String getValue(SslKeyStoreEntry keyItem) {
                return keyItem.getAlias();
            }
        };
        aliasColumn.setSortable(true);
        cellTable.addColumn(aliasColumn, columnHeaderName);
        cellTable.setColumnWidth(aliasColumn, 50, Style.Unit.PCT);
    }

    protected void addTypeColumn(CellTable<SslKeyStoreEntry> cellTable, String columnHeaderName) {
        Column<SslKeyStoreEntry, String> typeColumn = new Column<SslKeyStoreEntry, String>(new ButtonCell()) {
            @Override
            public String getValue(SslKeyStoreEntry object) {
                return object.getType();
            }

            /** {@inheritDoc} */
            @Override
            public void render(Context context, SslKeyStoreEntry object, SafeHtmlBuilder sb) {
                if (object != null && object.getType() != null) {
                    super.render(context, object, sb);
                }
            }
        };
        cellTable.addColumn(typeColumn, columnHeaderName);
        cellTable.setColumnWidth(typeColumn, 30, Style.Unit.PX);
    }

    protected Column<SslKeyStoreEntry, String> addDeleteColumn(CellTable<SslKeyStoreEntry> cellTable, String columnHeaderName) {
        Column<SslKeyStoreEntry, String> deleteKeyColumn = new Column<SslKeyStoreEntry, String>(new ButtonCell()) {
            @Override
            public String getValue(SslKeyStoreEntry object) {
                return "Delete";
            }
        };
        cellTable.addColumn(deleteKeyColumn, columnHeaderName);
        cellTable.setColumnWidth(deleteKeyColumn, 30, Style.Unit.PX);
        return deleteKeyColumn;
    }

    @Override
    public void setKeys(@NotNull Array<SslKeyStoreEntry> keys) {
        // Wraps Array in java.util.List
        List<SslKeyStoreEntry> appList = new ArrayList<SslKeyStoreEntry>();
        for (int i = 0; i < keys.size(); i++) {
            appList.add(keys.get(i));
        }
        this.keys.setRowData(appList);
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @UiHandler("btnUpload")
    public void onUploadKeyButtonClicked(ClickEvent event) {
        delegate.onUploadClicked();
    }

}
