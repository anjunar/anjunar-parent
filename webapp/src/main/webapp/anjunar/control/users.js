import {builder, customViews} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import HateoasGrid from "../../library/simplicity/hateoas/hateoas-grid.js";

export default class Users extends HTMLElement {

    #users;

    get users() {
        return this.#users;
    }

    set users(value) {
        this.#users = value;
    }

    render() {
        builder(this, {
            element : "div",
            style: {
                display: "block",
                position: "absolute",
                top: "50%",
                left: "50%",
                width : "1400px",
                transform: "translate(-50%, -50%)"
            },
            children : [
                {
                    element : HateoasGrid,
                    model : this.#users,
                    emptyItem : {
                        direct : () => {
                            return {
                                firstName : "",
                                lastName : "",
                                picture : {
                                    data : ""
                                }
                            }
                        }
                    },
                    onItem : (event) => {
                        document.location.hash = `#/anjunar/control/user?id=${event.detail.item.id}`
                    },
                    onCreate : (event) => {
                        window.location.hash = `#/anjunar/control/user`
                    },
                    meta : {
                        item : {
                            element : (people) => {
                                return {
                                    element : "div",
                                    style : {
                                        margin : "2px",
                                        border : "1px solid var(--main-normal-color)",
                                        width : "194px",
                                        height : "194px",
                                        position : "relative"
                                    },
                                    children : [
                                        {
                                            element : "div",
                                            style : {
                                                margin : "auto",
                                                position: "absolute",
                                                top: "50%",
                                                left: "50%",
                                                transform : "translate(-50%, -50%)",
                                                overflow : "auto"
                                            },
                                            children : [
                                                {
                                                    element : "img",
                                                    src : () => {
                                                        if (people.picture) {
                                                            return people.picture.data
                                                        }
                                                        return "";
                                                    },
                                                    style : {
                                                        maxWidth : "150px",
                                                        maxHeight : "150px"
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
            ]
        })
    }

}

customViews.define({
    name : "control-users",
    class : Users,
    guard(activeRoute) {
        return {
            users : jsonClient.get(`service/control/users`)
        }
    }
})