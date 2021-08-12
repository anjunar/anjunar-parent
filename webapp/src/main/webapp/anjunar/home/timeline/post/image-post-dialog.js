import {builder, customViews, HTMLWindow} from "../../../../library/simplicity/simplicity.js";
import MatImageUpload from "../../../../library/simplicity/components/form/mat-image-upload.js";
import HateoasButton from "../../../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../../../library/simplicity/hateoas/hateoas-form.js";
import DomTextarea from "../../../../library/simplicity/directives/dom-textarea.js";
import {windowManager} from "../../../../library/simplicity/services/window-manager.js";
import {jsonClient} from "../../../../library/simplicity/services/client.js";

export default class ImagePostDialog extends HTMLWindow {

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
                            element: "div",
                            style: {
                                maxWidth: "400px",
                                margin: "auto"
                            },
                            children: [
                                {
                                    element: MatImageUpload,
                                    showButton: false,
                                    name: "image"
                                }
                            ]
                        }, {
                            element: "div",
                            style: {
                                display: "flex",
                                alignItems: "center",
                                backgroundColor: "var(--main-dark2-color)",
                                padding: "5px"
                            },
                            children: [
                                {
                                    element: "div",
                                    text: "Insert additional Content"
                                },
                                {
                                    element: "button",
                                    type: "button",
                                    className: "button material-icons",
                                    text: "photo",
                                    onClick: () => {
                                        let imageUpload = this.querySelector("mat-image-upload");
                                        imageUpload.click();
                                    }
                                }
                            ]
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
    name: "image-post-dialog",
    header: "Post",
    resizable: false,
    class: ImagePostDialog,
    guard(activeRoute) {
        if (activeRoute.queryParams.id) {
            return {
                post : jsonClient.get(`service/home/timeline/post?id=${activeRoute.queryParams.id}`)
            }
        } else {
            return {
                post : jsonClient.get(`service/home/timeline/post/create?type=image`)
            }
        }
    }
})