/*
 * Copyright 2020 Toshiki Iga
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.igapyon.sitemapgenerator4j;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;

/**
 * @see https://www.sitemaps.org/protocol.html
 */
public class SitemapGenerator4jTest {
    /**
     * Simple sample
     * 
     * @param args
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        SitemapInfo info = new SitemapInfo();

        {
            SitemapInfoUrl url = new SitemapInfoUrl();
            info.addUrl(url);
            url.setLoc("http://example.com/");
            url.setLastmod(new Date());
            url.setChangefreq(SitemapInfoUrl.Changefreq.Daily);
            url.setPriority("0.8");
        }

        {
            SitemapInfoUrl url = new SitemapInfoUrl();
            info.addUrl(url);
        }

        new SitemapGenerator4j().write(info, new File("./target/sitemap.xml"));
    }
}
