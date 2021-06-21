package de.bitvale.anjunar.pages.page.images.image;

import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.pages.PageImage;
import de.bitvale.anjunar.pages.page.images.PageResource;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("pages/page/images/image")
@ApplicationScoped
public class ImageController {

    private final EntityManager entityManager;

    @Inject
    public ImageController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public ImageController() {
        this(null);
    }

    @POST
    @Consumes("application/json")
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public Response upload(ImageResource resource) {

        Pattern pattern = Pattern.compile("data:(\\w+)/(\\w+);base64,.*", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(resource.getData());
        boolean matches = matcher.matches();
        String type = matcher.group(1);
        String subType = matcher.group(2);

        String result = resource
                .getData()
                .replaceFirst("data:(\\w+)/(\\w+);base64,", "");

        Base64.Decoder decoder = Base64.getMimeDecoder();
        byte[] decode = decoder.decode(result);

        PageImage image = new PageImage();
        image.setName(resource.getName());
        image.setType(type);
        image.setSubType(subType);
        image.setLastModified(resource.getLastModified());
        image.setData(decode);

        Page page = entityManager.find(Page.class, resource.getPage().getId());
        image.setPage(page);

        entityManager.persist(image);

        return Response.ok().build();

    }

    @PUT
    @Consumes("application/json")
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public Response update(ImageResource resource) {

        PageImage image = entityManager.find(PageImage.class, resource.getId());

        image.setName(resource.getName());
        image.setLastModified(resource.getLastModified());
        PageResource resourcePage = resource.getPage();
        if (resourcePage == null) {
            image.setPage(null);
        } else {
            Page page = entityManager.find(Page.class, resourcePage.getId());
            image.setPage(page);
        }

        return Response.ok().build();
    }


    @GET
    @Produces({"image/gif", "image/jpeg", "image/png", "image/webp"})
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Response load(@QueryParam("id") UUID id) {

        PageImage image = entityManager.find(PageImage.class, id);

        Response.ResponseBuilder response = Response.ok(image.getData());
        response.header("Content-Disposition", "attachment; filename=" + image.getName());
        return response.build();
    }

    @DELETE
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public Response delete(@QueryParam("id") UUID id) {
        PageImage image = entityManager.find(PageImage.class, id);
        entityManager.remove(image);
        return Response.ok().build();
    }

}
