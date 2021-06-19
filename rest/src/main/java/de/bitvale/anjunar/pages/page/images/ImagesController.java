package de.bitvale.anjunar.pages.page.images;

import de.bitvale.anjunar.pages.page.images.image.ImageResource;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListController;
import de.bitvale.common.security.Identity;
import de.bitvale.common.rest.Secured;
import de.bitvale.anjunar.pages.PageImage;
import de.bitvale.anjunar.pages.Page;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
@Secured
@Path("pages/page/images")
public class ImagesController implements ListController<ImageResource, ImagesSearch> {

    private final ImagesService service;

    private final Identity identity;

    @Inject
    public ImagesController(ImagesService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public ImagesController() {
        this(null, null);
    }

    @Override
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<ImageResource> list(ImagesSearch search) {

        long count = service.count(search);
        List<PageImage> images = service.find(search);

        List<ImageResource> resources = new ArrayList<>();
        for (PageImage image : images) {
            ImageResource resource = new ImageResource();
            resource.setId(image.getId());
            resource.setName(image.getName());

            Base64.Encoder encoder = Base64.getMimeEncoder();
            byte[] decode = encoder.encode(image.getData());
            String preString = String.format("data:%s/%s;base64,", image.getType(), image.getSubType());

            resource.setData(preString + new String(decode));
            resource.setLastModified(image.getLastModified());
            resources.add(resource);

            Page page = image.getPage();
            if (page != null) {
                PageResource pageResource = new PageResource();
                pageResource.setId(page.getId());
                pageResource.setTitle(page.getTitle());
                resource.setPage(pageResource);
            }

            identity.createLink("pages/page/images/image?id=" + image.getId(), "GET", "read", resource::addLink);
            identity.createLink("pages/page/images/image?id=" + image.getId(), "DELETE", "delete", resource::addLink);
        }

        return new Container<>(resources, count);
    }

}
