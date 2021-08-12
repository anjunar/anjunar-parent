import {customViews, HTMLWindow} from "../library/simplicity/simplicity.js";

export default class Error404 extends HTMLWindow {

}

customViews.define({
    name : "errors-404",
    class : Error404,
    header : "Error 404"
});