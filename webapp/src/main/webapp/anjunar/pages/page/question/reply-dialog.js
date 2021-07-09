import {builder, customViews, HTMLWindow} from "../../../../library/simplicity/simplicity.js";
import MatEditor from "../../../../library/simplicity/components/form/mat-editor.js";
import HateoasButton from "../../../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../../../library/simplicity/hateoas/hateoas-form.js";
import {jsonClient} from "../../../../library/simplicity/services/client.js";
import {windowManager} from "../../../../library/simplicity/services/window-manager.js";

export default class ReplyDialog extends HTMLWindow {

    #reply;

    get reply() {
        return this.#reply;
    }

    set reply(value) {
        this.#reply = value;
    }

    render() {
        builder(this, {
            element: HateoasForm,
            model: this.#reply,
            children: [
                {
                    element: MatEditor,
                    name: "editor",
                    style: {
                        height: "300px",
                        width: "800px"
                    }
                },
                {
                    element: HateoasButton,
                    hateoas: "save",
                    text: "Save",
                    onAfterSubmit: () => {
                        document.reloadAll();
                        windowManager.close(this.window);
                    }
                },
                {
                    element: HateoasButton,
                    hateoas: "update",
                    text: "Update",
                    onAfterSubmit: () => {
                        document.reloadAll();
                        windowManager.close(this.window);
                    }
                },
                {
                    element: HateoasButton,
                    hateoas: "delete",
                    text: "Delete",
                    onAfterSubmit: () => {
                        document.reloadAll();
                        windowManager.close(this.window);
                    }
                }
            ]
        })
    }

}

customViews.define({
    name: "topic-reply-dialog",
    class: ReplyDialog,
    header: "Reply",
    resizable: false,
    guard(activeRoute) {
        if (activeRoute.queryParams.id) {
            return {
                reply: jsonClient.get(`service/pages/page/topics/topic/replies/reply?id=${activeRoute.queryParams.id}`)
            }
        } else {
            return {
                reply: jsonClient.get(`service/pages/page/topics/topic/replies/reply/create?topic=${activeRoute.queryParams.topic}`)
            }
        }
    }
});
