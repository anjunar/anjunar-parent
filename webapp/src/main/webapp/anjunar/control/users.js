import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import HateoasGrid from "../../library/simplicity/hateoas/hateoas-grid.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";

export default class Users extends HTMLWindow {

    #users;

    get users() {
        return this.#users;
    }

    set users(value) {
        this.#users = value;
    }

    render() {

        this.style.display = "block";

        this.window.addEventListener("windowEndResize", () => {
            let matGrid = this.querySelector("mat-grid");
            matGrid.reload();
        })

        builder(this, [
            {
                element: HateoasGrid,
                model: this.#users,
                onItems: () => {
                    let domWindow = this.window.querySelector("dom-window");
                    domWindow.checkScrollBars();
                },
                limit: () => {
                    let x = Math.floor(this.parentElement.offsetHeight / 200);
                    let y = Math.floor(this.parentElement.offsetWidth / 200);
                    return x * y;
                },
                emptyItem: {
                    direct: () => {
                        return {
                            firstName: "",
                            lastName: "",
                            picture: {
                                data: ""
                            }
                        }
                    }
                },
                onItem: (event) => {
                    document.location.hash = `#/anjunar/control/user?id=${event.detail.item.id}`
                },
                onCreate: (event) => {
                    window.location.hash = `#/anjunar/control/user`
                },
                meta: {
                    item: {
                        element: (people) => {
                            return {
                                element: "div",
                                style: {
                                    margin: "2px",
                                    border: "1px solid var(--main-dark1-color)",
                                    width: "194px",
                                    height: "194px",
                                    position: "relative"
                                },
                                children: [
                                    {
                                        element: "div",
                                        style: {
                                            margin: "auto",
                                            position: "absolute",
                                            top: "50%",
                                            left: "50%",
                                            transform: "translate(-50%, -50%)",
                                            overflow: "auto"
                                        },
                                        children: [
                                            {
                                                element: "img",
                                                src: () => {
                                                    if (people.picture) {
                                                        return people.picture.data
                                                    }
                                                    return "";
                                                },
                                                style: {
                                                    maxWidth: "150px",
                                                    maxHeight: "150px"
                                                }
                                            },
                                            {
                                                element: "div",
                                                style: {
                                                    textAlign: "center"
                                                },
                                                text: `${people.firstName} ${people.lastName}`
                                            }
                                        ]
                                    }
                                ]
                            }
                        }
                    }
                }
            }
        ])
    }

}

let i18n = i18nFactory({
    Users: {
        "en-DE": "Users",
        "de-DE": "Leute"
    }
})

customViews.define({
    name: "control-users",
    class: Users,
    header: i18n("Users"),
    width: "600px",
    height: "300px",
    resizable: true,
    guard(activeRoute) {
        return {
            users: jsonClient.get(`service/control/users`)
        }
    }
})