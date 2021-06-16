import {builder, customViews, i18nFactory} from "../../../library/simplicity/simplicity.js";
import DomInput from "../../../library/simplicity/directives/dom-input.js";
import MatEditor from "../../../library/simplicity/components/form/mat-editor.js";
import {jsonClient} from "../../../library/simplicity/services/client.js";
import HateoasButton from "../../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../../library/simplicity/hateoas/hateoas-form.js";

export default class Topic extends HTMLElement {

    #topic;

    get topic() {
        return this.#topic;
    }

    set topic(value) {
        this.#topic = value;
    }

    render() {
        let page = this.queryUpwards("pages-page");
        let html = page.html;

        builder(this, {
            element: HateoasForm,
            model : this.#topic,
            style: {
                backgroundColor: "var(--main-dimmed-color)",
                padding: "5px"
            },
            children: [
                {
                    element: "h4",
                    text: i18n("Title")
                },
                {
                    element: "div",
                    text: i18n("Be specific and imagine you’re asking a question to another person")
                }, {
                    element: DomInput,
                    type: "text",
                    name : "topic",
                    style: {
                        height: "24px",
                        width: "calc(100% - 10px)",
                        border: "1px solid var(--main-normal-color)"
                    },
                    placeholder: i18n("Insert here...")
                },
                {
                    element: "h4",
                    text: "Body"
                },
                {
                    element: "div",
                    text: i18n("Include all the information someone would need to answer your question")
                },
                {
                    element: MatEditor,
                    name : "editor",
                    style: {
                        height: "300px"
                    }
                },
                {
                    element: HateoasButton,
                    hateoas : "save",
                    text : i18n("Save"),
                    onAfterSubmit : (event) => {
                        window.location.hash = `#/anjunar/pages/page?id=${html.form.id}#/anjunar/pages/page/topic/replies?id=${event.detail.id}`
                    }
                },
                {
                    element: HateoasButton,
                    hateoas : "update",
                    text : i18n("Update"),
                    onAfterSubmit : (event) => {
                        window.location.hash = `#/anjunar/pages/page?id=${html.form.id}#/anjunar/pages/page/topic/replies?id=${event.detail.id}`
                    }
                },
                {
                    element: HateoasButton,
                    hateoas : "delete",
                    text : i18n("Delete"),
                    onAfterSubmit : (event) => {
                        window.location.hash = `#/anjunar/pages/page?id=${html.form.id}#/anjunar/pages/text`
                    }
                }
            ]
        })
    }
}

customViews.define({
    name: "page-topic",
    class: Topic,
    guard(activeRoute) {
        if (activeRoute.queryParams.id) {
            return {
                topic: jsonClient.get(`service/pages/page/topics/topic?id=${activeRoute.queryParams.id}`)
            }
        }
        return {
            topic: jsonClient.get(`service/pages/page/topics/topic/create?page=${activeRoute.queryParams.page}`)
        }
    }
})

const i18n = i18nFactory({
    "Include all the information someone would need to answer your question" : {
        en : "Include all the information someone would need to answer your question",
        de : "Geben Sie alle Informationen an, die jemand benötigt, um Ihre Frage zu beantworten"
    },
    "Be specific and imagine you’re asking a question to another person" : {
        en : "Be specific and imagine you’re asking a question to another person",
        de : "Seien Sie konkret und stellen Sie sich vor, Sie stellen einer anderen Person eine Frage"
    },
    "Insert here..." : {
        en : "Insert here...",
        de : "Hier einfügen..."
    },
    "Title" : {
        en : "Title",
        de : "Titel"
    },
    Save : {
        en : "Save",
        de : "Speichern"
    },
    Update : {
        en : "Update",
        de : "Aktualisieren"
    },
    Delete : {
        en : "Delete",
        de : "Löschen"
    }
});

