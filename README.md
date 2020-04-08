# sitemapgenerator4j

Simple Sitemap Generator for Java.

See more details below:
  https://www.sitemaps.org/index.html

## License

Apache 2.0

## Usage

### Java source (input)

```java
    SitemapInfo info = new SitemapInfo();

    SitemapInfoUrl url = new SitemapInfoUrl();
    info.addUrl(url);
    url.setLoc("http://example.com/");
    url.setLastmod(new Date());
    url.setChangefreq(SitemapInfoUrl.Changefreq.Daily);
    url.setPriority("0.8");

    new SitemapGenerator4j().write(info, new File("./sitemap.xml"));
```

### XML file (output)


```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
    <url>
        <loc>http://example.com/</loc>
        <lastmod>2020-04-08T23:55:55+09:00</lastmod>
        <changefreq>daily</changefreq>
        <priority>0.8</priority>
    </url>
</urlset>
```

