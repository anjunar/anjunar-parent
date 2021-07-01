import {customComponents, loader} from "../library/simplicity2020/simplicity.js"

const validator = loader("./errors/404.html");
const {html, css} = validator([]);

export const name = "errors-404";

export default customComponents.define(name, class Error404 extends HTMLElement {}, {html: html, css: css});
