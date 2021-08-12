import {builder, customViews, HTMLWindow} from "../../../../../library/simplicity/simplicity.js";
import HateoasButton from "../../../../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../../../../library/simplicity/hateoas/hateoas-form.js";
import DomTextarea from "../../../../../library/simplicity/directives/dom-textarea.js";
import {windowManager} from "../../../../../library/simplicity/services/window-manager.js";
import {jsonClient} from "../../../../../library/simplicity/services/client.js";

export default class CommentDialog extends HTMLWindow {

    #comment;

    get comment() {
        return this.#comment;
    }

    set comment(value) {
        this.#comment = value;
    }

    render() {
        builder(this, {
            element: HateoasForm,
            model: this.#comment,
            children: [
                {
                    element: "div",
                    children: [
                        {
                            element: DomTextarea,
                            cols: "10",
                            rows: "10",
                            name: "text",
                            style: {
                                width: "400px"
                            }
                        }
                    ]
                },
                {
                    element: "div",
                    children: [
                        {
                            element: HateoasButton,
                            hateoas: "update",
                            text: "Send",
                            onAfterSubmit: () => {
                                this.dispatchEvent(new CustomEvent("update"))
                                document.reloadAll();
                                windowManager.close(this.window);
                            }
                        },
                        {
                            element: HateoasButton,
                            hateoas: "delete",
                            text: "Delete",
                            onAfterSubmit: () => {
                                this.dispatchEvent(new CustomEvent("update"))
                                document.reloadAll();
                                windowManager.close(this.window);
                            }
                        }
                    ]
                }
            ]
        })
    }
}

customViews.define({
    name: "comment-dialog",
    header: "Comment",
    resizable: false,
    class: CommentDialog,
    guard(activeRoute) {
        if (activeRoute.queryParams.id) {
            return {
                comment : jsonClient.get(`service/home/timeline/post/comments/comment?id=${activeRoute.queryParams.id}`)
            }
        } else {
            return {
                comment : jsonClient.get(`service/home/timeline/post/comments/comment/create`)
            }
        }
    }
})