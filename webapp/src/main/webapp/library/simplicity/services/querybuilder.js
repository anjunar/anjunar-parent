export class QueryBuilder {

    #url
    #result

    #queryParams = new Map();

    constructor(url) {
        this.#url = url;
    }

    queryParam(key, value) {
        this.#queryParams.set(key, value);
        return this;
    }

    build() {
        this.#result = this.#url;
        for (const param of this.#queryParams) {
            if (this.#url.indexOf(param[0]) === -1) {
                if (this.#result.indexOf("?") === -1) {
                    this.#result = this.#result + `?${param[0]}=${param[1]}`
                } else {
                    this.#result = this.#result + `&${param[0]}=${param[1]}`
                }
            } else {
                let s = `${param[0]}=([\\d\\w]+)`;
                let regex = new RegExp(s)
                let replace = this.#result.replace(regex, (match, p1) => {
                    return `${param[0]}=${param[1]}`;
                });
                this.#result = replace;
            }
        }
        return this.#result;
    }
}