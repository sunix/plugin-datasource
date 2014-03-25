/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2014] Codenvy, S.A.
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

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

@DefaultLocale("en")
public interface SslMessages extends Messages {
    @DefaultMessage("Cancel")
    String cancelButton();

    @DefaultMessage("Upload")
    String uploadButton();

    @DefaultMessage("Key/Cert files")
    String fileNameFieldTitle();

    @DefaultMessage("Alias")
    String keyAlias();

    @DefaultMessage("Alias can not be empty")
    String aliasValidationError();

    @DefaultMessage("Upload SSL Certificate")
    String managerUploadButton();

    @DefaultMessage("SSL Keystore")
    String sslManagerTitle();

    @DefaultMessage("Do you want to delete ssh keys for <b>{0}</b>")
    String deleteSslKeyQuestion(String alias);
}
