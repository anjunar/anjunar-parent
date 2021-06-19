package de.bitvale.anjunar.pages.page.forum.topic.replies.reply;

import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.FormController;
import de.bitvale.common.rest.api.meta.MetaForm;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.common.rest.Secured;
import de.bitvale.anjunar.pages.page.forum.Reply;
import de.bitvale.anjunar.pages.page.forum.Topic;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page/topics/topic/replies/reply")
@Secured
public class TopicReplyController implements FormController<TopicReplyResource> {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public TopicReplyController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public TopicReplyController() {
        this(null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public MetaForm<TopicReplyResource> create(@QueryParam("topic") UUID uuid) {
        TopicReplyResource resource = new TopicReplyResource();

        resource.setTopic(uuid);
        resource.setOwner(new UserResource());
        resource.setEditor(new Editor());

        identity.createLink("pages/page/topics/topic/replies/reply", "POST", "save", resource::addAction);

        MetaForm<TopicReplyResource> metaForm = new MetaForm<>(resource, identity.getLanguage());

        return metaForm;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaForm<TopicReplyResource> read(UUID id) {

        Reply reply = entityManager.find(Reply.class, id);

        TopicReplyResource resource = new TopicReplyResource();
        resource.setId(reply.getId());
        Editor editor = new Editor();
        editor.setHtml(reply.getHtml());
        editor.setText(reply.getText());
        resource.setEditor(editor);
        resource.setTopic(reply.getTopic().getId());
        resource.setCreated(reply.getCreated());
        UserResource owner = new UserResource();

        User user = reply.getOwner();
        owner.setId(user.getId());
        owner.setFirstName(user.getFirstName());
        owner.setLastName(user.getLastName());
        owner.setBirthDate(user.getBirthDate());

        Blob image = new Blob();
        image.setName(user.getPicture().getName());
        image.setLastModified(user.getPicture().getLastModified());
        image.setData(FileDiskUtils.buildBase64(user.getPicture().getType(), user.getPicture().getSubType(), user.getPicture().getData()));

        owner.setImage(image);
        resource.setOwner(owner);

        if (identity.getUser().getId().equals(reply.getOwner().getId())) {
            identity.createLink("pages/page/topics/topic/replies/reply?id=" + reply.getId(), "PUT", "update", resource::addAction);
            identity.createLink("pages/page/topics/topic/replies/reply?id=" + reply.getId(), "DELETE", "delete", resource::addAction);
        }

        MetaForm<TopicReplyResource> metaForm = new MetaForm<>(resource, identity.getLanguage());
        Property property = metaForm.find("owner");
        identity.createLink("control/users", "POST", "list", property::addLink);

        return metaForm;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public TopicReplyResource save(TopicReplyResource form) {

        Topic topic = entityManager.find(Topic.class, form.getTopic());

        Reply reply = new Reply();

        reply.setText(form.getEditor().getText());
        reply.setHtml(form.getEditor().getHtml());
        reply.setOwner(identity.getUser());
        reply.setTopic(topic);

        entityManager.persist(reply);

        form.setId(reply.getId());

        identity.createLink("pages/page/topics/topic/replies/reply?id=" + reply.getId(), "GET", "read", form::addAction);
        identity.createLink("pages/page/topics/topic/replies/reply?id=" + reply.getId(), "PUT", "update", form::addAction);
        identity.createLink("pages/page/topics/topic/replies/reply?id=" + reply.getId(), "DELETE", "delete", form::addAction);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public TopicReplyResource update(UUID id, TopicReplyResource form) {

        Topic topic = entityManager.find(Topic.class, form.getTopic());

        Reply reply = entityManager.find(Reply.class, id);

        if (! reply.getOwner().getId().equals(identity.getUser().getId())) {
            throw new NotAllowedException("Not Allowed");
        }

        reply.setText(form.getEditor().getText());
        reply.setHtml(form.getEditor().getHtml());
        reply.setOwner(identity.getUser());
        reply.setTopic(topic);

        identity.createLink("pages/page/topics/topic/replies/reply?id=" + reply.getId(), "GET", "read", form::addAction);
        identity.createLink("pages/page/topics/topic/replies/reply?id=" + reply.getId(), "DELETE", "delete", form::addAction);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public void delete(UUID id) {
        Reply reply = entityManager.getReference(Reply.class, id);
        entityManager.remove(reply);
    }
}
