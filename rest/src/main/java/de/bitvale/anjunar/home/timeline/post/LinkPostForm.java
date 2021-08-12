package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.timeline.Image;
import de.bitvale.anjunar.timeline.LinkPost;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LinkPostForm extends AbstractPostForm {

    private final static Logger log = LoggerFactory.getLogger(LinkPostForm.class);

    @Input(type = "url")
    private URL link;

    @Input(type = "text")
    private String title;

    @Input(type = "text")
    private String description;

    @Input(type = "image")
    private Blob image;

    @Override
    public <E> E accept(AbstractPostFormVisitor<E> visitor) {
        return visitor.visit(this);
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public static LinkPostForm factory(LinkPost post) {
        LinkPostForm form = new LinkPostForm();
        form.setLink(post.getLink());
        form.setTitle(post.getTitle());
        form.setDescription(post.getDescription());
        form.setImage(Blob.factory(post.getImage()));
        AbstractPostForm.abstractFactory(form, post);
        return form;
    }

    public static LinkPost updater(LinkPostForm resource, LinkPost post, Identity identity, EntityManager entityManager) {
        post.setLink(resource.getLink());

        try {
            Document document = Jsoup.connect(post.getLink().toString()).get();
            Elements openGraphMetaElements = document.select("meta[property]");
            Element imageElement = openGraphMetaElements.stream().filter(element -> element.attr("property").equals("og:image")).findFirst().get();
            Element titleElement = openGraphMetaElements.stream().filter(element -> element.attr("property").equals("og:title")).findFirst().get();
            Element descriptionElement = openGraphMetaElements.stream().filter(element -> element.attr("property").equals("og:description")).findFirst().get();

            String image = imageElement.attr("content");
            String title = titleElement.attr("content");
            String description = descriptionElement.attr("content");

            post.setTitle(title);
            post.setDescription(description);
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(image))
                        .build();

                HttpResponse<byte[]> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                System.out.println(httpResponse);

                Optional<String> contentType = httpResponse.headers().firstValue("content-type");
                String[] split = image.split("/");
                String filename = split[split.length - 1];

                Image imageFile = new Image();
                imageFile.setData(httpResponse.body());
                imageFile.setLastModified(LocalDateTime.now());
                imageFile.setName(filename);
                imageFile.setType(contentType.get().split("/")[0]);
                imageFile.setSubType(contentType.get().split("/")[1]);

                post.setImage(imageFile);
            } catch (IOException e) {
                log.error(e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


/*
        SourceContent content = TextCrawler.scrape(post.getLink().toString(), 100);
        if (content.isSuccess()) {
        }
*/


        AbstractPostForm.abstractUpdater(resource, post, identity, entityManager);
        return post;
    }

}
