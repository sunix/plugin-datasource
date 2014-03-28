package com.codenvy.ide.ext.datasource.server.ssl;

import java.io.FileOutputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Iterator;

import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;

/**
 * The trustStore is pretty similar to the keystore except that
 * - it doesn't take key
 */
public class TrustStoreObject extends KeyStoreObject {

    public TrustStoreObject() throws Exception {
        super();
    }

    protected String getKeyStorePassword() {
        String sPass = System.getProperty("javax.net.ssl.trustStorePassword");
        return sPass;
    }

    protected String getKeyStoreLocation() {
        String store = System.getProperty("javax.net.ssl.trustStore");
        return store;
    }
    
    @Override
    public Response addNewKeyCertificate(@QueryParam("alias") String alias,
                                         Iterator<FileItem> uploadedFilesIterator) throws Exception {
        Certificate[] certs = null;
        while (uploadedFilesIterator.hasNext()) {
            FileItem fileItem = uploadedFilesIterator.next();
            if (!fileItem.isFormField()) {
                if ("certFile".equals(fileItem.getFieldName())) {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    certs = cf.generateCertificates(fileItem.getInputStream()).toArray(new Certificate[]{});
                }
            }
        }

        if (certs == null) {
            throw new WebApplicationException(Response.ok("<pre>Can't find input file.</pre>", MediaType.TEXT_HTML).build());
        }

        keystore.setCertificateEntry(alias, certs[0]);
        keystore.store(new FileOutputStream(keyStoreLocation), keyStorePassword.toCharArray());

        return Response.ok("", MediaType.TEXT_HTML).build();
    }
}
