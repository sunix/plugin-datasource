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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.nuodb;

import com.google.gwt.user.cellview.client.DataGrid;

public interface DataGridResourcesInvisible extends DataGrid.Resources {

    interface DataGridStyle extends DataGrid.Style {
    }

    @Source({"DataGrid-invisible.css", "com/codenvy/ide/api/ui/style.css"})
    DataGridStyle dataGridStyle();

}
