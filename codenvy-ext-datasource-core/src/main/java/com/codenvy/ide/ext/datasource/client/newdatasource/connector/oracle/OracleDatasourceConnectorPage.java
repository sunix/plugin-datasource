package com.codenvy.ide.ext.datasource.client.newdatasource.connector.oracle;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.Resources;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.JdbcDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Created by Wafa on 20/01/14.
 */
public class OracleDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage implements
                                                                                     JdbcDatasourceConnectorView.ActionDelegate {
    public static final String ORACLE_DB_ID        = "oracle";
    private static final int   DEFAULT_PORT_ORACLE = 1521;

    private final DtoFactory   dtoFactory;

    @Inject
    public OracleDatasourceConnectorPage(final JdbcDatasourceConnectorView view,
                                         final NotificationManager notificationManager,
                                         final DtoFactory dtoFactory,
                                         final DatasourceManager datasourceManager,
                                         final EventBus eventBus,
                                         final DatasourceClientService service,
                                         final Resources resources) {
        super(view, "Oracle", resources.getOracleLogo(), ORACLE_DB_ID, datasourceManager, eventBus, service, notificationManager);
        this.dtoFactory = dtoFactory;
    }


    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(getView());
        getView().setPort(DEFAULT_PORT_ORACLE);
    }


    protected DatabaseConfigurationDTO getConfiguredDatabase() {
        String datasourceId = wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME);
        DatabaseConfigurationDTO result =
                                          dtoFactory.createDto(DatabaseConfigurationDTO.class)
                                                    .withDatabaseName(getView().getDatabaseName())
                                                    .withHostname(getView().getHostname()).withPort(getView().getPort())
                                                    .withUsername(getView().getUsername())
                                                    .withPassword(getView().getPassword())
                                                    .withDatabaseType(DatabaseType.ORACLE)
                                                    .withDatasourceId(datasourceId);
        return result;
    }
}
