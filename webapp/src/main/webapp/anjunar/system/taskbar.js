import {builder, customComponents} from "../../library/simplicity/simplicity.js";
import {windowManager} from "../../library/simplicity/services/window-manager.js";

export default class Taskbar extends HTMLElement {

    render() {

        builder(this, {
            element : "div",
            style : {
                display : "flex"
            },
            children : {
                items : () => {
                    return windowManager.windows;
                },
                item : (item) => {
                    return {
                        element : "div",
                        style : {
                            height : "48px",
                            lineHeight : "48px",
                            width : "100px",
                            border : "1px solid var(--main-grey-color)",
                            textAlign : "center"
                        },
                        text : item.header,
                        onClick : (event) => {
                            event.stopPropagation();
                            windowManager.show(item);
                            return false;
                        }
                    }
                }
            }
        })

    }

}

customComponents.define("system-task-manager", Taskbar)