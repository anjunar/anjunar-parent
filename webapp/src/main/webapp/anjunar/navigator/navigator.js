import {builder, customViews} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import MatInputHolder from "../../library/simplicity/hateoas/hateoas-input.js";
import HateoasButton from "../../library/simplicity/hateoas/hateoas-button.js";
import HateoasTable from "../../library/simplicity/hateoas/hateoas-table.js";
import {hateoas} from "../../library/simplicity/services/tools.js";
import HateoasForm from "../../library/simplicity/hateoas/hateoas-form.js";

export default class Navigator extends HTMLElement {

    #service;

    get service() {
        return this.#service;
    }

    set service(value) {
        this.#service = value;
    }

    render() {

        this.style.display = "block"

        this.#service.meta = this.#service.meta || {};
        this.#service.meta.properties = this.#service.meta.properties || [];
        this.#service.columns = this.#service.columns || [];

        this.#service.actions = this.#service.actions || [];

        builder(this, [
            {
                element : "div",
                text : () => {
                    return atob(this.queryParams.link)
                }
            },
            {
                element: "div",
                children: this.#service.links.map((link) => {
                    return {
                        element: "a",
                        href: `#/anjunar/navigator/navigator?link=${btoa(link.url)}`,
                        text: link.rel,
                        style: {
                            margin: "5px"
                        }
                    }
                })
            },
            {
                element : "div",
                style: {
                    margin : "auto",
                    width : "800px"
                },
                children: [
                    {
                        element: HateoasForm,
                        model: this.#service,
                        children: [
                            {
                                element: "div",
                                children: this.#service.meta.properties.map((property) => {
                                    return {
                                        element: MatInputHolder,
                                        name: property.name
                                    }
                                })
                            }, {
                                element: "div",
                                children: this.#service.actions.map((link) => {
                                    return {
                                        element: HateoasButton,
                                        hateoas: link.rel,
                                        text: link.rel,
                                        onAfterSubmit : (event) => {
                                            let link = hateoas(event.detail.links, "redirect")
                                            if (link) {
                                                window.location.hash = `#/anjunar/navigator/navigator?link=${btoa(link.url)}`
                                            }
                                        }
                                    }

                                })
                            }
                        ]
                    }
                ]
            },
            {
                element: HateoasTable,
                model : this.#service,
                onRow : (event) => {
                    let link = hateoas(event.detail.actions, "read");
                    window.location.hash = `#/anjunar/navigator/navigator?link=${btoa(link.url)}`
                },
                onCreate : (event) => {
                    let link = event.detail;
                    window.location.hash = `#/anjunar/navigator/navigator?link=${btoa(link.url)}`
                }
            }
        ])
    }

}

customViews.define({
    name: "anjunar-navigator",
    class: Navigator,
    guard(activeRoute) {
        return {
            service: jsonClient.get(atob(activeRoute.queryParams.link))
        }
    }
})