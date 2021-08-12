export function parse(multipart) {

    let result = new Map();

    let uuidRegex = /--([\w]{8}-[\w]{4}-[\w]{4}-[\w]{4}-[\w]{12})/g;

    let boundaries = [];
    let uuid = true;
    while (uuid) {
        uuid = uuidRegex.exec(multipart);
        if (uuid) {
            let start = uuid.index + uuid[0].length;
            uuid = uuidRegex.exec(multipart);
            if (uuid) {
                let end = uuid.index;
                boundaries.push({start : start, end : end})
                uuidRegex.lastIndex = start;
            }
        }
    }

    for (const boundary of boundaries) {
        let content = multipart.substring(boundary.start, boundary.end);
        let contentDispositionRegex = /Content-Disposition:\s([\w-]+);\sname="([\w]+)"/g;
        let contentTypeRegex = /Content-Type:\s([\w\/]+)/g;

        let contentDisposition = contentDispositionRegex.exec(content);
        let contentType = contentTypeRegex.exec(content);

        let value = content.substring(contentType.index + contentType[0].length).trim();

        result.set(contentDisposition[2], {
            disposition : contentDisposition[1],
            type : contentType[1],
            value : value
        })
    }

    return result;

}

export function multipartToJSON(multipart) {
    let result = {};

    function select(value) {
        switch (value.type) {
            case "text/plain" : return value.value;
            case "application/json" : return JSON.parse(value.value);
        }
    }

    multipart.forEach((value, key) => {
        result[key] = select(value);
    })

    return result;
}