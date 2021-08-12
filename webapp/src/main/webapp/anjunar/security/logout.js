import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";
import {windowManager} from "../../library/simplicity/services/window-manager.js";

class Logout extends HTMLWindow {

    render() {
        builder(this, {
            element : "div",
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
                                windowManager.closeAll();
                                document.location.reload();
                            })
                    }
                }
            ]
        })
    }

}

export default customViews.define({
    name : "security-logout",
    class : Logout,
    header : "Logout",
    resizable : false
})

const i18n = i18nFactory({
    "Do you really logout?" : {
        "en-DE" : "Do you really logout?",
        "de-DE" : "Möchten sie sich wirklich Abmelden?"
    },
    "Send" : {
        "en-DE" : "Send",
        "de-DE" : "Senden"
    }
});
