import MatToolbar from "../library/simplicity/components/navigation/mat-toolbar.js";
import DomRouter from "../library/simplicity/directives/dom-router.js";
import {builder, customComponents} from "../library/simplicity/simplicity.js";
import {registerExceptionHandler} from "../library/simplicity/services/client.js";
import {hateoas} from "../library/simplicity/services/tools.js";
import {loadRoot} from "./service.js";
import DomSelect from "../library/simplicity/directives/dom-select.js";
import {getLanguage, i18nFactory, setLanguage} from "../library/simplicity/services/i18nResolver.js";

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

    get service() {
        return loadRoot();
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
                            element: "a",
                            text: i18n("Welcome"),
                            href: "#/anjunar/welcome",
                            style: {
                                marginLeft: "5px"
                            }
                        },
                        {
                            element: "a",
                            text : () => {
                                return this.service.user.firstName;
                            },
                            href : `#/anjunar/home/timeline`,
                            style: {
                                marginLeft: "5px",
                                display : () => {
                                    return hateoas(this.service.links, "timeline") ? "inline" : "none"
                                }
                            }
                        },
                        {
                            element: "a",
                            text: i18n("People"),
                            href: `#/anjunar/control/users`,
                            style: {
                                marginLeft: "5px",
                                display : () => {
                                    return hateoas(this.service.links, "users") ? "inline" : "none"
                                }
                            }
                        },
                        {
                            element: "a",
                            text: "Editor",
                            href : `#/anjunar/pages/editor`,
                            style: {
                                marginLeft: "5px",
                                display : () => {
                                    return hateoas(this.service.links, "editor") ? "inline" : "none"
                                }

                            }
                        },
                        {
                            element: "a",
                            text: i18n("Search"),
                            href: `#/anjunar/pages/search`,
                            style: {
                                marginLeft: "5px",
                                display : () => {
                                    return hateoas(this.service.links, "search") ? "inline" : "none"
                                }
                            }

                        },
                        {
                            element: "a",
                            text: "Navigator",
                            href : `#/anjunar/navigator/navigator?link=${btoa("service")}`,
                            style: {
                                marginLeft: "5px",
                                display : () => {
                                    return hateoas(this.service.links, "navigator") ? "inline" : "none"
                                }
                            }

                        }, {
                            element: DomSelect,
                            style: {
                                marginLeft: "5px",
                                backgroundColor : "var(--main-normal-color)"
                            },
                            value : {
                                input : () => {
                                    return getLanguage() || "en"
                                },
                                output : (value) => {
                                    setLanguage(value);
                                }
                            },
                            onChange(event) {
                                document.dispatchEvent(new CustomEvent("language", {detail : event.target.value}))
                                location.reload();
                            },
                            children : [{
                                element : "option",
                                value : "en-DE",
                                text : "English",
                                attributes : {
                                    selected : false
                                }
                            }, {
                                element: "option",
                                value: "de-DE",
                                text : "Deutsch",
                            }]
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
                        height: "calc(100vh - 52px)",
                        width: "100%",
                        overflow: "auto"
                    }
                }
            ]
        })
    }


}

const i18n = i18nFactory({
    "Welcome" : {
        "en-DE" : "Welcome",
        "de-DE" : "Willkommen"
    },
    "People" : {
        "en-DE" : "People",
        "de-DE" : "Leute"
    },
    "Search" : {
        "en-DE" : "Search",
        "de-DE" : "Suchen"
    }
});


customComponents.define("app-anjunar", App);

