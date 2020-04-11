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

import java.util.Date;

/**
 * Sitemap URL information.
 * 
 * <p>Related link</p>
 * <ul>
 * <li>https://www.sitemaps.org/protocol.html</li>
 * </ul>
 */
public class SitemapInfoUrl {
    /**
     * loc of url.
     */
    private String loc = "http://example.com/";

    /**
     * Updated date.
     */
    private Date lastmod = null;

    /**
     * always
     * hourly
     * daily
     * weekly
     * monthly
     * yearly
     * never
     */
    private Changefreq changefreq = null;

    /**
     * 0.8
     */
    private String priority = null;

    /**
     * Get freq of change.
     *
     * @return Freq of change.
     */
    public Changefreq getChangefreq() {
        return changefreq;
    }

    /**
     * Set change freq.
     * 
     * @param changefreq Freq of change.
     */
    public void setChangefreq(Changefreq changefreq) {
        this.changefreq = changefreq;
    }

    /**
     * Get priority string.
     * 
     * @return Priority.
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Set priority string. ex: 0.8
     *    
     * @param priority Priority string. ex: 0.8
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Get loc url.
     * 
     * @return oc URL: ex: http://example.com/
     */
    public String getLoc() {
        return loc;
    }

    /**
     * Set loc url. [required]
     * 
     * @param loc URL: ex: http://example.com/
     */
    public void setLoc(String loc) {
        this.loc = loc;
    }

    /**
     * Get last modified.
     * 
     * @return Date of last modified.
     */
    public Date getLastmod() {
        return lastmod;
    }

    /**
     * Set last modified.
     * 
     * @param lastmod Date of last modified.
     */
    public void setLastmod(Date lastmod) {
        this.lastmod = lastmod;
    }

    /**
     * Enum of changefreq.
     */
    public static enum Changefreq {
        /** change each time they are accessed. */
        Always,
        /** change hourly. */
        Hourly,
        /** change daily. */
        Daily,
        /** change weekly. */
        Weekly,
        /** change monthly. */
        Monthly,
        /** change yearly. */
        Yearly,
        /** never change. */
        Never;
    }
}
