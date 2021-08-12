import {builder, customViews, HTMLWindow} from "../../../library/simplicity/simplicity.js";
import DomInput from "../../../library/simplicity/directives/dom-input.js";
import MatEditor from "../../../library/simplicity/components/form/mat-editor.js";
import {jsonClient} from "../../../library/simplicity/services/client.js";
import HateoasButton from "../../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../../library/simplicity/hateoas/hateoas-form.js";
import {i18nFactory} from "../../../library/simplicity/services/i18nResolver.js";
import {windowManager} from "../../../library/simplicity/services/window-manager.js";

export default class Question extends HTMLWindow {

    #topic;

    get topic() {
        return this.#topic;
    }

    set topic(value) {
        this.#topic = value;
    }

    render() {

        builder(this, {
            element: HateoasForm,
            model: this.#topic,
            style: {
                backgroundColor: "var(--main-dark2-color)",
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
                    name: "topic",
                    style: {
                        height: "24px",
                        width: "calc(100% - 10px)",
                        border: "1px solid var(--main-dark1-color)"
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
                    name: "editor",
                    style: {
                        height: "300px"
                    }
                },
                {
                    element: HateoasButton,
                    hateoas: "save",
                    text: i18n("Save"),
                    onAfterSubmit: (event) => {
                        window.location.hash = `#/anjunar/pages/page/question/replies?id=${event.detail.id}`
                        document.reloadAll();
                        windowManager.close(this.window);
                    }
                },
                {
                    element: HateoasButton,
                    hateoas: "update",
                    text: i18n("Update"),
                    onAfterSubmit: (event) => {
                        window.location.hash = `#/anjunar/pages/page/question/replies?id=${event.detail.id}`
                        document.reloadAll();
                        windowManager.close(this.window);
                    }
                },
                {
                    element: HateoasButton,
                    hateoas: "delete",
                    text: i18n("Delete"),
                    onAfterSubmit: (event) => {
                        window.location.hash = `#/anjunar/pages/page?id=${this.queryParams.page}`
                        document.reloadAll();
                        windowManager.close(this.window);
                    }
                }
            ]
        })
    }
}

const i18n = i18nFactory({
    "Include all the information someone would need to answer your question": {
        "en-DE": "Include all the information someone would need to answer your question",
        "de-DE": "Geben Sie alle Informationen an, die jemand benötigt, um Ihre Frage zu beantworten"
    },
    "Be specific and imagine you’re asking a question to another person": {
        "en-DE": "Be specific and imagine you’re asking a question to another person",
        "de-DE": "Seien Sie konkret und stellen Sie sich vor, Sie stellen einer anderen Person eine Frage"
    },
    "Insert here...": {
        "en-DE": "Insert here...",
        "de-DE": "Hier einfügen..."
    },
    "Title": {
        "en-DE": "Title",
        "de-DE": "Titel"
    },
    "Save": {
        "en-DE": "Save",
        "de-DE": "Speichern"
    },
    "Update": {
        "en-DE": "Update",
        "de-DE": "Aktualisieren"
    },
    "Delete": {
        "en-DE": "Delete",
        "de-DE": "Löschen"
    },
    "Question" : {
        "en-DE" : "Question",
        "de-DE" : "Frage"
    }
});


customViews.define({
    name: "page-topic",
    class: Question,
    header: i18n("Question"),
    resizable: true,
    width: 900,
    height: 600,
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