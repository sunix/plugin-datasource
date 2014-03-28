/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.ext.datasource.client;

import static com.codenvy.ide.api.ui.action.Anchor.BEFORE;
import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_MAIN_MENU;
import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_WINDOW;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.Constraints;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.keybinding.KeyBindingAgent;
import com.codenvy.ide.api.ui.keybinding.KeyBuilder;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.workspace.PartStackType;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.ext.datasource.client.common.CellTableResources;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerPartPresenter;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceAction;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPagePresenter;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.google.cloud.sql.GoogleCloudSqlConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.mssqlserver.MssqlserverDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.mysql.MysqlDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.nuodb.NuoDBDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.oracle.OracleDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.postgres.PostgresDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.sqllauncher.ExecuteSqlAction;
import com.codenvy.ide.util.input.CharCodeWithModifiers;
import com.codenvy.ide.util.input.KeyCodeMap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Extension definition for the datasource plugin.
 */
@Singleton
@Extension(title = "Datasource Extension", version = "1.0.0")
public class DatasourceExtension {

    public static boolean SHOW_ITEM                  = true;
    public static String  DS_GROUP_MAIN_MENU         = "DatasourceMainMenu";

    private static String DS_ACTION_SHORTCUT_EXECUTE = "DatasourceActionExecute";

    @Inject
    public DatasourceExtension(WorkspaceAgent workspaceAgent,
                               DatasourceExplorerPartPresenter dsExplorer,
                               ActionManager actionManager,
                               NewDatasourceAction newDSConnectionAction,
                               Provider<NewDatasourceWizardPagePresenter> newDatasourcePageProvider,
                               @NewDatasourceWizardQualifier DefaultWizard wizard,
                               NewDatasourceConnectorAgent connectorAgent,
                               DatasourceUiResources resources,
                               CellTableResources celltableResources,
                               Provider<PostgresDatasourceConnectorPage> pgConnectorPageProvider,
                               Provider<MysqlDatasourceConnectorPage> mysqlConnectorPageProvider,
                               Provider<OracleDatasourceConnectorPage> oracleConnectorPageProvider,
                               Provider<MssqlserverDatasourceConnectorPage> mssqlserverConnectorPageProvider,
                               Provider<NuoDBDatasourceConnectorPage> nuodbConnectorPageProvider,
                               Provider<GoogleCloudSqlConnectorPage> googleCloudSqlConnectorPageProvider,
                               AvailableJdbcDriversService availableJdbcDrivers,
                               ExecuteSqlAction executeSqlAction,
                               KeyBindingAgent keyBindingAgent) {

        workspaceAgent.openPart(dsExplorer, PartStackType.NAVIGATION);

        // create de "Datasource" menu in menubar and insert it
        DefaultActionGroup mainMenu = (DefaultActionGroup)actionManager.getAction(GROUP_MAIN_MENU);
        DefaultActionGroup defaultDatasourceMainGroup = new DefaultActionGroup("Datasource", true, actionManager);
        actionManager.registerAction(DS_GROUP_MAIN_MENU, defaultDatasourceMainGroup);
        Constraints beforeWindow = new Constraints(BEFORE, GROUP_WINDOW);
        mainMenu.add(defaultDatasourceMainGroup, beforeWindow);

        // add submenu "New datasource" to Datasource menu
        actionManager.registerAction("NewDSConnection", newDSConnectionAction);
        defaultDatasourceMainGroup.add(newDSConnectionAction);

        wizard.addPage(newDatasourcePageProvider);

        // fetching available drivers list from the server
        availableJdbcDrivers.fetch();

        // inject CSS
        resources.datasourceUiCSS().ensureInjected();
        celltableResources.cellTableStyle().ensureInjected();

        // counter to add different priorities to all connectors - to increment after each #register(NewDatasourceConnector)
        int connectorCounter = 0;

        // add a new postgres connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> pgWizardPages = Collections.createArray();
        pgWizardPages.add(pgConnectorPageProvider);
        connectorAgent.register(PostgresDatasourceConnectorPage.PG_DB_ID, connectorCounter, "PostgreSQL",
                                resources.getPostgreSqlLogo(), "org.postgresql.Driver", pgWizardPages);

        connectorCounter++;

        // Add a new mysql connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> mysqlWizardPages = Collections.createArray();
        mysqlWizardPages.add(mysqlConnectorPageProvider);
        connectorAgent.register(MysqlDatasourceConnectorPage.MYSQL_DB_ID, connectorCounter, "MySQL",
                                resources.getMySqlLogo(), "com.mysql.jdbc.Driver", mysqlWizardPages);

        connectorCounter++;

        // add a new oracle connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> oracleWizardPages = Collections.createArray();
        oracleWizardPages.add(oracleConnectorPageProvider);
        connectorAgent.register(OracleDatasourceConnectorPage.ORACLE_DB_ID, connectorCounter,
                                "Oracle", resources.getOracleLogo(), "oracle.jdbc.OracleDriver", oracleWizardPages);

        connectorCounter++;

        // add a new SQLserver connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> sqlServerWizardPages = Collections.createArray();
        sqlServerWizardPages.add(mssqlserverConnectorPageProvider);
        connectorAgent.register(MssqlserverDatasourceConnectorPage.SQLSERVER_DB_ID, connectorCounter,
                                "MsSqlServer", resources.getSqlServerLogo(), "net.sourceforge.jtds.jdbc.Driver", sqlServerWizardPages);

        connectorCounter++;

        // add a new NuoDB connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> nuoDBWizardPages = Collections.createArray();
        nuoDBWizardPages.add(nuodbConnectorPageProvider);
        connectorAgent.register(NuoDBDatasourceConnectorPage.NUODB_DB_ID, connectorCounter,
                                "NuoDB", resources.getNuoDBLogo(), "com.nuodb.jdbc.Driver", nuoDBWizardPages);

        connectorCounter++;

        // add a new GoogleCloudSQL connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> googleCloudSQLWizardPages = Collections.createArray();
        googleCloudSQLWizardPages.add(googleCloudSqlConnectorPageProvider);
        connectorAgent.register(GoogleCloudSqlConnectorPage.GOOGLECLOUDSQL_DB_ID, connectorCounter,
                "GoogleCloudSQL", resources.getGoogleCloudSQLLogo(), "com.mysql.jdbc.Driver", googleCloudSQLWizardPages);

        connectorCounter++;


        // Add execute shortcut
        actionManager.registerAction(DS_ACTION_SHORTCUT_EXECUTE, executeSqlAction);
        final CharCodeWithModifiers key = new KeyBuilder().action().charCode(KeyCodeMap.ENTER).build();
        keyBindingAgent.getGlobal().addKey(key, DS_ACTION_SHORTCUT_EXECUTE);
    }
}
