# sitemapgenerator4j

Simple Sitemap Generator for Java.

See more details below:
  https://www.sitemaps.org/index.html

## License

Apache 2.0

## Usage

```Java
    SitemapInfo info = new SitemapInfo();

    SitemapInfoUrl url = new SitemapInfoUrl();
    info.addUrl(url);
    url.setLoc("http://example.com/");
    url.setLastmod(new Date());
    url.setChangefreq(SitemapInfoUrl.Changefreq.Daily);
    url.setPriority("0.8");

    new SitemapGenerator4j().write(info, new File("./sitemap.xml"));
```
