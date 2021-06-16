import {builder, customViews} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";


function div() {
}

function button() {

}

export default class Words extends HTMLElement {
    render() {

        function click() {

        }

        function left() {

        }

        builder(this, div({style: {color: "#eeeeee"}})([
            div({style : {color : "eeeeee"}, onClick : click})([
                button({type : "button", class : "button", onClick : click, left : left()})
            ])
        ]));
    }
}

customViews.define({
    name: "pages-words",
    class: Words,
    guard(activeRoute) {
        return {
            words: jsonClient.get("service/pages/like")
        }
    }
})