import {builder, customViews, HTMLWindow} from "../../../../library/simplicity/simplicity.js";
import MatImageUpload from "../../../../library/simplicity/components/form/mat-image-upload.js";
import HateoasButton from "../../../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../../../library/simplicity/hateoas/hateoas-form.js";
import DomTextarea from "../../../../library/simplicity/directives/dom-textarea.js";
import {windowManager} from "../../../../library/simplicity/services/window-manager.js";
import {jsonClient} from "../../../../library/simplicity/services/client.js";
import HateoasInput from "../../../../library/simplicity/hateoas/hateoas-input.js";

export default class LinkPostDialog extends HTMLWindow {

    #post;

    get post() {
        return this.#post;
    }

    set post(value) {
        this.#post = value;
    }

    render() {
        builder(this, {
            element: HateoasForm,
            model: this.#post,
            children: [
                {
                    element: "div",
                    children: [
                        {
                            element: DomTextarea,
                            cols: "10",
                            rows: "10",
                            placeholder: "Write here...",
                            name: "text",
                            style: {
                                width: "400px"
                            },
                        }, {
                            element: HateoasInput,
                            name : "link",
                            placeholder : "Link"
                        }
                    ]
                },
                {
                    element: "div",
                    style: {
                        display: "flex"
                    },
                    children: [
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
                }
            ]
        })
    }

}

customViews.define({
    name: "link-post-dialog",
    header: "Post",
    resizable: false,
    class: LinkPostDialog,
    guard(activeRoute) {
        if (activeRoute.queryParams.id) {
            return {
                post : jsonClient.get(`service/home/timeline/post?id=${activeRoute.queryParams.id}`)
            }
        } else {
            return {
                post : jsonClient.get(`service/home/timeline/post/create?type=link`)
            }
        }
    }
})