import MatToolbar from "../library/simplicity/components/navigation/mat-toolbar.js";
import DomRouter from "../library/simplicity/directives/dom-router.js";
import {builder, customComponents} from "../library/simplicity/simplicity.js";
import {registerExceptionHandler} from "../library/simplicity/services/client.js";
import {hateoas} from "../library/simplicity/services/tools.js";
import DomSelect from "../library/simplicity/directives/dom-select.js";
import {i18nFactory} from "../library/simplicity/services/i18nResolver.js";
import Clock from "./system/clock.js";
import Taskbar from "./system/taskbar.js";

export default class App extends HTMLElement {

    constructor() {
        super();

        registerExceptionHandler((response) => {
            if (response.status === 401) {
                document.location.hash = `/anjunar/security/login`;
            }
            if (response.status === 404) {
                document.location.hash = `/errors/404`;
            }
        })
    }

    render() {
        builder(this, {
            element: "div", style: {height: "100vh"},
            children: [
                {
                    element: MatToolbar,
                    left: [
                        {element: "div", text: "Anjunar alpha 1.0"}
                    ],
                    middle: [
                        {
                            element: "div",
                            style : {
                                display : "flex",
                                justifyContent : "space-around"
                            },
                            children : [
                                {
                                    element: "a",
                                    href: "#/anjunar/security/register",
                                    style: {
                                        marginLeft: "5px",
                                        display: () => {
                                            return hateoas(user.links, "register") ? "flex" : "none"
                                        },
                                        alignItems : "center"
                                    },
                                    children: [
                                        {
                                            element: "span",
                                            className: "material-icons",
                                            text: "app_registration"
                                        }, {
                                            element: "span",
                                            text: i18n("Register"),
                                            style : {
                                                lineHeight : "24px"
                                            }
                                        }
                                    ]
                                },
                                {
                                    element: "a",
                                    href: `#/anjunar/home/timeline`,
                                    style: {
                                        marginLeft: "5px",
                                        display: () => {
                                            return hateoas(user.links, "timeline") ? "flex" : "none"
                                        },
                                        alignItems : "center"
                                    },
                                    children: [
                                        {
                                            element: "span",
                                            className: "material-icons",
                                            text: "timeline"
                                        }, {
                                            element: "span",
                                            text: () => {
                                                return user.firstName;
                                            },
                                            style : {
                                                lineHeight : "24px"
                                            }
                                        }
                                    ]

                                },
                                {
                                    element: "a",
                                    href: `#/anjunar/control/users`,
                                    style: {
                                        marginLeft: "5px",
                                        display: () => {
                                            return hateoas(user.links, "users") ? "flex" : "none"
                                        },
                                        alignItems : "center"
                                    },
                                    children: [
                                        {
                                            element: "span",
                                            className: "material-icons",
                                            text: "people"
                                        }, {
                                            element: "span",
                                            text: i18n("People")
                                        }
                                    ]
                                },
                                {
                                    element: "a",
                                    href: `#/anjunar/pages/editor`,
                                    style: {
                                        marginLeft: "5px",
                                        display: () => {
                                            return hateoas(user.links, "editor") ? "flex" : "none"
                                        },
                                        alignItems : "center"
                                    },
                                    children : [
                                        {
                                            element : "span",
                                            className : "material-icons",
                                            text : "mode_edit"
                                        }, {
                                            element: "span",
                                            text: "Editor"
                                        }
                                    ]
                                },
                                {
                                    element: "a",
                                    href: `#/anjunar/pages/search`,
                                    style: {
                                        marginLeft: "5px",
                                        display: () => {
                                            return hateoas(user.links, "search") ? "flex" : "none"
                                        },
                                        alignItems : "center"
                                    },
                                    children : [
                                        {
                                            element : "span",
                                            className : "material-icons",
                                            text : "search"
                                        }, {
                                            element: "span",
                                            text: i18n("Search")
                                        }
                                    ]
                                },
                                {
                                    element: "a",
                                    href: `#/anjunar/navigator/navigator?link=${btoa("service")}`,
                                    style: {
                                        marginLeft: "5px",
                                        display : "flex",
                                        alignItems : "center"
                                    },
                                    children : [
                                        {
                                            element : "span",
                                            className : "material-icons",
                                            text : "apps"
                                        }, {
                                            element: "span",
                                            text: "Navigator"
                                        }
                                    ]
                                }
                            ]
                        }
                    ],
                    right: [
                        {
                            element: "a", children: [
                                {
                                    element: "img",
                                    src: "images/chrome_logo.png",
                                    style: {height: "32px", width: "32px"}
                                }
                            ]
                        },
                        {
                            element: "a", children: [
                                {
                                    element: "img",
                                    src: "images/edge.png",
                                    style: {height: "32px", width: "32px"}
                                }
                            ]
                        }
                    ]
                },
                {
                    element: DomRouter,
                    level: 0,
                    style: {
                        display: "block",
                        position: "relative",
                        height: "calc(100vh - 102px)",
                        width: "100%",
                        backgroundImage: 'url("images/7044.jpg")'
                    }
                }, {
                    element: MatToolbar,
                    style: {
                        zIndex: "9999"
                    },
                    left: [
                        {
                            element: "button",
                            type: "button",
                            className: "material-icons",
                            text: "login",
                            style: {
                                display: () => {
                                    return hateoas(user.links, "login") ? "inline" : "none"
                                }
                            },
                            onClick: () => {
                                window.location.hash = "#/anjunar/security/login"
                            }
                        },
                        {
                            element: "button",
                            type: "button",
                            className: "material-icons",
                            text: "logout",
                            style: {
                                display: () => {
                                    return hateoas(user.links, "logout") ? "inline" : "none"
                                }
                            },
                            onClick: () => {
                                window.location.hash = "#/anjunar/security/logout"
                            }
                        }
                    ],
                    middle: [
                        {
                            element: Taskbar
                        }
                    ],
                    right: [
                        {
                            element: "div",
                            style: {
                                display: "flex"
                            },
                            children: [
                                {
                                    element: "div",
                                    style: {
                                        flex: "1"
                                    }
                                },
                                {
                                    element: DomSelect,
                                    style: {
                                        marginLeft: "5px",
                                        backgroundColor: "var(--main-dark1-color)"
                                    },
                                    value: {
                                        input: () => {
                                            return user.language || "en"
                                        },
                                        output: (value) => {
                                            user.language = value;
                                        }
                                    },
                                    onChange(event) {
                                        let request = new XMLHttpRequest();

                                        request.addEventListener("loadend", () => {
                                            document.dispatchEvent(new CustomEvent("language", {detail: event.target.value}))
                                            location.reload();
                                        })

                                        let url = "service/lang?lang=" + event.target.value;

                                        request.open("GET", url)
                                        request.send();
                                    },
                                    children: [{
                                        element: "option",
                                        value: "en-DE",
                                        text: "English",
                                        attributes: {
                                            selected: false
                                        }
                                    }, {
                                        element: "option",
                                        value: "de-DE",
                                        text: "Deutsch",
                                    }]
                                },
                                {
                                    element: Clock,
                                    style: {
                                        display: "block",
                                        margin: "10px"
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        })


    }

}

const i18n = i18nFactory({
    "Register": {
        "en-DE": "Register",
        "de-DE": "Registrieren"
    },
    "People": {
        "en-DE": "People",
        "de-DE": "Leute"
    },
    "Search": {
        "en-DE": "Search",
        "de-DE": "Suchen"
    }
});


customComponents.define("app-anjunar", App);

