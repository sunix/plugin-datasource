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
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.ext.datasource.client.common.HyperlinkCell;
import com.google.gwt.user.cellview.client.Header;

/**
 * A header/footer that displays a hyperlink.
 * 
 * @author "Mickaël Leduque"
 */
public class HyperlinkHeader extends Header<String> {

    /** The URL of the link. */
    private final String url;

    public HyperlinkHeader(final String url, final String linkText) {
        super(new HyperlinkCell(url, linkText));
        this.url = url;
    }

    @Override
    public String getValue() {
        return this.url;
    }
}
