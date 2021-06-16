import {customComponents} from "../simplicity.js";

export default class DomInputFile extends HTMLInputElement {

    render() {
        this.type = "file";

        let onChange = (event) => {
            let files = event.target.files;

            for (let i = 0, f; f = files[i]; i++) {

                if (!f.type.match('image.*')) {
                    continue;
                }

                let reader = new FileReader();

                reader.onload = ((theFile) => {
                    return (e) => {
                        this.dispatchEvent(new CustomEvent("loadend", {
                            detail: {
                                load: {
                                    data: e.target.result,
                                    name: theFile.name,
                                    lastModified: theFile.lastModified
                                }
                            }
                        }))
                    };
                })(f);

                reader.readAsDataURL(f);
            }

        }

        this.addEventListener("change", onChange);
    }

}

customComponents.define("dom-input-file", DomInputFile, {extends : "input"})