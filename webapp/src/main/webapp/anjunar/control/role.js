import {builder, customViews} from "../../library/simplicity/simplicity.js";
import MatTable from "../../library/simplicity/components/table/mat-table.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import {hateoas} from "../../library/simplicity/services/tools.js";
import DomInput from "../../library/simplicity/directives/dom-input.js";

export default class Role extends HTMLElement {

    #role;

    get role() {
        return this.#role;
    }

    set role(value) {
        this.#role = value;
    }

    render() {
        builder(this, {
            element : MatTable,
            items : {
                direct : (query, callback) => {
                    let link = hateoas(this.#role.links, "permissions")
                    let sort = query.sort.map((sort => "&sort=" + sort));
                    jsonClient.get(`${link.url}?role=${this.#role.id}&index=${query.index}&limit=${query.limit}${sort.join("")}`)
                        .then(response => {
                            callback(response.rows, response.size, response.links);
                        })
                }
            },
            limit : 15,
            meta : {
                colgroup : [
                    {
                        element :() => {
                            return {
                                element : "div",
                                attributes : {}
                            }
                        }
                    },
                    {
                        element : () => {
                            return {
                                element : "div",
                                attributes : {
                                    path : "url"
                                }
                            }
                        }
                    },
                    {
                        element :() => {
                            return {
                                element : "div",
                                attributes : {
                                    path : "method"
                                }
                            }
                        }
                    }
                ],
                header : [
                    {
                        element : () => {
                            return {
                                element : "div",
                                children : [
                                    {
                                        element : DomInput,
                                        style : {
                                            width : "16px"
                                        },
                                        type : "checkbox",
                                        onClick : (event) => {
                                            event.stopPropagation();
                                            return false;
                                        },
                                        onChange : (event) => {
                                            let inputs = this
                                                .querySelector("tbody")
                                                .querySelectorAll("input");
                                            if (event.target.checked) {
                                                for (const input of inputs) {
                                                    input.checked = true
                                                    input.dispatchEvent(new Event("change"));
                                                }
                                            } else {
                                                for (const input of inputs) {
                                                    input.checked = false
                                                    input.dispatchEvent(new Event("change"));
                                                }
                                            }
                                        }
                                    }
                                ]
                            }
                        }
                    },
                    {
                        element : () => {
                            return {
                                element : "strong",
                                text : "URL"
                            }
                        }
                    },
                    {
                        element : () => {
                            return {
                                element : "strong",
                                text : "Method"
                            }
                        }
                    }
                ],
                body : [
                    {
                        element : (permission) => {
                            return {
                                element : DomInput,
                                style : {
                                    width : "16px"
                                },
                                type : "checkbox",
                                value : {
                                    input() {
                                        return permission.selected;
                                    },
                                    output(value) {
                                        permission.selected = value;
                                    }
                                },
                                onChange() {
                                    let link = hateoas(permission.links, "update")
                                    jsonClient.put(`${link.url}?id=${permission.id}&role=${role.id}`, {body : permission})
                                }
                            }
                        }
                    },
                    {
                        element : (permission) => {
                            return {
                                element : "div",
                                text : permission.url
                            }
                        }
                    },
                    {
                        element : (permission) => {
                            return {
                                element : "div",
                                text : permission.method
                            }
                        }
                    }
                ]
            },
        })
    }

}

customViews.define({
    name: "control-role",
    class: Role,
    guard(activeRoute) {
        if (activeRoute.queryParams.id) {
            return {
                role: jsonClient.get(`${activeRoute.queryParams.link}?id=${activeRoute.queryParams.id}`)
            }
        } else {
            return {
                role: jsonClient.get(activeRoute.queryParams.link)
            }
        }
    }
})