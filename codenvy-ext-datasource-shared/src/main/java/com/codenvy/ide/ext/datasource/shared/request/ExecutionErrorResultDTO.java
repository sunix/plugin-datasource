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
package com.codenvy.ide.ext.datasource.shared.request;

import com.codenvy.dto.shared.DTO;

@DTO
public interface ExecutionErrorResultDTO extends RequestResultDTO {

    static int TYPE = 0;

    ExecutionErrorResultDTO withSqlExecutionError(SqlExecutionError error);

    ExecutionErrorResultDTO withOriginRequest(String request);
}
