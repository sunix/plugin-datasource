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
package com.codenvy.ide.ext.datasource.shared;

import com.codenvy.dto.shared.DTO;

@DTO
public interface DefaultDatasourceDefinitionDTO extends DatabaseConfigurationDTO {


    // String getHostName();
    //
    // int getPort();
    //
    //
    // void setHostName(String hostname);
    //
    // void setPort(int port);


    DefaultDatasourceDefinitionDTO withHostName(String hostname);

    DefaultDatasourceDefinitionDTO withPort(int port);


    /* Change return type of parent with* methods */
    DefaultDatasourceDefinitionDTO withDatasourceId(String type);

    DefaultDatasourceDefinitionDTO withDatabaseName(String databaseName);

    DefaultDatasourceDefinitionDTO withDatabaseType(DatabaseType type);
}
