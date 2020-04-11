/**
 * <h1>sitemapgenerator4j</h1>
 * <p>Simple Sitemap Generator for Java.</p>
 * <h2>License</h2>
 * Apache 2.0
 * <h2>Usage</h2>
 * <h3>Java source (input)</h3>
 * <pre>
    SitemapInfo info = new SitemapInfo();

    SitemapInfoUrl url = new SitemapInfoUrl();
    info.addUrl(url);
    url.setLoc("http://example.com/");
    url.setLastmod(new Date());
    url.setChangefreq(SitemapInfoUrl.Changefreq.Daily);
    url.setPriority("0.8");

    new SitemapGenerator4j().write(info, new File("./sitemap.xml"));
 * </pre>
 * <h3>XML file (output)</h3>
 * <pre>
&lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt;
&lt;urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"&gt;
    &lt;url&gt;
        &lt;loc&gt;http://example.com/&lt;/loc&gt;
        &lt;lastmod&gt;2020-04-08T23:55:55+09:00&lt;/lastmod&gt;
        &lt;changefreq&gt;daily&lt;/changefreq&gt;
        &lt;priority&gt;0.8&lt;/priority&gt;
    &lt;/url&gt;
&lt;/urlset&gt;
 * </pre>
 */
package jp.igapyon.sitemapgenerator4j;
