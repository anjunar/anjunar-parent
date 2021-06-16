import {builder, customViews} from "../../library/simplicity/simplicity.js";
import MatTable from "../../library/simplicity/components/table/mat-table.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import {hateoas} from "../../library/simplicity/services/tools.js";

export default class Roles extends HTMLElement {

    render() {
        builder(this, {
            element: MatTable,
            items: {
                direct : (query, callback) => {
                    jsonClient.get(`${this.queryParams.link}?index=${query.index}&limit=${query.limit}`)
                        .then(response => {
                            callback(response.rows, response.size, response.links)
                        })
                }
            },
            onRow : (event) => {
                let link = hateoas(event.detail.links, "read");
                window.location.hash = `#/anjunar/control/role?id=${event.detail.id}&link=${link.url}`
            },
            meta : {
                header: [
                    {
                        element : () => {
                            return {
                                element: "strong",
                                text: "Name"
                            }
                        }
                    },
                    {
                        element : () => {
                            return {
                                element: "strong",
                                text: "Description"
                            }
                        }
                    }
                ],
                body: [
                    {
                        element : (role) => {
                            return {
                                element : "div",
                                text : role.name
                            }
                        }
                    },
                    {
                        element : (role) => {
                            return {
                                element : "div",
                                text : role.description
                            }
                        }
                    }
                ]
            }
        })
    }
}

customViews.define({
    name: "control-roles",
    class: Roles
})