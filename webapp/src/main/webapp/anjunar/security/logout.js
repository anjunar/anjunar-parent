import {builder, customViews, i18nFactory} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import {loadRoot} from "../service.js";

export default class Logout extends HTMLElement {

    render() {
        builder(this, {
            element : "div",
            style: {
                display: "block",
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)"
            },
            children : [
                {
                    element : "p",
                    text : i18n("Do you really logout?")
                },
                {
                    element : "button",
                    text : i18n("Send"),
                    className : "button",
                    onClick : () => {
                        jsonClient.post("service/security/logout")
                            .then((response) => {
                                loadRoot(true);
                                document.location.hash = "#/anjunar/welcome";
                            })
                    }
                }
            ]
        })
    }

}

customViews.define({
    name : "security-logout",
    class : Logout
})

const i18n = i18nFactory({
    "Do you really logout?" : {
        en : "Do you really logout?",
        de : "Möchten sie sich wirklich Abmelden?"
    },
    "Send" : {
        en : "Send",
        de : "Senden"
    }
});
