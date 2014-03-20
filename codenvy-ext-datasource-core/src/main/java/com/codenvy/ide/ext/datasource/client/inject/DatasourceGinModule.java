/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.client.inject;

import com.codenvy.ide.api.extension.ExtensionGinModule;
import com.codenvy.ide.api.ui.preferences.PreferencesPagePresenter;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.ext.datasource.client.AvailableJdbcDriversService;
import com.codenvy.ide.ext.datasource.client.AvailableJdbcDriversServiceRestImpl;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoOracle;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoOracleImpl;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoStore;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoStoreImpl;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceClientServiceImpl;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.DatasourceManagerPrefImpl;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditorPresenter;
import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTORenderer.Resources;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerView;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPageView;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPageViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardProvider;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgentImpl;
import com.codenvy.ide.ext.datasource.client.properties.DataEntityPropertiesView;
import com.codenvy.ide.ext.datasource.client.properties.DataEntityPropertiesViewImpl;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracleImpl;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlResourceProvider;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlRequestLauncherFactory;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlRequestLauncherView;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlRequestLauncherViewImpl;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreClientService;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreClientServiceImpl;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreManagerPresenter;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreManagerView;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreManagerViewImpl;
import com.codenvy.ide.ext.datasource.client.ssl.upload.UploadSslKeyDialogView;
import com.codenvy.ide.ext.datasource.client.ssl.upload.UploadSslKeyDialogViewImpl;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.google.inject.Singleton;

@ExtensionGinModule
public class DatasourceGinModule extends AbstractGinModule {
    /** {@inheritDoc} */
    @Override
    protected void configure() {
        bind(DatasourceExplorerView.class).to(DatasourceExplorerViewImpl.class)
                                          .in(Singleton.class);
        bind(DatasourceClientService.class).to(DatasourceClientServiceImpl.class)
                                           .in(Singleton.class);
        bind(DefaultWizard.class).annotatedWith(NewDatasourceWizardQualifier.class)
                                 .toProvider(NewDatasourceWizardProvider.class)
                                 .in(Singleton.class);
        bind(NewDatasourceConnectorAgent.class).to(NewDatasourceConnectorAgentImpl.class).in(Singleton.class);
        bind(NewDatasourceWizardPageView.class).to(NewDatasourceWizardPageViewImpl.class);
        bind(DefaultNewDatasourceConnectorView.class).to(DefaultNewDatasourceConnectorViewImpl.class);

        bind(DatasourceManager.class).to(DatasourceManagerPrefImpl.class).in(Singleton.class);

        bind(DataEntityPropertiesView.class).to(DataEntityPropertiesViewImpl.class);

        bind(SqlRequestLauncherView.class).to(SqlRequestLauncherViewImpl.class);

        install(new GinFactoryModuleBuilder()
                                             .implement(SqlRequestLauncherView.class, SqlRequestLauncherViewImpl.class)
                                             .build(SqlRequestLauncherFactory.class));

        bind(SqlResourceProvider.class);

        bind(ReadableContentTextEditor.class).to(ReadableContentTextEditorPresenter.class);

        bind(AvailableJdbcDriversService.class).to(AvailableJdbcDriversServiceRestImpl.class).in(Singleton.class);

        bind(DatabaseInfoStore.class).to(DatabaseInfoStoreImpl.class);

        bind(DatabaseInfoOracle.class).to(DatabaseInfoOracleImpl.class);
        bind(EditorDatasourceOracle.class).to(EditorDatasourceOracleImpl.class).in(Singleton.class);

        bind(Resources.class).in(Singleton.class);
        bind(com.codenvy.ide.Resources.class).to(Resources.class).in(Singleton.class);

        // Add and bind ssl keystore manager preference page and views
        GinMultibinder<PreferencesPagePresenter> prefBinder = GinMultibinder.newSetBinder(binder(), PreferencesPagePresenter.class);
        prefBinder.addBinding().to(SslKeyStoreManagerPresenter.class);
        bind(SslKeyStoreClientService.class).to(SslKeyStoreClientServiceImpl.class).in(Singleton.class);
        bind(SslKeyStoreManagerView.class).to(SslKeyStoreManagerViewImpl.class).in(Singleton.class);
        bind(UploadSslKeyDialogView.class).to(UploadSslKeyDialogViewImpl.class).in(Singleton.class);

    }
}
