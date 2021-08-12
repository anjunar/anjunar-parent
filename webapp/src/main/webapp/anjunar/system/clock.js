import {customComponents} from "../../library/simplicity/simplicity.js";
import {dateFormat} from "../../library/simplicity/services/tools.js";

export default class Clock extends HTMLElement {

    render() {
        this.style.display = "block";

        setInterval(() => {
            let date = Date.now();
            let format = dateFormat(date);
            this.textContent = format;
        }, 1000)

    }

}

customComponents.define("system-clock", Clock)